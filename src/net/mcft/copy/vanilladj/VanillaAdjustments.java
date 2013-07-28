package net.mcft.copy.vanilladj;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;

@Mod(modid = Constants.modId,
     version = "@VERSION@",
     useMetadata = true)
public class VanillaAdjustments {
	
	@Instance(Constants.modId)
	public static VanillaAdjustments instance;
	
	@EventHandler
	public void load(FMLInitializationEvent event) {
		
		
		
	}
	
}
