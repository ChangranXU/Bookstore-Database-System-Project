package models.db;
import java.sql.*;

public class itemDB {
    private String isbn;
    private int item_quantity;

    public itemDB(String isbn, int item_quantity) {
        this.isbn = isbn;
        this.item_quantity = item_quantity;
    }

    public void insertToDBbyDB(Connection conn){
        try {
            String sql = "REPLACE INTO item (isbn, item_quantity) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, isbn);
            pstmt.setInt(2, item_quantity);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("[Error]"+e.getMessage());
        }
    }
    
}
