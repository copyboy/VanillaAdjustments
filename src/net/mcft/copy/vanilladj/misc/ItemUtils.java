package net.mcft.copy.vanilladj.misc;

import net.minecraft.item.Item;

public final class ItemUtils {
	
	private ItemUtils() {  }
	
	public static void setDurability(int durability, Item... tools) {
		for (Item tool : tools)
			tool.setMaxDamage(durability);
	}
	
}
