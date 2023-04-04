package models.db;
import java.sql.*;

public class orderDB {
    private String oid;
    private String uid;
    private int item_quantity;
    private String isbn;

    public orderDB(String oid, String uid, int item_quantity, String isbn) {
        this.oid = oid;
        this.uid = uid;
        this.item_quantity = item_quantity;
        this.isbn = isbn;
    }

    public void insertToDBbyDB(Connection conn){
        try {
            String sql = "INSERT INTO order (oid, uid, item_quantity, isbn) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, oid);
            pstmt.setString(2, uid);
            pstmt.setInt(3, item_quantity);
            pstmt.setString(4, isbn);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
}
