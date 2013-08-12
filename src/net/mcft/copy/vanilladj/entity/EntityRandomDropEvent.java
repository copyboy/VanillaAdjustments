package net.mcft.copy.vanilladj.entity;

import java.util.Random;

import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.item.Item;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import cpw.mods.fml.common.Loader;

public class EntityRandomDropEvent {
	
	@ForgeSubscribe
	public void onLivingUpdate(LivingUpdateEvent event) {
		
		if (event.entity.worldObj.isRemote) return;
		Random rand = event.entity.worldObj.rand;
		
		if (event.entity instanceof EntityChicken &&
		    !Loader.isModLoaded("ChickenShed"))
			if ((event.entity.ticksExisted % 4000 == 0) && (rand.nextInt(6) == 0))
				event.entity.dropItem(Item.feather.itemID, 1 + rand.nextInt(2));
		
	}
	
}
