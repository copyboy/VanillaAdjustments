package net.mcft.copy.vanilladj.player;

import net.mcft.copy.vanilladj.VanillaAdjustments;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;

public class PlayerDeathDropModifier {
	
	@ForgeSubscribe
	public void on(PlayerDropsEvent event) {
		for (EntityItem item : event.drops)
			item.lifespan = VanillaAdjustments.config.getInteger("playerDeathItemLifespan") * 20;
	}
	
}
