package net.mcft.copy.vanilladj.item;

import java.util.List;

import net.mcft.copy.vanilladj.misc.Constants;
import net.minecraft.block.BlockWood;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemStick extends Item {
	
	private Icon[] icons;
	
	public ItemStick(int id) {
		super(id);
		setFull3D();
		setUnlocalizedName("stick");
		setHasSubtypes(true);
		setMaxDamage(0);
		setCreativeTab(CreativeTabs.tabMaterials);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		String[] woodTypes = BlockWood.woodType;
		icons = new Icon[woodTypes.length];
		icons[0] = iconRegister.registerIcon("stick");
		for (int i = 1; i < icons.length; i++)
			icons[i] = iconRegister.registerIcon(Constants.modId + ":stick_" + woodTypes[i]);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int damage) {
		return icons[Math.min(damage, icons.length - 1)];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int id, CreativeTabs tab, List list) {
		for (int i = 0; i < BlockWood.woodType.length; i++)
			list.add(new ItemStack(id, 1, i));
	}
	
}
