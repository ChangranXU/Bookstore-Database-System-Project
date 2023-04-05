package cli;

import java.util.*;
import java.sql.*;
import models.*;

public class DatabaseCLI implements CLIInterface {
    private Database db;
    private Scanner sc;

    public DatabaseCLI(Database db, Scanner sc) {
        this.db = db;
        this.sc = sc;
    }

    public void startCLI() {
        while (true) {
            printMenu();
            int choice = sc.nextInt();
            System.out.println();
            switch (choice) {
                case 1: optCreateAllTables(); break;
                case 2: optDeleteAllTables(); break;
                case 3: optLoadFromDatafile(); break;
                case 4: return;
                default: System.out.println("[Error] Invalid operation, choose again.\n");
            }
        }
    }

    private void printMenu() {
        System.out.println("-----Database Operation Menu-----");
        System.out.println(">1. Create all tables");
        System.out.println(">2. Delete all tables");
        System.out.println(">3. Load data from file");
        System.out.println(">4. Return to the main menu");
        System.out.printf("Enter your query: ");
    }

    private void optCreateAllTables() {
        try {
            db.createAllTables();
            System.out.println("Done! All tables have been initialized!\n");
        } catch (SQLException e) {
            if (e.toString().contains("exists")) {
                System.out.println("[Error] Tables already created.\n");
            } else {
                System.out.println("[Error] Cannot create tables.\n");
            }
        }
    }

    private void optDeleteAllTables() {
        try {
            db.deleteAllTables();
            System.out.println("Done! All tables have been removed!\n");
        } catch (SQLException e) {
            if (e.toString().contains("Unknown")) {
                System.out.println("[Error] Tables do not exist.\n");
            } else {
                System.out.println("[Error] Cannot delete tables.\n");
            }
        }
    }

    private void optLoadFromDatafile() {
        System.out.printf("Type in the path of the file you want to import. You can type given_data if you downloaded the whole project. ");
        String folderPath = sc.next();
        try {
            db.loadDataFromFiles(folderPath);
            System.out.println("Done! Data has been loaded from file!\n");
        } catch (Exception e) {
            System.out.println("[Error] Cannot load data from file.\n");
        }
    }

}

