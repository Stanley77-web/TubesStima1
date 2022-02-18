package za.co.entelect.challenge;

import za.co.entelect.challenge.command.*;
import za.co.entelect.challenge.entities.*;
import za.co.entelect.challenge.enums.State;
import za.co.entelect.challenge.enums.PowerUps;
import za.co.entelect.challenge.enums.Terrain;

import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Bot {

    private static final int maxSpeed = 9;
    private List<Command> directionList = new ArrayList<>();

    private final Random random;
    private final GameState gameState;
    private final Car opponent;
    private final Car myCar;
    private boolean dontTurnLeft; // true jika mobil tidak boleh belok kiri
    private boolean dontTurnRight; // true jika mobil tidak boleh belok kanan

    private final static Command TURN_RIGHT = new ChangeLaneCommand(1);
    private final static Command TURN_LEFT = new ChangeLaneCommand(-1);

    // Inisialisasi objek atau variable pada konstruktor
    public Bot(Random random, GameState gameState) {
        this.random = random;
        this.gameState = gameState;
        this.myCar = gameState.player;
        this.opponent = gameState.opponent;
        this.dontTurnLeft = false;
        this.dontTurnRight = false;

        directionList.add(TURN_LEFT);
        directionList.add(TURN_RIGHT);
    }

    // fungsi untuk menentukan strategi mobil yang akan dipertandingkan agar bisa memenangkan pertandingan. Urutan kode akan menentukan urutan langkah yang akan dilakukan pada mobil (dari atas ke bawah).
    public Command run() {
        // Variabel yang menyimpan blok terjangkau di depan
        List<Object> blocks = getBlocksInFront(this.myCar.position.lane, this.myCar.position.block + 1);
        // Variabel yang menyimpan blok terjangkau di kiri
        List<Object> leftBlocks = getBlocksInFront(this.myCar.position.lane - 1, this.myCar.position.block);
        // Variabel yang menyimpan blok terjangkau di kanan
        List<Object> rightBlocks = getBlocksInFront(this.myCar.position.lane + 1, this.myCar.position.block);
        // Variabel yang menyimpan blok terjangkau yang bisa dicapai saat pindah tempat untuk boost di kanan
        List<Object> extendedLaneBoostRight = getBlocksInFront(this.myCar.position.lane + 1, this.myCar.position.block + this.myCar.speed + 15);
        // Variabel yang menyimpan blok terjangkau yang bisa dicapai saat pindah tempat untuk boost di kiri mobil
        List<Object> extendedLaneBoostLeft = getBlocksInFront(this.myCar.position.lane - 1, this.myCar.position.block + this.myCar.speed + 15);
        // Variabel yang menyimpan blok terjangkau di depan saat accelerate
        List<Object> extendedBlocksAcc = getBlocksInFrontToAcc(this.myCar.position.lane, this.myCar.position.block + 1, nextSpeedIfAcc());
        // Variabel yang menyimpan blok terjangkau di depan saat boost
        List<Object> extendedBlocksBoost = getBlocksInFrontToAcc(this.myCar.position.lane, this.myCar.position.block + 1, 15);
        // Lakukan fix jika mobil menerima damage maksimum
        if (this.myCar.damage == 5) {
            return new FixCommand();
        }

        // Lakukan accelerate jika mobil tidak berjalan
        if (this.myCar.speed == 0) {
            return new AccelerateCommand();
        }

        // Lakukan fix jika kondisi cocok
        if (this.myCar.speed == this.maxSpeed()) {
            if (this.myCar.damage >= 2 && this.opponent.position.block < this.myCar.position.block)
                return new FixCommand();
            else if (this.myCar.damage >= 3) {
                return new FixCommand();
            }
        }
        // Cek block di depan jangkauan
        Command extendedCommand = checkExtendedLane();
        if (extendedCommand != null) {
            return extendedCommand;
        }

        // Hindari jebakan jika tepat didepan mobil ada jebakan
        if (blocks != null) {
            if (!laneNotContainObstacle(blocks)) {
                if (leftBlocks != null && rightBlocks != null) {// Jika terdapat lane kiri dan kanan mobil
                    if (laneNotContainObstacle(leftBlocks) && laneNotContainObstacle(rightBlocks)) {//
                        if (laneContainBoost(leftBlocks) && !this.dontTurnLeft) {
                            return TURN_LEFT;
                        } else if (laneContainBoost(rightBlocks) && !this.dontTurnRight) {
                            return TURN_RIGHT;
                        } else if ((laneContainLizard(leftBlocks) || laneContainEMP(leftBlocks)) && !this.dontTurnLeft) {
                            return TURN_LEFT;
                        } else if ((laneContainLizard(rightBlocks) || laneContainEMP(rightBlocks)) && !this.dontTurnRight) {
                            return TURN_RIGHT;
                        } else if ((laneContainTweet(leftBlocks) || laneContainOilpower(leftBlocks)) && !this.dontTurnLeft) {
                            return TURN_LEFT;
                        } else if (!this.dontTurnRight){
                            return TURN_RIGHT;
                        }
                        else if (!this.dontTurnLeft){
                            return TURN_LEFT;
                        }
                        else if (this.myCar.position.lane==3){
                            return TURN_LEFT;
                        }
                        else {
                            return TURN_RIGHT;
                        }
                    }
                    else if (laneNotContainObstacle(rightBlocks)) {
                        return TURN_RIGHT;
                    }
                    else if (laneNotContainObstacle(leftBlocks)) {
                        return TURN_LEFT;
                    }

                    else {
                        if (hasPowerUp(PowerUps.LIZARD, this.myCar.powerups) && blockNotContainObstacle(blocks,blocks.size())) {// Jika punya powerup lizard dan saat melompat tidak mendarat dijebakan
                            return new LizardCommand();
                        }
                        else {
                            if (minObstacle(blocks, rightBlocks, leftBlocks) == 1) {
                                return new AccelerateCommand();
                            } else if (minObstacle(blocks, rightBlocks, leftBlocks) == 2) {
                                return TURN_RIGHT;
                            } else if (minObstacle(blocks, rightBlocks, leftBlocks) == 3) {
                                return TURN_LEFT;
                            } else {
                                if (!blocks.contains(Terrain.WALL) || !blocks.contains((Terrain.CYBERTRUCK))) {
                                    return new AccelerateCommand();
                                } else if (!rightBlocks.contains(Terrain.WALL) || !rightBlocks.contains((Terrain.CYBERTRUCK))) {
                                    return TURN_RIGHT;
                                } else if (!leftBlocks.contains(Terrain.WALL) || !leftBlocks.contains((Terrain.CYBERTRUCK))) {
                                    return TURN_LEFT;
                                } else {
                                    if (this.myCar.speed >= 8) {
                                        return new DecelerateCommand();
                                    } else {
                                        return new AccelerateCommand();
                                    }
                                }
                            }
                        }
                    }
                }
                if (leftBlocks != null) {// Jika hanya terdapat lane kiri
                    if (laneNotContainObstacle(leftBlocks)) {
                        return TURN_LEFT;
                    }
                    else {
                        if (hasPowerUp(PowerUps.LIZARD, this.myCar.powerups) && blockNotContainObstacle(blocks,blocks.size())) { // Jika punya powerup lizard dan saat melompat tidak mendarat dijebakan
                            return new LizardCommand();
                        }
                        else {
                            if (minObstacle(blocks, null, leftBlocks) == 1) {
                                return new AccelerateCommand();
                            }
                            else if (minObstacle(blocks, null, leftBlocks) == 3) {
                                return TURN_LEFT;
                            } else {
                                if (!blocks.contains(Terrain.WALL) || !blocks.contains((Terrain.CYBERTRUCK))) {
                                    return new AccelerateCommand();
                                }
                                else if (!leftBlocks.contains(Terrain.WALL) || !leftBlocks.contains((Terrain.CYBERTRUCK))) {
                                    return TURN_LEFT;
                                }
                                else {
                                    if (this.myCar.speed >= 8) {
                                        return new DecelerateCommand();
                                    } else {
                                        return new AccelerateCommand();
                                    }
                                }
                            }
                        }
                    }
                }
                if (rightBlocks!=null){
                    if (laneNotContainObstacle(rightBlocks)) {// Jika hanya terdapat lane kanan
                        return TURN_RIGHT;
                    }
                    else {
                        if (hasPowerUp(PowerUps.LIZARD, this.myCar.powerups) && blockNotContainObstacle(blocks,blocks.size())) { // Jika punya powerup lizard dan saat melompat tidak mendarat dijebakan
                            return new LizardCommand();
                        }
                        else {
                            if (minObstacle(blocks, rightBlocks, null) == 1) {
                                return new AccelerateCommand();
                            }
                            else if (minObstacle(blocks, rightBlocks, null) == 2) {
                                return TURN_RIGHT;
                            } else {
                                if (!blocks.contains(Terrain.WALL) || !blocks.contains((Terrain.CYBERTRUCK))) {
                                    return new AccelerateCommand();
                                }
                                else if (!rightBlocks.contains(Terrain.WALL) || !rightBlocks.contains((Terrain.CYBERTRUCK))) {
                                    return TURN_RIGHT;
                                }
                                else {
                                    if (this.myCar.speed >= 8) {
                                        return new DecelerateCommand();
                                    } else {
                                        return new AccelerateCommand();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // pakai emp jika terdapat mobil musuh di depan dan kecepatan mobil musuh tidak pada kecepatan awal
        if (hasPowerUp(PowerUps.EMP,this.myCar.powerups) && this.opponent.position.block > this.myCar.position.block && (this.myCar.position.lane == this.opponent.position.lane || this.myCar.position.lane+1 == this.opponent.position.lane || this.myCar.position.lane -1 == this.opponent.position.lane) && this.opponent.speed!=3) {
            return new EmpCommand();
        }

        // fix jika mobil terkena damage dan memiliki boost dan tidak terkena jebakan saat melakukan boost
        if (this.myCar.damage > 0) {
            if ((extendedBlocksBoost)!=null) {
                if (countPowerUps(PowerUps.BOOST, this.myCar.powerups) >= 1 && laneNotContainObstacle(extendedBlocksBoost)) {
                    return new FixCommand();
                }
            }
        }

        // Lakukan boost jika keadaan memungkinkan
        if (this.myCar.damage == 0 && this.myCar.speed < 15) {
            if (hasPowerUp(PowerUps.BOOST, this.myCar.powerups)) {
                if (extendedBlocksBoost != null) {
                    if (laneNotContainObstacle(extendedBlocksBoost)  && this.myCar.state != State.HIT_EMP){ // block depan saat boost tidak ada jebakan dan status mobil tidak sedang dalam keadaan tertembak EMP
                        return new BoostCommand();
                    }
                }
            }
        }

        // Cek apakah ada lane terjangkau yang mengandung power up boost
        if (laneContainBoost(blocks)) {
            return goStraight(extendedBlocksAcc);
        }
        if (laneContainBoost(leftBlocks)) {
            if (!this.dontTurnLeft) {
                return TURN_LEFT;
            }
        }
        if (laneContainBoost(rightBlocks)) {
            if (!this.dontTurnRight) {
                return TURN_RIGHT;
            }
        }

        // Cek apakah ada lane terjangkau yang mengandung power up lizard
        if (laneContainLizard(blocks)) {
            return goStraight(extendedBlocksAcc);
        }
        if (laneContainLizard(leftBlocks)) {
            if (!this.dontTurnLeft) {
                return TURN_LEFT;
            }
        }
        if (laneContainLizard(rightBlocks)) {
            if (!this.dontTurnRight) {
                return TURN_RIGHT;
            }
        }

        // pakai tweet jika terdapat susunan jebakan sehingga membuat musuh tidak bisa menghindar
        if (hasPowerUp(PowerUps.TWEET, this.myCar.powerups)) {
            if (placeTweet()!=null) {
                return placeTweet();
            }
        }
        // pakai oil jika terdapat jebakan di sisi kanan dan kiri mobil
        if (this.opponent.position.block +10 < this.myCar.position.block && canPutOil()) {
            return new OilCommand();
        }

        // Cek apakah ada sisi kiri dan kanan kosong sehingga bisa melakukan boost
        if (hasPowerUp(PowerUps.BOOST, this.myCar.powerups)) {
            if (extendedLaneBoostRight!=null) {
                if (laneNotContainObstacle(extendedLaneBoostRight)) {
                    return TURN_RIGHT;
                }
            }
            if (extendedLaneBoostLeft!=null) {
                if (laneNotContainObstacle(extendedLaneBoostLeft)) {
                    return TURN_LEFT;
                }
            }
        }

        // Cek apakah ada lane terjangkau yang mengandung power up EMP
        if (laneContainEMP(leftBlocks)) {
            if (!this.dontTurnLeft) {
                return TURN_LEFT;
            }
        }
        if (laneContainEMP(rightBlocks)) {
            if (!this.dontTurnRight) {
                return TURN_RIGHT;
            }
        }

        // Naikkan kecepatan jika kecepatan kurang dari max speed
        if (this.myCar.speed<this.maxSpeed()) {
            return goStraight(extendedBlocksAcc);
        }

        // Cek apakah ada lane terjangkau yang mengandung power up tweet
        if (laneContainTweet(leftBlocks)) {
            if (!this.dontTurnLeft) {
                return TURN_LEFT;
            }
        }
        if (laneContainTweet(rightBlocks)) {
            if (!this.dontTurnRight) {
                return TURN_RIGHT;
            }
        }

        // Cek apakah ada lane terjangkau yang mengandung power up oil
        if (this.opponent.position.block<this.myCar.position.block) {
            if (laneContainOilpower(leftBlocks)) {
                if (!this.dontTurnLeft) {
                    return TURN_LEFT;
                }
            }
            if (laneContainOilpower(rightBlocks)) {
                if (!this.dontTurnRight) {
                    return TURN_RIGHT;
                }
            }
        }

        // Cek apakah ada musuh tepat didepan menghalangi
        if (this.myCar.position.lane==this.opponent.position.lane && this.opponent.position.block==this.myCar.position.block+1) {
            if (leftBlocks!=null) {
                if (!dontTurnLeft && laneNotContainObstacle(leftBlocks)) {
                    return TURN_LEFT;
                }
            }
            if (rightBlocks!=null) {
                if (!dontTurnRight && laneNotContainObstacle(rightBlocks)) {
                    return TURN_RIGHT;
                }
            }
        }

        // pakai tweet jika ada
        if (hasPowerUp(PowerUps.TWEET, this.myCar.powerups)){
            return new TweetCommand(this.opponent.position.lane, this.opponent.position.block + this.opponent.speed + 4);

        }

        return new DoNothingCommand(); // tidak melakukan apapun
    }



    /* Cek lane di depan jangkauan agar mobil tidak terjebak*/
    private Command checkExtendedLane() {
        List<Object> extendedLane1;
        List<Object> extendedLane2;
        List<Object> extendedLane3;
        List<Object> extendedLane4;
        List<Object> Lane2;
        List<Object> Lane3;
        int speed;
        int speedAcc;
        // biar kejangkau trapnya saat kecepatan kecil
        if (this.myCar.speed<6) {
            speed=12;
            speedAcc=8;
        }
        else {
            speed=this.myCar.speed;
            speedAcc=nextSpeedIfAcc();
        }
        switch (this.myCar.position.lane) {
            case 1:
                extendedLane1 = getBlocksInFrontToAcc(1, this.myCar.position.block + speedAcc + 1, speedAcc);
                extendedLane2 = getBlocksInFront(2, this.myCar.position.block + speed);
                Lane2 = getBlocksInFront(2, this.myCar.position.block);
                if (extendedLane1 != null && extendedLane2 != null) {
                    if (!laneNotContainObstacle(extendedLane1) && !laneNotContainObstacle(extendedLane2)) {
                        if (laneNotContainObstacle(Lane2)) {
                            return TURN_RIGHT;
                        }
                    }
                }
                break;
            case 2:
                extendedLane1 = getBlocksInFront(1, this.myCar.position.block + speed);
                extendedLane2 = getBlocksInFrontToAcc(2, this.myCar.position.block + speedAcc + 1, speedAcc);
                extendedLane3 = getBlocksInFront(3, this.myCar.position.block + speed);
                extendedLane4 = getBlocksInFront(4, this.myCar.position.block + speed - 1);
                Lane3 = getBlocksInFront(3, this.myCar.position.block);
                if (extendedLane1 != null && extendedLane2 != null && extendedLane3 != null) {
                    if (!laneNotContainObstacle(extendedLane1) && !laneNotContainObstacle(extendedLane2) && !laneNotContainObstacle((extendedLane3))) {
                        if (laneNotContainObstacle(Lane3)) {
                            return TURN_RIGHT;
                        }
                    }
                }
                if (extendedLane2 != null && extendedLane3 != null && extendedLane4 != null) {
                    if (!laneNotContainObstacle(extendedLane2) && !laneNotContainObstacle(extendedLane3) && !laneNotContainObstacle((extendedLane4))) {
                        this.dontTurnRight = true;
                    }
                }
                if (extendedLane1 != null && extendedLane2 != null) {
                    if (!laneNotContainObstacle(extendedLane1) && !laneNotContainObstacle(extendedLane2)) {
                        this.dontTurnLeft = true;
                    }
                }
                break;
            case 3:
                extendedLane1 = getBlocksInFront(1, this.myCar.position.block + speed - 1);
                extendedLane2 = getBlocksInFront(2, this.myCar.position.block + speed);
                extendedLane3 = getBlocksInFrontToAcc(3, this.myCar.position.block + speedAcc + 1, speedAcc);
                extendedLane4 = getBlocksInFront(4, this.myCar.position.block + speed);
                Lane2 = getBlocksInFront(2, this.myCar.position.block);

                if (extendedLane2 != null && extendedLane3 != null && extendedLane4 != null) {
                    if (!laneNotContainObstacle(extendedLane2) && !laneNotContainObstacle(extendedLane3) && !laneNotContainObstacle((extendedLane4))) {
                        if (laneNotContainObstacle(Lane2)) {
                            return TURN_LEFT;
                        }
                    }
                }
                if (extendedLane1 != null && extendedLane2 != null && extendedLane3 != null){
                    if (!laneNotContainObstacle(extendedLane1) && !laneNotContainObstacle(extendedLane2) && !laneNotContainObstacle((extendedLane3))) {
                        this.dontTurnLeft = true;
                    }
                }
                if (extendedLane3 != null && extendedLane4 != null) {
                    if (!laneNotContainObstacle(extendedLane3) && !laneNotContainObstacle((extendedLane4))) {
                        this.dontTurnRight = true;
                    }
                }
                break;
            case 4:
                extendedLane3 = getBlocksInFront(3, this.myCar.position.block + speed);
                extendedLane4 = getBlocksInFrontToAcc(4, this.myCar.position.block + speedAcc + 1, speedAcc);
                Lane3 = getBlocksInFront(3, this.myCar.position.block);
                if (extendedLane3 != null && extendedLane4 != null) {
                    if (!laneNotContainObstacle(extendedLane3) && !laneNotContainObstacle((extendedLane4))) {
                        if (laneNotContainObstacle(Lane3)) {
                            return TURN_LEFT;
                        }
                    }
                }
                break;
        }
        return null;
    }

    // memasang tweet agar musuh terjebak jika terdapat jebakan di beberapa bagian lane
    private Command placeTweet() {
        List<Object> backline1 = getBlocksInBack(1);
        List<Object> backline2 =getBlocksInBack(2);
        List<Object> backline3 =getBlocksInBack(3);
        List<Object> backline4 =getBlocksInBack(4);
        if (this.myCar.position.block-this.opponent.position.block>this.opponent.speed+5) { // jika musuh berada di belakang mobil
            if (!laneNotContainObstacle(backline1) && !laneNotContainObstacle(backline2) && !laneNotContainObstacle(backline3)) {
                return new TweetCommand(4, this.myCar.position.block - 1);
            } else if ((!laneNotContainObstacle(backline1) && !laneNotContainObstacle(backline2) && !laneNotContainObstacle(backline4))) {
                return new TweetCommand(3, this.myCar.position.block - 1);
            } else if (!laneNotContainObstacle(backline1) && !laneNotContainObstacle(backline3) && !laneNotContainObstacle(backline4)) {
                return new TweetCommand(2, this.myCar.position.block - 1);
            } else if (!laneNotContainObstacle(backline2) && !laneNotContainObstacle(backline3) && !laneNotContainObstacle(backline4)) {
                return new TweetCommand(1, this.myCar.position.block - 1);
            }
            else if (countPowerUps(PowerUps.TWEET,this.myCar.powerups)>5) {
                if (!laneNotContainObstacle(backline1) && !laneNotContainObstacle(backline2)) {
                    return new TweetCommand(3, this.myCar.position.block - 1);
                } else if (!laneNotContainObstacle(backline1) && !laneNotContainObstacle(backline3)) {
                    return new TweetCommand(2, this.myCar.position.block - 1);
                } else if (!laneNotContainObstacle(backline2) && !laneNotContainObstacle(backline4)) {
                    return new TweetCommand(3, this.myCar.position.block - 1);
                } else if (!laneNotContainObstacle(backline3) && !laneNotContainObstacle(backline4)) {
                    return new TweetCommand(2, this.myCar.position.block - 1);
                }
            }

        }
        return null;
    }

    /* Mengembalikan nilai maximum speed sesuai keadaan mobil saat ini */
    private int maxSpeed() {
        if (this.myCar.damage == 0){
            return 15;
        }
        if (this.myCar.damage == 1){
            return 9;
        }
        if (this.myCar.damage == 2){
            return 8;
        }
        if (this.myCar.damage == 3){
            return 6;
        }
        if (this.myCar.damage == 4){
            return 3;
        }
        return 0;
    }

    /* Mengembalikan nilai kecepatan jika melakukan accelerate */
    private int nextSpeedIfAcc() {
        return this.myCar.speed < 3 ? 3 : this.myCar.speed < 6 ? 6 : this.myCar.speed < 8 ? 8 : 9;
    }


    /* Mengembalikan nilai true jika blocks mengandung EMP */
    private boolean laneContainEMP(List<Object> blocks) {
        if (blocks != null) {
            if (laneNotContainObstacle(blocks)) {
                return blocks.contains(Terrain.EMP);
            }
        }
        return false;
    }

    /* Mengembalikan nilai true jika blocks mengandung Tweet */
    private boolean laneContainTweet(List<Object> blocks) {
        if (blocks != null) {
            if (laneNotContainObstacle(blocks)) {
                return blocks.contains(Terrain.TWEET);
            }
        }
        return false;
    }

    /* Mengembalikan nilai true jika blocks mengandung lizard */
    private boolean laneContainLizard(List<Object> blocks) {
        if (blocks != null) {
            if (laneNotContainObstacle(blocks)) {
                return blocks.contains(Terrain.LIZARD);
            }
        }
        return false;
    }

    /* Mengembalikan nilai true jika blocks mengandung boost */
    private boolean laneContainBoost(List<Object> blocks) {
        if (blocks != null) {
            if (laneNotContainObstacle(blocks)) {
                return blocks.contains(Terrain.BOOST);
            }
        }
        return false;
    }
    /* Mengembalikan nilai true jika blocks mengandung oilpower */
    private boolean laneContainOilpower(List<Object> blocks) {
        if (blocks != null) {
            if (laneNotContainObstacle(blocks)) {
                return blocks.contains(Terrain.OIL_POWER);
            }
        }
        return false;
    }

    /* Mengembalikan nilai true jika blocks tidak mengandung jebakan */
    private boolean laneNotContainObstacle(List<Object> blocks) {
        return !blocks.contains(Terrain.MUD) && !blocks.contains(Terrain.OIL_SPILL) && !blocks.contains(Terrain.WALL) && !blocks.contains(Terrain.CYBERTRUCK);
    }

    // mengembalikan nilai true jika block di posisi tertentu tidak berisi jebakan
    private boolean blockNotContainObstacle(List<Object> blocks, int position) {
        return blocks.get(position - 1) != Terrain.MUD && blocks.get(position - 1) != Terrain.WALL && blocks.get(position - 1) != Terrain.OIL_SPILL && blocks.get(position - 1) !=Terrain.CYBERTRUCK;
    }
    // Cek apakah tidak menabrak saat melakukan accelerate
    private Command goStraight(List<Object> blocks) {
        if (laneNotContainObstacle(blocks)) {
            return new AccelerateCommand();
        }
        return new DoNothingCommand();
    }

    // cek apakah blok bagus untuk oil
    private boolean canPutOil() {
        List<Object> backlineRight =getBlocksInBack(this.myCar.position.lane+1);
        List<Object> backlineLeft =getBlocksInBack(this.myCar.position.lane-1);
        if (hasPowerUp(PowerUps.OIL,this.myCar.powerups)) {
            if(backlineRight!=null && backlineLeft!=null) {
                return !laneNotContainObstacle(backlineRight) && !laneNotContainObstacle(backlineLeft);
            }
            else if (backlineLeft!=null) {
                return !laneNotContainObstacle(backlineLeft);
            }
            else { //backlineRight!=null
                return !laneNotContainObstacle(backlineRight);

            }
        }
        return false;
    }


    //mengembalikan block dari map berisi objek dari lane yang dipilih. Isi block memiliki jumlah sesuai dengan kecepatan mobil saat ini.
    private List<Object> getBlocksInFront(int lane, int block) {
        if (lane < 1 || lane > 4) {
            return null;
        }
        List<Lane[]> map = gameState.lanes;
        List<Object> blocks = new ArrayList<>();
        int startBlock = map.get(0)[0].position.block;
        Lane[] laneList = map.get(lane - 1);
        if (laneList != null) {
            int reachableBlock = block - startBlock + this.myCar.speed - 1;
            try {
                for (int i = max(block - startBlock, 0); i <= reachableBlock && i < laneList.length; i++) {
                    if (laneList[i] == null || laneList[i].terrain == Terrain.FINISH) {
                        break;
                    }
                    if (laneList[i].isOccupiedByCyberTruck) blocks.add(Terrain.CYBERTRUCK); // Jika terdapat cybertruck, isi dengan cybertruck
                    else blocks.add(laneList[i].terrain);

                }
            }
            catch (Exception e){
                return null;
            }
        }
        return blocks.isEmpty() ? null : blocks;
    }


    // mengembalikan block dari map berisi objek dari lane yang dipilih. Isi block memiliki jumlah sesuai dengan kecepatan mobil saat accelerate
    private List<Object> getBlocksInFrontToAcc(int lane, int block, int range) {
        if (lane < 1 || lane > 4) {
            return null;
        }
        List<Lane[]> map = gameState.lanes;
        List<Object> blocks = new ArrayList<>();
        int startBlock = map.get(0)[0].position.block;
        Lane[] laneList = map.get(lane - 1);
        int reachableBlock = block - startBlock + range - 1;
        for (int i = max(block - startBlock, 0); i <= reachableBlock && i<laneList.length; i++) {
            if (laneList[i] == null || laneList[i].terrain == Terrain.FINISH) {
                break;
            }
            if (laneList[i].isOccupiedByCyberTruck) blocks.add(Terrain.CYBERTRUCK);
            else blocks.add(laneList[i].terrain);
        }
        return blocks;
    }

    // mengembalikan block dari map berisi objek dari lane yang dipilih. Isi block diambil dari belakang mobil yang bisa didapat dan 4 blok di depan mobil
    private List<Object> getBlocksInBack(int lane) {
        if (lane < 1 || lane > 4) {
            return null;
        }

        List<Lane[]> map = gameState.lanes;
        int startBlock = map.get(0)[0].position.block;
        List<Object> blocks = new ArrayList<>();
        Lane[] laneList = map.get(lane - 1);
        for (int i = 0; i < max(this.myCar.position.block - startBlock, 0) + 4; i++) {
            if (laneList[i] == null || laneList[i].terrain == Terrain.FINISH) {
                break;
            }
            if (laneList[i].isOccupiedByCyberTruck) blocks.add(Terrain.CYBERTRUCK);
            else blocks.add(laneList[i].terrain);
        }
        return blocks;
    }
    /* Fungsi untuk mengecek apakah mobil memiliki suatu power up */
    private Boolean hasPowerUp(PowerUps powerUpToCheck, PowerUps[] available){
        for (PowerUps powerUp: available) {
            if (powerUp != null){
                if (powerUp.equals(powerUpToCheck)) {
                    return true;
                }
            }
        }
        return false;
    }

    /* Fungsi untuk menghitung suatu power up yang dimiliki */
    private int countPowerUps(PowerUps powerUpToCheck, PowerUps[] available){
        int powerUpCount = 0;
        for (PowerUps powerUp: available) {
            if (powerUp != null){
                if (powerUp.equals(powerUpToCheck)) {
                    powerUpCount++;
                }
            }
        }
        return powerUpCount;

    }

    // Mengembalikan lane dengan jebakan paling sedikit. Me-return 1 jika lane yang dipakai mobil paling sedikit, 2 jika di kanan mobil paling sedikit, 3 jika di kiri mobil paling sedikit, 4 jika terdapat 2 atau lebih lane yang memiliki jumlah jebakan sama
    private int minObstacle(List<Object> blocks, List<Object> Rightblocks, List<Object> Leftblocks) {
        int count1=0,count2=0,count3=0;
        if (Rightblocks!=null && Leftblocks!=null) {
            int min1;
            for (Object block : blocks) {
                if (block.equals(Terrain.MUD) || block.equals(Terrain.WALL) || block.equals(Terrain.OIL_SPILL)) {
                    count1++;
                }
            }
            for (Object rightblock : Rightblocks) {
                if (rightblock.equals(Terrain.MUD) || rightblock.equals(Terrain.WALL) || rightblock.equals(Terrain.OIL_SPILL)) {
                    count2++;
                }
            }
            for (Object leftblock : Leftblocks) {
                if (leftblock.equals(Terrain.MUD) || leftblock.equals(Terrain.WALL) || leftblock.equals(Terrain.OIL_SPILL)) {
                    count3++;
                }
            }
            min1=min(count1,count2);
            if(min1==count1 && min1<count3) {
                return 1;
            }
            else if (min1==count2 && min1<count3) {
                return 2;
            }
            else if (min1>count3){
                return 3;
            }
            else { //min1=count3
                return 4;
            }
        }
        else if (Leftblocks!=null) {
            for (Object block : blocks) {
                if (block.equals(Terrain.MUD) || block.equals(Terrain.WALL) || block.equals(Terrain.OIL_SPILL)) {
                    count1++;
                }
            }
            for (Object leftblock : Leftblocks) {
                if (leftblock.equals(Terrain.MUD) || leftblock.equals(Terrain.WALL) || leftblock.equals(Terrain.OIL_SPILL)) {
                    count3++;
                }
            }
            if(count1<count3) {
                return 1;
            }
            else if (count1>count3) {
                return 3;
            }
            else { //count1=count3
                return 4;
            }
        }
        else { //Rightblock!=null
            for (Object block : blocks) {
                if (block.equals(Terrain.MUD) || block.equals(Terrain.WALL) || block.equals(Terrain.OIL_SPILL)) {
                    count1++;
                }
            }
            for (Object rightblock : Rightblocks) {
                if (rightblock.equals(Terrain.MUD) || rightblock.equals(Terrain.WALL) || rightblock.equals(Terrain.OIL_SPILL)) {
                    count2++;
                }
            }
            if(count1<count2) {
                return 1;
            }
            else if (count1>count2) {
                return 2;
            }
            else { //count1=count2
                return 4;
            }
        }
    }
}