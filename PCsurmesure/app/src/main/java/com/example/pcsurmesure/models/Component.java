package com.example.pcsurmesure.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Component {

    private String type;
    private String subType;
    private String description;
    private int quantity;

    private String comment;

    private String dateCréation;

    private String dateModification;



//    Constructor
    public Component(){}
    public Component(String type, String subType, String description, int quantity
    , String comment){
        this.type = type;
        this.subType = subType;
        this.description = description;
        this.quantity = quantity;
        this.comment = comment;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.dateCréation = dateFormat.format(new Date());
        this.dateModification = null;
    }

    // Getter and Setter for type
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // Getter and Setter for subType
    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    // Getter and Setter for description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getter and Setter for quantity
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Getter and Setter for comment
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    // Getter and Setter for dateCréation
    public String getDateCréation() {
        return dateCréation;
    }

    public void setDateCréation(String dateCréation) {
        this.dateCréation = dateCréation;
    }

    // Getter and Setter for dateModification
    public String getDateModification() {
        return dateModification;
    }

    public void setDateModification(String dateModification) {
        this.dateModification = dateModification;
    }
}
