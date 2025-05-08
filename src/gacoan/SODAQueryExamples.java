package gacoan;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Constraint;
import com.db4o.query.Query;

public class SODAQueryExamples {
    public static void example1() {
        // SODA 1: Find Gold members
        ObjectContainer db = DatabaseHelper.getDB();
        Query query = db.query();
        query.constrain(Member.class);
        query.descend("level").constrain("Gold");
        ObjectSet<Member> result = query.execute();
        System.out.println("\nSODA 1: Gold members");
        while(result.hasNext()) {
            System.out.println(result.next());
        }
    }

    public static void example2() {
        // SODA 2: Find orders with status "Diproses"
        ObjectContainer db = DatabaseHelper.getDB();
        Query query = db.query();
        query.constrain(Pesanan.class);
        query.descend("status").constrain("Diproses");
        ObjectSet<Pesanan> result = query.execute();
        System.out.println("\nSODA 2: Orders in progress");
        while(result.hasNext()) {
            System.out.println(result.next());
        }
    }

    public static void example3() {
        // SODA 3: Find menus not in "Mie" category
        ObjectContainer db = DatabaseHelper.getDB();
        Query query = db.query();
        query.constrain(Menu.class);
        query.descend("kategori").constrain("Mie").not();
        ObjectSet<Menu> result = query.execute();
        System.out.println("\nSODA 3: Menus not in Mie category");
        while(result.hasNext()) {
            System.out.println(result.next());
        }
    }

    public static void example4() {
        // SODA 4: Find orders with total > 50000 AND status "Selesai"
        ObjectContainer db = DatabaseHelper.getDB();
        Query query = db.query();
        query.constrain(Pesanan.class);
        Constraint c1 = query.descend("totalBayar").constrain(50000).greater();
        Constraint c2 = query.descend("status").constrain("Selesai");
        query.constrain(c1).and(c2);
        ObjectSet<Pesanan> result = query.execute();
        System.out.println("\nSODA 4: Completed orders with total > 50000");
        while(result.hasNext()) {
            System.out.println(result.next());
        }
    }

    public static void example5() {
        // SODA 5: Sort customers by registration date
        ObjectContainer db = DatabaseHelper.getDB();
        Query query = db.query();
        query.constrain(Pelanggan.class);
        query.descend("tanggalDaftar").orderAscending();
        ObjectSet<Pelanggan> result = query.execute();
        System.out.println("\nSODA 5: Customers sorted by registration date");
        while(result.hasNext()) {
            System.out.println(result.next());
        }
    }

    public static void example6() {
        // SODA 6: Find menus with price < 10000 OR in "Snack" category
        ObjectContainer db = DatabaseHelper.getDB();
        Query query = db.query();
        query.constrain(Menu.class);
        Constraint c1 = query.descend("harga").constrain(10000).smaller();
        Constraint c2 = query.descend("kategori").constrain("Snack");
        query.constrain(c1).or(c2);
        ObjectSet<Menu> result = query.execute();
        System.out.println("\nSODA 6: Menus with price < 10000 OR in Snack category");
        while(result.hasNext()) {
            System.out.println(result.next());
        }
    }
}