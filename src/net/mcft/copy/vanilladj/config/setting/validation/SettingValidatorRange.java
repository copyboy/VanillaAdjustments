package net.mcft.copy.vanilladj.config.setting.validation;

public class SettingValidatorRange implements ISettingValidator {
	
	public final double minValue;
	public final double maxValue;
	
	public SettingValidatorRange(double minValue, double maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	
	@Override
	public boolean isValid(Object value) {
		double doubleValue;
		if (value instanceof Integer) doubleValue = (Integer)value;
		else if (value instanceof Double) doubleValue = (Double)value;
		else throw new Error("value is not a Number");
		return ((doubleValue >= minValue) && (doubleValue <= maxValue));
	}
	
}
