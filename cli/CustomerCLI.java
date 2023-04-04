package cli;

import java.util.*;
import models.*;

public class CustomerCLI implements CLIInterface {
    private Database db;
    private Scanner sc;

    public CustomerCLI(Database db, Scanner sc) {
        this.db = db;
        this.sc = sc;
    }

    public void startCLI() {
        while (true) {
            printMenu();
            int choice = sc.nextInt();
            System.out.println();
            switch (choice) {
                case 1: optBookSearch(); break;
                case 2: optPlaceOrder(); break;
                case 3: optCheckHistoryOrders(); break;
                case 4: return;
                default: System.out.println("[Error] Invalid operation, choose again.\n");
            }
        }
    }

    private void printMenu() {
        System.out.println("-----Customer Operation-----");
        System.out.println(">1. Book Search");
        System.out.println(">2. Place Order");
        System.out.println(">3. Check History Orders");
        System.out.println(">4. Back to the main menu");
        System.out.printf("Please Enter Your Query: ");
    }

    private void optBookSearch() {
        while(true){
        printSubMenu1();
        int choice = sc.nextInt();
        System.out.println();
            switch (choice) {
                case 1: optSearchByISBN(); break;
                case 2: optSearchByTitle(); break;
                case 3: optSearchByAuthor(); break;
                case 4: return;
                default: System.out.println("[Error] Invalid operation, choose again.\n");
            }
    }
    }

    private void printSubMenu1() {
        System.out.println("-----Choose the Grouped Order-----");
        System.out.println(">1. Search by ISBN");
        System.out.println(">2. Search by Title");
        System.out.println(">3. Search by Author");
        System.out.println(">4. Back to the customer operation menu");
        System.out.printf("Please Enter Your Query: ");
    }


    private void optPlaceOrder(){
        System.out.printf("Enter The User ID: ");
        String userID = sc.next();
        System.out.printf("Enter The Order ID You Like: ");
        String orderID = sc.next();
        int flag=0;
        ArrayList<String> ISBNList=new ArrayList<String>();
        ArrayList<Integer> item_quantity=new ArrayList<Integer>();
        while(flag==0){
        System.out.printf("Enter The Book ISBN You Like Order: ");
        String isbn=sc.next();
        System.out.printf("Enter The Quantity of Books You Like Order: ");
        int quantity=sc.nextInt();
        if(!db.testISBN(isbn)){
            System.out.println("The ISBN You Entered Is Not Exist!");
            continue;
        }
        if(!db.testQuantity(isbn,quantity)){
            System.out.println("We have not enough book!");
            db.printQuantity(isbn);
            continue;
        }
        ISBNList.add(isbn);
        item_quantity.add(quantity);
        System.out.printf("Do You Want To Add Another ISBN? (Y/N): ");
        String choice=sc.next();
        if(choice.equals("N")||choice.equals("n")){
            flag=1;}
        }
        db.placeOrder(userID,orderID,ISBNList,item_quantity);
        System.out.println("-----------------------");
    }

    private void optCheckHistoryOrders(){
        System.out.printf("Enter The User ID: ");
        String userID = sc.next();
        System.out.println("-----History Orders-----");
        db.printHistoryOrders(userID);
        System.out.println("-----------------------");
    }

    private void optSearchByISBN(){
        System.out.printf("Enter The Book ISBN: ");
        String isbn = sc.next();
        System.out.println("-----Book List-----");
        db.printBookListByISBN(isbn);
        System.out.println("-----------------------");
    }

    private void optSearchByTitle(){
        System.out.printf("Enter The Book Title: ");
        String title = sc.next();
        System.out.println("-----Book List-----");
        db.printBookListByTitle(title);
        System.out.println("-----------------------");
    }

    private void optSearchByAuthor(){
        System.out.printf("Enter The Book Author: ");
        String author = sc.next();
        System.out.println("-----Book List-----");
        db.printBookListByAuthor(author);
        System.out.println("-----------------------");
    }
}