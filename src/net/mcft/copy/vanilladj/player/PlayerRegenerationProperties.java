package net.mcft.copy.vanilladj.player;

import net.mcft.copy.vanilladj.VanillaAdjustments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.FoodStats;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class PlayerRegenerationProperties implements IExtendedEntityProperties {
	
	public static final String identifier = "vanilladj.regeneration";
	
	private EntityPlayer player;
	private int lastHurtResistanceTime;
	
	private double healProgress = 0;
	
	private int timePenalty = 0;
	private int timePenaltyTimeout = 0;
	private int timePenaltyApplied = 0;
	
	private int bonusTime = 0;
	private int bonusPenaltyTimeout = 0;
	
	@Override
	public void init(Entity entity, World world) {
		player = (EntityPlayer)entity;
	}
	
	public void update() {
		
		updateTimePenalty();
		updateBonus();
		
		lastHurtResistanceTime = player.hurtResistantTime;
		
		updateHealing();
		
	}
	
	private void updateTimePenalty() {
		if (timePenaltyTimeout > 0) timePenaltyTimeout--;
		else if (player.hurtResistantTime > lastHurtResistanceTime) { // See if the player got hurt.
			int penalty = VanillaAdjustments.config.getTicks("regeneration.hurtTimePenalty");
			int hurtTimePenaltyMaximum = VanillaAdjustments.config.getTicks("regeneration.hurtTimePenaltyMaximum");
			int hurtTimePenaltyMaximumPerHeal = VanillaAdjustments.config.getTicks("regeneration.hurtTimePenaltyMaximumPerHeal");
			penalty = Math.min(penalty, hurtTimePenaltyMaximum - timePenalty);
			penalty = Math.min(penalty, hurtTimePenaltyMaximumPerHeal - timePenaltyApplied);
			if (penalty > 0) {
				timePenalty += penalty;
				timePenaltyApplied += penalty;
				int hurtTimePenaltyTimeout = VanillaAdjustments.config.getTicks("regeneration.hurtTimePenaltyTimeout");
				timePenaltyTimeout = Math.max(hurtTimePenaltyTimeout, 10);
			}
		}
	}
	
	private void updateBonus() {
		int bonusMaximumTime = VanillaAdjustments.config.getTicks("regeneration.bonusMaximumTime");
		if (bonusTime < bonusMaximumTime) bonusTime++;
		
		if (bonusPenaltyTimeout > 0) bonusPenaltyTimeout--;
		else if (player.hurtResistantTime > lastHurtResistanceTime) { // See if the player got hurt.
			int penalty = VanillaAdjustments.config.getTicks("regeneration.bonusHurtPenalty");
			bonusTime = Math.max(bonusTime - penalty, 0);
			int bonusHurtPenaltyTimeout = VanillaAdjustments.config.getTicks("regeneration.bonusHurtPenaltyTimeout");
			bonusPenaltyTimeout = Math.max(bonusHurtPenaltyTimeout, 10);
		}
	}
	
	private void updateHealing() {
		FoodStats food = player.getFoodStats();
		
		// Reset foodTimer to disable Vanilla healing
		// regardless of naturalRegeneration gamerule.
		if (food.getFoodLevel() > 0)
			food.foodTimer = 0;
		
		if (timePenalty > 0) timePenalty--;
		else if (player.shouldHeal()) {
			double foodFactor;
			int hungerMaximum = VanillaAdjustments.config.getInteger("regeneration.hungerMaximum");
			int hungerMinimum = VanillaAdjustments.config.getInteger("regeneration.hungerMinimum");
			if (food.getFoodLevel() >= hungerMaximum) foodFactor = 1.0;
			else if (food.getFoodLevel() < hungerMinimum) foodFactor = 0.0;
			else foodFactor = (double)(food.getFoodLevel() - hungerMinimum + 1) / (hungerMaximum - hungerMinimum + 1);
			
			double bonusFactor = calculateBonusFactor();
			healProgress += foodFactor * bonusFactor;
			
			int timeBetweenHeal = VanillaAdjustments.config.getTicks("regeneration.timeBetweenHeal");
			if (healProgress >= timeBetweenHeal) {
				player.heal(1.0F);
				float exhaustion = (float)VanillaAdjustments.config.getDouble("regeneration.exhaustion");
				food.addExhaustion(exhaustion);
				healProgress -= timeBetweenHeal;
				timePenaltyApplied = 0;
			}
		} else healProgress = 0;
	}
	
	private double calculateBonusFactor() {
		double minFactor = VanillaAdjustments.config.getDouble("regeneration.bonusMinimumFactor");
		double maxFactor = VanillaAdjustments.config.getDouble("regeneration.bonusMaximumFactor");
		if (minFactor == maxFactor)
			return minFactor;
		
		int bonusStartTime = VanillaAdjustments.config.getTicks("regeneration.bonusStartTime");
		int bonusStopTime = VanillaAdjustments.config.getTicks("regeneration.bonusStopTime");
		
		if (bonusTime <= bonusStartTime) return minFactor;
		else if (bonusTime >= bonusStopTime) return maxFactor;
		else return (minFactor + (double)(bonusTime - bonusStartTime) / (bonusStopTime - bonusStartTime)
		                                                              * (maxFactor - minFactor));
	}
	
	@Override
	public void saveNBTData(NBTTagCompound compound) {
		compound.setDouble("healProgress", healProgress);
		
		compound.setInteger("timePenalty", timePenalty);
		compound.setInteger("timePenaltyTimeout", timePenaltyTimeout);
		compound.setInteger("timePenaltyApplied", timePenaltyApplied);
		
		compound.setInteger("bonusTime", bonusTime);
		compound.setInteger("bonusPenaltyTimeout", bonusPenaltyTimeout);
	}
	
	@Override
	public void loadNBTData(NBTTagCompound compound) {
		healProgress = compound.getDouble("healProgress");
		
		timePenalty = compound.getInteger("timePenalty");
		timePenaltyTimeout = compound.getInteger("timePenaltyTimeout");
		timePenaltyApplied = compound.getInteger("timePenaltyApplied");
		
		bonusTime = compound.getInteger("bonusTime");
		bonusPenaltyTimeout = compound.getInteger("bonusPenaltyTimeout");
	}
	
}
