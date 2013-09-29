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
import java.util.Map;

import net.mcft.copy.vanilladj.VanillaAdjustments;
import net.mcft.copy.vanilladj.config.setting.BooleanSetting;
import net.mcft.copy.vanilladj.config.setting.DoubleSetting;
import net.mcft.copy.vanilladj.config.setting.IntegerSetting;
import net.mcft.copy.vanilladj.config.setting.Setting;
import net.mcft.copy.vanilladj.config.setting.TimeSetting;
import net.mcft.copy.vanilladj.misc.Utils;

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
		
		new TimeSetting(this, "playerDeathItemLifespan", 30 * 60).
				setCommentDefault("Changes the time items will stay when dropped by a player who died.");
		
		healSettings();
		
	}
	
	private void healSettings() {
		
		new BooleanSetting(this, "regeneration.enabled", false).
				setCommentDefault("Controls if the advanced healing mechanics are enabled.");
		new TimeSetting(this, "regeneration.timeBetweenHeal", 30).range(0, Integer.MAX_VALUE).
				setCommentDefault("The base time between healing half a heart.");
		
		newLine();
		
		new IntegerSetting(this, "regeneration.hungerMinimum", 15).range(0, 20).
				setCommentDefault("Natural regeration effect starts at this hunger level. Valid: 0 to 20.");
		new IntegerSetting(this, "regeneration.hungerMaximum", 18).range(0, 20).
				setCommentDefault("Natural regeration is at maximum effectiveness at this hunger level. Valid: 0 to 20.");
		new DoubleSetting(this, "regeneration.exhaustion", 3.0).
				setCommentDefault("The amount of exhaustion added when healing naturally.");
		
		newLine();
		
		new TimeSetting(this, "regeneration.hurtTimePenalty", 0).
				setCommentDefault("When hurt, increases the time it takes to heal by this amount.");
		new TimeSetting(this, "regeneration.hurtTimePenaltyTimeout", 10).
				setCommentDefault("Time of immunity from the hurt time penalty after it being applied.");
		new TimeSetting(this, "regeneration.hurtTimePenaltyMaximum", 10).
				setCommentDefault("Maximum hurt time penalty at a time.");
		new TimeSetting(this, "regeneration.hurtTimePenaltyMaximumPerHeal", 20).
				setCommentDefault("Maximum hurt time penalty per heal cycle.");
		
		newLine();
		
		new DoubleSetting(this, "regeneration.bonusMinimumFactor", 1.0).
				setCommentDefault("Without the bonus, apply this factor to the base heal time.");
		new DoubleSetting(this, "regeneration.bonusMaximumFactor", 1.0).
				setCommentDefault("Without the full bonus, apply this factor to the base heal time.");
		new TimeSetting(this, "regeneration.bonusStartTime", 60).
				setCommentDefault("After this time, the healing factor will start to increase.");
		new TimeSetting(this, "regeneration.bonusStopTime", 120).
				setCommentDefault("After this time, the healing factor will be at its maximum.");
		new TimeSetting(this, "regeneration.bonusMaximumTime", 180).
				setCommentDefault("The maximum accumulated bonus time.");
		new TimeSetting(this, "regeneration.bonusHurtPenalty", 120).
				setCommentDefault("When hurt, decreases the bonus time by this amount, and with that the factor.");
		new TimeSetting(this, "regeneration.bonusHurtPenaltyTimeout", 10).
				setCommentDefault("Time of immunity from the bonus hurt penalty after it being applied.");
		
	}
	
	private void comment(String comment) { fileStructure.add("# " + comment); }
	private void newLine() { fileStructure.add(""); }
	
	public Object getValue(String setting) {
		return settings.get(setting).value;
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
	
	public void load() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			List<String> currentCategory = new ArrayList<String>();
			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.isEmpty() || line.startsWith("#")) continue;
				if (line.contains("=")) {
					String[] split = line.split("=");
					String name = split[0].trim();
					String value = split[1].trim();
					if (currentCategory.size() > 0)
						name = Utils.join(currentCategory, ".") + "." + name;
					Setting setting = settings.get(name);
					if (setting != null) {
						if (!setting.parse(value))
							VanillaAdjustments.log.warning("Configuration: Couldn't parse '" + value + "' for " + name + ".");
					} else VanillaAdjustments.log.warning("Configuration: Unknown setting '" + name + "'.");
				} else if (line.endsWith("{")) {
					String category = line.substring(0, line.length() - 1).trim().toLowerCase();
					currentCategory.add(category);
				} else if (line.equals("}")) {
					if (currentCategory.size() > 0)
						currentCategory.remove(currentCategory.size() - 1);
					else VanillaAdjustments.log.warning("Configuration: Closing category when there isn't one open.");
				} else VanillaAdjustments.log.warning("Configuration: Could not parse line '" + line + "'.");
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
								writer.write(Utils.repeat("  ", j + i) + "\n");
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
							writer.write(setting.category[i].toUpperCase() + " {\n");
							writer.write(Utils.repeat("  ", i) + "\n");
						}
					}
					
					if (setting.hasComment()) {
						writer.write(Utils.repeat("  ", currentCategory.size()));
						writer.write("# " + setting.getComment() + "\n");
					}
					writer.write(Utils.repeat("  ", currentCategory.size()));
					writer.write(setting.name + " = " + setting.valueString() + "\n");
					
				}
			}
			
			for (int i = currentCategory.size() - 1; i >= 0; i--) {
				writer.write(Utils.repeat("  ", i + 1) + "\n");
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
