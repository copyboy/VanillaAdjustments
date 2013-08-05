package net.mcft.copy.vanilladj.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

/** Iterates over all recipes and hands them to all listeners. */
public class RecipeIterator {
	
	private List<IRecipeListener> listeners = new ArrayList<IRecipeListener>();
	
	private boolean iterating = false;
	private boolean remove;
	
	public void run() {
		
		if (iterating) throw new IllegalStateException("Already iterating.");
		iterating = true;
		
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		int count = recipes.size();
		for (int i = 0; i < count; i++) {
			IRecipe recipe = recipes.get(i);
			remove = false;
			for (IRecipeListener listener : listeners)
				listener.doSomethingWith(this, recipe);
			if (remove) {
				recipes.remove(i);
				count--;
				i--;
			}
		}
		
	}
	
	public void remove() {
		if (!iterating) throw new IllegalStateException("Not iterating.");
		remove = true;
	}
	
	public void listen(IRecipeListener listener) {
		listeners.add(listener);
	}
	
}
