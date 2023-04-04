package models.file;
import java.sql.*;
import models.db.*;

public class buy implements FileInputDBInterface{
    private String uid;
    private String name;
    private String address;
    private String isbn;
    private int item_quantity;
    
    public void parseFromLine(String inputLine){
        String[] tokens = inputLine.split(",");
        uid = tokens[0];
        name = tokens[1];
        address = tokens[2];
        isbn = tokens[3];
        item_quantity = Integer.parseInt(tokens[4]);

    }

    public void insertToDB(Connection conn){
        //buy db
        buyDB buy = new buyDB(uid, isbn, item_quantity);
        buy.insertToDBbyDB(conn);

        //item db
        //itemDB item = new itemDB(isbn, item_quantity);
        //item.insertToDBbyDB(conn);

        //customer db
        customerDB customer = new customerDB(uid, name, address);
        customer.insertToDBbyDB(conn);
    }
}
