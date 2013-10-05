package net.mcft.copy.vanilladj.config.setting.list;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.mcft.copy.vanilladj.config.Configuration;
import net.mcft.copy.vanilladj.misc.Constants;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class EntityDropListSetting extends ListSetting<EntityDropListSetting.Entry> {
	
	private static final Entry[] defaults = new Entry[]{
		new Entry("Chicken", false, new ItemStack(Item.feather, 1, Constants.anyDamage), 1.0, 1, 2, true),
		new Entry("Cow", false, new ItemStack(Item.leather, 1, Constants.anyDamage), 1.0, 1, 2, true),
		new Entry("Pig", false, new ItemStack(Item.porkRaw, 1, Constants.anyDamage), 0.5, 1, 1, true)
	};
	
	@Override
	public String getDelimiter() { return ";"; }
	
	public EntityDropListSetting(Configuration config, String fullName) {
		super(config, fullName, true, new ElementHandler(), defaults);
	}
	
	public static class ElementHandler implements IElementHandler<Entry> {
		
		private static final Pattern pattern = Pattern.compile("^([\\w\\.*]+) ([=+]) (\\d+)(?:-(\\d+))?x ([\\w\\.:]+)(?: with (\\d+(?:\\.\\d+)?)%)?( P)?$");
		private static final IElementHandler<ItemStack> base = new ItemListSetting.ElementHandler();
		
		@Override
		public Entry read(String str) throws Exception {
			Matcher matcher = pattern.matcher(str);
			if (!matcher.matches())
				throw new Exception("Invalid entity drop syntax.");
			
			String entity = matcher.group(1);
			boolean empty = (matcher.group(2).equals("="));
			int minAmount = Integer.parseInt(matcher.group(3));
			int maxAmount = ((matcher.group(4) != null) ? Integer.parseInt(matcher.group(4)) : minAmount);
			ItemStack stack = base.read(matcher.group(5));
			double chance = ((matcher.group(6) != null) ? Double.parseDouble(matcher.group(6)) : 100) / 100;
			boolean player = (matcher.group(7) != null);
			
			return new Entry(entity, empty, stack, chance, minAmount, maxAmount, true);
		}
		@Override
		public String write(Entry value) {
			StringBuilder sb = new StringBuilder();
			sb.append(value.entity);
			sb.append(value.empty ? " = " : " + ");
			sb.append(value.minAmount);
			if (value.maxAmount != value.minAmount)
				sb.append("-" + value.maxAmount);
			sb.append("x ");
			sb.append(base.write(value.stack));
			if (value.chance < 1)
				sb.append(" with " + new DecimalFormat("#.##").format(value.chance * 100) + "%");
			if (value.player)
				sb.append(" P");
			return sb.toString();
		}
		
	}
	
	public static class Entry {
		
		public final String entity;
		public final boolean empty;
		public final ItemStack stack;
		public final double chance;
		public final int minAmount;
		public final int maxAmount;
		public final boolean player;
		
		public Entry(String entity, boolean empty, ItemStack stack,
		             double chance, int minAmount, int maxAmount, boolean player) {
			this.entity = entity;
			this.empty = empty;
			this.stack = stack;
			this.chance = chance;
			this.minAmount = minAmount;
			this.maxAmount = maxAmount;
			this.player = player;
		}
		
	}
	
}
