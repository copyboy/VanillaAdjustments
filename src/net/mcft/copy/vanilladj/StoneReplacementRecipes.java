package net.mcft.copy.vanilladj;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public final class StoneReplacementRecipes {
	
	private static Object[] replaceIn = {
		Item.swordStone,
		Item.pickaxeStone,
		Item.shovelStone,
		Item.axeStone,
		Item.hoeStone,
		Block.dispenser,
		Block.dropper,
		Block.pistonBase,
		Item.brewingStand
	};
	
	private StoneReplacementRecipes() {  }
	
	public static void add() {
		
		Item cobblestone = Item.itemsList[Block.cobblestone.blockID];
		Item smoothstone = Item.itemsList[Block.stone.blockID];
		
		Set<Item> replaceItems = new HashSet<Item>(replaceIn.length);
		for (Object obj : replaceIn) {
			if (obj instanceof Item)
				replaceItems.add((Item)obj);
			else if (obj instanceof Block)
				replaceItems.add(Item.itemsList[((Block)obj).blockID]);
		}
		
		VanillaAdjustments.log.info("Replacing cobblestone with smoothstone in recipes of " + replaceItems.size() + " items.");
		
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		for (IRecipe recipe : recipes) {
			ItemStack output = recipe.getRecipeOutput();
			if ((output == null) || !replaceItems.contains(output.getItem())) continue;
			
			if (recipe instanceof ShapedRecipes) {
				
				ShapedRecipes shapedRecipe = (ShapedRecipes)recipe;
				ItemStack[] items = shapedRecipe.recipeItems;
				for (int i = 0; i < items.length; i++)
					if ((items[i] != null) && (items[i].getItem()) == cobblestone)
						items[i] = new ItemStack(smoothstone);
				
			} else if (recipe instanceof ShapelessRecipes) {
				
				ShapelessRecipes shapelessRecipe = (ShapelessRecipes)recipe;
				List<ItemStack> items = shapelessRecipe.recipeItems;
				for (int i = 0; i < items.size(); i++)
					if (items.get(i).getItem() == cobblestone)
						items.set(i, new ItemStack(smoothstone));
				
			} else if (recipe instanceof ShapedOreRecipe) {
				
				ShapedOreRecipe shapedOreRecipe = (ShapedOreRecipe)recipe;
				Object[] items = shapedOreRecipe.getInput();
				for (int i = 0; i < items.length; i++)
					if ((items[i] instanceof ItemStack) &&
					    (((ItemStack)items[i]).getItem() == cobblestone))
						items[i] = new ItemStack(smoothstone);
				
			} else if (recipe instanceof ShapelessOreRecipe) {
				
				ShapelessOreRecipe shapelessRecipe = (ShapelessOreRecipe)recipe;
				List items = shapelessRecipe.getInput();
				for (int i = 0; i < items.size(); i++)
					if ((items.get(i) instanceof ItemStack) &&
					    (((ItemStack)items.get(i)).getItem() == cobblestone))
						items.set(i, new ItemStack(smoothstone));
				
			}
		}
		
	}
	
}
