package net.mcft.copy.vanilladj.player;

import java.util.EnumSet;

import net.mcft.copy.vanilladj.Config;
import net.mcft.copy.vanilladj.misc.Constants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import net.minecraft.world.World;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class PlayerRegenerationHandler implements ITickHandler {
	
	@Override
	public String getLabel() { return Constants.modName + "." + getClass().getSimpleName(); }
	
	@Override
	public EnumSet<TickType> ticks() { return EnumSet.of(TickType.PLAYER); }
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		EntityPlayer player = (EntityPlayer)tickData[0];
		boolean naturalRegeneration = player.worldObj.getGameRules().getGameRuleBooleanValue("naturalRegeneration");
		if ((Config.healTime <= 0) || naturalRegeneration) return;
		
		FoodStats food = player.getFoodStats();
		int foodLevel = food.getFoodLevel();
		if (foodLevel < 8) return;

		int healTicks = Config.healTime * 20;
		if (foodLevel < 12) healTicks *= 4;
		else if (foodLevel < 15) healTicks *= 2;
		
		if (player.ticksExisted % healTicks > 0) return;
		
		player.heal(1.0F);
		food.addExhaustion(3.0F);
	}
	
	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {  }
	
}
