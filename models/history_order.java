package models;

public class history_order {
    String oid;
    String title;
    int item_quantity;
    String order_time;
    String order_status;
    public history_order(String oid, String title, int item_quantity, String order_time ,String order_status){
        this.oid = oid;
        this.title = title;
        this.item_quantity = item_quantity;
        this.order_time = order_time;
        this.order_status = order_status;
    }
}
