package net.mcft.copy.vanilladj.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.mcft.copy.vanilladj.misc.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class RecipeContainer {
	
	public final IRecipe recipe;
	public final List items;
	public final boolean isShapeless;
	public final boolean isOreRecipe;
	
	public RecipeContainer(IRecipe recipe) {
		
		this.recipe = recipe;
		
		if (recipe instanceof ShapedRecipes) {
			items = Arrays.asList(((ShapedRecipes)recipe).recipeItems);
			isShapeless = false;
			isOreRecipe = false;
		} else if (recipe instanceof ShapelessRecipes) {
			items = ((ShapelessRecipes)recipe).recipeItems;
			isShapeless = true;
			isOreRecipe = false;
		} else if (recipe instanceof ShapedOreRecipe) {
			items = Arrays.asList(((ShapedOreRecipe)recipe).getInput());
			isShapeless = false;
			isOreRecipe = true;
		} else if (recipe instanceof ShapelessOreRecipe) {
			items = ((ShapelessOreRecipe)recipe).getInput();
			isShapeless = true;
			isOreRecipe = true;
		} else {
			items = Arrays.asList();
			isShapeless = false;
			isOreRecipe = false;
		}
		
	}
	
	public List<ItemStack> getItemStacksOnly() {
		List<ItemStack> itemStacks = new ArrayList<ItemStack>();
		for (Object item : items)
			if ((item != null) && (item instanceof ItemStack))
				itemStacks.add((ItemStack)item);
		return itemStacks;
	}
	
	public void replace(ItemStack replace, ItemStack with) {
		for (int i = 0; i < items.size(); i++) {
			Object item = items.get(i);
			if ((item instanceof ItemStack) &&
			    Utils.matches(replace, (ItemStack)item))
				items.set(i, with.copy());
		}
	}
	public void replaceOreDict(ItemStack replace, ItemStack with) {
		for (int i = 0; i < items.size(); i++)
			if (Utils.matchesOreDict(replace, items.get(i)))
				items.set(i, with.copy());
	}
	
	public IRecipe cloneRecipe() {
		
		IRecipe clonedRecipe = null;
		
		if (recipe instanceof ShapedRecipes) {
			
			ShapedRecipes shapedRecipe = (ShapedRecipes)recipe;
			clonedRecipe = new ShapedRecipes(shapedRecipe.recipeWidth, shapedRecipe.recipeHeight,
			                                 shapedRecipe.recipeItems, recipe.getRecipeOutput());
			
		} else if (recipe instanceof ShapedOreRecipe) {
			
			ShapedOreRecipe shapedOreRecipe = (ShapedOreRecipe)recipe;
			
			// I don't get why there's no constructor for ShapedOreRecipe
			// which just takes width, (optionally height) and items.
			
			int width = ReflectionHelper.getPrivateValue(ShapedOreRecipe.class, shapedOreRecipe, "width");
			boolean mirrored = ReflectionHelper.getPrivateValue(ShapedOreRecipe.class, shapedOreRecipe, "mirrored");
			
			String[] shape = new String[items.size() / width];
			Arrays.fill(shape, new String(new char[width]));
			
			clonedRecipe = new ShapedOreRecipe(recipe.getRecipeOutput(), mirrored, new Object[]{ shape });
			System.arraycopy(shapedOreRecipe.getInput(), 0, ((ShapedOreRecipe)clonedRecipe).getInput(), 0, items.size());
			
		} else if (recipe instanceof ShapelessRecipes)
			clonedRecipe = new ShapelessRecipes(recipe.getRecipeOutput(), items);
		else if (recipe instanceof ShapelessOreRecipe)
			clonedRecipe = new ShapelessOreRecipe(recipe.getRecipeOutput(), items);
		
		return clonedRecipe;
		
	}
	
}
