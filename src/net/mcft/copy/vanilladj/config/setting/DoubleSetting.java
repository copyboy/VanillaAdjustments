package net.mcft.copy.vanilladj.config.setting;

import net.mcft.copy.vanilladj.config.Configuration;
import net.mcft.copy.vanilladj.config.setting.validation.ISettingValidator;
import net.mcft.copy.vanilladj.config.setting.validation.SettingValidatorRange;

public class DoubleSetting extends Setting<Double> {
	
	public DoubleSetting(Configuration config, String namespace, double defaultValue) {
		super(config, namespace, defaultValue);
	}
	
	@Override
	public DoubleSetting setComment(String comment) {
		super.setComment(comment);
		return this;
	}
	@Override
	public DoubleSetting setCommentDefault(String comment) {
		super.setCommentDefault(comment);
		return this;
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
