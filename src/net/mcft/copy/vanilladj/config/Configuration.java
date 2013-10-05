package net.mcft.copy.vanilladj.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.mcft.copy.vanilladj.VanillaAdjustments;
import net.mcft.copy.vanilladj.config.setting.BooleanSetting;
import net.mcft.copy.vanilladj.config.setting.DoubleSetting;
import net.mcft.copy.vanilladj.config.setting.IntegerSetting;
import net.mcft.copy.vanilladj.config.setting.Setting;
import net.mcft.copy.vanilladj.config.setting.TimeSetting;
import net.mcft.copy.vanilladj.config.setting.list.EntityDropListSetting;
import net.mcft.copy.vanilladj.config.setting.list.ItemListSetting;
import net.mcft.copy.vanilladj.config.setting.list.RecipeReplaceListSetting;
import net.mcft.copy.vanilladj.misc.Utils;
import net.minecraft.block.Block;

public class Configuration {
	
	public final File file;
	
	public Map<String, Setting> settings = new HashMap<String, Setting>();
	
	public List<Object> fileStructure = new ArrayList<Object>();
	
	public Configuration(File file) {
		
		this.file = file;
		
		comment("Vanilla Adjustments Configuration File");
		newLine();
		comment("Note: Changes to this file will be overriden,");
		comment("      except for changes to the actual settings.");
		newLine();
		
		new TimeSetting(this, "playerDeathItemLifespan", 30 * 60)
				.setCommentDefault("Changes the time items will stay when dropped by a player who died.");
		
		healSettings();
		adjustmentSettings();
		recipesSettings();
		generationSettings();
		
	}
	
	// Settings
	
	private void healSettings() {
		
		new BooleanSetting(this, "regeneration.enabled", false)
				.setCommentDefault("Controls if the advanced healing mechanics are enabled.");
		new TimeSetting(this, "regeneration.timeBetweenHeal", 30).range(0, Integer.MAX_VALUE)
				.setCommentDefault("The base time between healing half a heart.");
		
		new IntegerSetting(this, "regeneration.hunger.minimum", 15).range(0, 20)
				.setCommentDefault("Natural regeration effect starts at this hunger level. Valid: 0 to 20.");
		new IntegerSetting(this, "regeneration.hunger.maximum", 18).range(0, 20)
				.setCommentDefault("Natural regeration is at maximum effectiveness at this hunger level. Valid: 0 to 20.");
		new DoubleSetting(this, "regeneration.hunger.exhaustion", 3.0)
				.setCommentDefault("The amount of exhaustion added when healing naturally.");
		
		new TimeSetting(this, "regeneration.hurtPenalty.amount", 0)
				.setCommentDefault("When hurt, increases the time it takes to heal by this amount.");
		new TimeSetting(this, "regeneration.hurtPenalty.timeout", 10)
				.setCommentDefault("Time of immunity from the hurt time penalty after it being applied.");
		new TimeSetting(this, "regeneration.hurtPenalty.maximum", 10)
				.setCommentDefault("Maximum hurt time penalty at a time.");
		new TimeSetting(this, "regeneration.hurtPenalty.maximumPerHeal", 20)
				.setCommentDefault("Maximum hurt time penalty per heal cycle.");
		
		new DoubleSetting(this, "regeneration.bonus.minimumFactor", 1.0)
				.setCommentDefault("Without the bonus, apply this factor to the base heal time.");
		new DoubleSetting(this, "regeneration.bonus.maximumFactor", 1.0)
				.setCommentDefault("Without the full bonus, apply this factor to the base heal time.");
		new TimeSetting(this, "regeneration.bonus.startTime", 60)
				.setCommentDefault("After this time, the healing factor will start to increase.");
		new TimeSetting(this, "regeneration.bonus.stopTime", 5 * 60)
				.setCommentDefault("After this time, the healing factor will be at its maximum.");
		new TimeSetting(this, "regeneration.bonus.maximumTime", 6 * 60)
				.setCommentDefault("The maximum accumulated bonus time.");
		new TimeSetting(this, "regeneration.bonus.hurtPenalty", 3 * 60)
				.setCommentDefault("When hurt, decreases the bonus time by this amount, and with that the factor.");
		new TimeSetting(this, "regeneration.bonus.hurtPenaltyTimeout", 15)
				.setCommentDefault("Time of immunity from the bonus hurt penalty after it being applied.");
		
	}
	
	private void recipesSettings() {
		
		new ItemListSetting(this, "recipes.disable", false)
				.setComment("Removes the recipes of these items from the game.");
		
		new ItemListSetting(this, "recipes.reverse", false,
		                    Block.woodSingleSlab, Block.stoneSingleSlab, Block.stairsWoodOak,
		                    Block.stairsWoodSpruce, Block.stairsWoodBirch, Block.stairsWoodJungle,
		                    Block.stairsSandStone, Block.stairsCobblestone, Block.stairsBrick,
		                    Block.stairsStoneBrick, Block.stairsNetherBrick, Block.stairsNetherQuartz)
				.setComment("Adds reverse recipes for these items.");
		
		new RecipeReplaceListSetting(this, "recipes.replace")
				.setComment("Replaces one thing with another in recipes.");
		
	}
	
	private void adjustmentSettings() {
		
		new EntityDropListSetting(this, "adjustment.mobDrops")
				.setComment("Adds to (+) or replaces (=) a mob's drops. P = only when player killed it.");
		
	}
	
	private void generationSettings() {
		
	}
	
	// Comments etc.
	
	private void comment(String comment) { fileStructure.add("# " + comment); }
	private void newLine() { fileStructure.add(""); }
	
	// Getting setting values
	
	public Object getValue(String setting) {
		return settings.get(setting.toLowerCase(Locale.ENGLISH)).value;
	}
	public boolean getBoolean(String setting) {
		return (Boolean)getValue(setting);
	}
	public int getInteger(String setting) {
		return (Integer)getValue(setting);
	}
	public double getDouble(String setting) {
		return (Double)getValue(setting);
	}
	public int getTicks(String setting) {
		return getInteger(setting) * 20;
	}
	
	// Loading / saving
	
	public void load() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			List<String> currentCategory = new ArrayList<String>();
			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.isEmpty() || line.startsWith("#")) continue;
				try {
					if (line.contains("=")) {
						String[] split = line.split("=", -1);
						String name = split[0].trim();
						String value = split[1].trim();
						if (currentCategory.size() > 0)
							name = Utils.join(currentCategory, ".") + "." + name;
						Setting setting = settings.get(name.toLowerCase(Locale.ENGLISH));
						if (setting != null) setting.read(reader, value);
						else VanillaAdjustments.log.warning("Configuration: Unknown setting '" + name + "'.");
					} else if (line.endsWith("{")) {
						String category = line.substring(0, line.length() - 1).trim().toLowerCase(Locale.ENGLISH);
						currentCategory.add(category);
					} else if (line.equals("}")) {
						if (currentCategory.size() > 0)
							currentCategory.remove(currentCategory.size() - 1);
						else VanillaAdjustments.log.warning("Configuration: Closing category when there isn't one open.");
					} else throw new Exception();
				} catch (Exception e) {
					VanillaAdjustments.log.warning("Configuration: Could not parse line '" + line + "'.");
				}
			}
		} catch (FileNotFoundException e) {
			// Do nothing.
		} catch (Exception e) {
			VanillaAdjustments.log.warning("Could not load configuration file!");
			e.printStackTrace();
		} finally {
			if (reader != null)
				try { reader.close(); } catch (Exception e) {  }
		}
	}
	
	public void save() {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			
			List<String> currentCategory = new ArrayList<String>();
			
			for (Object obj : fileStructure) {
				if (obj instanceof String) {
					
					writer.write(Utils.repeat("  ", currentCategory.size()));
					writer.write((String)obj + "\n");
					
				} else if (obj instanceof Setting) {
					Setting setting = (Setting)obj;
					
					boolean closed = false;
					for (int i = 0; i < currentCategory.size(); i++)
						if ((setting.category.length <= i) ||
						    !(setting.category[i].equalsIgnoreCase(currentCategory.get(i)))) {
							for (int j = currentCategory.size() - 1; j >= i; j--) {
								currentCategory.remove(j);
								if (j < 1) writer.write(Utils.repeat("  ", j + 1) + "\n");
								writer.write(Utils.repeat("  ", j) + "}\n");
							}
							writer.write(Utils.repeat("  ", i) + "\n");
							closed = true;
							break;
						}
					
					if (setting.category.length > currentCategory.size()) {
						if (!closed)
							writer.write(Utils.repeat("  ", currentCategory.size()) + "\n");
						for (int i = currentCategory.size(); i < setting.category.length; i++) {
							currentCategory.add(setting.category[i]);
							writer.write(Utils.repeat("  ", i));
							writer.write(setting.category[i].toUpperCase(Locale.ENGLISH) + " {\n");
							if (i < 1) writer.write("  \n");
						}
					}
					
					setting.write(writer, Utils.repeat("  ", currentCategory.size()));
				}
			}
			
			for (int i = currentCategory.size() - 1; i >= 0; i--) {
				if (i < 1) writer.write("  \n");
				writer.write(Utils.repeat("  ", i) + "}\n");
			}
			
		} catch (Exception e) {
			VanillaAdjustments.log.warning("Could not save configuration file!");
			e.printStackTrace();
		} finally {
			if (writer != null)
				try { writer.close(); } catch (Exception e) {  }
		}
		
	}
	
}
