package net.mcft.copy.vanilladj.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

public class RecipeIterator {
	
	private List<IRecipeListener> listeners = new ArrayList<IRecipeListener>();
	
	public void run() {
		
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		int count = recipes.size();
		for (int i = 0; i < count; i++)
			for (IRecipeListener listener : listeners)
				listener.doSomethingWith(recipes.get(i));
		
	}
	
	public void listen(IRecipeListener listener) {
		listeners.add(listener);
	}
	
}
