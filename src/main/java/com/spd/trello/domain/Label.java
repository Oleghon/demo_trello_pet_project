package com.spd.trello.domain;

public class Label {
    private String colorName;

    public Label(String colorName) {
        this.colorName = colorName;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }
}
