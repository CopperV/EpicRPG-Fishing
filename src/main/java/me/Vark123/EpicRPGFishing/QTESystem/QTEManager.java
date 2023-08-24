package me.Vark123.EpicRPGFishing.QTESystem;

import java.util.LinkedList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import de.tr7zw.nbtapi.NBTItem;
import me.Vark123.EpicRPGFishing.Main;
import me.Vark123.EpicRPGFishing.KhorinisFishing.FishingController;
import me.Vark123.EpicRPGFishing.KhorinisFishing.Events.KoloniaFishEvent;
import me.Vark123.EpicRPGFishing.Tools.Pair;
import me.Vark123.EpicRPGFishing.Tools.Utils;

public final class QTEManager {

	private static final QTEManager inst = new QTEManager();
	
	private QTEManager() {
		
	}
	
	public static final QTEManager get() {
		return inst;
	}
	
	public void startQTE(Player p, FishHook hook) {
		ItemStack it = p.getInventory().getItemInMainHand();
		if(it == null || !it.getType().equals(Material.FISHING_ROD)) {
			Utils.stopFishing(hook);
			return;
		}
		
		NBTItem nbt = new NBTItem(it);
		if(!nbt.hasTag("type") || !nbt.getString("type").equalsIgnoreCase("fish")) {
			Utils.stopFishing(hook);
			return;
		}
		
		int progress = 1;
		int complexity = Integer.parseInt(nbt.getString("complexity"));
		int difficulty = Integer.parseInt(nbt.getString("difficulty"));
		int reflex = (int)(Double.parseDouble(nbt.getString("reflex"))*20);
		int lucky = Integer.parseInt(nbt.getString("lucky"));
		
		BossBar progressBar = Bukkit.createBossBar(" ", BarColor.BLUE, BarStyle.SEGMENTED_10);
		progressBar.setVisible(false);
		progressBar.addPlayer(p);
		progressBar.setProgress(1);
		
		QTEHolder holer = new QTEHolder(p, hook, progress, complexity,
				difficulty, reflex, reflex, lucky,
				0, new LinkedList<>(),
				progressBar, null, null);
		generateQTE(holer);
	}
	
	public void generateQTE(QTEHolder holder) {
		Player p = holder.getPlayer();
		
		if(holder.getTask() != null) {
			if(holder.getTask().isCancelled())
				return;
			else
				holder.getTask().cancel();
		}
		p.closeInventory();
		if(!holder.getHook().isValid()) {
			holder.getTimeBar().setVisible(false);
			Utils.stopFishing(holder.getHook());
			return;
		}
		
		holder.getTimeBar().setVisible(false);
		
		Pair<Material,Material> materials = QTEItems.getQTEItems();
		ItemStack green = new ItemStack(materials.getKey());{
			ItemMeta im = green.getItemMeta();
			im.setDisplayName(" ");
			green.setItemMeta(im);
		}
		ItemStack red = new ItemStack(materials.getValue());{
			ItemMeta im = red.getItemMeta();
			im.setDisplayName(" ");
			red.setItemMeta(im);
		}
		holder.setCorrect(green);
		
		int slots = holder.getDifficulty()*9;
		int greenSlot = new Random().nextInt(slots);
		
		Inventory inv = Bukkit.createInventory(holder, slots, "§e§oLOWIENIE");
		for(int i = 0; i < slots; ++i)
			inv.setItem(i, red);
		inv.setItem(greenSlot, green);
		
		holder.setTimer(holder.getReflex());
		holder.getTimeBar().setTitle("§bCzas §b§o"+String.format("%.2f", holder.getReflex()/20.0)+" §bsekund");
		holder.getTimeBar().setProgress(1);
		holder.getTimeBar().setVisible(true);
		BukkitTask task = new BukkitRunnable() {
			@Override
			public void run() {
				if(isCancelled())
					return;
				if(holder.getTimer() <= 0) {
					cancelQTE(holder);
					return;
				}
				holder.decreaseTimer();
				holder.getTimeBar().setTitle("§bCzas §b§o"+String.format("%.2f", holder.getTimer()/20.0)+" §bsekund");
				holder.getTimeBar().setProgress(((double)holder.getTimer())/((double)holder.getReflex()));
			}
		}.runTaskTimerAsynchronously(Main.getInst(), 0, 1);
		holder.setTask(task);

		p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 2);
		p.openInventory(inv);
	}
	
	public void cancelQTE(QTEHolder holder) {
		if(holder.getTask() == null 
				|| holder.getTask().isCancelled())
			return;
		Player p = holder.getPlayer();
		new BukkitRunnable() {
			@Override
			public void run() {
				holder.getTask().cancel();
				p.closeInventory();
				holder.getTimeBar().setVisible(false);
				Utils.stopFishing(holder.getHook());
			}
		}.runTask(Main.getInst());
	}
	
	public void finishQTE(QTEHolder holder) {
		Player p = holder.getPlayer();
		holder.getTask().cancel();
		p.closeInventory();
		holder.getTimeBar().setVisible(false);
		Utils.stopFishing(holder.getHook());
		
		p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
		p.spawnParticle(Particle.TOTEM, p.getLocation().clone().add(0,1.25,0),
				15, 0.5, 0.5, 0.5, 0.2);
		
		int lucky = holder.getLucky();
		ItemStack it = FishingController.get().getRandomFish(lucky);
		
		KoloniaFishEvent event = new KoloniaFishEvent(p, it);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled())
			return;
		p.getInventory().addItem(it).values().forEach(item -> {
			me.Vark123.EpicRPG.Utils.Utils.dropItemStack(p, item);
		});
		
		double avr = holder.getAvrTime();
		double var = holder.calcVariance();
		if(avr > 0 && avr < .3)
			Bukkit.broadcast("§7[§4§lUWAGA§7] §cMozliwy AutoFish u §7"+p.getName()+" §c- sredni czas reakcji: §7§o"+String.format("%.4f", avr)+"", "ekipa.powiadomienia");
		if(var > 0 && var < 0.05)
			Bukkit.broadcast("§7[§4§lUWAGA§7] §cMozliwy AutoFish u §7"+p.getName()+" §c- odchylenie od sredniej: §7§o"+String.format("%.4f", var)+"", "ekipa.powiadomienia");
	}
	
}
