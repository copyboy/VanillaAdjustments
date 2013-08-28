package net.mcft.copy.vanilladj;

import java.util.logging.Logger;

import net.mcft.copy.vanilladj.block.BlockWoodFence;
import net.mcft.copy.vanilladj.block.BlockWoodPressurePlate;
import net.mcft.copy.vanilladj.entity.EntityDropModifier;
import net.mcft.copy.vanilladj.entity.EntityRandomDropEvent;
import net.mcft.copy.vanilladj.item.ItemPickaxeDiamond;
import net.mcft.copy.vanilladj.item.ItemPickaxeIron;
import net.mcft.copy.vanilladj.item.ItemStick;
import net.mcft.copy.vanilladj.misc.Constants;
import net.mcft.copy.vanilladj.misc.ItemUtils;
import net.mcft.copy.vanilladj.misc.Utils;
import net.mcft.copy.vanilladj.recipe.RecipeItemReplacerWood;
import net.mcft.copy.vanilladj.recipe.RecipeIterator;
import net.mcft.copy.vanilladj.recipes.SlabRecipeReverser;
import net.mcft.copy.vanilladj.recipes.StairRecipeReverser;
import net.mcft.copy.vanilladj.recipes.StoneRecipeReplacer;
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
	public void init(FMLInitializationEvent event) {
		
		MinecraftForge.EVENT_BUS.register(new EntityDropModifier());
		MinecraftForge.EVENT_BUS.register(new EntityRandomDropEvent());
		
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
		
		iterator.listen(StoneRecipeReplacer.instance);
		iterator.listen(SlabRecipeReverser.instance);
		iterator.listen(StairRecipeReverser.instance);
		
		iterator.listen(new RecipeItemReplacerWood(
				Item.stick, ItemStick.class, false,
				Block.pressurePlatePlanks, BlockWoodPressurePlate.class, true,
				Block.fence, BlockWoodFence.class, true
			));
		
		iterator.run();
		
	}
	
}
