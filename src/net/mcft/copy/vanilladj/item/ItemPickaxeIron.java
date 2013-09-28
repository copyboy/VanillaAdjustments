package net.mcft.copy.vanilladj.item;

import net.mcft.copy.vanilladj.misc.RandomUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemPickaxeIron extends ItemPickaxe {
	
	public ItemPickaxeIron(int id) {
		super(id, EnumToolMaterial.IRON);
		setUnlocalizedName("pickaxeIron");
		setTextureName("iron_pickaxe");
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, int blockID, int x, int y, int z, EntityLivingBase player) {
		if ((Block.blocksList[blockID] instanceof BlockStone) &&
		    RandomUtils.getBoolean(1.0 / 3)) return true;
		return super.onBlockDestroyed(stack, world, blockID, x, y, z, player);
	}
	
}
