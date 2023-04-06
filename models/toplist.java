package models;

import java.util.ArrayList;

public class toplist {
    int lending_frequency;
    String isbn;
    String title;
    String authors;
    double price;
    int inventory_quantity;
    public toplist(int lending_frequency, String isbn, String title, ArrayList<String> author, double price, int inventory_quantity){
        this.lending_frequency = lending_frequency;
        this.isbn = isbn;
        this.title = title;
        this.authors = String.join(", ", author);
        this.price = price;
        this.inventory_quantity = inventory_quantity;
    }
}
