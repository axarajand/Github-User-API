Navigasi perpindahan halaman aplikasi menggunakan Intent.

Menerapkan Parcelable untuk transaksi data antar Activity.

Menerapkan RecyclerView untuk menampilkan data dalam bentuk list.

Menambahkan fungsi klik pada RecyclerView.

Menerapkan SearchView untuk melakukan pencarian data.

Menggunakan TabLayout sebagai navigasi antar halaman.

Menerapkan Localization untuk dukungan beberapa bahasa di dalam aplikasi.

Melakukan network request dengan nilai kembalian berupa Json.

Terdapat indikator loading saat aplikasi memuat data.

Menerapkan CRUD database.

Menerapkan Alarm Manger untuk menjalankan suatu proses pada waktu tertentu secara berulang.

Membuat aplikasi consumer untuk menguji Content Provider dalam menyediakan data.

Menampilkan notifikasi pada device.

* List User
  * Menampilkan data pada halaman aplikasi.
  * Menggunakan RecyclerView untuk menampilkan data.

* Detail User
  * Terdapat informasi detail dari seorang user.
  * Menggunakan Parcelable sebagai interface dari obyek data yang akan dikirimkan antar Activity.
  * Menampilkan fragment List Follower & List Following yang diambil dari API.
  * Menggunakan TabLayout, BottomNavigationView, atau yang lainnya sebagai navigasi antara halaman List Follower dan List Following.

* Search User
  * Pencarian user menggunakan data dari API berjalan dengan baik.
  * Pengguna dapat melihat halaman detail dari hasil daftar pencarian.
  * Data list user yang ditampilkan menggunakan RecyclerView.
  * List Item untuk RecyclerView disusun menggunakan ConstraintLayout.

* Favorite User
  * Aplikasi bisa menambah dan menghapus user dari daftar favorite.
  * Aplikasi mempunyai halaman yang menampilkan daftar favorite.
  * Menampilkan halaman detail dari daftar favorite.

* Reminder
  * Terdapat pengaturan untuk menghidupkan dan mematikan reminder di halaman Setting.
  * Daily reminder untuk kembali ke aplikasi yang berjalan pada pukul 09.00 AM.

* Consumer App
  * Module baru yang menampilkan daftar user favorite.
  * Menggunakan Content Provider sebagai mekanisme untuk mengakses data dari satu aplikasi ke aplikasi lain.

