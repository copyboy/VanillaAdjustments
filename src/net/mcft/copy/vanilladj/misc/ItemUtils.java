package net.mcft.copy.vanilladj.misc;

import net.minecraft.item.Item;

public final class ItemUtils {
	
	private ItemUtils() {  }
	
	public static void setToolDurability(int durability, Item... tools) {
		for (Item tool : tools)
			tool.setMaxDamage(durability);
	}
	
	public static void adjustArmorDurability(double factor, Item... armors) {
		for (Item armor : armors)
			armor.setMaxDamage((int)(armor.getMaxDamage() * factor));
	}
	
}
