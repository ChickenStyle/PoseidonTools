package me.chickenstyle.poseidontools.utils;

import me.chickenstyle.poseidontools.PoseidonTools;
import me.chickenstyle.poseidontools.ToolType;

import java.util.HashMap;
import java.util.Map;

public class PoseidonTool {

    private ToolType toolType;
    private int currentLevel;
    private int maxLevel;
    private int currentXP;
    private int xpNeeded;
    private Map<String, Integer> enchantmentsLevel;

    protected PoseidonTool(ToolType toolType, int currentLevel, int currentXP,
                        Map<String, Integer> enchantmentsLevel) {
        this.toolType = toolType;
        this.currentLevel = currentLevel;
        this.maxLevel = PoseidonTools.getInstance().getConfig().getInt("toolsData." + toolType + ".maxLevel");
        this.currentXP = currentXP;
        calculateXPNeeded();
        this.enchantmentsLevel = enchantmentsLevel;

        if (enchantmentsLevel == null) return;
        for (String enchantment : toolType.getEnchantments()) {
            if (!enchantmentsLevel.containsKey(enchantment)) {
                enchantmentsLevel.put(enchantment,0);
            }
        }

    }

    protected PoseidonTool(ToolType toolType, int currentLevel, int currentXP) {
        this(toolType, currentLevel,currentXP, null);
        Map<String,Integer> enchantments = new HashMap<>();

        for (String enchantment : toolType.getEnchantments()) {
            enchantments.put(enchantment, 0);
        }

        this.enchantmentsLevel = enchantments;

    }

    protected PoseidonTool(ToolType toolType) {
        this(toolType, 1, 0);
    }

    private void calculateXPNeeded() {
        int xp = 0;
        for (int i = 0; i < currentLevel; i++) {
            xp += 100;
        }
        this.xpNeeded = xp;
    }

    public ToolType getToolType() {
        return toolType;
    }

    public void setToolType(ToolType toolType) {
        this.toolType = toolType;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public int getCurrentXP() {
        return currentXP;
    }

    public void setCurrentXP(int currentXP) {
        this.currentXP = currentXP;
    }

    //Returns true if tool leveled up
    public boolean addXP(int xp) {
        boolean leveledUp = false;
        while (this.currentXP + xp >= this.xpNeeded) {
            xp -= (this.xpNeeded - this.currentXP);
            if (this.currentLevel + 1 <= this.maxLevel) {
                this.currentXP = 0;
                this.currentLevel++;
                leveledUp = true;
            } else {
                xp = (this.xpNeeded - this.currentXP);
            }
        }
        calculateXPNeeded();
        this.currentXP += xp;
        return leveledUp;
    }

    public int getXPNeeded() {
        return xpNeeded;
    }

    public void setXPNeeded(int xpNeeded) {
        this.xpNeeded = xpNeeded;
    }

    public Map<String, Integer> getEnchantmentsLevel() {
        return enchantmentsLevel;
    }

    public void setEnchantmentsLevel(Map<String, Integer> enchantmentsLevel) {
        this.enchantmentsLevel = enchantmentsLevel;
    }
}
