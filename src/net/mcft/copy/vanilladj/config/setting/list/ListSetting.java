package net.mcft.copy.vanilladj.config.setting.list;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.mcft.copy.vanilladj.VanillaAdjustments;
import net.mcft.copy.vanilladj.config.Configuration;
import net.mcft.copy.vanilladj.config.setting.Setting;

public abstract class ListSetting<T> extends Setting<List<T>> {
	
	public final IElementHandler<T> handler;
	public boolean multiline;
	
	public ListSetting(Configuration config, String fullName, boolean multiline,
	                   IElementHandler<T> handler, T... defaults) {
		super(config, fullName, Arrays.asList(defaults));
		this.handler = handler;
		this.multiline = multiline;
	}
	
	public String getDelimiter() { return ","; }
	
	@Override
	public void read(BufferedReader reader, String value) throws IOException {
		multiline = value.equals("[");
		if (multiline) {
			List<String> elements = new ArrayList<String>();
			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.equals("]")) break;
				String[] split = line.split(getDelimiter(), -1);
				for (String s : split)
					elements.add(s.trim());
			}
			readElements(elements.toArray(new String[0]));
		} else if (!value.isEmpty()) {
			String[] elements = value.split(getDelimiter());
			for (int i = 0; i < elements.length; i++)
				elements[i] = elements[i].trim();
			readElements(elements);
		} else readElements();
	}
	protected void readElements(String... elements) {
		value = new ArrayList<T>();
		for (String element : elements) {
			try { value.add(handler.read(element)); }
			catch (Exception e) {
				VanillaAdjustments.log.warning("Configuration: Couldn't parse element '" + element + "' of " + fullName + ":");
				VanillaAdjustments.log.warning("               " + e);
				value = defaultValue;
				return;
			}
		}
	}
	
	@Override
	public void writeValue(BufferedWriter writer, String padding) throws IOException {
		if (multiline) writer.write("[\n");
		for (int i = 0; i < value.size(); i++) {
			T v = value.get(i);
			if (multiline) writer.write(padding + "  ");
			writer.write(handler.write(v));
			if (multiline) writer.write("\n");
			else if (i < value.size() - 1)
				writer.write(getDelimiter() + " ");
		}
		if (multiline) writer.write(padding + "]");
		writer.write("\n");
	}
	
	@Override
	public boolean parse(String str) { return false; }
	
}
