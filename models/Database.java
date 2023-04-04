package models;

import java.sql.*;
import java.io.*;
import java.util.ArrayList;

import models.file.*;

public class Database {
    final String dbAddr = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/test";
    final String dbUsername = "csci3170";
    final String dbPassword = "testfor3170";

    final String[] tableNames = {"customer", "book", "item", "author", "order_details", "borrow","order"};

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

    // ====== ADMIN OPERATIONS =======

    /**********/
 
    // ======Bookstore Operations ======//
    public void printOrderedOrder(String userID){

        try {
            //no record
            PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(OID) FROM order_details WHERE shipping_status = 'Ordered' AND OID in (SELECT DISTINCT OID FROM order WHERE UID =?)");
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
            System.out.println("|OID|Order Quantity|Order Date|");
            stmt = conn.prepareStatement("SELECT OID FROM order_details WHERE shipping_status = 'Shipped' AND OID in (SELECT DISTINCT OID FROM order WHERE UID =?)");
            stmt.setString(1, userID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String OID = rs.getString(1); 
                int orderQuantity;
                Timestamp orderTime;

                //order quantity, order time
                stmt = conn.prepareStatement("SELECT order_quantity, order_time FROM order_details WHERE OID = ?  AND shipping_status = 'Ordered' ORDER BY order_date DESC");
                stmt.setString(1, OID);
                ResultSet rs2 = stmt.executeQuery();
                rs2.next();
                orderQuantity=rs2.getInt(1);
                orderTime=rs2.getTimestamp(2);

                //print result
                System.out.println("|" + OID + "|" + orderQuantity + "|" + TimeConv.timeToStr(orderTime) + "|");
            }
            System.out.println("End of Query\n");
        } catch (SQLException e) {
            System.out.println("[Error] Failed to list the records.\n");
        }
    }

    public void printShippedOrder(String userID){

        try {
            //no record
            PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(OID) FROM order_details WHERE shipping_status = 'Shipped' AND OID in (SELECT DISTINCT OID FROM order WHERE UID =?)");
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
            System.out.println("|OID|Order Quantity|Order Time|");
            stmt = conn.prepareStatement("SELECT OID FROM order_details WHERE shipping_status = 'Shipped' AND OID in (SELECT DISTINCT OID FROM order WHERE UID =?)");
            stmt.setString(1, userID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String OID = rs.getString(1); 
                int orderQuantity;
                Timestamp orderTime;

                //order quantity, ordeer date
                stmt = conn.prepareStatement("SELECT order_quantity, order_time FROM order_details WHERE OID = ?  AND shipping_status = 'Shipped' ORDER BY order_date DESC");
                stmt.setString(1, OID);
                ResultSet rs2 = stmt.executeQuery();
                rs2.next();
                orderQuantity=rs2.getInt(1);
                orderTime=rs2.getTimestamp(2);

                //print result
                System.out.println("|" + OID + "|" + orderQuantity + "|" + TimeConv.timeToStr(orderTime) + "|");
            }
            System.out.println("End of Query\n");
        } catch (SQLException e) {
            System.out.println("[Error] Failed to list the records.\n");
        }
    }

    public void printReceivedOrder(String userID){

        try {
            //no record
            PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(OID) FROM order_details WHERE shipping_status = 'Received' AND OID in (SELECT DISTINCT OID FROM order WHERE UID =?)");
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
            System.out.println("|OID|Order Quantity|Order Time|");
            stmt = conn.prepareStatement("SELECT OID FROM order_details WHERE shipping_status = 'Received' AND OID in (SELECT DISTINCT OID FROM order WHERE UID =?)");
            stmt.setString(1, userID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String OID = rs.getString(1); 
                int orderQuantity;
                Timestamp orderTime;

                //order quantity, ordeer date
                stmt = conn.prepareStatement("SELECT order_quantity, order_time FROM order_details WHERE OID = ?  AND shipping_status = 'Received' ORDER BY order_date DESC");
                stmt.setString(1, OID);
                ResultSet rs2 = stmt.executeQuery();
                rs2.next();
                orderQuantity=rs2.getInt(1);
                orderTime=rs2.getTimestamp(2);

                //print result
                System.out.println("|" + OID + "|" + orderQuantity + "|" + TimeConv.timeToStr(orderTime) + "|");
            }
            System.out.println("End of Query\n");
        } catch (SQLException e) {
            System.out.println("[Error] Failed to list the records.\n");
        }

    }

    public void printMostPopularBooks(){

        try {
            //no record
            PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT COUNT(ISBN) FROM borrow");
            ResultSet num = stmt.executeQuery();
            num.next();
            int count = num.getInt(1);
            if(count==0||count<10) {
                System.out.println("|                             |");
                System.out.println("       Need More Data :(");
                System.out.println("|                             |");
                return;
            }

            System.out.println("|Lending frequency|ISBN|Title|Author|Price|Inventory Quantity|");
            stmt = conn.prepareStatement("SELECT SUM(item_quantity), ISBN FROM borrow GROUP BY ISBN ORDER BY SUM(item_quantity) DESC LIMIT 10");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int frequency = rs.getInt(1);
                String ISBN = rs.getString(2);
                String title;
                ArrayList<String> authors = new ArrayList<String>();
                int price;
                int inventory_quantity;

                stmt = conn.prepareStatement("SELECT author FROM author WHERE ISBN = ?");
                stmt.setString(1, ISBN);
                ResultSet authorRS = stmt.executeQuery();
                while (authorRS.next()) {
                    authors.add(authorRS.getString(1));
                }
                
                stmt = conn.prepareStatement("SELECT title, price, inventory_quantity FROM book WHERE ISBN=?");
                stmt.setString(1, ISBN);
                ResultSet rs2 = stmt.executeQuery();
                rs2.next();
                title=rs2.getString(1);
                price=rs2.getInt(2);
                inventory_quantity=rs2.getInt(3);

                //print result
                System.out.println("|" + frequency + "|" + ISBN + "|");
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

    public void optOrderUpdate(String OID){
        try{
            PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(OID) FROM order WHERE OID=?");
            stmt.setString(1,OID);
            ResultSet num = stmt.executeQuery();
            num.next();
            int count = num.getInt(1);
            if(count==0) {
                System.out.println("Fail. NO such order. Please try again");
                return;
             }
            stmt=conn.prepareStatement("SELECT shipping_status FROM order_details WHERE OID=?");
            stmt.setString(1,OID);
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
                stmt=conn.prepareStatement("SELECT order_time FROM order_details WHERE OID=?");
                stmt.setString(1,OID);
                rs = stmt.executeQuery();
                rs.next();
                Timestamp orderTime = rs.getTimestamp(1);
                Timestamp currentTime = new Timestamp(System.currentTimeMillis());
                long diff = currentTime.getTime() - orderTime.getTime();
                long diffSeconds = diff / 1000 % 60;
                if(diffSeconds>30){
                stmt=conn.prepareStatement("UPDATE order_details SET shipping_status='Shipped' WHERE OID=?");
                stmt.setString(1,OID);
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