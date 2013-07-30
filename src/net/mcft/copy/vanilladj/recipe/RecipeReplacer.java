package net.mcft.copy.vanilladj.recipe;

import java.util.List;

import net.mcft.copy.vanilladj.misc.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

/** Replaces one item with another in recipes of some items. */
public class RecipeReplacer implements IRecipeListener {
	
	private final ItemStack replace;
	private final ItemStack with;
	
	private final ItemStack[] replaceIn;
	
	public RecipeReplacer(Object replace, Object with, Object... replaceIn) {
		
		this.replace = Utils.makeStack(replace);
		this.with = Utils.makeStack(with);
		
		this.replaceIn = new ItemStack[replaceIn.length];
		for (int i = 0; i < replaceIn.length; i++)
			this.replaceIn[i] = Utils.makeStack(replaceIn[i]);
		
	}
	
	@Override
	public void doSomethingWith(IRecipe recipe) {
		
		ItemStack output = recipe.getRecipeOutput();
		if ((output == null) || ((replaceIn.length > 0) && !Utils.contains(replaceIn, output))) return;
		
		if (recipe instanceof ShapedRecipes) {
			
			ShapedRecipes shapedRecipe = (ShapedRecipes)recipe;
			ItemStack[] items = shapedRecipe.recipeItems;
			for (int i = 0; i < items.length; i++)
				if (Utils.matches(items[i], replace))
					items[i] = with.copy();
			
		} else if (recipe instanceof ShapelessRecipes) {
			
			ShapelessRecipes shapelessRecipe = (ShapelessRecipes)recipe;
			List<ItemStack> items = shapelessRecipe.recipeItems;
			for (int i = 0; i < items.size(); i++)
				if (Utils.matches(items.get(i), replace))
					items.set(i, with.copy());
			
		} else if (recipe instanceof ShapedOreRecipe) {
			
			ShapedOreRecipe shapedOreRecipe = (ShapedOreRecipe)recipe;
			Object[] items = shapedOreRecipe.getInput();
			for (int i = 0; i < items.length; i++)
				if ((items[i] instanceof ItemStack) &&
				    Utils.matches((ItemStack)items[i], replace))
				    items[i] = with.copy();
			
		} else if (recipe instanceof ShapelessOreRecipe) {
			
			ShapelessOreRecipe shapelessRecipe = (ShapelessOreRecipe)recipe;
			List items = shapelessRecipe.getInput();
			for (int i = 0; i < items.size(); i++)
				if ((items.get(i) instanceof ItemStack) &&
					Utils.matches((ItemStack)items.get(i), replace))
					items.set(i, with.copy());
			
		}
		
	}
	
}
