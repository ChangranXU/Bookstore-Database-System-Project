package models.db;
import java.sql.*;

public class authorDB {
    private String author_name;
    private String isbn;

    public authorDB(String author_name, String isbn) {
        this.author_name = author_name;
        this.isbn = isbn;
    }
    
    public void insertToDBbyDB(Connection conn){
        try{
            String sql = "INSERT INTO author(author_name,isbn) VALUES(?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, author_name);
            pstmt.setString(2, isbn);
            pstmt.executeUpdate();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
