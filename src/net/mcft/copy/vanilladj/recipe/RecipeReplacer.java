package net.mcft.copy.vanilladj.recipe;

import net.mcft.copy.vanilladj.misc.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

/** Replaces one item with another in recipes of some items. */
public class RecipeReplacer implements IRecipeListener {
	
	private final ItemStack replace;
	private final ItemStack with;
	
	private final ItemStack[] replaceIn;
	
	public RecipeReplacer(ItemStack replace, ItemStack with, ItemStack... replaceIn) {
		this.replace = replace;
		this.with = with;
		this.replaceIn = replaceIn;
	}
	
	@Override
	public void doSomethingWith(RecipeIterator iterator, IRecipe recipe) {
		ItemStack output = recipe.getRecipeOutput();
		if ((output == null) || (replaceIn.length <= 0) || !Utils.contains(replaceIn, output)) return;
		
		RecipeContainer container = new RecipeContainer(recipe);
		container.replace(replace, with);
	}
	
}
