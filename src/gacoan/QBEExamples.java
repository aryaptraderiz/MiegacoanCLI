package gacoan;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;

public class QBEExamples {
    public static void example1() {
        // QBE 1: Find customer by exact ID
        ObjectContainer db = DatabaseHelper.getDB();
        Pelanggan proto = new Pelanggan("P001", null, null, null);
        ObjectSet<Pelanggan> result = db.queryByExample(proto);
        System.out.println("\nQBE 1: Find customer with ID P001");
        while(result.hasNext()) {
            System.out.println(result.next());
        }
    }

    public static void example2() {
        // QBE 2: Find all members with Gold level
        ObjectContainer db = DatabaseHelper.getDB();
        Member proto = new Member(null, null, null, null, "Gold");
        ObjectSet<Member> result = db.queryByExample(proto);
        System.out.println("\nQBE 2: Find all Gold members");
        while(result.hasNext()) {
            System.out.println(result.next());
        }
    }

    public static void example3() {
        // QBE 3: Find all noodle menus
        ObjectContainer db = DatabaseHelper.getDB();
        Menu proto = new Menu(null, null, 0, "Mie", null);
        ObjectSet<Menu> result = db.queryByExample(proto);
        System.out.println("\nQBE 3: Find all noodle menus");
        while(result.hasNext()) {
            System.out.println(result.next());
        }
    }

    public static void example4() {
        // QBE 4: Find completed orders
        ObjectContainer db = DatabaseHelper.getDB();
        Pesanan proto = new Pesanan(null, null);
        proto.updateStatus("Selesai");
        ObjectSet<Pesanan> result = db.queryByExample(proto);
        System.out.println("\nQBE 4: Find completed orders");
        while(result.hasNext()) {
            System.out.println(result.next());
        }
    }

    public static void example5() {
        // QBE 5: Find available menus
        ObjectContainer db = DatabaseHelper.getDB();
        Menu proto = new Menu(null, null, 0, null, null);
        proto.setTersedia(true);
        ObjectSet<Menu> result = db.queryByExample(proto);
        System.out.println("\nQBE 5: Find available menus");
        while(result.hasNext()) {
            System.out.println(result.next());
        }
    }

    public static void example6() {
        // QBE 6: Find orders for specific customer
        ObjectContainer db = DatabaseHelper.getDB();
        Pelanggan custProto = new Pelanggan("P001", null, null, null);
        Pesanan proto = new Pesanan(null, custProto);
        ObjectSet<Pesanan> result = db.queryByExample(proto);
        System.out.println("\nQBE 6: Find orders for customer P001");
        while(result.hasNext()) {
            System.out.println(result.next());
        }
    }
}