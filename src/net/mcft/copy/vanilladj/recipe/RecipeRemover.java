package net.mcft.copy.vanilladj.recipe;

import net.mcft.copy.vanilladj.misc.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class RecipeRemover implements IRecipeListener {

private final ItemStack[] remove;
	
	public RecipeRemover(ItemStack... remove) {
		this.remove = remove;
	}
	
	@Override
	public void doSomethingWith(RecipeIterator iterator, IRecipe recipe) {
		ItemStack output = recipe.getRecipeOutput();
		if ((output != null) && (remove.length > 0) && Utils.contains(remove, output))
			iterator.remove();
	}

}
