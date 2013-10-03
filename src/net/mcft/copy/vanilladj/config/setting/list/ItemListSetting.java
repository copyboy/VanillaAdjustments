package net.mcft.copy.vanilladj.config.setting.list;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.mcft.copy.vanilladj.config.Configuration;
import net.mcft.copy.vanilladj.misc.Constants;
import net.mcft.copy.vanilladj.misc.Utils;
import net.minecraft.item.ItemStack;

public class ItemListSetting extends ListSetting<ItemStack> {
	
	public ItemListSetting(Configuration config, String fullName, boolean multiline, ItemStack... defaults) {
		super(config, fullName, multiline, new ElementHandler(), defaults);
	}
	public ItemListSetting(Configuration config, String fullName, boolean multiline, Object... defaults) {
		this(config, fullName, multiline, Utils.makeStacks(defaults));
	}
	
	public static class ElementHandler implements IElementHandler<ItemStack> {
		
		private static final Pattern pattern = Pattern.compile("^([0-9]+)(?::([0-9]+))?$");
		
		@Override
		public ItemStack read(String str) throws Exception {
			Matcher matcher = pattern.matcher(str);
			if (!matcher.matches())
				throw new Exception("Invalid item syntax for '" + str + "'.");
			
			int id = Integer.parseInt(matcher.group(1));
			int damage = ((matcher.group(2) != null) ? Integer.parseInt(matcher.group(2)) : Constants.anyDamage);
			return new ItemStack(id, 1, damage);
		}
		@Override
		public String write(ItemStack value) {
			String str = Integer.toString(value.itemID);
			if (value.getItemDamage() != Constants.anyDamage)
				str += ":" + value.getItemDamage();
			return str;
		}
		
	}
	
}
