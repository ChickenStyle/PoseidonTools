package me.chickenstyle.poseidontools.utils;

import me.chickenstyle.poseidontools.PoseidonTools;

public enum Message {
    LEVEL_UP_MESSAGE,
    ENCHANTMENT_UPGRADE,
    FAILED_ENCHANTMENT_UPGRADE,
    ACTIVATED_ENCHANTMENT,
    FAILED_ABILITY_UPGRADE;

    private String text;

    public static void loadMessages() {
        for (Message message : values()) {
            message.text = PoseidonTools.getInstance().getMSGConfig().getString("messages." + message);
        }
    }

    public String toMSG() {
        return Utils.color(text);
    }

    public String toMSG(PlaceHolder... placeHolders) {
        return Utils.replacePlaceHolders(text, placeHolders);
    }
}
