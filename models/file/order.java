package models.file;
import java.sql.*;
import models.db.*;

public class order implements FileInputDBInterface {
    private String oid;
    private String uid;
    private String isbn;
    private int item_quantity;
    private int order_quantity;
    private String shipping_status;
    private Timestamp order_time;

    public void parseFromLine(String inputLine) {
        String[] tokens = inputLine.split(",");
        oid = tokens[0];
        uid = tokens[1];
        isbn = tokens[2];
        item_quantity = Integer.parseInt(tokens[3]);
        order_quantity = Integer.parseInt(tokens[4]);
        shipping_status = tokens[5];
        order_time = Timestamp.valueOf(tokens[6]);
    }

    public void insertToDB(Connection conn){
        //order_details db
        order_detailsDB order_details = new order_detailsDB(oid, order_quantity, shipping_status, order_time);
        order_details.insertToDBbyDB(conn);

        //order db
        orderDB order = new orderDB(oid, uid, item_quantity,isbn);
        order.insertToDBbyDB(conn);
    }

    
}
