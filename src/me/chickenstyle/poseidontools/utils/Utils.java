package me.chickenstyle.poseidontools.utils;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

public class Utils {

    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&',text);
    }

    public static String replacePlaceHolders(String text, PlaceHolder... placeHolders) {
        if (placeHolders == null) return color(text);

        for (PlaceHolder placeHolder : placeHolders) {
            text = text.replaceAll(placeHolder.getPlaceHolder(), placeHolder.getData());
        }
        return color(text);
    }

    public static String[] replacePlaceHolders(String[] texts, PlaceHolder... placeHolders) {
        for (int i = 0; i < texts.length; i++) {
            texts[i] = replacePlaceHolders(texts[i], placeHolders);
        }
        return texts;
    }

    public static List<String> replacePlaceHolders(List<String> texts, PlaceHolder... placeHolders) {
        return Arrays.asList(replacePlaceHolders(texts.toArray(new String[0]), placeHolders));
    }

}