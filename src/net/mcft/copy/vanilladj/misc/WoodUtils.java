package net.mcft.copy.vanilladj.misc;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockWood;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;

public final class WoodUtils {
	
	private WoodUtils() {  }
	
	@SideOnly(Side.CLIENT)
	public static Icon[] registerIcons(IconRegister iconRegister, String before) {
		String[] woodTypes = BlockWood.woodType;
		Icon[] icons = new Icon[woodTypes.length];
		for (int i = 0; i < icons.length; i++)
			icons[i] = iconRegister.registerIcon(before + woodTypes[i]);
		return icons;
	}

	@SideOnly(Side.CLIENT)
	public static Icon[] registerIcons(IconRegister iconRegister) {
		return registerIcons(iconRegister, "planks_");
	}
	
}
