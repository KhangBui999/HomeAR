package com.universal.homear;

import java.util.ArrayList;

public class Furniture {
    private String id;
    private String name;
    private int price;
    private int stock;
    private String detail;
    private String colour;
    private String dimensions;
    private String weight;

    public Furniture() {
    }

    public Furniture(String id, String name, int price, int stock, String detail, String colour, String dimensions, String weight) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.detail = detail;
        this.colour = colour;
        this.dimensions = dimensions;
        this.weight = weight;
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

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

}
