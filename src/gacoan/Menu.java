package gacoan;

public class Menu {
    private String id;
    private String nama;
    private double harga;
    private String kategori;
    private String deskripsi;
    private boolean tersedia;
    
    public Menu(String id, String nama, double harga, String kategori, String deskripsi) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.kategori = kategori;
        this.deskripsi = deskripsi;
        this.tersedia = true;
    }
    
    public String getId() { return id; }
    public String getNama() { return nama; }
    public double getHarga() { return harga; }
    public String getKategori() { return kategori; }
    public String getDeskripsi() { return deskripsi; }
    public boolean isTersedia() { return tersedia; }
    
    public void setHarga(double harga) { this.harga = harga; }
    public void setTersedia(boolean tersedia) { this.tersedia = tersedia; }
    
    @Override
    public String toString() {
        return String.format("ID: %s | %s | Rp%,.0f | %s | %s | %s", 
               id, nama, harga, kategori, 
               tersedia ? "Tersedia" : "Habis", 
               deskripsi);
    }
}