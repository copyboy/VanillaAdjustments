package net.mcft.copy.vanilladj.config.setting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.mcft.copy.vanilladj.config.Configuration;

public class TimeSetting extends IntegerSetting {
	
	public TimeSetting(Configuration config, String fullName, Integer defaultValue) {
		super(config, fullName, defaultValue);
	}
	
	@Override
	public TimeSetting range(int minValue, int maxValue) {
		super.range(minValue, maxValue);
		return this;
	}
	
	@Override
	public String valueString() { return toTimeString(value); }
	@Override
	public String defaultString() { return toTimeString(defaultValue); }
	
	@Override
	public boolean parse(String str) {
		Pattern pattern = Pattern.compile("^(\\d+h)?(\\d+m)?(\\d+s)?$");
		Matcher matcher = pattern.matcher(str);
		if (!matcher.matches() || (matcher.groupCount() < 1)) return false;
		int val = 0;
		for (int i = 1; i <= matcher.groupCount(); i++) {
			String s = matcher.group(i);
			if ((s == null) || s.isEmpty()) continue;
			int v = Integer.parseInt(s.substring(0, s.length() - 1));
			switch (s.charAt(s.length() - 1)) {
				case 'h': val += v * 3600; break;
				case 'm': val += v * 60; break;
				case 's': val += v; break;
			}
		}
		value = val;
		return true;
	}
	
	public static String toTimeString(int time) {
		int hours = time / 3600;
		int minutes = (time % 3600) / 60;
		int seconds = time % 60;
		String str = "";
		if (hours > 0) str += hours + "h";
		if (minutes > 0) str += minutes + "m";
		if ((seconds > 0) || str.isEmpty())
			str += seconds + "s";
		return str;
	}
	
}
