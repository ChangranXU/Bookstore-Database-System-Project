package models;

import java.sql.*;
import java.io.*;
import java.util.ArrayList;

import models.file.*;

public class Database {
    final String dbAddr = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/test";
    final String dbUsername = "csci3170";
    final String dbPassword = "testfor3170";

    //final String[] tableNames = {"customer", "book", "item", "author", "order_details", "buy","order"};
    final String[] tableNames = {"customer", "book", "author", "order_details", "buy","order"};

    private Connection conn = null;

    public void connect() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        this.conn = DriverManager.getConnection(dbAddr, dbUsername, dbPassword);
    }

    // ======Main Menu Operations ======//
    public void countAndPrintAllRecordsInTables() {
        for (int i = 0; i < tableNames.length; i++) {
            try {
                PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM " + tableNames[i]);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                int count = rs.getInt(1);
                System.out.println(tableNames[i] + ": " + count);
            } catch (SQLException e) {
                System.out.println("[Error] No such table or tables are not initialized yet.");
            }
        }
        System.out.println();
    }

    // ====== DATABASE OPERATIONS =======

    //create tables
    public void createAllTables() throws SQLException{
        PreparedStatement[] stmts={
            conn.prepareStatement("CREATE TABLE customer (uid VARCHAR(45) NOT NULL, name VARCHAR(45) NOT NULL, address VARCHAR(100) NOT NULL, PRIMARY KEY (uid))"),
            conn.prepareStatement("CREATE TABLE book (isbn VARCHAR(45) NOT NULL, title VARCHAR(45) NOT NULL, price double NOT NULL, inventory_quantity int NOT NULL, PRIMARY KEY (isbn))"),
            //conn.prepareStatement("CREATE TABLE item (isbn VARCHAR(45) NOT NULL, item_quantity int NOT NULL, PRIMARY KEY (isbn, item_quantity))"),
            conn.prepareStatement("CREATE TABLE author (isbn VARCHAR(45) NOT NULL, author_name VARCHAR(45) NOT NULL, PRIMARY KEY (isbn, author_name))"),
            conn.prepareStatement("CREATE TABLE order_details (oid VARCHAR(45) NOT NULL, order_time TIMESTAMP NOT NULL, order_quantity int NOT NULL, shipping_status VARCHAR(45) NOT NULL, PRIMARY KEY (oid))"),
            conn.prepareStatement("CREATE TABLE buy (uid VARCHAR(45) NOT NULL, isbn VARCHAR(45) NOT NULL, item_quantity int NOT NULL, PRIMARY KEY (uid, isbn, item_quantity))"),
            conn.prepareStatement("CREATE TABLE order (uid VARCHAR(45) NOT NULL, oid VARCHAR(45) NOT NULL, item_quantity int NOT NULL, isbn  VARCHAR(45) NOT NULL, PRIMARY KEY (uid, oid,item_quantity,isbn))")
        };
        for (int i = 0; i < stmts.length; i++) {
            stmts[i].execute();
        }
    } 

    //delete tables
    public void deleteAllTables() throws SQLException{
        PreparedStatement[] stmts={
            conn.prepareStatement("DROP TABLE customer"),
            conn.prepareStatement("DROP TABLE book"),
            //conn.prepareStatement("DROP TABLE item"),
            conn.prepareStatement("DROP TABLE author"),
            conn.prepareStatement("DROP TABLE order_details"),
            conn.prepareStatement("DROP TABLE buy"),
            conn.prepareStatement("DROP TABLE order")
        };
        for (int i = 0; i < stmts.length; i++) {
            stmts[i].execute();
        }
    }

    //load data
    public void loadDataFromFiles(String folderPath) throws SQLException, IOException{
        InputDB(buy.class, folderPath + "/buy.csv");
        InputDB(book.class, folderPath + "/book.csv");
        InputDB(order.class, folderPath + "/order.csv");
    }

    private void InputDB(Class<?> type, String filePath) {
        try{
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = br.readLine()) != null) {
            FileInputDBInterface file= (FileInputDBInterface) type.getDeclaredConstructor().newInstance(type);
            file.parseFromLine(line);
            file.insertToDB(conn);
            line=br.readLine();
        }
        br.close();}
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // ======Customer Operations ======//
    public void placeOrder(String userID, String orderID, ArrayList<String> ISBNList,ArrayList<Integer>item_quantity){
        try{
            //insert into order_details
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO order_details (oid, order_quantity, shipping_status, order_date) VALUES (?, ?, ?, ?)");
            stmt.setString(1, orderID);
            int sum=0;
            for(int i=0;i<item_quantity.size();i++){
                sum+=item_quantity.get(i);
            }
            stmt.setInt(2, sum);
            stmt.setString(3, "Ordered");
            stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            stmt.execute();

            //modify book inventory_quantity
            for(int i=0;i<ISBNList.size();i++){
                stmt=conn.prepareStatement("UPDATE book SET inventory_quantity = inventory_quantity - ? WHERE isbn = ?");
                stmt.setInt(1, item_quantity.get(i));
                stmt.setString(1,ISBNList.get(i));
                stmt.execute();
            }

            //add buy record
            for(int i=0;i<ISBNList.size();i++){
                stmt=conn.prepareStatement("INSERT INTO buy (uid, isbn, item_quantity) VALUES (?, ?, ?)");
                stmt.setString(1, userID);
                stmt.setString(2, ISBNList.get(i));
                stmt.setInt(3, item_quantity.get(i));
                stmt.execute();
            }

            //add order record
            for(int i=0;i<ISBNList.size();i++){
                stmt=conn.prepareStatement("INSERT INTO order ( oid, uid,item_quantity, isbn) VALUES (?, ?, ?, ?)");
                stmt.setString(1, orderID);
                stmt.setString(2, userID);
                stmt.setInt(3, item_quantity.get(i));
                stmt.setString(4, ISBNList.get(i));
                stmt.execute();
            }

            System.out.println("Order placed successfully.");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void printHistoryOrders(String userID){
        try {
            //no record
            PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(oid) FROM order_details WHERE oid in (SELECT DISTINCT oid FROM order WHERE uid =?)");
            stmt.setString(1, userID);
            ResultSet num = stmt.executeQuery();
            num.next();
            int count = num.getInt(1);
            if(count==0) {
                System.out.println("|                             |");
                System.out.println("     No order record found.");
                System.out.println("|                             |");
                return;
            }

            //print order record
            System.out.println("|oid|Order Quantity|Order Date|Order Status|");
            stmt = conn.prepareStatement("SELECT oid FROM order_details WHERE oid in (SELECT DISTINCT oid FROM order WHERE uid =?)");
            stmt.setString(1, userID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String oid = rs.getString(1);
                stmt = conn.prepareStatement("SELECT order_date, shipping_status  FROM order_details WHERE oid = ?");
                stmt.setString(1, oid);
                ResultSet rs2 = stmt.executeQuery();
                rs2.next();
                Timestamp order_date = rs2.getTimestamp(1);
                String shipping_status = rs2.getString(2);
                stmt = conn.prepareStatement("SELECT isbn, item_quantity FROM order WHERE oid = ?");
                stmt.setString(1, oid);
                ResultSet rs3 = stmt.executeQuery();
                while(rs3.next()){
                    String isbn = rs3.getString(1);
                    int item_quantity = rs3.getInt(2);
                    stmt = conn.prepareStatement("SELECT title FROM book WHERE isbn = ?");
                    stmt.setString(1, isbn);
                    ResultSet rs4 = stmt.executeQuery();
                    rs4.next();
                    String title = rs4.getString(1);
                    System.out.println("|"+oid+"|"+title+"|"+item_quantity+"|"+TimeConv.timeToStr(order_date)+"|"+shipping_status+"|");
                }
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void printBookListByISBN(String isbn){
        try {
            //no record
            PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(isbn) FROM book WHERE isbn = ?");
            stmt.setString(1, isbn);
            ResultSet num = stmt.executeQuery();
            num.next();
            int count = num.getInt(1);
            if(count==0) {
                System.out.println("|                             |");
                System.out.println("     No book record found.");
                System.out.println("|                             |");
                return;
            }

            //print book record
            System.out.println("|ISBN|Title|Author|Price|Inventory Quantity|");
            stmt = conn.prepareStatement("SELECT title, price, inventory_quantity FROM book WHERE isbn = ?");
            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            String title = rs.getString(1);
            double price = rs.getDouble(2);
            int inventory_quantity = rs.getInt(4);
            stmt = conn.prepareStatement("SELECT author_name FROM author WHERE isbn = ?");
            stmt.setString(1, isbn);
            ResultSet rs2 = stmt.executeQuery();
            ArrayList<String> author =new ArrayList<String>();
            while(rs2.next()){
                author.add(rs2.getString(1));
            }
            System.out.printf("|"+isbn+"|"+title+"|");
            for(int i=0;i<author.size();i++){
                System.out.printf(author.get(i));
                if(i!=author.size()-1) System.out.printf(",");
            }
            System.out.println("|"+price+"|"+inventory_quantity+"|");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void printBookListByTitle(String title){
        try {
            //no record
            PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(isbn) FROM book WHERE title = ?");
            stmt.setString(1, title);
            ResultSet num = stmt.executeQuery();
            num.next();
            int count = num.getInt(1);
            if(count==0) {
                System.out.println("|                             |");
                System.out.println("     No book record found.");
                System.out.println("|                             |");
                return;
            }

            //print book record
            System.out.println("|ISBN|Title|Author|Price|Inventory Quantity|");
            stmt = conn.prepareStatement("SELECT isbn, price, inventory_quantity FROM book WHERE title = ?");
            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                String isbn = rs.getString(1);
                double price = rs.getDouble(2);
                int inventory_quantity = rs.getInt(4);
                stmt = conn.prepareStatement("SELECT author_name FROM author WHERE isbn = ?");
                stmt.setString(1, isbn);
                ResultSet rs2 = stmt.executeQuery();
                ArrayList<String> author =new ArrayList<String>();
                while(rs2.next()){
                    author.add(rs2.getString(1));
                }
                System.out.printf("|"+isbn+"|"+title+"|");
                for(int i=0;i<author.size();i++){
                    System.out.printf(author.get(i));
                    if(i!=author.size()-1) System.out.printf(",");
                }
                System.out.println("|"+price+"|"+inventory_quantity+"|");
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void printBookListByAuthor(String author){
        try {
            //no record
            PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(isbn) FROM author WHERE author_name = ?");
            stmt.setString(1, author);
            ResultSet num = stmt.executeQuery();
            num.next();
            int count = num.getInt(1);
            if(count==0) {
                System.out.println("|                             |");
                System.out.println("     No book record found.");
                System.out.println("|                             |");
                return;
            }

            //print book record
            System.out.println("|ISBN|Title|Author|Price|Inventory Quantity|");
            stmt = conn.prepareStatement("SELECT isbn FROM author WHERE author_name = ?");
            stmt.setString(1, author);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                String isbn = rs.getString(1);
                stmt = conn.prepareStatement("SELECT title, price, inventory_quantity FROM book WHERE isbn = ?");
                stmt.setString(1, isbn);
                ResultSet rs2 = stmt.executeQuery();
                rs2.next();
                String title = rs2.getString(1);
                double price = rs2.getDouble(2);
                int inventory_quantity = rs2.getInt(4);
                System.out.println("|"+isbn+"|"+title+"|"+author+"|"+price+"|"+inventory_quantity+"|");
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // ======Bookstore Operations ======//
    public void printOrderedOrder(String userID){

        try {
            //no record
            PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(oid) FROM order_details WHERE shipping_status = 'Ordered' AND oid in (SELECT DISTINCT oid FROM order WHERE uid =?)");
            stmt.setString(1, userID);
            ResultSet num = stmt.executeQuery();
            num.next();
            int count = num.getInt(1);
            if(count==0) {
                System.out.println("|                             |");
                System.out.println("     No order record found.");
                System.out.println("|                             |");
                return;
            }

            System.out.println("Ordered Order Record:");
            System.out.println("|oid|Order Quantity|Order Date|");
            stmt = conn.prepareStatement("SELECT oid FROM order_details WHERE shipping_status = 'Shipped' AND oid in (SELECT DISTINCT oid FROM order WHERE uid =?)");
            stmt.setString(1, userID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String oid = rs.getString(1); 
                int orderQuantity;
                Timestamp orderTime;

                //order quantity, order time
                stmt = conn.prepareStatement("SELECT order_quantity, order_time FROM order_details WHERE oid = ?  AND shipping_status = 'Ordered' ORDER BY order_time DESC");
                stmt.setString(1, oid);
                ResultSet rs2 = stmt.executeQuery();
                rs2.next();
                orderQuantity=rs2.getInt(1);
                orderTime=rs2.getTimestamp(2);

                //print result
                System.out.println("|" + oid + "|" + orderQuantity + "|" + TimeConv.timeToStr(orderTime) + "|");
            }
            System.out.println("End of Query\n");
        } catch (SQLException e) {
            System.out.println("[Error] Failed to list the records.\n");
        }
    }

    public void printShippedOrder(String userID){

        try {
            //no record
            PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(oid) FROM order_details WHERE shipping_status = 'Shipped' AND oid in (SELECT DISTINCT oid FROM order WHERE uid =?)");
            stmt.setString(1, userID);
            ResultSet num = stmt.executeQuery();
            num.next();
            int count = num.getInt(1);
            if(count==0) {
                System.out.println("|                             |");
                System.out.println("     No order record found.");
                System.out.println("|                             |");
                return;
            }

            System.out.println("Ordered Shipped Record:");
            System.out.println("|oid|Order Quantity|Order Time|");
            stmt = conn.prepareStatement("SELECT oid FROM order_details WHERE shipping_status = 'Shipped' AND oid in (SELECT DISTINCT oid FROM order WHERE uid =?)");
            stmt.setString(1, userID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String oid = rs.getString(1); 
                int orderQuantity;
                Timestamp orderTime;

                //order quantity, ordeer date
                stmt = conn.prepareStatement("SELECT order_quantity, order_time FROM order_details WHERE oid = ?  AND shipping_status = 'Shipped' ORDER BY order_time DESC");
                stmt.setString(1, oid);
                ResultSet rs2 = stmt.executeQuery();
                rs2.next();
                orderQuantity=rs2.getInt(1);
                orderTime=rs2.getTimestamp(2);

                //print result
                System.out.println("|" + oid + "|" + orderQuantity + "|" + TimeConv.timeToStr(orderTime) + "|");
            }
            System.out.println("End of Query\n");
        } catch (SQLException e) {
            System.out.println("[Error] Failed to list the records.\n");
        }
    }

    public void printReceivedOrder(String userID){

        try {
            //no record
            PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(oid) FROM order_details WHERE shipping_status = 'Received' AND oid in (SELECT DISTINCT oid FROM order WHERE uid =?)");
            stmt.setString(1, userID);
            ResultSet num = stmt.executeQuery();
            num.next();
            int count = num.getInt(1);
            if(count==0) {
                System.out.println("|                             |");
                System.out.println("    No order record found.");
                System.out.println("|                             |");
                return;
            }

            System.out.println("Ordered Received Record:");
            System.out.println("|oid|Order Quantity|Order Time|");
            stmt = conn.prepareStatement("SELECT oid FROM order_details WHERE shipping_status = 'Received' AND oid in (SELECT DISTINCT oid FROM order WHERE uid =?)");
            stmt.setString(1, userID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String oid = rs.getString(1); 
                int orderQuantity;
                Timestamp orderTime;

                //order quantity, ordeer date
                stmt = conn.prepareStatement("SELECT order_quantity, order_time FROM order_details WHERE oid = ?  AND shipping_status = 'Received' ORDER BY order_time DESC");
                stmt.setString(1, oid);
                ResultSet rs2 = stmt.executeQuery();
                rs2.next();
                orderQuantity=rs2.getInt(1);
                orderTime=rs2.getTimestamp(2);

                //print result
                System.out.println("|" + oid + "|" + orderQuantity + "|" + TimeConv.timeToStr(orderTime) + "|");
            }
            System.out.println("End of Query\n");
        } catch (SQLException e) {
            System.out.println("[Error] Failed to list the records.\n");
        }

    }

    public void printMostPopularBooks(){

        try {
            //no record
            PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT COUNT(isbn) FROM buy");
            ResultSet num = stmt.executeQuery();
            num.next();
            int count = num.getInt(1);
            if(count==0||count<10) {
                System.out.println("|                             |");
                System.out.println("       Need More Data :(");
                System.out.println("|                             |");
                return;
            }

            System.out.println("|Lending frequency|isbn|Title|Author|Price|Inventory Quantity|");
            stmt = conn.prepareStatement("SELECT SUM(item_quantity), isbn FROM buy GROUP BY isbn ORDER BY SUM(item_quantity) DESC LIMIT 10");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int frequency = rs.getInt(1);
                String isbn = rs.getString(2);
                String title;
                ArrayList<String> authors = new ArrayList<String>();
                double price;
                int inventory_quantity;

                stmt = conn.prepareStatement("SELECT author_name FROM author WHERE isbn = ?");
                stmt.setString(1, isbn);
                ResultSet authorRS = stmt.executeQuery();
                while (authorRS.next()) {
                    authors.add(authorRS.getString(1));
                }
                
                stmt = conn.prepareStatement("SELECT title, price, inventory_quantity FROM book WHERE isbn=?");
                stmt.setString(1, isbn);
                ResultSet rs2 = stmt.executeQuery();
                rs2.next();
                title=rs2.getString(1);
                price=rs2.getDouble(2);
                inventory_quantity=rs2.getInt(3);

                //print result
                System.out.printf("|" + frequency + "|" + isbn + "|");
                boolean first = true;
                for (String author : authors) {
                    System.out.printf(first ? author : ", " + author);
                    first = false;
                } 
                System.out.println("|" + title + "|"  +price + "|" + inventory_quantity + "|");
            }
            System.out.println("End of Query\n");
        } catch (SQLException e) {
            System.out.println("[Error] Failed to list the books.\n");
        }
    }

    public void optOrderUpdate(String oid){
        try{
            PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(oid) FROM order WHERE oid=?");
            stmt.setString(1,oid);
            ResultSet num = stmt.executeQuery();
            num.next();
            int count = num.getInt(1);
            if(count==0) {
                System.out.println("Fail. NO such order. Please try again");
                return;
             }
            stmt=conn.prepareStatement("SELECT shipping_status FROM order_details WHERE oid=?");
            stmt.setString(1,oid);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            String status = rs.getString(1);
            if(status.equals("Shipped")){
                System.out.println("Fail. Order has been shipped.");
            }
            else if(status.equals("Received")){
                System.out.println("Fail. Order has been received.");
            }
            else{
                stmt=conn.prepareStatement("SELECT order_time FROM order_details WHERE oid=?");
                stmt.setString(1,oid);
                rs = stmt.executeQuery();
                rs.next();
                Timestamp orderTime = rs.getTimestamp(1);
                Timestamp currentTime = new Timestamp(System.currentTimeMillis());
                long diff = currentTime.getTime() - orderTime.getTime();
                long diffSeconds = diff / 1000 % 60;
                if(diffSeconds>30){
                stmt=conn.prepareStatement("UPDATE order_details SET shipping_status='Shipped' WHERE oid=?");
                stmt.setString(1,oid);
                stmt.execute();
                System.out.println("Success. Order has been shipped.");}
                else{
                    System.out.println("Fail. Order has not been ordered for 30 seconds.");
                }
            }

        }
        catch(SQLException e){
            System.out.println("[Error] Failed to load order.\n");
        }
    }

}