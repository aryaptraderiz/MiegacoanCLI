package gacoan;

public class PesananDetail {
    private Menu menu;
    private int jumlah;
    private double hargaSatuan;
    
    public PesananDetail(Menu menu, int jumlah) {
        this.menu = menu;
        this.jumlah = jumlah;
        this.hargaSatuan = menu.getHarga();
    }
    
    public double getSubtotal() {
        return jumlah * hargaSatuan;
    }
    
    public Menu getMenu() { return menu; }
    public int getJumlah() { return jumlah; }
    public double getHargaSatuan() { return hargaSatuan; }
    
    @Override
    public String toString() {
        return String.format("%s x%d | Rp%,.0f | Subtotal: Rp%,.0f", 
               menu.getNama(), jumlah, hargaSatuan, getSubtotal());
    }
}