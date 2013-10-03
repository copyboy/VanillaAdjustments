package net.mcft.copy.vanilladj.misc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

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
	
	public static ItemStack[] makeStacks(Object... objects) {
		List<ItemStack> stacks = new ArrayList<ItemStack>();
		boolean intAllowed = false;
		for (Object object : objects) {
			if (object instanceof ItemStack) {
				stacks.add((ItemStack)object);
				intAllowed = false;
			} else if (object instanceof Block) {
				stacks.add(new ItemStack((Block)object, 1, Constants.anyDamage));
				intAllowed = true;
			} else if (object instanceof Item) {
				stacks.add(new ItemStack((Item)object, 1, Constants.anyDamage));
				intAllowed = true;
			} else if (object instanceof Integer) {
				if (!intAllowed) throw new Error("Integer behind non-block/item.");
				stacks.get(stacks.size() - 1).setItemDamage((Integer)object);
				intAllowed = false;
			} else throw new Error("Invalid type.");
		}
		return stacks.toArray(new ItemStack[0]);
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
	
	public static boolean matchesOreDict(ItemStack a, Object b) {
		if (b == null) return (a == null);
		else if (b instanceof ItemStack)
			return matches(a, (ItemStack)b);
		else if (b instanceof String) {
			int id = OreDictionary.getOreID(a);
			return ((id >= 0) ? OreDictionary.getOreName(id).equals(b) : false);
		} else if (b instanceof ArrayList) {
			for (ItemStack s : (ArrayList<ItemStack>)b)
				if (matches(a, s)) return true;
			return false;
		} else throw new IllegalArgumentException("b is not a String or ItemStack.");
	}
	
	public static int indexOf(ItemStack[] items, ItemStack stack) {
		for (int i = 0; i < items.length; i++)
			if (Utils.matches(stack, items[i]))
				return i;
		return -1;
	}
	public static boolean contains(ItemStack[] items, ItemStack stack) {
		return (indexOf(items, stack) >= 0);
	}
	
	public static void replaceWithMetadata(Block replace, Class<? extends Block> with) {
		int id = replace.blockID;
		Block.blocksList[id] = null;
		Item.itemsList[id] = null;
		try {
			Block block = with.getConstructor(int.class).newInstance(id);
			new ItemBlockWithMetadata(id - 256, block);
		} catch (Exception e) { throw new Error(e); }
	}
	
	public static void replace(Item replace, Class<? extends Item> with) {
		int id = replace.itemID;
		Item.itemsList[id] = null;
		try { with.getConstructor(int.class).newInstance(id - 256); }
		catch (Exception e) { throw new Error(e); }
	}
	
	public static String repeat(String str, int num) {
		if (str == null) return null;
		StringBuilder sb = new StringBuilder(str.length() * num);
		for (int i = 0; i < num; i++) sb.append(str);
		return sb.toString();
	}
	
	public static String join(Collection col, String delimiter) {
		StringBuilder sb = new StringBuilder();
		Iterator iterator = col.iterator();
		while (iterator.hasNext()) {
			sb.append(iterator.next());
			if (!iterator.hasNext()) break;
			sb.append(delimiter);
		}
		return sb.toString();
	}
	
}
