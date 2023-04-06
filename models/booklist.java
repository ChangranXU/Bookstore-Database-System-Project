package models;

import java.util.ArrayList;

public class booklist {
    String isbn;
    String title;
    String authors;
    double price;
    int inventory_quantity;
    public booklist(String isbn, String title, ArrayList<String> author, double price, int inventory_quantity){
        this.isbn = isbn;
        this.title = title;
        this.authors = String.join(", ", author);
        this.price = price;
        this.inventory_quantity = inventory_quantity;
    }
}
