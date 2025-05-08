package gacoan;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class MieGacoanApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public static void main(String[] args) {
        initializeSystem();
        runMainMenu();
        shutdownSystem();
    }

    private static void initializeSystem() {
        DatabaseHelper.initialize();
        DataInitializer.initializeSampleData();
    }

    private static void shutdownSystem() {
        DatabaseHelper.closeDB();
        System.out.println("Aplikasi ditutup. Terima kasih!");
    }

    private static void runMainMenu() {
        boolean running = true;
        while (running) {
            clearScreen();
            System.out.println("=== SISTEM MANAJEMEN MIE GACOAN ===");
            System.out.println("1. Kelola Pelanggan");
            System.out.println("2. Kelola Menu");
            System.out.println("3. Kelola Pesanan");
            System.out.println("4. Laporan");
            System.out.println("5. Backup Database");
            System.out.println("6. Contoh Query");
            System.out.println("0. Keluar");
            System.out.print("Pilih menu: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> menuPelanggan();
                    case 2 -> menuMenu();
                    case 3 -> menuPesanan();
                    case 4 -> menuLaporan();
                    case 5 -> backupDatabase();
                    case 6 -> contohQuery();
                    case 0 -> running = false;
                    default -> showError("Pilihan tidak valid!");
                }
            } catch (Exception e) {
                handleInputError();
            }
        }
    }

    private static void menuPelanggan() {
        boolean back = false;
        while (!back) {
            clearScreen();
            System.out.println("=== KELOLA PELANGGAN ===");
            System.out.println("1. Tambah Pelanggan");
            System.out.println("2. Lihat Semua Pelanggan");
            System.out.println("3. Cari Pelanggan");
            System.out.println("4. Update Pelanggan");
            System.out.println("5. Hapus Pelanggan");
            System.out.println("6. Tambah Member");
            System.out.println("0. Kembali");
            System.out.print("Pilih: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> tambahPelanggan(false);
                    case 2 -> lihatSemuaPelanggan();
                    case 3 -> cariPelanggan();
                    case 4 -> updatePelanggan();
                    case 5 -> hapusPelanggan();
                    case 6 -> tambahPelanggan(true);
                    case 0 -> back = true;
                    default -> showError("Pilihan tidak valid!");
                }
            } catch (Exception e) {
                handleInputError();
            }
        }
    }

    private static void tambahPelanggan(boolean isMember) {
        clearScreen();
        System.out.println("\n=== TAMBAH " + (isMember ? "MEMBER" : "PELANGGAN") + " BARU ===");

        System.out.print("ID Pelanggan: ");
        String id = scanner.nextLine();

        System.out.print("Nama: ");
        String nama = scanner.nextLine();

        System.out.print("No HP: ");
        String noHp = scanner.nextLine();

        System.out.print("Alamat: ");
        String alamat = scanner.nextLine();

        ObjectContainer db = DatabaseHelper.getDB();

        if (isMember) {
            System.out.print("Level Member (Silver/Gold/Platinum): ");
            String level = scanner.nextLine();
            db.store(new Member(id, nama, noHp, alamat, level));
        } else {
            db.store(new Pelanggan(id, nama, noHp, alamat));
        }

        db.commit();
        showSuccess("Data berhasil disimpan!");
    }

    private static void lihatSemuaPelanggan() {
        clearScreen();
        ObjectContainer db = DatabaseHelper.getDB();
        ObjectSet<Pelanggan> result = db.queryByExample(Pelanggan.class);

        System.out.println("\n=== DAFTAR PELANGGAN ===");
        if (result.isEmpty()) {
            System.out.println("Tidak ada data pelanggan");
        } else {
            result.forEach(System.out::println);
        }
        pause();
    }

    private static void cariPelanggan() {
        clearScreen();
        System.out.println("\n=== CARI PELANGGAN ===");
        System.out.print("Masukkan ID/Nama Pelanggan: ");
        String keyword = scanner.nextLine();
        
        ObjectContainer db = DatabaseHelper.getDB();
        Query query = db.query();
        query.constrain(Pelanggan.class);
        query.descend("id").constrain(keyword).like();
        query.descend("nama").constrain(keyword).like().or(query.descend("id").constrain(keyword).like());
        
        ObjectSet<Pelanggan> result = query.execute();
        
        System.out.println("\n=== HASIL PENCARIAN ===");
        if (result.isEmpty()) {
            System.out.println("Tidak ditemukan pelanggan dengan kriteria tersebut");
        } else {
            while (result.hasNext()) {
                System.out.println(result.next());
            }
        }
        pause();
    }

    private static void updatePelanggan() {
        clearScreen();
        System.out.println("\n=== UPDATE PELANGGAN ===");
        System.out.print("Masukkan ID Pelanggan yang akan diupdate: ");
        String id = scanner.nextLine();
        
        ObjectContainer db = DatabaseHelper.getDB();
        Pelanggan pelanggan = DataUpdater.findPelangganById(id);
        
        if (pelanggan == null) {
            showError("Pelanggan tidak ditemukan!");
            return;
        }
        
        System.out.println("\nData saat ini:");
        System.out.println(pelanggan);
        
        System.out.print("\nNama Baru (kosongkan jika tidak diubah): ");
        String nama = scanner.nextLine();
        if (!nama.isEmpty()) pelanggan.setNama(nama);
        
        System.out.print("No HP Baru (kosongkan jika tidak diubah): ");
        String noHp = scanner.nextLine();
        if (!noHp.isEmpty()) pelanggan.setNoHp(noHp);
        
        System.out.print("Alamat Baru (kosongkan jika tidak diubah): ");
        String alamat = scanner.nextLine();
        if (!alamat.isEmpty()) pelanggan.setAlamat(alamat);
        
        db.store(pelanggan);
        db.commit();
        showSuccess("Data pelanggan berhasil diupdate!");
    }

    private static void hapusPelanggan() {
        clearScreen();
        System.out.println("\n=== HAPUS PELANGGAN ===");
        System.out.print("Masukkan ID Pelanggan yang akan dihapus: ");
        String id = scanner.nextLine();
        
        ObjectContainer db = DatabaseHelper.getDB();
        Pelanggan pelanggan = DataUpdater.findPelangganById(id);
        
        if (pelanggan == null) {
            showError("Pelanggan tidak ditemukan!");
            return;
        }
        
        System.out.println("\nData yang akan dihapus:");
        System.out.println(pelanggan);
        
        System.out.print("\nApakah Anda yakin ingin menghapus? (y/n): ");
        String confirm = scanner.nextLine();
        
        if (confirm.equalsIgnoreCase("y")) {
            db.delete(pelanggan);
            db.commit();
            showSuccess("Pelanggan berhasil dihapus!");
        } else {
            showError("Penghapusan dibatalkan");
        }
    }

    private static void menuMenu() {
        boolean back = false;
        while (!back) {
            clearScreen();
            System.out.println("=== KELOLA MENU ===");
            System.out.println("1. Tambah Menu");
            System.out.println("2. Lihat Semua Menu");
            System.out.println("3. Cari Menu");
            System.out.println("4. Update Harga Menu");
            System.out.println("5. Hapus Menu");
            System.out.println("0. Kembali");
            System.out.print("Pilih: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> tambahMenu();
                    case 2 -> lihatSemuaMenu();
                    case 3 -> cariMenu();
                    case 4 -> updateHargaMenu();
                    case 5 -> hapusMenu();
                    case 0 -> back = true;
                    default -> showError("Pilihan tidak valid!");
                }
            } catch (Exception e) {
                handleInputError();
            }
        }
    }

    private static void tambahMenu() {
        clearScreen();
        System.out.println("\n=== TAMBAH MENU BARU ===");

        System.out.print("ID Menu: ");
        String id = scanner.nextLine();

        System.out.print("Nama Menu: ");
        String nama = scanner.nextLine();

        System.out.print("Harga: ");
        double harga = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Kategori (Mie/Minuman/Snack): ");
        String kategori = scanner.nextLine();

        System.out.print("Deskripsi: ");
        String deskripsi = scanner.nextLine();

        Menu menu = new Menu(id, nama, harga, kategori, deskripsi);
        DatabaseHelper.getDB().store(menu);
        DatabaseHelper.getDB().commit();
        showSuccess("Menu berhasil ditambahkan!");
    }

    private static void lihatSemuaMenu() {
        clearScreen();
        ObjectContainer db = DatabaseHelper.getDB();
        ObjectSet<Menu> result = db.queryByExample(Menu.class);

        System.out.println("\n=== DAFTAR MENU ===");
        if (result.isEmpty()) {
            System.out.println("Tidak ada data menu");
        } else {
            result.forEach(System.out::println);
        }
        pause();
    }

    private static void cariMenu() {
        clearScreen();
        System.out.println("\n=== CARI MENU ===");
        System.out.print("Masukkan ID/Nama Menu: ");
        String keyword = scanner.nextLine();
        
        ObjectContainer db = DatabaseHelper.getDB();
        Query query = db.query();
        query.constrain(Menu.class);
        query.descend("id").constrain(keyword).like();
        query.descend("nama").constrain(keyword).like().or(query.descend("id").constrain(keyword).like());
        
        ObjectSet<Menu> result = query.execute();
        
        System.out.println("\n=== HASIL PENCARIAN ===");
        if (result.isEmpty()) {
            System.out.println("Tidak ditemukan menu dengan kriteria tersebut");
        } else {
            while (result.hasNext()) {
                System.out.println(result.next());
            }
        }
        pause();
    }

    private static void updateHargaMenu() {
        clearScreen();
        System.out.println("\n=== UPDATE HARGA MENU ===");
        System.out.print("Masukkan ID Menu yang akan diupdate: ");
        String id = scanner.nextLine();
        
        System.out.print("Harga Baru: ");
        double hargaBaru = scanner.nextDouble();
        scanner.nextLine();
        
        DataUpdater.updateMenuHarga(id, hargaBaru);
        pause();
    }

    private static void hapusMenu() {
        clearScreen();
        System.out.println("\n=== HAPUS MENU ===");
        System.out.print("Masukkan ID Menu yang akan dihapus: ");
        String id = scanner.nextLine();
        
        DataUpdater.deleteMenu(id);
        pause();
    }

    private static void menuPesanan() {
        boolean back = false;
        while (!back) {
            clearScreen();
            System.out.println("=== KELOLA PESANAN ===");
            System.out.println("1. Buat Pesanan Baru");
            System.out.println("2. Lihat Semua Pesanan");
            System.out.println("3. Cari Pesanan");
            System.out.println("4. Update Status Pesanan");
            System.out.println("0. Kembali");
            System.out.print("Pilih: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> buatPesanan();
                    case 2 -> lihatSemuaPesanan();
                    case 3 -> cariPesanan();
                    case 4 -> updateStatusPesanan();
                    case 0 -> back = true;
                    default -> showError("Pilihan tidak valid!");
                }
            } catch (Exception e) {
                handleInputError();
            }
        }
    }

    private static void buatPesanan() {
        clearScreen();
        System.out.println("\n=== BUAT PESANAN BARU ===");

        // Get customer
        System.out.print("ID Pelanggan: ");
        String idPelanggan = scanner.nextLine();
        
        ObjectContainer db = DatabaseHelper.getDB();
        Pelanggan pelanggan = DataUpdater.findPelangganById(idPelanggan);
        
        if (pelanggan == null) {
            showError("Pelanggan tidak ditemukan!");
            return;
        }
        
        System.out.println("Pelanggan: " + pelanggan.getNama());
        
        // Create order
        System.out.print("ID Pesanan: ");
        String idPesanan = scanner.nextLine();
        Pesanan pesanan = new Pesanan(idPesanan, pelanggan);
        
        // Add items
        boolean tambahLagi = true;
        while (tambahLagi) {
            System.out.print("\nID Menu: ");
            String idMenu = scanner.nextLine();
            
            Menu menuProto = new Menu(idMenu, null, 0, null, null);
            ObjectSet<Menu> menuResult = db.queryByExample(menuProto);
            
            if (!menuResult.hasNext()) {
                showError("Menu tidak ditemukan!");
                continue;
            }
            
            Menu menu = menuResult.next();
            System.out.println("Menu: " + menu.getNama() + " - Rp" + menu.getHarga());
            
            System.out.print("Jumlah: ");
            int jumlah = scanner.nextInt();
            scanner.nextLine();
            
            pesanan.addItem(menu, jumlah);
            
            System.out.print("Tambah menu lagi? (y/n): ");
            String jawab = scanner.nextLine();
            tambahLagi = jawab.equalsIgnoreCase("y");
        }
        
        db.store(pesanan);
        db.commit();
        showSuccess("Pesanan berhasil dibuat!");
    }

    private static void lihatSemuaPesanan() {
        clearScreen();
        ObjectContainer db = DatabaseHelper.getDB();
        ObjectSet<Pesanan> result = db.queryByExample(Pesanan.class);

        System.out.println("\n=== DAFTAR PESANAN ===");
        if (result.isEmpty()) {
            System.out.println("Tidak ada data pesanan");
        } else {
            result.forEach(p -> {
                System.out.println(p);
                p.getItems().forEach(item -> System.out.println("  " + item));
            });
        }
        pause();
    }

    private static void cariPesanan() {
        clearScreen();
        System.out.println("\n=== CARI PESANAN ===");
        System.out.print("Masukkan ID Pesanan/Pelanggan: ");
        String keyword = scanner.nextLine();
        
        ObjectContainer db = DatabaseHelper.getDB();
        Query query = db.query();
        query.constrain(Pesanan.class);
        query.descend("id").constrain(keyword).like();
        query.descend("pelanggan").descend("id").constrain(keyword).like().or(query.descend("id").constrain(keyword).like());
        
        ObjectSet<Pesanan> result = query.execute();
        
        System.out.println("\n=== HASIL PENCARIAN ===");
        if (result.isEmpty()) {
            System.out.println("Tidak ditemukan pesanan dengan kriteria tersebut");
        } else {
            while (result.hasNext()) {
                Pesanan pesanan = result.next();
                System.out.println(pesanan);
                System.out.println("Items:");
                pesanan.getItems().forEach(item -> System.out.println("  " + item));
            }
        }
        pause();
    }

    private static void updateStatusPesanan() {
        clearScreen();
        System.out.println("\n=== UPDATE STATUS PESANAN ===");
        System.out.print("Masukkan ID Pesanan: ");
        String id = scanner.nextLine();
        
        ObjectContainer db = DatabaseHelper.getDB();
        Pesanan pesanan = DataUpdater.findPesananById(id);
        
        if (pesanan == null) {
            showError("Pesanan tidak ditemukan!");
            return;
        }
        
        System.out.println("Status saat ini: " + pesanan.getStatus());
        
        System.out.print("Status Baru (Dibuat/Diproses/Selesai): ");
        String status = scanner.nextLine();
        
        pesanan.updateStatus(status);
        db.store(pesanan);
        db.commit();
        showSuccess("Status pesanan berhasil diupdate!");
    }

    private static void menuLaporan() {
        boolean back = false;
        while (!back) {
            clearScreen();
            System.out.println("=== LAPORAN ===");
            System.out.println("1. Laporan Penjualan");
            System.out.println("2. Menu Populer");
            System.out.println("3. Member Aktif");
            System.out.println("0. Kembali");
            System.out.print("Pilih: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> generateLaporanPenjualan();
                    case 2 -> LaporanGenerator.generateLaporanMenuPopuler();
                    case 3 -> LaporanGenerator.generateLaporanMemberAktif();
                    case 0 -> back = true;
                    default -> showError("Pilihan tidak valid!");
                }
            } catch (Exception e) {
                handleInputError();
            }
        }
    }

    private static void generateLaporanPenjualan() {
        clearScreen();
        System.out.println("\n=== LAPORAN PENJUALAN ===");
        
        System.out.println("Masukkan Tanggal Awal (dd/MM/yyyy): ");
        String tglAwalStr = scanner.nextLine();
        System.out.println("Masukkan Tanggal Akhir (dd/MM/yyyy): ");
        String tglAkhirStr = scanner.nextLine();
        
        try {
            Date tglAwal = dateFormat.parse(tglAwalStr + " 00:00");
            Date tglAkhir = dateFormat.parse(tglAkhirStr + " 23:59");
            
            LaporanGenerator.generateLaporanPenjualan(tglAwal, tglAkhir);
        } catch (Exception e) {
            showError("Format tanggal tidak valid!");
        }
        pause();
    }

    private static void backupDatabase() {
        clearScreen();
        System.out.println("\n=== BACKUP DATABASE ===");
        System.out.print("Masukkan path/lokasi backup: ");
        String path = scanner.nextLine();
        
        DatabaseHelper.backupDatabase(path);
        pause();
    }

    private static void contohQuery() {
        clearScreen();
        System.out.println("\n=== CONTOH QUERY ===");
        System.out.println("1. Query By Example (QBE)");
        System.out.println("2. Native Query");
        System.out.println("3. SODA Query");
        System.out.println("0. Kembali");
        System.out.print("Pilih: ");

        try {
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> runQBEExamples();
                case 2 -> runNativeQueryExamples();
                case 3 -> runSODAQueryExamples();
                case 0 -> { return; }
                default -> showError("Pilihan tidak valid!");
            }
        } catch (Exception e) {
            handleInputError();
        }
        pause();
    }

    private static void runQBEExamples() {
        clearScreen();
        System.out.println("\n=== QBE EXAMPLES ===");
        QBEExamples.example1();
        QBEExamples.example2();
        QBEExamples.example3();
        QBEExamples.example4();
        QBEExamples.example5();
        QBEExamples.example6();
    }

    private static void runNativeQueryExamples() {
        clearScreen();
        System.out.println("\n=== NATIVE QUERY EXAMPLES ===");
        NativeQueryExamples.example1();
        NativeQueryExamples.example2();
        NativeQueryExamples.example3();
        NativeQueryExamples.example4();
        NativeQueryExamples.example5();
        NativeQueryExamples.example6();
    }

    private static void runSODAQueryExamples() {
        clearScreen();
        System.out.println("\n=== SODA QUERY EXAMPLES ===");
        SODAQueryExamples.example1();
        SODAQueryExamples.example2();
        SODAQueryExamples.example3();
        SODAQueryExamples.example4();
        SODAQueryExamples.example5();
        SODAQueryExamples.example6();
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void pause() {
        System.out.println("\nTekan Enter untuk melanjutkan...");
        scanner.nextLine();
    }

    private static void showError(String message) {
        System.out.println("\nERROR: " + message);
        pause();
    }

    private static void showSuccess(String message) {
        System.out.println("\nSUCCESS: " + message);
        pause();
    }

    private static void handleInputError() {
        scanner.nextLine(); // clear buffer
        showError("Input tidak valid!");
    }
}