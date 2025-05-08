package gacoan;

import com.db4o.*;
import com.db4o.config.*;

public class DatabaseHelper {
    private static ObjectContainer db;
    
    public static void initialize() {
        try {
            EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
            
            // Configure indexes for better performance
            config.common().objectClass(Pelanggan.class).objectField("id").indexed(true);
            config.common().objectClass(Member.class).objectField("id").indexed(true);
            config.common().objectClass(Menu.class).objectField("id").indexed(true);
            config.common().objectClass(Pesanan.class).objectField("id").indexed(true);
            
            db = Db4oEmbedded.openFile(config, "mie_gacoan.db4o");
            System.out.println("Database initialized successfully");
        } catch (Exception e) {
            System.err.println("Database initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static ObjectContainer getDB() {
        if (db == null || db.ext().isClosed()) {
            initialize();
        }
        return db;
    }
    
    public static void closeDB() {
        if (db != null) {
            db.close();
            System.out.println("Database closed successfully");
        }
    }
    
    public static void backupDatabase(String backupPath) {
        try {
            db.ext().backup(backupPath);
            System.out.println("Database backup created at: " + backupPath);
        } catch (Exception e) {
            System.err.println("Backup failed: " + e.getMessage());
        }
    }
}