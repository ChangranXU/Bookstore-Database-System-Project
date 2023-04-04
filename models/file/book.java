package models.file;
import java.sql.*;
import models.db.*;


public class book implements FileInputDBInterface {
    private String isbn;
    private String title;
    private String[] authors;
    private int inventory_quantity;
    private double price;

    public void parseFromLine(String inputLine) {
        String[] tokens = inputLine.split(",");
        isbn = tokens[0];
        title = tokens[1];
        authors = tokens[2].split(";");
        inventory_quantity = Integer.parseInt(tokens[3]);
        price = Double.parseDouble(tokens[4]);
    }

    public void insertToDB(Connection conn){
        //book db
        bookDB book = new bookDB(isbn, title,  price,inventory_quantity);
        book.insertToDBbyDB(conn);
        //author db
        for(String author: authors){
            authorDB authorDB = new authorDB(author,isbn);
            authorDB.insertToDBbyDB(conn);
        }
    }
}
