package com.universal.homear;

public class CartItem {

    private Furniture furniture;
    private int quantity;

    public CartItem() {
    }

    public CartItem(Furniture furniture, int quantity) {
        this.furniture = furniture;
        this.quantity = quantity;
    }

    public Furniture getFurniture() {
        return furniture;
    }

    public void setFurniture(Furniture furniture) {
        this.furniture = furniture;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
