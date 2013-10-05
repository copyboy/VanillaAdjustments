package net.mcft.copy.vanilladj.config.setting.list;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.mcft.copy.vanilladj.config.Configuration;
import net.mcft.copy.vanilladj.misc.Utils;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RecipeReplaceListSetting extends ListSetting<RecipeReplaceListSetting.Entry> {
	
	private static final Object[] replaceCobblestoneWithStoneIn = {
		Item.swordStone, Item.shovelStone, Item.pickaxeStone, Item.axeStone, Item.hoeStone,
		Block.dispenser, Block.dropper, Block.pistonBase, Block.lever, Item.brewingStand
	};
	
	@Override
	public String getDelimiter() { return ";"; }
	
	public RecipeReplaceListSetting(Configuration config, String fullName) {
		super(config, fullName, true, new ElementHandler(),
				new Entry(Block.cobblestone, Block.stone, replaceCobblestoneWithStoneIn));
	}
	
	public static class ElementHandler implements IElementHandler<Entry> {
		
		private static final Pattern pattern = Pattern.compile(String.format("^(%1$s) with (%1$s) in (%1$s(?:, ?%1$s)*)$", "[\\w\\.:]+"));
		private static final IElementHandler<ItemStack> base = new ItemListSetting.ElementHandler();
		
		@Override
		public Entry read(String str) throws Exception {
			Matcher matcher = pattern.matcher(str);
			if (!matcher.matches())
				throw new Exception("Invalid replace syntax.");
			
			ItemStack replace = base.read(matcher.group(1));
			ItemStack with = base.read(matcher.group(2));
			String[] replaceInStr = matcher.group(3).split(", ?");
			ItemStack[] replaceIn = new ItemStack[replaceInStr.length];
			for (int i = 0; i < replaceIn.length; i++)
				replaceIn[i] = base.read(replaceInStr[i]);
			
			return new Entry(replace, with, replaceIn);
		}
		@Override
		public String write(Entry value) {
			StringBuilder sb = new StringBuilder();
			sb.append(base.write(value.replace));
			sb.append(" with ");
			sb.append(base.write(value.with));
			sb.append(" in ");
			for (int i = 0; i < value.replaceIn.length; i++) {
				sb.append(base.write(value.replaceIn[i]));
				if (i < value.replaceIn.length - 1)
					sb.append(", ");
			}
			return sb.toString();
		}
		
	}
	
	public static class Entry {
		
		public final ItemStack replace, with;
		public final ItemStack[] replaceIn;
		
		public Entry(ItemStack replace, ItemStack with, ItemStack... replaceIn) {
			this.replace = replace;
			this.with = with;
			this.replaceIn = replaceIn;
		}
		public Entry(Object replace, Object with, Object... replaceIn) {
			this(Utils.makeStack(replace), Utils.makeStack(with), Utils.makeStacks(replaceIn));
		}
		
	}
	
}
