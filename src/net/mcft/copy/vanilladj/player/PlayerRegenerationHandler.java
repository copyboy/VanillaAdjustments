package net.mcft.copy.vanilladj.player;

import net.mcft.copy.vanilladj.VanillaAdjustments;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class PlayerRegenerationHandler {
	
	@ForgeSubscribe
	public void onEntityConstructing(EntityConstructing event) {
		if (!(event.entity instanceof EntityPlayer) ||
		    (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) ||
		    !VanillaAdjustments.config.getBoolean("regeneration.enabled")) return;
		EntityPlayer player = (EntityPlayer)event.entity;
		player.registerExtendedProperties(PlayerRegenerationProperties.identifier,
		                                  new PlayerRegenerationProperties());
	}
	
	@ForgeSubscribe
	public void onLivingUpdate(LivingUpdateEvent event) {
		if (!(event.entity instanceof EntityPlayer) ||
		    (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) ||
		    !VanillaAdjustments.config.getBoolean("regeneration.enabled")) return;
		EntityPlayer player = (EntityPlayer)event.entity;
		((PlayerRegenerationProperties)player.getExtendedProperties(
				PlayerRegenerationProperties.identifier)).update();
	}
	
}
