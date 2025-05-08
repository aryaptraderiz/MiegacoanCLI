package gacoan;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Pesanan {
    private String id;
    private Date tanggal;
    private Pelanggan pelanggan;
    private List<PesananDetail> items;
    private String status;
    private double totalHarga;
    private double diskon;
    private double pajak;
    private double totalBayar;
    
    public Pesanan(String id, Pelanggan pelanggan) {
        this.id = id;
        this.tanggal = new Date();
        this.pelanggan = pelanggan;
        this.items = new ArrayList<>();
        this.status = "Dibuat";
        this.totalHarga = 0;
        this.diskon = 0;
        this.pajak = 0;
        this.totalBayar = 0;
        
        if (pelanggan instanceof Member) {
            this.diskon = ((Member) pelanggan).getDiskon();
        }
    }
    
    public void addItem(Menu menu, int jumlah) {
        PesananDetail detail = new PesananDetail(menu, jumlah);
        items.add(detail);
        hitungTotal();
    }
    
    public void hitungTotal() {
        totalHarga = items.stream().mapToDouble(d -> d.getSubtotal()).sum();
        pajak = totalHarga * 0.1; // 10% tax
        totalBayar = (totalHarga + pajak) * (1 - diskon);
    }
    
    public void updateStatus(String status) {
        this.status = status;
        if (status.equals("Selesai") && pelanggan instanceof Member) {
            int poin = (int) (totalHarga / 10000); // 1 point per Rp10,000
            ((Member) pelanggan).tambahPoin(poin);
        }
    }
    
    public String getId() { return id; }
    public Date getTanggal() { return tanggal; }
    public Pelanggan getPelanggan() { return pelanggan; }
    public List<PesananDetail> getItems() { return items; }
    public String getStatus() { return status; }
    public double getTotalHarga() { return totalHarga; }
    public double getDiskon() { return diskon; }
    public double getPajak() { return pajak; }
    public double getTotalBayar() { return totalBayar; }
    
    @Override
    public String toString() {
        return String.format("ID: %s | Tgl: %s | Pelanggan: %s | Total: Rp%,.0f | Status: %s", 
               id, tanggal, pelanggan.getNama(), totalBayar, status);
    }
}