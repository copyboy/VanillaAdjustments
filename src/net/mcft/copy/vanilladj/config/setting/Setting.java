package net.mcft.copy.vanilladj.config.setting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.mcft.copy.vanilladj.VanillaAdjustments;
import net.mcft.copy.vanilladj.config.Configuration;
import net.mcft.copy.vanilladj.config.setting.validation.ISettingValidator;

public abstract class Setting<T> {
	
	private String comment = null;
	
	public final Configuration config;
	public final String fullName;
	public final String[] category;
	public final String name;
	public final T defaultValue;
	
	public T value;
	public List<ISettingValidator> validators = new ArrayList<ISettingValidator>();
	
	public Setting setComment(String comment) { this.comment = comment; return this; }
	public Setting setCommentDefault(String comment) { return setComment(comment + " Default: %DEFAULT%."); }
	public boolean hasComment() { return (comment != null); }
	public String getComment() { return (hasComment() ? comment.replace("%DEFAULT%", defaultString()) : null); }
	
	public Setting(Configuration config, String fullName, T defaultValue) {
		
		this.config = config;
		this.fullName = fullName;
		
		String[] split = fullName.split("\\.");
		category = Arrays.copyOfRange(split, 0, split.length - 1);
		name = split[split.length - 1];
		
		this.defaultValue = defaultValue;
		value = defaultValue;
		
		config.settings.put(fullName, this);
		config.fileStructure.add(this);
		
	}
	
	public Setting addValidator(ISettingValidator validator) {
		validators.add(validator);
		return this;
	}
	
	public String valueString() {
		return value.toString();
	}
	public String defaultString() {
		return defaultValue.toString();
	}
	
	public void read(BufferedReader reader, String value) throws IOException {
		if (!parse(value))
			VanillaAdjustments.log.warning("Configuration: Couldn't parse '" + value + "' for " + fullName + ".");
	}
	
	public void write(BufferedWriter writer, String padding) throws IOException {
		if (hasComment())
			writer.write(padding + "# " + getComment() + "\n");
		writer.write(padding + name + " = ");
		writeValue(writer, padding);
	}
	public void writeValue(BufferedWriter writer, String padding) throws IOException {
		writer.write(valueString() + "\n");
	}
	
	public abstract boolean parse(String str);
	
}
