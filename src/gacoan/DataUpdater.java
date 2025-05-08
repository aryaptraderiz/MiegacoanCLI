package gacoan;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;

public class DataUpdater {
    public static void updatePelangganAlamat(String id, String alamatBaru) {
        ObjectContainer db = DatabaseHelper.getDB();
        
        try {
            Pelanggan pelanggan = findPelangganById(id);
            if (pelanggan != null) {
                pelanggan.setAlamat(alamatBaru);
                db.store(pelanggan);
                db.commit();
                System.out.println("Alamat pelanggan berhasil diupdate");
            } else {
                System.out.println("Pelanggan dengan ID " + id + " tidak ditemukan");
            }
        } catch (Exception e) {
            System.err.println("Error updating pelanggan: " + e.getMessage());
        }
    }
    
    public static Pelanggan findPelangganById(String id) {
        ObjectContainer db = DatabaseHelper.getDB();
        
        // Cari sebagai Pelanggan biasa
        Pelanggan proto = new Pelanggan(id, null, null, null);
        ObjectSet<Pelanggan> result = db.queryByExample(proto);
        
        if (result.hasNext()) {
            return result.next();
        }
        
        // Jika tidak ditemukan, cari sebagai Member
        Member protoMember = new Member(id, null, null, null, null);
        ObjectSet<Member> memberResult = db.queryByExample(protoMember);
        
        return memberResult.hasNext() ? memberResult.next() : null;
    }
    
    public static Pesanan findPesananById(String id) {
        ObjectContainer db = DatabaseHelper.getDB();
        Pesanan proto = new Pesanan(id, null);
        ObjectSet<Pesanan> result = db.queryByExample(proto);
        return result.hasNext() ? result.next() : null;
    }
    
    public static void updateMenuHarga(String id, double hargaBaru) {
        ObjectContainer db = DatabaseHelper.getDB();
        
        Menu proto = new Menu(id, null, 0, null, null);
        ObjectSet<Menu> result = db.queryByExample(proto);
        
        if (result.hasNext()) {
            Menu menu = result.next();
            menu.setHarga(hargaBaru);
            db.store(menu);
            db.commit();
            System.out.println("Harga menu berhasil diupdate");
        } else {
            System.out.println("Menu tidak ditemukan");
        }
    }
    
    public static void deletePelanggan(String id) {
        ObjectContainer db = DatabaseHelper.getDB();
        
        Pelanggan pelanggan = findPelangganById(id);
        if (pelanggan != null) {
            db.delete(pelanggan);
            db.commit();
            System.out.println("Pelanggan berhasil dihapus");
        } else {
            System.out.println("Pelanggan tidak ditemukan");
        }
    }
    
    public static void deleteMenu(String id) {
        ObjectContainer db = DatabaseHelper.getDB();
        
        Menu proto = new Menu(id, null, 0, null, null);
        ObjectSet<Menu> result = db.queryByExample(proto);
        
        if (result.hasNext()) {
            Menu menu = result.next();
            db.delete(menu);
            db.commit();
            System.out.println("Menu berhasil dihapus");
        } else {
            System.out.println("Menu tidak ditemukan");
        }
    }
    
    // Query Examples
    public static void queryExampleQBE() {
        ObjectContainer db = DatabaseHelper.getDB();
        System.out.println("\n=== QBE Example: Mie Menus ===");
        
        Menu proto = new Menu(null, null, 0, "Mie", null);
        ObjectSet<Menu> result = db.queryByExample(proto);
        
        while (result.hasNext()) {
            System.out.println(result.next());
        }
    }
    
    public static void queryExampleNative() {
        ObjectContainer db = DatabaseHelper.getDB();
        System.out.println("\n=== Native Query: Orders > Rp50,000 ===");
        
        ObjectSet<Pesanan> result = db.query(new com.db4o.query.Predicate<Pesanan>() {
            public boolean match(Pesanan pesanan) {
                return pesanan.getTotalBayar() > 50000;
            }
        });
        
        while (result.hasNext()) {
            System.out.println(result.next());
        }
    }
    
    public static void queryExampleSODA() {
        ObjectContainer db = DatabaseHelper.getDB();
        System.out.println("\n=== SODA Query: Gold Members ===");
        
        Query query = db.query();
        query.constrain(Member.class);
        query.descend("level").constrain("Gold");
        ObjectSet<Member> result = query.execute();
        
        while (result.hasNext()) {
            System.out.println(result.next());
        }
    }
}