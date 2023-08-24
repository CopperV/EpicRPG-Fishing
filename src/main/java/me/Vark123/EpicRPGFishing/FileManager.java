package me.Vark123.EpicRPGFishing;

import java.io.File;

import lombok.Getter;
import me.Vark123.EpicRPGFishing.KhorinisFishing.Config;

public class FileManager {

	@Getter
	private static File khorinisConfig = new File(Main.getInst().getDataFolder(), "config-khorinis.yml");
	
	private FileManager() { }
	
	public static void init() {
		if(Main.getInst().getDataFolder().exists())
			Main.getInst().getDataFolder().mkdir();
		
		Main.getInst().saveResource("config-khorinis.yml", false);
		
		Config.get().load();
	}
	
}
