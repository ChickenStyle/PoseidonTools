package me.chickenstyle.poseidontools.utils;

public class PlaceHolder {
    private final String placeHolder;
    private final String data;

    public PlaceHolder(String placeHolder, String data) {
        this.placeHolder = "%" + placeHolder + "%";
        this.data = data;
    }

    public String getPlaceHolder() {
        return placeHolder;
    }

    public String getData() {
        return data;
    }
}
