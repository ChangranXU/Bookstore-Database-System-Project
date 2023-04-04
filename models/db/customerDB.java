package models.db;
import java.sql.*;

public class customerDB {
    private String uid;
    private String name;
    private String address;

    public customerDB(String uid, String name, String address){
        this.uid = uid;
        this.name = name;
        this.address = address;
    }

    public void insertToDBbyDB(Connection conn){
        try{
            String sql = "INSERT INTO customer VALUES(?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, uid);
            pstmt.setString(2, name);
            pstmt.setString(3, address);
            pstmt.executeUpdate();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    
}
