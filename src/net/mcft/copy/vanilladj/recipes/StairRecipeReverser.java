package net.mcft.copy.vanilladj.recipes;

import net.mcft.copy.vanilladj.recipe.RecipeReverser;
import net.minecraft.block.Block;

public final class StairRecipeReverser extends RecipeReverser {
	
	private static final Object[] reverse = {
		Block.stairsWoodOak,
		Block.stairsWoodSpruce,
		Block.stairsWoodBirch,
		Block.stairsWoodJungle,
		Block.stairsSandStone,
		Block.stairsCobblestone,
		Block.stairsBrick,
		Block.stairsStoneBrick,
		Block.stairsNetherBrick,
		Block.stairsNetherQuartz
	};
	
	public static final StairRecipeReverser instance = new StairRecipeReverser();
	
	private StairRecipeReverser() { super(reverse); }
	
}
