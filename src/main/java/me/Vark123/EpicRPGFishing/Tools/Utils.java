package me.Vark123.EpicRPGFishing.Tools;

import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPGFishing.Main;
import net.minecraft.network.protocol.game.PacketPlayOutAnimation;

public final class Utils {

	private Utils() { }
	
	public static void stopFishing(FishHook hook) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Player p = (Player) hook.getShooter();
				hook.remove();
				
				if(p == null || !p.isOnline())
					return;
				PacketPlayOutAnimation packet = new PacketPlayOutAnimation(((CraftPlayer)p).getHandle(), 0);
				((CraftPlayer)p).getHandle().b.a(packet);
				p.playSound(p.getLocation(), Sound.ENTITY_FISHING_BOBBER_RETRIEVE, 1, 0.5f);
			}
		}.runTask(Main.getInst());
	}
	
}
