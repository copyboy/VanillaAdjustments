package net.mcft.copy.vanilladj.item;

import net.mcft.copy.vanilladj.misc.RandomUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemPickaxeDiamond extends ItemPickaxe {
	
	public ItemPickaxeDiamond(int id) {
		super(id, EnumToolMaterial.EMERALD);
		setUnlocalizedName("pickaxeDiamond");
		func_111206_d("diamond_pickaxe");
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, int blockID, int x, int y, int z, EntityLivingBase player) {
		if ((Block.blocksList[blockID] instanceof BlockStone) &&
		    RandomUtils.getBoolean(1.0 / 3)) return true;
		return super.onBlockDestroyed(stack, world, blockID, x, y, z, player);
	}
	
}
