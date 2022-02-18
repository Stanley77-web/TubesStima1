# Tugas Besar 1 IF 2211 Strategi Algoritma
> Aplikasi Algoritma Brute Force untuk Membuat Suatu Program Solver untuk Permainan _Search Word Puzzle_

## Daftar Isi
  - [Tugas Besar 1 IF 2211 Strategi Algoritma](#tugas-besar-1-if-2211-strategi-algoritma)
  - [Daftar Isi](#daftar-isi)
  - [Informasi Umum](#informasi-umum)
  - [Teknologi Digunakan](#teknologi-digunakan)
  - [Penggunaan](#penggunaan)
  - [Penulis](#penulis)
<!-- * [License](#license) -->

## Informasi Umum
Membuat bot untuk permainan _overdrive_ dengan menggunakan memanfaatkan algoritma _greedy_ <br /> <br />
Algoritma greedy yang digunakan dalam pengembangan bot ini adalah: 
- Program mampu menerima file txt yang berisi file configurasi [matrix kumpulan huruf puzzle dan dilanjutkan dengan list kumpulan kata yang dicari].
- Program mampu menampilkan output berupa jumlah perbandingan yang dilakukan dan waktu eksekusi program disertai dengan pemberitauan arah pencarian kata ditemukan
- Output program diakhir dengan tampilan puzzle yang sudah diberi warna untuk kata-kata yang berhasil ditemukan
hasil kompresi gambar (perubahan jumlah pixel gambar).
<!-- You don't have to answer all the questions - just the ones relevant to your project. -->

## Teknologi Digunakan
- [Java - version 16.0.2](https://en.wikipedia.org/wiki/Java) 
- [JRE - version 16.0.2+7-67](https://en.wikipedia.org/wiki/Java_(software_platform))
- [NPM - version 8.1.0](https://nodejs.org/en/download/)
- [IntelIiJ IDEA - edition 2021.3.2](https://www.jetbrains.com/idea/)
- [Windows OS - version 10+](https://en.wikipedia.org/wiki/Microsoft_Windows) **Disarankan**

## Penggunaan
***[Perhatikan]***
Program ini disarankan hanya digunakan di windows OS. Namun, pada windows biasanya tidak _enable support_ untuk pewarnaan tulisan pada terminal dengan memanfaatkan ANSI escape. Agar bisa terminal dapat _support_ dengan hal tersebut, berikut ini langkah-langkah untuk mengaktifkan ANSI escape
1. Buka kolom pencarian pada _taskbar_
2. Ketikan ```run``` pada kolom pencarian kemudian klik open seperti gambar berikut:

   ![image](https://media.discordapp.net/attachments/830474732477874226/935733597602730014/unknown.png?width=713&height=470)

3. Ketika app ```run``` sudah terbuka, ketikan ```regedit``` kemudian klik 'OK' seperti gambar berikut:
    
   ![image](https://cdn.discordapp.com/attachments/830474732477874226/935734847014580244/unknown.png)
    
4. Atur path menjadi ```Computer\HKEY_CURRENT_USER\Console``` lalu klik kanan pada area kosong dan ```new > DWORD (32-bit) Value``` seperti gambar berikut:

   ![image](https://cdn.discordapp.com/attachments/830474732477874226/935738712711651378/unknown.png)

5. Ganti nama variabel yang baru dibuat menjadi ```VirtualTerminalLevel```

   ![image](https://cdn.discordapp.com/attachments/830474732477874226/935739023387942963/unknown.png)
   ![image](https://cdn.discordapp.com/attachments/830474732477874226/935739508861829170/unknown.png)

6. _Double_ klik pada variabel tersebut, kemudian ubah Value datanya dari ```0``` menjadi ```1```, kemudian klik ```OK``` seperti gambar berikut:

   ![image](https://cdn.discordapp.com/attachments/830474732477874226/935740482762449026/unknown.png)

Berikut ini cara menjalankan dan menggunakan program
1. Klik folder program ```bin``` yang berisi file class dan jar seperti gambar berikut:

  ![image](https://media.discordapp.net/attachments/830474732477874226/935741546496680016/unknown.png)
  
2. Buka terminal ```cmd``` atau ```powershell``` pada path folder tersebut dengan cara klik kanan pada area kosong kemudian klik ```Open in Windows Terminal``` seperti gambar berikut:

  ![image](https://cdn.discordapp.com/attachments/830474732477874226/935741700624756756/unknown.png)
  
3. Ketikan ```java -jar --enable-preview SearchWord.jar``` pada terminal kemudian tekan ```enter``` 
4. Masukan nama file configurasi dan juga versi brute force yang diinginkanseperti gambar dibawah ini

  ![image](https://cdn.discordapp.com/attachments/830474732477874226/935743685310693387/unknown.png)

5. Program akan menampilkan hasil pencarian kata dari file configurasi seperti gambar di bawah ini:

  ![image](https://media.discordapp.net/attachments/830474732477874226/935724070757072906/unknown.png?width=1134&height=662)
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
