package me.Vark123.EpicRPGFishing.KhorinisFishing;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.TreeMap;

import org.apache.commons.lang3.mutable.MutableInt;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import lombok.Getter;
import me.Vark123.EpicRPGFishing.FileManager;

@Getter
public final class KhorinisConfig {

	private static final KhorinisConfig conf = new KhorinisConfig();
	
	private boolean init;
	
	private final Collection<String> allowedWorlds;
	private final Collection<String> disabledRegions;
	private final TreeMap<Integer, String> fishChances;
	
	private final int multiplier;
	
	private final Random rand = new Random();
	
	private KhorinisConfig() {
		allowedWorlds = new HashSet<>();
		disabledRegions = new HashSet<>();
		fishChances = new TreeMap<>();
		multiplier = 10;
	}
	
	public static final KhorinisConfig get() {
		return conf;
	}
	
	public void load() {
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(FileManager.getKhorinisConfig());
		
		this.allowedWorlds.addAll(fYml.getStringList("worlds"));
		this.disabledRegions.addAll(fYml.getStringList("blocked_regions"));
		
		MutableInt chanceController = new MutableInt();
		ConfigurationSection fish = fYml.getConfigurationSection("fish");
		fish.getKeys(false).stream().forEachOrdered(key -> {
			String mmId = fish.getString(key+".mm_id");
			int chance = fish.getInt(key+".chance");
			fishChances.put(chanceController.addAndGet(chance), mmId);
		});
	}
	
}
