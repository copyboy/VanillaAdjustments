package net.mcft.copy.vanilladj.entity;

import java.util.Random;

import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

public class EntityDropModifier {
	
	@ForgeSubscribe
	public void onEntityDrop(LivingDropsEvent event) {

		Random rand = event.entity.worldObj.rand;
		if (!event.source.getDamageType().equals("player")) return;
		
		if (event.entityLiving instanceof EntityCow)
			event.drops.add(event.entity.dropItem(Item.leather.itemID, 1 + rand.nextInt(3)));
		
		if (event.entityLiving instanceof EntityPig) {
			Item item = (event.entityLiving.isBurning() ? Item.porkCooked : Item.porkRaw);
			event.drops.add(event.entity.dropItem(item.itemID, 1 + rand.nextInt(3)));
		}
		
	}
	
}