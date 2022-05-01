package me.chickenstyle.poseidontools.nms;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface NMSHandler {
	
	ItemStack addIntTag(ItemStack item,String tag,int data);
	
	ItemStack addStringTag(ItemStack item,String tag,String data);
	
	boolean hasTag(ItemStack item,String tag);
	
	int getIntData(ItemStack item,String tag);
	
	String getStringData(ItemStack item,String tag);
	
	ItemStack removeTag(ItemStack item,String tag);

	ItemStack addGlow(ItemStack item);

	boolean isItem(Material material);
}
