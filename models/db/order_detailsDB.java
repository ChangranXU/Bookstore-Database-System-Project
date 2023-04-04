package models.db;
import java.sql.*;

public class order_detailsDB {
    private String oid;
    private int order_quantity;
    private String shipping_status;
    private Timestamp order_time;

    public order_detailsDB(String oid, int order_quantity, String shipping_status, Timestamp order_time) {
        this.oid = oid;
        this.order_quantity = order_quantity;
        this.shipping_status = shipping_status;
        this.order_time = order_time;
    }

    public void insertToDBbyDB(Connection conn){
        try {
            String sql = "REPLACE INTO order_details (oid, order_quantity, shipping_status, order_time) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, oid);
            pstmt.setInt(2, order_quantity);
            pstmt.setString(3, shipping_status);
            pstmt.setTimestamp(4, order_time);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("[Error]"+e.getMessage());
        }
    }
    
}
