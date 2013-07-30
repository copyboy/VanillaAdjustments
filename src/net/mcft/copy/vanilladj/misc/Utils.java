package net.mcft.copy.vanilladj.misc;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class Utils {
	
	private Utils() {  }
	
	public static ItemStack makeStack(Object object, int meta, int stackSize) {
		int id;
		if (object instanceof ItemStack) return (ItemStack)object;
		else if (object instanceof Block) id = ((Block)object).blockID;
		else if (object instanceof Item) id = ((Item)object).itemID;
		else throw new IllegalArgumentException(
				((object != null) ? object.getClass().toString() : "null") +
				" is not a valid object for makeStack.");
		return new ItemStack(id, stackSize, meta);
	}
	public static ItemStack makeStack(Object object, int meta) {
		return makeStack(object, meta, 1);
	}
	public static ItemStack makeStack(Object object) {
		return makeStack(object, Constants.anyDamage);
	}
	
	public static boolean matches(ItemStack a, ItemStack b) {
		if (a == b) return true;
		if ((a == null) || (b == null)) return false;
		int aDmg = a.getItemDamage();
		int bDmg = b.getItemDamage();
		return ((a.itemID == b.itemID) && ((aDmg == bDmg) ||
		                                   (aDmg == Constants.anyDamage) ||
		                                   (bDmg == Constants.anyDamage)));
	}
	
	public static boolean contains(ItemStack[] items, ItemStack stack) {
		for (ItemStack s : items)
			if (Utils.matches(stack, s))
				return true;
		return false;
	}
	
}
