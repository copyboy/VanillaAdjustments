package net.mcft.copy.vanilladj;

import java.io.File;

import net.minecraftforge.common.Configuration;

public final class Config {
	
	private static final String categoryDifficulty = "difficulty";
	
	public static int healTime = 0;
	
	private Config() {  }
	
	public static void load(File file) {
		
		Configuration config = new Configuration(file);
		config.load();
		
		healTime = config.get(categoryDifficulty, "healTime", healTime,
		                      "Only works with naturalRegeneration off. Time in seconds between 1/2 heart natural healing at full hunger. 0 is disabled.").getInt();
		
		config.save();
		
	}
	
}
