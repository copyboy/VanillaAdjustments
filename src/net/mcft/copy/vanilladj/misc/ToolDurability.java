package net.mcft.copy.vanilladj.misc;

import net.minecraft.item.Item;

public final class ToolDurability {
	
	private ToolDurability() {  }
	
	public static void set(int durability, Item... tools) {
		for (Item tool : tools)
			tool.setMaxDamage(durability);
	}
	
}
