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
			String currentCategory = null;
			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.isEmpty() || line.startsWith("#")) continue;
				if (line.contains("=")) {
					String[] split = line.split("=");
					String name = split[0].trim();
					String value = split[1].trim();
					if (currentCategory != null)
						name = currentCategory + "." + name;
					Setting setting = settings.get(name);
					if (setting != null) {
						if (!setting.parse(value))
							VanillaAdjustments.log.warning("Configuration: Couldn't parse '" + value + "' for " + name + ".");
					} else VanillaAdjustments.log.warning("Configuration: Unknown setting '" + name + "'.");
				} else if (line.endsWith("{")) {
					String category = line.substring(0, line.length() - 1).trim().toLowerCase();
					if (currentCategory != null)
						VanillaAdjustments.log.warning("Configuration: Opening category when there's already one open.");
					currentCategory = category;
				} else if (line.equals("}")) {
					if (currentCategory != null) {
						currentCategory = null;
					} else VanillaAdjustments.log.warning("Configuration: Closing category when there isn't one open.");
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
			
			String category = null;
			
			for (Object obj : fileStructure) {
				if (obj instanceof String) {
					
					if (category != null) writer.write("  ");
					writer.write((String)obj + "\n");
					
				} else if (obj instanceof Setting) {
					Setting setting = (Setting)obj;
					
					if ((setting.category == null) ? (category == null)
					                               : !setting.category.equalsIgnoreCase(category)) {
						if (category != null) writer.write("}\n");
						writer.write("\n");
						category = setting.category;
						if (category != null) writer.write(category.toUpperCase() + " {\n");
					}
					
					if (setting.hasComment()) {
						if (category != null) writer.write("  ");
						writer.write("# " + setting.getComment() + "\n");
					}
					if (category != null) writer.write("  ");
					writer.write(setting.name + " = " + setting.valueString() + "\n");
					
				}
			}
			
			if (category != null) writer.write("}\n");
			
		} catch (Exception e) {
			VanillaAdjustments.log.warning("Could not save configuration file!");
			e.printStackTrace();
		} finally {
			if (writer != null)
				try { writer.close(); } catch (Exception e) {  }
		}
		
	}
	
}
