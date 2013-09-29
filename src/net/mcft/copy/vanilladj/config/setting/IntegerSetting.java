package net.mcft.copy.vanilladj.config.setting;

import net.mcft.copy.vanilladj.config.Configuration;
import net.mcft.copy.vanilladj.config.setting.validation.ISettingValidator;
import net.mcft.copy.vanilladj.config.setting.validation.SettingValidatorRange;

public class IntegerSetting extends Setting<Integer> {
	
	public IntegerSetting(Configuration config, String namespace, int defaultValue) {
		super(config, namespace, defaultValue);
	}
	
	@Override
	public IntegerSetting setComment(String comment) {
		super.setComment(comment);
		return this;
	}
	@Override
	public IntegerSetting setCommentDefault(String comment) {
		super.setCommentDefault(comment);
		return this;
	}
	
	public IntegerSetting range(int minValue, int maxValue) {
		addValidator(new SettingValidatorRange(minValue, maxValue));
		return this;
	}
	
	@Override
	public boolean parse(String str) {
		int val;
		try { val = Integer.parseInt(str); }
		catch (Exception e) { return false; }
		for (ISettingValidator validator : validators)
			if (!validator.isValid(val))
				return false;
		value = val;
		return true;
	}
	
}
