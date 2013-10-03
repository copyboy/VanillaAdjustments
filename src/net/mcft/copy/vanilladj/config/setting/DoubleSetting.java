package net.mcft.copy.vanilladj.config.setting;

import net.mcft.copy.vanilladj.config.Configuration;
import net.mcft.copy.vanilladj.config.setting.validation.ISettingValidator;
import net.mcft.copy.vanilladj.config.setting.validation.SettingValidatorRange;

public class DoubleSetting extends Setting<Double> {
	
	public DoubleSetting(Configuration config, String fullName, double defaultValue) {
		super(config, fullName, defaultValue);
	}
	
	public DoubleSetting range(double minValue, double maxValue) {
		addValidator(new SettingValidatorRange(minValue, maxValue));
		return this;
	}
	
	@Override
	public boolean parse(String str) {
		double val;
		try { val = Double.parseDouble(str); }
		catch (Exception e) { return false; }
		for (ISettingValidator validator : validators)
			if (!validator.isValid(val))
				return false;
		value = val;
		return true;
	}
	
}
