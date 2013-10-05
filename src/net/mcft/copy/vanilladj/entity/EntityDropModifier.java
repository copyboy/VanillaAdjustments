package net.mcft.copy.vanilladj.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.mcft.copy.vanilladj.VanillaAdjustments;
import net.mcft.copy.vanilladj.config.setting.list.EntityDropListSetting.Entry;
import net.mcft.copy.vanilladj.misc.Utils;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

public class EntityDropModifier {
	
	private static Map<Item, Item> burningReplace = new HashMap<Item, Item>();
	static {
		burningReplace.put(Item.porkRaw, Item.porkCooked);
		burningReplace.put(Item.fishRaw, Item.fishCooked);
		burningReplace.put(Item.beefRaw, Item.beefCooked);
		burningReplace.put(Item.chickenRaw, Item.chickenCooked);
	}
	
	@ForgeSubscribe
	public void onEntityDrop(LivingDropsEvent event) {
		
		Random rand = event.entity.worldObj.rand;
		String entity = (String)EntityList.classToStringMapping.get(event.entity.getClass());
		
		for (Entry drop : (List<Entry>)VanillaAdjustments.config.getValue("adjustment.mobDrops")) {
			if (!Utils.wildcardMatch(drop.entity, entity)) continue;
			if (drop.empty) event.drops.clear();
			if ((drop.player && !event.recentlyHit) ||
			    (rand.nextDouble() > drop.chance)) continue;
			int amount = drop.minAmount;
			if (drop.maxAmount > drop.minAmount)
				amount += rand.nextInt(drop.maxAmount - drop.minAmount);
			if (amount <= 0) continue;
			ItemStack stack = drop.stack.copy();
			drop.stack.stackSize = amount;
			if (event.entity.isBurning()) {
				Item item = burningReplace.get(stack.getItem());
				if (item != null) stack.itemID = item.itemID;
			}
			event.drops.add(new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, stack));
		}
		
	}
	
}