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
	
	public ItemPickaxeDiamond() {
		super(1, EnumToolMaterial.EMERALD);
		setUnlocalizedName("pickaxeDiamond");
		func_111206_d("diamond_pickaxe");
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, int x, int y, int z, int side, EntityLivingBase player) {
		if ((Block.blocksList[world.getBlockId(x, y, z)] instanceof BlockStone) &&
		    RandomUtils.getBoolean(1.0 / 3)) return true;
		return super.onBlockDestroyed(stack, world, x, y, z, side, player);
	}
	
}
