package com.universal.homear;

public class Cart {
    private String id;
    private String name;
    private int price;
    private String stock;

    public Cart(){

    }

    public Cart(String id, String name, int price, String stock){
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

}
