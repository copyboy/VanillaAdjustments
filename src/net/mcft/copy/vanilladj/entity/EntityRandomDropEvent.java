package net.mcft.copy.vanilladj.entity;

import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.item.Item;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import cpw.mods.fml.common.Loader;

public class EntityRandomDropEvent {

	@ForgeSubscribe
	public void onLivingUpdate(LivingUpdateEvent event) {
		if(event.entity.worldObj.isRemote) {
			return;
		}
		
		if(!Loader.isModLoaded("ChickenShed") && event.entity instanceof EntityChicken) {
			EntityChicken chicken = (EntityChicken)event.entity;
			
			if(chicken.worldObj.rand.nextInt(32000) == 0) {
				chicken.dropItem(Item.feather.itemID, 1);
			}
		}
	}
}
