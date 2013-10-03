package net.mcft.copy.vanilladj.config.setting;

import net.mcft.copy.vanilladj.config.Configuration;
import net.mcft.copy.vanilladj.config.setting.validation.ISettingValidator;


public class BooleanSetting extends Setting<Boolean> {
	
	public BooleanSetting(Configuration config, String fullName, boolean defaultValue) {
		super(config, fullName, defaultValue);
	}
	
	@Override
	public boolean parse(String str) {
		boolean val = str.equalsIgnoreCase("true");
		if (!val && !str.equalsIgnoreCase("false")) return false;
		for (ISettingValidator validator : validators)
			if (!validator.isValid(val))
				return false;
		value = val;
		return true;
	}
	
}
