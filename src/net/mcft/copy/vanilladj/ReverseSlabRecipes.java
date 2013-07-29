package net.mcft.copy.vanilladj;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public final class ReverseSlabRecipes {
	
	public static Object[] reverseThis = {
		new ItemStack(Block.woodSingleSlab, 1, 0), new ItemStack(Block.planks, 1, 0),
		new ItemStack(Block.woodSingleSlab, 1, 1), new ItemStack(Block.planks, 1, 1),
		new ItemStack(Block.woodSingleSlab, 1, 2), new ItemStack(Block.planks, 1, 2),
		new ItemStack(Block.woodSingleSlab, 1, 3), new ItemStack(Block.planks, 1, 3),
		new ItemStack(Block.stoneSingleSlab, 1, 0), Block.stone,
		new ItemStack(Block.stoneSingleSlab, 1, 1), Block.sandStone,
		new ItemStack(Block.stoneSingleSlab, 1, 3), Block.cobblestone,
		new ItemStack(Block.stoneSingleSlab, 1, 4), Block.brick,
		new ItemStack(Block.stoneSingleSlab, 1, 5), Block.stoneBrick,
		new ItemStack(Block.stoneSingleSlab, 1, 6), Block.netherBrick,
		new ItemStack(Block.stoneSingleSlab, 1, 7), Block.blockNetherQuartz
	};
	
	private ReverseSlabRecipes() {  }
	
	public static void add() {
		
		for (int i = 0; i < reverseThis.length; i += 2) {
			
			ItemStack slab = ((reverseThis[i] instanceof ItemStack)
					? (ItemStack)reverseThis[i]
					: new ItemStack((Block)reverseThis[i], 1, 0));
			ItemStack block = ((reverseThis[i + 1] instanceof ItemStack)
					? (ItemStack)reverseThis[i + 1]
					: new ItemStack((Block)reverseThis[i + 1], 1, 0));
			
			GameRegistry.addShapelessRecipe(block, slab, slab);
			
		}
		
	}
	
}
