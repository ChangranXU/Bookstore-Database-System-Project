package models.db;
import java.sql.*;


public class bookDB {
    private String isbn;
    private String title;
    private double price;
    private int inventory_quantity;

    public bookDB(String isbn, String title, double price, int inventory_quantity) {
        this.isbn = isbn;
        this.title = title;
        this.price = price;
        this.inventory_quantity = inventory_quantity;
    }

    public void insertToDBbyDB(Connection conn){
        try {
            String sql = "REPLACE INTO book (isbn, title, price, inventory_quantity) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, isbn);
            pstmt.setString(2, title);
            pstmt.setDouble(3, price);
            pstmt.setInt(4, inventory_quantity);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("[Error]"+e.getMessage());
        }
    }
    
}
