package net.mcft.copy.vanilladj;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public final class ReverseStairRecipes {
	
	public static Object[] reverseThis = {
		new ItemStack(Block.stairsWoodOak, 1, 0),    new ItemStack(Block.planks, 3, 0),
		new ItemStack(Block.stairsWoodSpruce, 1, 0), new ItemStack(Block.planks, 3, 1),
		new ItemStack(Block.stairsWoodBirch, 1, 0),  new ItemStack(Block.planks, 3, 2),
		new ItemStack(Block.stairsWoodJungle, 1, 0), new ItemStack(Block.planks, 3, 3),
		new ItemStack(Block.stairsSandStone, 1, 0),    Block.sandStone,
		new ItemStack(Block.stairsCobblestone, 1, 0),  Block.cobblestone,
		new ItemStack(Block.stairsBrick, 1, 0),        Block.brick,
		new ItemStack(Block.stairsStoneBrick, 1, 0),   Block.stoneBrick,
		new ItemStack(Block.stairsNetherBrick, 1, 0),  Block.netherBrick,
		new ItemStack(Block.stairsNetherQuartz, 1, 0), Block.blockNetherQuartz
	};
	
	private ReverseStairRecipes() {  }
	
	public static void add() {
		
		for (int i = 0; i < reverseThis.length; i += 2) {
			
			ItemStack stair = ((reverseThis[i] instanceof ItemStack)
					? (ItemStack)reverseThis[i]
					: new ItemStack((Block)reverseThis[i], 1, 0));
			ItemStack block = ((reverseThis[i + 1] instanceof ItemStack)
					? (ItemStack)reverseThis[i + 1]
					: new ItemStack((Block)reverseThis[i + 1], 3, 0));
			
			GameRegistry.addShapelessRecipe(block, stair, stair);
			
		}
		
	}
	
}
