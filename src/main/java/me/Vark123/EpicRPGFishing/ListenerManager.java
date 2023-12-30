package me.Vark123.EpicRPGFishing;

import org.bukkit.Bukkit;

import me.Vark123.EpicRPGFishing.KhorinisFishing.Listeners.FishingHookListener;
import me.Vark123.EpicRPGFishing.KhorinisFishing.Listeners.FishingRodUseListener;
import me.Vark123.EpicRPGFishing.Listeners.FishingRodClickListener;
import me.Vark123.EpicRPGFishing.QTESystem.Listeners.QTEInvClickListener;
import me.Vark123.EpicRPGFishing.QTESystem.Listeners.QTEInvCloseListener;
import me.Vark123.EpicRPGFishing.Tanalorr.Listeners.TanalorrFishingRodUseListener;
import me.Vark123.EpicRPGFishing.Tanalorr.Listeners.TanalorrHookListener;

public class ListenerManager {

	private ListenerManager() { }
	
	public static void registerListeners() {
		Main inst = Main.getInst();
		
		Bukkit.getPluginManager().registerEvents(new FishingRodClickListener(), inst);
		Bukkit.getPluginManager().registerEvents(new FishingRodUseListener(), inst);
		Bukkit.getPluginManager().registerEvents(new FishingHookListener(), inst);
		Bukkit.getPluginManager().registerEvents(new QTEInvClickListener(), inst);
		Bukkit.getPluginManager().registerEvents(new QTEInvCloseListener(), inst);

		Bukkit.getPluginManager().registerEvents(new TanalorrFishingRodUseListener(), inst);
		Bukkit.getPluginManager().registerEvents(new TanalorrHookListener(), inst);
	}
	
}
