package net.mcft.copy.vanilladj.recipe;

import java.util.ArrayList;
import java.util.List;

import net.mcft.copy.vanilladj.VanillaAdjustments;
import net.mcft.copy.vanilladj.misc.Constants;
import net.mcft.copy.vanilladj.misc.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;
import cpw.mods.fml.common.registry.GameRegistry;

/** Adds reverse crafting recipes for recipes of some items. */
public class RecipeReverser implements IRecipeListener {
	
	private final ItemStack[] reverse;
	
	public RecipeReverser(ItemStack... reverse) {
		this.reverse = reverse;
	}
	
	@Override
	public void doSomethingWith(RecipeIterator iterator, IRecipe recipe) {
		ItemStack output = recipe.getRecipeOutput();
		if ((output == null) || (reverse.length <= 0) || !Utils.contains(reverse, output)) return;
		reverse(recipe);
	}
	
	public static void reverse(IRecipe recipe) {
		
		ItemStack output = recipe.getRecipeOutput();
		int resultAmount = output.stackSize;
		int recipeAmount = 0;
		
		RecipeContainer container = new RecipeContainer(recipe);
		
		ItemStack recipeItem = null;
		for (ItemStack item : container.getItemStacksOnly()) {
			if (recipeItem == null) recipeItem = item.copy();
			else if (!Utils.matches(recipeItem, item)) {
				VanillaAdjustments.log.warning("Failed to create reverse recipe for " + output.getDisplayName() + ": " +
				                               "Recipe can't contain more than one type of item.");
				return;
			}
			recipeAmount++;
		}
		
		int gcd = gcd(resultAmount, recipeAmount);
		resultAmount /= gcd;
		recipeAmount /= gcd;
		if (resultAmount > 9) {
			VanillaAdjustments.log.warning("Failed to create reverse recipe for " + output.getDisplayName() + ".");
			return;
		}
		
		List<ItemStack> recipeItems = new ArrayList<ItemStack>(resultAmount);
		for (int i = 0; i < resultAmount; i++) recipeItems.add(output);
		
		output = recipeItem.copy();
		if (output.getItemDamage() == Constants.anyDamage)
			output.setItemDamage(0);
		output.stackSize = recipeAmount;
		
		GameRegistry.addRecipe(new ShapelessRecipes(output, recipeItems));
		
	}
	
	private static int gcd(int a, int b) {
		return ((b == 0) ? a : gcd(b, a % b));
	}
	
}
