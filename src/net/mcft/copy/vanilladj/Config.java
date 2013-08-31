package net.mcft.copy.vanilladj;

import java.io.File;

import net.minecraftforge.common.Configuration;

public final class Config {
	
	private static final String categoryDifficulty = "difficulty";
	
	public static int healTime = 0;
	public static int playerDeathItemLifespan = 30 * 60;
	
	private Config() {  }
	
	public static void load(File file) {
		
		Configuration config = new Configuration(file);
		config.load();
		
		healTime = config.get(categoryDifficulty, "healTime", healTime,
		                      "Only works with naturalRegeneration off. Time in seconds between 1/2 heart natural healing at full hunger. 0 is disabled.").getInt();
		
		playerDeathItemLifespan = config.get(categoryDifficulty, "playerDeathItemLifespan", playerDeathItemLifespan,
		                                     "Time in seconds which items dropped from player deaths will stay in the world. (Default: 30 minutes, Vanilla: 5 minutes)").getInt();
		
		config.save();
		
	}
	
}
