package com.universal.homear;

import java.util.ArrayList;

public class Cart {
    private String id;
    private String name;
    private int price;
    private String stock;
    private int image;

    public Cart(){

    }

    public Cart(String id, String name, int price, String stock, int image){
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.image = image;
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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }


    public static ArrayList<Cart> getDummyData() {
        ArrayList<Cart> dummy = new ArrayList<>();
        dummy.add(new Cart("abc123", "Leather Chair", 112, "In Stock", R.drawable.chair_1));
        dummy.add(new Cart("qwe456", "Black Chair", 213, "Out of Stock", R.drawable.chair_2));
        dummy.add(new Cart("34t3g6", "Blachabfiu", 456, "In Stock", R.drawable.chair_1));
        dummy.add(new Cart("v34qt3", "kjwfbjwh b", 787, "In Stock", R.drawable.chair_2));
        dummy.add(new Cart("3v4t3t", "fkjew bfjhwb", 123, "Out of Stock", R.drawable.chair_1));
        dummy.add(new Cart("qdfih2", "ddfwef bb", 233, "Out of Stock", R.drawable.chair_2));
        dummy.add(new Cart("gerf34", "ad kjfaf", 12313, "In Stock", R.drawable.chair_1));
        dummy.add(new Cart("asdf24", "dsfjk nr3wef", 12313, "Out of Stock", R.drawable.chair_2));
        return dummy;
    }

}
