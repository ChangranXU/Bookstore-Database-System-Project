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
            //check if the input is a positive integer
            String choice_ = sc.next();
            if(!models.utils.verifyInput.isPositiveInteger(choice_)){
                System.out.println("[Error] Invalid operation, choose again.\n");
                continue;
            }
            System.out.println();
            int choice=Integer.parseInt(choice_);
            switch (choice) {
                case 1: optBookSearch(); break;
                case 2: optPlaceOrder(); break;
                case 3: optCheckHistoryOrders(); break;
                case 4: optCreateUser(); break;
                case 5: return;
                default: System.out.println("[Error] Invalid operation, choose again.\n");
            }
        }
    }

    private void optCreateUser(){
        System.out.println("-----Create Account-----");
        System.out.printf("Enter the User ID: ");
        String userID = sc.next();
        if(!models.utils.verifyInput.isValidUid(userID)){
            System.out.println("[Error] UID length should less than 10, return to the customer operation menu.");
            return;
        }
        while(db.CreateverifyUser(userID) == true){
            System.out.println("[Error] Sorry the ID has already been occupied. Use a different name and try again.");
            System.out.println("Want to go back to the customer operation menu? (y/n) ");
            String choice = sc.next();
            if(choice.equals("y")||choice.equals("Y")){
                return;
            }
            else{
                System.out.printf("Enter the User ID: ");
                userID = sc.next();
                if(!models.utils.verifyInput.isValidUid(userID)){
                    System.out.println("[Error] UID length should less than 10, return to the customer operation menu.");
                    return;
                }
            }
        }
        System.out.printf("Enter the User Name: ");
        String userName = sc.next();
        if(!models.utils.verifyInput.isValidName(userName)){
            System.out.println("[Error] User name length should less than 50, return to the customer operation menu.");
            return;
        }
        if(!models.utils.verifyInput.isLegalInput(userName)){
            System.out.println("[Error] Contain _ or %, please remove them and try again.\n");
            return;
        }
        System.out.printf("Enter the User Address: ");
        String userAddress = sc.next();
        if(!models.utils.verifyInput.isValidAddress(userAddress)){
            System.out.println("[Error] Address length should less than 200. return to the customer operation menu.\n");
            return;
        }
        if(!models.utils.verifyInput.isLegalInput(userAddress)){
            System.out.println("[Error] Contain _ or %, please remove them and try again.\n");
            return;
        }
        db.createUser(userID, userName, userAddress);
    }

    private void printMenu() {
        System.out.println("-----Customer Operation-----");
        System.out.println(">1. Book Search");
        System.out.println(">2. Place Order");
        System.out.println(">3. Check History Orders");
        System.out.println(">4. Create Account");
        System.out.println(">5. Back to the main menu");
        System.out.printf("Please Enter Your Query. ");
    }

    private void optBookSearch() {
        while(true){
        printSubMenu1();
        //check if the input is a positive integer
        String choice_ = sc.next();
        if(!models.utils.verifyInput.isPositiveInteger(choice_)){
            System.out.println("[Error] Invalid operation, choose again.\n");
            continue;
        }
        int choice=Integer.parseInt(choice_);
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
        System.out.println("\nwarning: if you are not the registed customer, you are not allowed to place order!!!\n");
        System.out.printf("Enter your User ID: ");
        String userID = sc.next();
        if(db.verifyUser(userID) == false){
            System.out.println("[Error] uid does not exist, return to the customer operation menu.");
            return;
        }
        db.placeOrder(userID, sc);
    }

    private void optCheckHistoryOrders(){
        System.out.println("\nwarning: if you are not the registed customer, you are not allowed to check history orders!!!\n");
        System.out.printf("Enter The User ID: ");
        String userID = sc.next();
        if(db.verifyUser(userID) == false){
            System.out.println("[Error] login failed, return to the customer operation menu.");
            return;
        }
        System.out.println("\n----------------------------------------History Orders----------------------------------------"); 
        db.printHistoryOrders(userID);
    }

    private void optSearchByISBN(){
        System.out.printf("Enter The Book ISBN: ");
        String isbn = sc.next();
        System.out.println("\n------------------------------------------Book  List------------------------------------------"); 
        db.printBookListByISBN(isbn);
    }

    private void optSearchByTitle(){
        System.out.printf("Enter The Book Title: ");
        String title = sc.next();
        if(!models.utils.verifyInput.isLegalInput(title)){
            System.out.println("[Error] Contain _ or %, please remove them and try again.\n");
            return;
        }
        System.out.println("\n------------------------------------------Book  List------------------------------------------"); 
        db.searchBookListByTitle(title);
    }

    private void optSearchByAuthor(){
        System.out.printf("Enter The Book Author: ");
        String author = sc.next();
        if(!models.utils.verifyInput.isLegalInput(author)){
            System.out.println("[Error] Contain _ or %, please remove them and try again.\n");
            return;
        }
        System.out.println("\n------------------------------------------Book  List------------------------------------------"); 
        db.printBookListByAuthor(author);
    }
}