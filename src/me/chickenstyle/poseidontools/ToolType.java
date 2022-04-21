package me.chickenstyle.poseidontools;


import org.bukkit.Material;

public enum ToolType {
    MOBSWORD(Material.DIAMOND_SWORD, "Token_Booster", "Drop_Booster", "Key_Finder", "Spawner_Finder",
            "XP_Finder", "Immunity", "Multi_Kill"),
    POSEIDONS_AXE(Material.DIAMOND_AXE, "Efficiency", "Token_Booster", "Drop_Booster", "Key_Finder",
            "Spawner_Finder", "Speed"),
    ATLANTIS_PICKAXE(Material.DIAMOND_PICKAXE, "Token_Booster", "Drop_Booster", "Key_Finder",
            "Spawner_Finder", "Haste");

    private Material material;
    private String[] enchantments;

    ToolType(Material material, String... enchantments) {
        this.material = material;
        this.enchantments = enchantments;
    }

    public Material getMaterial() {
        return material;
    }

    public String[] getEnchantments() {
        return enchantments;
    }
}
