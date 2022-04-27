package me.chickenstyle.poseidontools;

import org.bukkit.Material;

public enum ToolType {
    MOBSWORD(Material.DIAMOND_SWORD, new String[]{"Gateway_to_Hell", "Rage_Berry"},
            "Token_Booster", "Drop_Booster", "Key_Finder", "Spawner_Finder", "XP_Finder", "Immunity"),
    POSEIDONS_AXE(Material.DIAMOND_AXE, new String[]{"Explosive_Foraging", "Spirit_of_the_Woods"},
            "Efficiency", "Token_Booster", "Drop_Booster", "Key_Finder", "Spawner_Finder", "Speed"),
    ATLANTIS_PICKAXE(Material.DIAMOND_PICKAXE, new String[]{"BlackHole", "Spicy_Taco"},
            "Token_Booster", "Drop_Booster", "Key_Finder", "Spawner_Finder", "Haste");

    private Material material;
    private String[] abilities;
    private String[] enchantments;

    ToolType(Material material, String[] abilities,String... enchantments) {
        this.material = material;
        this.abilities = abilities;
        this.enchantments = enchantments;
    }

    public Material getMaterial() {
        return material;
    }

    public String[] getEnchantments() {
        return enchantments;
    }

    public String[] getAbilities() {
        return abilities;
    }
}
