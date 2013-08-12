package net.mcft.copy.vanilladj.entity;

import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.item.Item;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

public class EntityDropModifier {
	
	@ForgeSubscribe
	public void onEntityDrop(LivingDropsEvent event) {
		if(event.source.getDamageType().equals("player")) {
			
			if(event.entityLiving instanceof EntityCow) {
				event.entityLiving.dropItem(Item.leather.itemID, 2);				
			}
			if(event.entityLiving instanceof EntityPig) {
				EntityPig pig = (EntityPig)event.entity;
				
				if(pig.isBurning()) {
					event.entityLiving.dropItem(Item.porkCooked.itemID, 2);
				}
				else {
					event.entityLiving.dropItem(Item.porkRaw.itemID, 2);
				}
			}
		}
	}
}