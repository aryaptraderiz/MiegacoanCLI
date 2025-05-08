package gacoan;

import java.util.Date;

public class Pelanggan {
    private String id;
    private String nama;
    private String noHp;
    private String alamat;
    private Date tanggalDaftar;
    
    public Pelanggan(String id, String nama, String noHp, String alamat) {
        this.id = id;
        this.nama = nama;
        this.noHp = noHp;
        this.alamat = alamat;
        this.tanggalDaftar = new Date();
    }
    
    public String getId() { return id; }
    public String getNama() { return nama; }
    public String getNoHp() { return noHp; }
    public String getAlamat() { return alamat; }
    public Date getTanggalDaftar() { return tanggalDaftar; }
    
    public void setNama(String nama) { this.nama = nama; }
    public void setNoHp(String noHp) { this.noHp = noHp; }
    public void setAlamat(String alamat) { this.alamat = alamat; }
    
    @Override
    public String toString() {
        return String.format("ID: %s | Nama: %s | Telp: %s | Alamat: %s | Terdaftar: %s", 
               id, nama, noHp, alamat, tanggalDaftar);
    }
}