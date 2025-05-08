package gacoan;

public class Member extends Pelanggan {
    private String level;
    private int poin;
    private double diskon;
    
    public Member(String id, String nama, String noHp, String alamat, String level) {
        super(id, nama, noHp, alamat);
        this.level = level;
        this.poin = 0;
        setDiskonBasedOnLevel();
    }
    
    private void setDiskonBasedOnLevel() {
        switch (level.toLowerCase()) {
            case "silver": diskon = 0.05; break;
            case "gold": diskon = 0.10; break;
            case "platinum": diskon = 0.15; break;
            default: diskon = 0.0;
        }
    }
    
    public String getLevel() { return level; }
    public int getPoin() { return poin; }
    public double getDiskon() { return diskon; }
    
    public void tambahPoin(int tambahan) {
        poin += tambahan;
        updateLevel();
    }
    
    private void updateLevel() {
        if (poin >= 1000) {
            level = "Platinum";
            diskon = 0.15;
        } else if (poin >= 500) {
            level = "Gold";
            diskon = 0.10;
        } else if (poin >= 100) {
            level = "Silver";
            diskon = 0.05;
        }
    }
    
    @Override
    public String toString() {
        return super.toString() + String.format(" | Level: %s | Poin: %d | Diskon: %.0f%%", 
               level, poin, diskon*100);
    }
}