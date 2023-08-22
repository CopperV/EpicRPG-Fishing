package me.Vark123.EpicRPGFishing;

import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;

@Getter
public class Main extends JavaPlugin {

	@Getter
	private static Main inst;
	
	private String prefix;

	@Override
	public void onEnable() {
		inst = this;
		
		prefix = "§x§1§0§7§9§d§a§lE§x§3§2§8§f§e§0§lp§x§5§3§a§6§e§7§li§x§7§5§b§c§e§d§lc§x§9§7§d§2§f§3§lR§x§b§8§e§9§f§a§lP§x§c§1§e§5§f§8§lG §x§b§0§c§7§e§d§lR§x§9§f§a§9§e§3§ly§x§8§e§8§b§d§8§lb§x§7§e§6§d§c§e§la§x§6§d§4§f§c§3§lk";
	
		FileManager.init();
		CommandManager.setExecutors();
		ListenerManager.registerListeners();
	}

	@Override
	public void onDisable() {
		
	}
	
	
	
}
