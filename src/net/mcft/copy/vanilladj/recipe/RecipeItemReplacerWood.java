package net.mcft.copy.vanilladj.recipe;

import net.mcft.copy.betterstorage.misc.Constants;
import net.mcft.copy.vanilladj.misc.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWood;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

public class RecipeItemReplacerWood implements IRecipeListener {
	
	private final ItemStack[] replace;
	private final Class[] with;
	private final boolean[] reverse;
	
	private ItemStack[] matchOnly = null;
	
	public RecipeItemReplacerWood(Object... replace) {
		
		if ((replace.length % 3) > 0)
			throw new IllegalArgumentException("Number of arguments need to be divisible by 3.");
		
		int count = replace.length / 3;
		this.replace = new ItemStack[count];
		this.with = new Class[count];
		this.reverse = new boolean[count];
		
		for (int i = 0; i < count; i++) {
			this.replace[i] = Utils.makeStack(replace[i * 3]);
			this.with[i] = (Class)replace[i * 3 + 1];
			this.reverse[i] = (Boolean)replace[i * 3 + 2];
		}
		
	}
	
	public void matchOnly(Object... items) {
		matchOnly = new ItemStack[items.length];
		for (int i = 0; i < items.length; i++)
			matchOnly[i] = Utils.makeStack(items[i]);
	}
	
	@Override
	public void doSomethingWith(RecipeIterator iterator, IRecipe recipe) {
		
		ItemStack output = recipe.getRecipeOutput();
		if (output == null) return;
		
		int index = Utils.indexOf(this.replace, output);
		if (index < 0) return;
		
		ItemStack replace = this.replace[index];
		Class with = this.with[index];
		boolean reverse = this.reverse[index];
		
		RecipeContainer container = new RecipeContainer(recipe);
		
		if (matchOnly != null)
			for (ItemStack match : matchOnly)
				for (Object item : container.items)
					if (!Utils.matchesOreDict(match, item))
						return;
		
		for (int i = 0; i < BlockWood.woodType.length; i++) {
			IRecipe clone = container.cloneRecipe();
			RecipeContainer cloneContainer = new RecipeContainer(clone);
			replaceWood(cloneContainer, Block.wood.blockID, i);
			replaceWood(cloneContainer, Block.planks.blockID, i);
			replaceWood(cloneContainer, Item.stick.itemID, i);
			ItemStack cloneOutput = clone.getRecipeOutput();
			cloneOutput.setItemDamage(i);
			GameRegistry.addRecipe(clone);
			if (reverse) RecipeReverser.reverse(clone);
		}
		
		if (Item.class.isAssignableFrom(with)) Utils.replace(replace.getItem(), with);
		else Utils.replaceWithMetadata(Block.blocksList[replace.itemID], with);
		
		iterator.remove();
		
	}
	
	private void replaceWood(RecipeContainer container, int id, int meta) {
		container.replaceOreDict(new ItemStack(id, 1, Constants.anyDamage), new ItemStack(id, 1, meta));
	}
	
}
