package net.mcft.copy.vanilladj.config.setting.validation;

import java.util.HashSet;
import java.util.Set;

public class SettingValidatorChoice implements ISettingValidator {
	
	public Set<Object> validValues = new HashSet<Object>();
	
	public SettingValidatorChoice(Object... validValues) {
		for (Object validValue : validValues)
			this.validValues.add(validValue);
	}
	
	@Override
	public boolean isValid(Object value) {
		return (validValues.contains(value));
	}
	
}
