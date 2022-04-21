package me.chickenstyle.poseidontools.nms;

import org.bukkit.inventory.ItemStack;

public interface NMSHandler {
	
	public ItemStack addIntTag(ItemStack item,String tag,int data);
	
	public ItemStack addStringTag(ItemStack item,String tag,String data);
	
	public boolean hasTag(ItemStack item,String tag);
	
	public int getIntData(ItemStack item,String tag);
	
	public String getStringData(ItemStack item,String tag);
	
	public ItemStack removeTag(ItemStack item,String tag);

	public ItemStack addGlow(ItemStack item);
}
