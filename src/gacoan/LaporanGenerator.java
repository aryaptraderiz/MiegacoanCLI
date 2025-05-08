package gacoan;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LaporanGenerator {
    public static void generateLaporanPenjualan(Date dari, Date sampai) {
        ObjectContainer db = DatabaseHelper.getDB();
        
        Query query = db.query();
        query.constrain(Pesanan.class);
        query.descend("tanggal").constrain(dari).greater().equal();
        query.descend("tanggal").constrain(sampai).smaller().equal();
        query.descend("status").constrain("Selesai");
        
        ObjectSet<Pesanan> result = query.execute();
        
        System.out.println("\n=== LAPORAN PENJUALAN ===");
        System.out.println("Periode: " + dari + " - " + sampai);
        System.out.println("====================================");
        
        double totalPendapatan = 0;
        while (result.hasNext()) {
            Pesanan pesanan = result.next();
            System.out.println(pesanan);
            totalPendapatan += pesanan.getTotalBayar();
        }
        
        System.out.println("====================================");
        System.out.printf("Total Pendapatan: Rp%,.0f\n", totalPendapatan);
    }
    
    public static void generateLaporanMenuPopuler() {
        ObjectContainer db = DatabaseHelper.getDB();
        
        // Query all order details
        ObjectSet<PesananDetail> details = db.queryByExample(PesananDetail.class);
        
        // Count menu popularity
        Map<String, Integer> menuCount = new HashMap<>();
        Map<String, Double> menuRevenue = new HashMap<>();
        
        while (details.hasNext()) {
            PesananDetail detail = details.next();
            String menuName = detail.getMenu().getNama();
            
            // Update count
            menuCount.put(menuName, menuCount.getOrDefault(menuName, 0) + detail.getJumlah());
            
            // Update revenue
            menuRevenue.put(menuName, menuRevenue.getOrDefault(menuName, 0.0) + detail.getSubtotal());
        }
        
        System.out.println("\n=== MENU POPULER ===");
        System.out.printf("%-30s %-10s %-15s\n", "Nama Menu", "Jumlah", "Total Pendapatan");
        System.out.println("------------------------------------------------");
        
        menuCount.entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
            .limit(5)
            .forEach(entry -> {
                String menuName = entry.getKey();
                System.out.printf("%-30s %-10d Rp%,-10.0f\n", 
                    menuName, 
                    entry.getValue(), 
                    menuRevenue.get(menuName));
            });
    }
    
    public static void generateLaporanMemberAktif() {
        ObjectContainer db = DatabaseHelper.getDB();
        
        Query query = db.query();
        query.constrain(Member.class);
        query.descend("poin").orderDescending();
        
        ObjectSet<Member> result = query.execute();
        
        System.out.println("\n=== TOP MEMBER ===");
        System.out.printf("%-5s %-20s %-10s %-10s\n", "Rank", "Nama", "Poin", "Level");
        System.out.println("----------------------------------------");
        
        int rank = 1;
        while (result.hasNext() && rank <= 10) {
            Member member = result.next();
            System.out.printf("%-5d %-20s %-10d %-10s\n", 
                rank++, 
                member.getNama(), 
                member.getPoin(),
                member.getLevel());
        }
    }
}