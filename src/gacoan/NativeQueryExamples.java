package gacoan;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import com.db4o.query.QueryComparator;
import java.util.List;  // Added this import

public class NativeQueryExamples {
    public static void example1() {
        // Native 1: Find orders with total payment > 50000
        ObjectContainer db = DatabaseHelper.getDB();
        ObjectSet<Pesanan> result = db.query(new Predicate<Pesanan>() {
            public boolean match(Pesanan pesanan) {
                return pesanan.getTotalBayar() > 50000;
            }
        });
        System.out.println("\nNative 1: Orders with total > 50000");
        while(result.hasNext()) {
            System.out.println(result.next());
        }
    }

    public static void example2() {
        // Native 2: Find menus between 10000 and 20000 price
        ObjectContainer db = DatabaseHelper.getDB();
        ObjectSet<Menu> result = db.query(new Predicate<Menu>() {
            public boolean match(Menu menu) {
                return menu.getHarga() >= 10000 && menu.getHarga() <= 20000;
            }
        });
        System.out.println("\nNative 2: Menus between 10000-20000 price");
        while(result.hasNext()) {
            System.out.println(result.next());
        }
    }

    public static void example3() {
        // Native 3: Find customers from specific address
        ObjectContainer db = DatabaseHelper.getDB();
        ObjectSet<Pelanggan> result = db.query(new Predicate<Pelanggan>() {
            public boolean match(Pelanggan pelanggan) {
                return pelanggan.getAlamat().contains("Jl. Merdeka");
            }
        });
        System.out.println("\nNative 3: Customers from Jl. Merdeka");
        while(result.hasNext()) {
            System.out.println(result.next());
        }
    }

    public static void example4() {
        // Native 4: Sort menus by price descending
        ObjectContainer db = DatabaseHelper.getDB();
        List<Menu> menus = db.query(new Predicate<Menu>() {
            public boolean match(Menu menu) {
                return true;
            }
        }, new QueryComparator<Menu>() {
            public int compare(Menu m1, Menu m2) {
                return Double.compare(m2.getHarga(), m1.getHarga());
            }
        });
        System.out.println("\nNative 4: Menus sorted by price desc");
        for(Menu m : menus) {
            System.out.println(m);
        }
    }

    public static void example5() {
        // Native 5: Find members with points > 100
        ObjectContainer db = DatabaseHelper.getDB();
        ObjectSet<Member> result = db.query(new Predicate<Member>() {
            public boolean match(Member member) {
                return member.getPoin() > 100;
            }
        });
        System.out.println("\nNative 5: Members with points > 100");
        while(result.hasNext()) {
            System.out.println(result.next());
        }
    }

    public static void example6() {
        // Native 6: Find orders containing specific menu
        ObjectContainer db = DatabaseHelper.getDB();
        ObjectSet<Pesanan> result = db.query(new Predicate<Pesanan>() {
            public boolean match(Pesanan pesanan) {
                for(PesananDetail item : pesanan.getItems()) {
                    if(item.getMenu().getId().equals("M001")) {
                        return true;
                    }
                }
                return false;
            }
        });
        System.out.println("\nNative 6: Orders containing M001 menu");
        while(result.hasNext()) {
            System.out.println(result.next());
        }
    }
}