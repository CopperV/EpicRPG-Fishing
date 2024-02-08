package me.Vark123.EpicRPGFishing;

import java.io.File;

import lombok.Getter;
import me.Vark123.EpicRPGFishing.KhorinisFishing.KhorinisConfig;
import me.Vark123.EpicRPGFishing.Tanalorr.TanalorrConfig;

public class FileManager {

	@Getter
	private static File khorinisConfig = new File(Main.getInst().getDataFolder(), "config-khorinis.yml");
	@Getter
	private static File tanalorrConfig = new File(Main.getInst().getDataFolder(), "config-tanalorr.yml");
	
	private FileManager() { }
	
	public static void init() {
		if(Main.getInst().getDataFolder().exists())
			Main.getInst().getDataFolder().mkdir();
		
		Main.getInst().saveResource("config-khorinis.yml", false);
		Main.getInst().saveResource("config-tanalorr.yml", false);
		
		KhorinisConfig.get().load();
		TanalorrConfig.get().load();
	}
	
}
