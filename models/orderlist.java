package models;

public class orderlist {
    String oid;
    int order_quantity;
    String order_time;
    public orderlist(String oid, int order_quantity, String order_time){
        this.oid = oid;
        this.order_quantity = order_quantity;
        this.order_time = order_time;
    }
}
