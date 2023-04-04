package models.db;
import java.sql.*;

public class buyDB {
    private String uid;
    private String isbn;
    private int item_quantity;

    public buyDB(String uid, String isbn, int item_quantity) {
        this.uid = uid;
        this.isbn = isbn;
        this.item_quantity = item_quantity;
    }

    public void insertToDBbyDB(Connection conn){
        try {
            String sql = "REPLACE INTO buy (uid, isbn, item_quantity) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, uid);
            pstmt.setString(2, isbn);
            pstmt.setInt(3, item_quantity);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("[Error]"+e.getMessage());
        }
    }
    
}
