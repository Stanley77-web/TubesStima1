# Tugas Besar 1 IF 2211 Strategi Algoritma
> Aplikasi Algoritma Greedy untuk Membuat Bot untuk Permainan _Overdrive_

## Daftar Isi
  - [Tugas Besar 1 IF 2211 Strategi Algoritma](#tugas-besar-1-if-2211-strategi-algoritma)
  - [Daftar Isi](#daftar-isi)
  - [Informasi Umum](#informasi-umum)
  - [Teknologi Digunakan](#teknologi-digunakan)
  - [Penggunaan](#penggunaan)
  - [Penulis](#penulis)
<!-- * [License](#license) -->

## Informasi Umum
Membuat bot untuk permainan _Overdrive_ dengan menggunakan memanfaatkan algoritma _greedy_ <br /> <br />
Algoritma greedy yang digunakan dalam pengembangan bot ini adalah: 
- Program mampu menerima file txt yang berisi file configurasi [matrix kumpulan huruf puzzle dan dilanjutkan dengan list kumpulan kata yang dicari].
- Program mampu menampilkan output berupa jumlah perbandingan yang dilakukan dan waktu eksekusi program disertai dengan pemberitauan arah pencarian kata ditemukan
- Output program diakhir dengan tampilan puzzle yang sudah diberi warna untuk kata-kata yang berhasil ditemukan
hasil kompresi gambar (perubahan jumlah pixel gambar).
<!-- You don't have to answer all the questions - just the ones relevant to your project. -->

## Teknologi Digunakan
- [Java - version 16.0.2](https://en.wikipedia.org/wiki/Java) 
- [JRE - version 16.0.2+7-67](https://en.wikipedia.org/wiki/Java_(software_platform))
- [NPM - version 8.1.0](https://nodejs.org/en/download/) **Optional**
- [IntelIiJ IDEA - edition 2021.3.2](https://www.jetbrains.com/idea/) **Optional**
- [Windows OS - version 10+](https://en.wikipedia.org/wiki/Microsoft_Windows) **Disarankan**

## Penggunaan
***[Perhatikan]***
**Pada pentunjuk ini diasumsikan pengguna sudah mendownload starter pack zip [disini ini](https://github.com/EntelectChallenge/2020-Overdrive/releases/tag/2020.3.4)**
Program ini disarankan hanya digunakan di windows OS. Untuk menjalan program ada dua pilihan cara, yaitu 
1. Menggunakan fitur maven pada IntelIiJ IDEA :
  1. Copykan seluruh isi file pada folder src ke ```\starter-bots\java```
  2. Buka folder starter menggunakan IntelIij IDEA
  3. Tunggu hingga IntelIij IDEA menampilkan fitur maven pada bagian kanan atas seperti pada gambar di bawah ini:

   ![image](https://cdn.discordapp.com/attachments/941288781401698307/944250982022385724/unknown.png)

  4. Klik fitur maven pada gambar sebelumnya hingga menampilkan tampilan seperti pada gambar di bawah ini:
    
   ![image](https://cdn.discordapp.com/attachments/941288781401698307/944251696475942912/unknown.png)
    
  5. Klik  ```>``` pada bagian kiri ```java-reference-bot``` dan ```java-starter-bot``` hingga menampilkan pilihan menu seperti gambar berikut:

   ![image](https://cdn.discordapp.com/attachments/941288781401698307/944252374724268102/unknown.png)

  6. Klik ```install``` pada kedua bagian pilihan menu tadi hinggan menampilkan gambar seperti:

   ![image](https://cdn.discordapp.com/attachments/941288781401698307/944252814568345620/unknown.png)
   ![image](https://cdn.discordapp.com/attachments/941288781401698307/944252920097046558/unknown.png)

  7. Untuk menjalankan program, langsung membukan file run.bat yang tersedia pada starter pack 

   ![image](https://cdn.discordapp.com/attachments/941288781401698307/944253492451737681/unknown.png)

2. Langsung menggunakan file executable pada folder bin (syarat sudah pernah melakukan install menggunakan maven seperti langkah di atas):
  1. Copykan file .jar pada folder bin ke ```\starter-bots\java\target```    
  2. Ubah nama pada .jar tadi menjadi ```java-starter-bot-jar-with-dependencies.jar``` atau buka file ```bot.json``` pada ```\starter-bots\java``` kemudian ubah isi ```"botFileName":``` menjadi nama file .jar pada folder bin

   ![image](https://cdn.discordapp.com/attachments/941288781401698307/944256857906770030/unknown.png) 
   ![image](https://cdn.discordapp.com/attachments/941288781401698307/944256347195715594/unknown.png) 
   
  3. Untuk menjalankan program, langsung membukan file run.bat yang tersedia pada starter pack

   ![image](https://cdn.discordapp.com/attachments/941288781401698307/944253492451737681/unknown.png)

Untuk dapat melihat hasil permainan dengan menggunakan visualizer pada [link disini](https://github.com/Affuta/overdrive-round-runner) (Optional) 

Selamat Mencoba!
    
## Penulis
<table>
    <tr>
      <td><b>Nama</b></td>
      <td><b>NIM</b></td>
    </tr>
    <tr>
      <td><b>Bariza Haqi</b></td>
      <td><b>13520018</b></td>
    </tr>
    <tr>
      <td><b>Timothy Stanley Setiawan</b></td>
      <td><b>13520028</b></td>
    </tr>
    <tr>
      <td><b>Rozan Fadhil Al Hafiz</b></td>
      <td><b>13520039</b></td>
    </tr>
</table>
