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
import cpw.mods.fml.common.registry.GameRegistry;

public class RecipeReverser implements IRecipeListener {
	
	private final ItemStack[] reverse;
	
	public RecipeReverser(Object... reverse) {
		
		this.reverse = new ItemStack[reverse.length];
		for (int i = 0; i < reverse.length; i++)
			this.reverse[i] = Utils.makeStack(reverse[i]);
		
	}
	
	@Override
	public void doSomethingWith(IRecipe recipe) {
		
		ItemStack output = recipe.getRecipeOutput();
		if ((output == null) || !Utils.contains(reverse, output)) return;

		int resultAmount = output.stackSize;
		int recipeAmount = 0;
		
		List<ItemStack> list = new ArrayList<ItemStack>();
		
		if (recipe instanceof ShapedRecipes)
			list.addAll(Arrays.asList(((ShapedRecipes)recipe).recipeItems));
		else if (recipe instanceof ShapelessRecipes)
			list.addAll(((ShapelessRecipes)recipe).recipeItems);
		else if (recipe instanceof ShapedOreRecipe)
			for (Object shapedItem : ((ShapedOreRecipe)recipe).getInput())
				if (shapedItem instanceof ItemStack)
					list.add((ItemStack)shapedItem);
		else if (recipe instanceof ShapelessOreRecipe)
			for (Object shapelessItem : ((ShapelessOreRecipe)recipe).getInput())
				if (shapelessItem instanceof ItemStack)
					list.add((ItemStack)shapelessItem);
		
		ItemStack recipeItem = null;
		for (ItemStack item : list) {
			if (item == null) continue;
			if (recipeItem == null) recipeItem = item.copy();
			else if (!Utils.matches(recipeItem, item)) return;
			recipeAmount++;
		}
		
		int gcd = gcd(resultAmount, recipeAmount);
		resultAmount /= gcd;
		recipeAmount /= gcd;
		if (resultAmount > 9) return;
		
		List<ItemStack> recipeItems = new ArrayList<ItemStack>(resultAmount);
		for (int i = 0; i < resultAmount; i++) recipeItems.add(output);
		
		output = recipeItem.copy();
		output.stackSize = recipeAmount;
		
		GameRegistry.addRecipe(new ShapelessRecipes(output, recipeItems));
		
	}
	
	private static int gcd(int a, int b) {
		return ((b == 0) ? a : gcd(b, a % b));
	}
	
}
