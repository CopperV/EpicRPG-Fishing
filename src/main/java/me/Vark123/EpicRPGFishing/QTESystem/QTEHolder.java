package me.Vark123.EpicRPGFishing.QTESystem;

import java.util.List;

import org.bukkit.boss.BossBar;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class QTEHolder implements InventoryHolder {
	
	private Player player;
	private FishHook hook;

	@Setter
	private int progress;
	private int complexity;
	
	private int difficulty;
	
	@Setter
	private int timer;
	private int reflex;
	
	private int lucky;
	
	private double avrTime;
	private List<Double> variance;
	
	private BossBar timeBar;
	@Setter
	private BukkitTask task;
	
	@Setter
	private ItemStack correct;
	
	@Override
	public Inventory getInventory() {
		return null;
	}
	
	public void addAvrTime(double avrTime) {
		this.avrTime += avrTime;
	}
	
	public double getAvrTime() {
		return avrTime / (double)progress;
	}

	public void decreaseTimer() {
		--timer;
	}
	
	public double calcVariance() {
		if(variance == null
				|| variance.isEmpty()
				|| variance.size() < 2)
			return 0;
		
		double avr = getAvrTime();
		double sum = 0;
		for(double var : variance)
			sum += Math.pow(var - avr, 2);
		double var = Math.sqrt(sum/(double)variance.size());
		return var;
	}
	
}
