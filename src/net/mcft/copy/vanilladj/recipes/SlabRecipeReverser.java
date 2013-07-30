package net.mcft.copy.vanilladj.recipes;

import net.mcft.copy.vanilladj.recipe.RecipeReverser;
import net.minecraft.block.Block;

/** Adds reverse slab crafting recipes, so that 2 slabs => 1 block. */
public final class SlabRecipeReverser extends RecipeReverser {
	
	private static final Object[] reverse = {
		Block.woodSingleSlab,
		Block.stoneSingleSlab
	};
	
	public static final SlabRecipeReverser instance = new SlabRecipeReverser();
	
	private SlabRecipeReverser() { super(reverse); }
	
}
