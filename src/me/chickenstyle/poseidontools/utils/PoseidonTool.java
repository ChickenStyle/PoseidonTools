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
    private int levelPoints;
    private Map<String, Integer> enchantmentsLevel;
    private Map<String,Integer> abilitiesLevel;

    protected PoseidonTool(ToolType toolType, int currentLevel, int currentXP, int levelPoints,
                        Map<String, Integer> enchantmentsLevel, Map<String,Integer> abilitiesLevel) {
        this.toolType = toolType;
        this.currentLevel = currentLevel;
        this.levelPoints = levelPoints;
        this.maxLevel = PoseidonTools.getInstance().getConfig().getInt("toolsData." + toolType + ".maxLevel");
        this.currentXP = currentXP;
        calculateXPNeeded();
        this.enchantmentsLevel = enchantmentsLevel;
        this.abilitiesLevel = abilitiesLevel;

        if (this.enchantmentsLevel == null) this.enchantmentsLevel = new HashMap<>();
        if (this.abilitiesLevel == null) this.abilitiesLevel = new HashMap<>();

        for (String enchantment : toolType.getEnchantments()) {
            if (!this.enchantmentsLevel.containsKey(enchantment)) {
                this.enchantmentsLevel.put(enchantment,0);
            }
        }

        for (String ability : toolType.getAbilities()) {
            if (!this.abilitiesLevel.containsKey(ability)) {
                this.abilitiesLevel.put(ability, 0);
            }
        }

    }

    protected PoseidonTool(ToolType toolType, int currentLevel, int currentXP) {
        this(toolType, currentLevel,currentXP, currentLevel, null, null);
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
                this.levelPoints++;
                leveledUp = true;
            } else {
                if (this.currentXP + xp >= this.xpNeeded) {
                    this.currentXP = this.xpNeeded;
                    xp = 0;
                }
                break;
            }
            calculateXPNeeded();
        }

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

    public boolean hasEnchantment(String enchantment) {
        return getEnchantmentLevel(enchantment) > 0;
    }

    public int getEnchantmentLevel(String enchantment) {
        if (!enchantmentsLevel.containsKey(enchantment)) return 0;
        return enchantmentsLevel.get(enchantment);
    }

    public int getEnchantmentMaxLevel(String enchantment) {
        return PoseidonTools.getInstance().getEnchantConfig().getConfig()
                .getInt(toolType + "." + enchantment +".maxLevel");
    }

    public boolean hasAbility(String ability){
        return getAbilityLevel(ability) > 0;
    }

    public int getAbilityLevel(String ability) {
        if (!abilitiesLevel.containsKey(ability)) return 0;
        return abilitiesLevel.get(ability);
    }

    public int getAbilityMaxLevel(String ability) {
        return PoseidonTools.getInstance().getAbilitiesConfig().getConfig()
                .getInt(toolType + "." + ability + ".maxLevel");
    }


    public void setAbilityLevel(String ability, int level) {
        abilitiesLevel.put(ability, level);
    }

    public void setEnchantmentsLevel(Map<String, Integer> enchantmentsLevel) {
        this.enchantmentsLevel = enchantmentsLevel;
    }

    public int getLevelPoints() {
        return levelPoints;
    }

    public void setLevelPoints(int levelPoints) {
        this.levelPoints = levelPoints;
    }
}
