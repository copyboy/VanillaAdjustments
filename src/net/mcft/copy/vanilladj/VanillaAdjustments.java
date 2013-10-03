package net.mcft.copy.vanilladj;

import java.util.List;
import java.util.logging.Logger;

import net.mcft.copy.vanilladj.block.BlockWoodFence;
import net.mcft.copy.vanilladj.block.BlockWoodPressurePlate;
import net.mcft.copy.vanilladj.config.Configuration;
import net.mcft.copy.vanilladj.config.setting.list.RecipeReplaceListSetting.Entry;
import net.mcft.copy.vanilladj.entity.EntityDropModifier;
import net.mcft.copy.vanilladj.entity.EntityRandomDropEvent;
import net.mcft.copy.vanilladj.item.ItemPickaxeDiamond;
import net.mcft.copy.vanilladj.item.ItemPickaxeIron;
import net.mcft.copy.vanilladj.item.ItemStick;
import net.mcft.copy.vanilladj.misc.Constants;
import net.mcft.copy.vanilladj.misc.ItemUtils;
import net.mcft.copy.vanilladj.misc.Utils;
import net.mcft.copy.vanilladj.player.PlayerDeathDropModifier;
import net.mcft.copy.vanilladj.player.PlayerRegenerationHandler;
import net.mcft.copy.vanilladj.recipe.RecipeItemReplacerWood;
import net.mcft.copy.vanilladj.recipe.RecipeIterator;
import net.mcft.copy.vanilladj.recipe.RecipeRemover;
import net.mcft.copy.vanilladj.recipe.RecipeReplacer;
import net.mcft.copy.vanilladj.recipe.RecipeReverser;
import net.minecraft.block.Block;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = Constants.modId,
     name = Constants.modName,
     version = "@VERSION@",
     useMetadata = true)
@NetworkMod(clientSideRequired = true,
            serverSideRequired = false)
public class VanillaAdjustments {
	
	@Instance(Constants.modId)
	public static VanillaAdjustments instance;
	
	public static Logger log;
	
	public static Configuration config;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		log = event.getModLog();
		
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		config.save();
		
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		
		MinecraftForge.EVENT_BUS.register(new EntityDropModifier());
		MinecraftForge.EVENT_BUS.register(new EntityRandomDropEvent());
		MinecraftForge.EVENT_BUS.register(new PlayerDeathDropModifier());
		MinecraftForge.EVENT_BUS.register(new PlayerRegenerationHandler());
		
		Utils.replace(Item.pickaxeIron, ItemPickaxeIron.class);
		Utils.replace(Item.pickaxeDiamond, ItemPickaxeDiamond.class);
		
		EnumToolMaterial.STONE.customCraftingMaterial = Item.itemsList[Block.stone.blockID];
		ItemUtils.setToolDurability(112, Item.swordWood, Item.pickaxeWood, Item.shovelWood, Item.axeWood, Item.hoeWood);
		ItemUtils.setToolDurability(144, Item.swordStone, Item.pickaxeStone, Item.shovelStone, Item.axeStone, Item.hoeStone);
		ItemUtils.setToolDurability(96, Item.swordGold, Item.pickaxeGold, Item.shovelGold, Item.axeGold, Item.hoeGold);
		
		ItemUtils.adjustArmorDurability(2.25, Item.helmetLeather, Item.plateLeather, Item.legsLeather, Item.bootsLeather);
		ItemUtils.adjustArmorDurability(1.25, Item.helmetGold, Item.plateGold, Item.legsGold, Item.bootsGold);
		
		OreDictionary.registerOre("stickWood", new ItemStack(Item.stick, 1, Constants.anyDamage));
		
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
		RecipeIterator iterator = new RecipeIterator();
		
		ItemStack[] disabledRecipes = ((List<ItemStack>)config.getValue("recipes.disable")).toArray(new ItemStack[0]);
		iterator.listen(new RecipeRemover(disabledRecipes));
		
		ItemStack[] reversedRecipes = ((List<ItemStack>)config.getValue("recipes.reverse")).toArray(new ItemStack[0]);
		iterator.listen(new RecipeReverser(reversedRecipes));
		
		for (Entry entry : (List<Entry>)config.getValue("recipes.replace"))
			iterator.listen(new RecipeReplacer(entry.replace, entry.with, entry.replaceIn));
		
		iterator.listen(new RecipeItemReplacerWood(
				Item.stick, ItemStick.class, false,
				Block.pressurePlatePlanks, BlockWoodPressurePlate.class, true,
				Block.fence, BlockWoodFence.class, true
			));
		
		iterator.run();
		
	}
	
}
