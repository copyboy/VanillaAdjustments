package net.mcft.copy.vanilladj.recipes;

import net.mcft.copy.vanilladj.recipe.RecipeReplacer;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

/** Replaces cobblestone in most vanilla recipes with smoothstone. */
public final class StoneRecipeReplacer extends RecipeReplacer {
	
	private static final Object replace = Block.cobblestone;
	private static final Object with = Block.stone;
	
	private static final Object[] replaceIn = {
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
	
	public static final StoneRecipeReplacer instance = new StoneRecipeReplacer();
	
	private StoneRecipeReplacer() { super(replace, with, replaceIn); }
	
}
