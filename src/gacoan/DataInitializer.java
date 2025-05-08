package gacoan;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import java.util.Arrays;

public class DataInitializer {
    public static void initializeSampleData() {
        ObjectContainer db = DatabaseHelper.getDB();
        
        try {
            // Clear existing data
            ObjectSet<Object> allObjects = db.queryByExample(Object.class);
            while (allObjects.hasNext()) {
                db.delete(allObjects.next());
            }
            
            // Create sample menus
            Menu[] menus = {
                new Menu("M001", "Mie Gacoan Level 1", 15000, "Mie", "Pedas level 1"),
                new Menu("M002", "Mie Gacoan Level 5", 25000, "Mie", "Pedas level 5"),
                new Menu("M003", "Mie Gacoan Keju", 20000, "Mie", "Dengan keju leleh"),
                new Menu("M004", "Mie Gacoan Seafood", 22000, "Mie", "Dengan seafood segar"),
                new Menu("D001", "Es Teh Manis", 5000, "Minuman", "Teh manis dingin"),
                new Menu("D002", "Es Jeruk", 7000, "Minuman", "Jeruk segar"),
                new Menu("D003", "Air Mineral", 3000, "Minuman", "Air mineral dingin"),
                new Menu("S001", "Kerupuk Udang", 3000, "Snack", "Kerupuk udang renyah"),
                new Menu("S002", "Tahu Crispy", 8000, "Snack", "Tahu goreng crispy"),
                new Menu("S003", "Kentang Goreng", 10000, "Snack", "Kentang goreng renyah")
            };
            
            Arrays.stream(menus).forEach(db::store);
            
            // Create sample customers
            Pelanggan[] pelanggans = {
                new Pelanggan("P001", "Budi Santoso", "0812345678", "Jl. Merdeka 123"),
                new Pelanggan("P002", "Siti Aminah", "0812987654", "Jl. Sudirman 45"),
                new Pelanggan("P003", "Rina Dewi", "0815112233", "Jl. Pahlawan 56"),
                new Member("P004", "Andi Wijaya", "0811122233", "Jl. Gatot Subroto 10", "Silver"),
                new Member("P005", "Dewi Lestari", "0814455566", "Jl. Pemuda 8", "Gold"),
                new Member("P006", "Eko Pratama", "0817788990", "Jl. Diponegoro 15", "Platinum"),
                new Member("P007", "Andy", "0817788922", "Jl. Pulau Bidadari", "Gold"),
            };
            
            Arrays.stream(pelanggans).forEach(db::store);
            
            // Create sample orders
            Pesanan pesanan1 = new Pesanan("ORD001", pelanggans[0]);
            pesanan1.addItem(menus[0], 2);
            pesanan1.addItem(menus[4], 1);
            pesanan1.updateStatus("Selesai");
            
            Pesanan pesanan2 = new Pesanan("ORD002", pelanggans[3]);
            pesanan2.addItem(menus[1], 1);
            pesanan2.addItem(menus[7], 2);
            pesanan2.addItem(menus[5], 1);
            pesanan2.updateStatus("Diproses");
            
            Pesanan pesanan3 = new Pesanan("ORD003", pelanggans[5]);
            pesanan3.addItem(menus[2], 2);
            pesanan3.addItem(menus[8], 3);
            pesanan3.addItem(menus[9], 1);
            pesanan3.updateStatus("Dibuat");
            
            db.store(pesanan1);
            db.store(pesanan2);
            db.store(pesanan3);
            
            db.commit();
            System.out.println("Sample data initialized successfully");
        } catch (Exception e) {
            System.err.println("Error initializing data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}