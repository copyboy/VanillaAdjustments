package net.mcft.copy.vanilladj;

import java.util.logging.Logger;

import net.mcft.copy.vanilladj.block.BlockWoodPressurePlate;
import net.mcft.copy.vanilladj.entity.EntityDropModifier;
import net.mcft.copy.vanilladj.entity.EntityRandomDropEvent;
import net.mcft.copy.vanilladj.item.ItemStick;
import net.mcft.copy.vanilladj.misc.Constants;
import net.mcft.copy.vanilladj.misc.ToolDurability;
import net.mcft.copy.vanilladj.recipe.RecipeItemReplacerWood;
import net.mcft.copy.vanilladj.recipe.RecipeIterator;
import net.mcft.copy.vanilladj.recipes.SlabRecipeReverser;
import net.mcft.copy.vanilladj.recipes.StairRecipeReverser;
import net.mcft.copy.vanilladj.recipes.StoneRecipeReplacer;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Constants.modId,
     name = Constants.modName,
     version = "@VERSION@",
     useMetadata = true)
public class VanillaAdjustments {
	
	@Instance(Constants.modId)
	public static VanillaAdjustments instance;
	
	public static Logger log;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		log = event.getModLog();
		
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
		ToolDurability.set(112, Item.swordWood, Item.pickaxeWood, Item.shovelWood, Item.axeWood, Item.hoeWood);
		ToolDurability.set(144, Item.swordStone, Item.pickaxeStone, Item.shovelStone, Item.axeStone, Item.hoeStone);
		ToolDurability.set(96, Item.swordGold, Item.pickaxeGold, Item.shovelGold, Item.axeGold, Item.hoeGold);
		
		OreDictionary.registerOre("stickWood", new ItemStack(Item.stick, 1, Constants.anyDamage));
		
		RecipeIterator iterator = new RecipeIterator();
		
		iterator.listen(StoneRecipeReplacer.instance);
		iterator.listen(SlabRecipeReverser.instance);
		iterator.listen(StairRecipeReverser.instance);
		
		iterator.listen(new RecipeItemReplacerWood(
				Item.stick, ItemStick.class, false,
				Block.pressurePlatePlanks, BlockWoodPressurePlate.class, true));
		
		iterator.run();
		
		MinecraftForge.EVENT_BUS.register(new EntityDropModifier());
		MinecraftForge.EVENT_BUS.register(new EntityRandomDropEvent());
		
	}
	
}
