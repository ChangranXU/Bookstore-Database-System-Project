package cli;

import java.util.Scanner;
import models.*;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime; 

public class MainCLI implements CLIInterface{
    private Database db;
    private Scanner sc;

    public MainCLI(Database db) {
        this.db = db;
        this.sc = new Scanner(System.in);
    }

    public void startCLI() {
        while (true) {
            printMainMenu();
            int choice = sc.nextInt();
            System.out.println();
            CLIInterface c = null;
            switch (choice) {
                case 1:  c = new DatabaseCLI(db, sc); break;
                case 2:  c = new CustomerCLI(db, sc); break;
                case 3: c = new BookstoreCLI(db, sc); break;
                case 4: return;
                default: System.out.println("[Error] Invalid operation, choose again.\n");
            }

            if (c != null) c.startCLI();
        }
    }

    private void printMainMenu() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now();  
        System.out.println("+System Date: "+dtf.format(now));  
        System.out.printf("+Database Records: ");
        db.countAndPrintAllRecordsInTables();
        System.out.println("——————————————————————————");
        System.out.println(">1. Database Operation");
        System.out.println(">2. Customer Operation");
        System.out.println(">3. Bookstore Operation");
        System.out.println(">4. Quit");
        System.out.printf("Please Enter Your Query: ");
    }
}