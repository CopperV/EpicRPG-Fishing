package me.Vark123.EpicRPGFishing;

import org.bukkit.Bukkit;

import me.Vark123.EpicRPGFishing.KhorinisFishing.Listeners.FishingHookListener;
import me.Vark123.EpicRPGFishing.KhorinisFishing.Listeners.FishingRodUseListener;
import me.Vark123.EpicRPGFishing.Listeners.FishingRodClickListener;

public class ListenerManager {

	private ListenerManager() { }
	
	public static void registerListeners() {
		Main inst = Main.getInst();
		
		Bukkit.getPluginManager().registerEvents(new FishingRodClickListener(), inst);
		Bukkit.getPluginManager().registerEvents(new FishingRodUseListener(), inst);
		Bukkit.getPluginManager().registerEvents(new FishingHookListener(), inst);
	}
	
}
