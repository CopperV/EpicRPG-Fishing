package me.Vark123.EpicRPGFishing.Tanalorr;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import de.tr7zw.nbtapi.NBTItem;
import lombok.Getter;
import me.Vark123.EpicRPGFishing.Main;
import me.Vark123.EpicRPGFishing.Tanalorr.Events.TanalorrFishEvent;
import me.Vark123.EpicRPGFishing.Tanalorr.Upgrades.TanalorrUpgradeType;
import me.Vark123.EpicRPGFishing.Tanalorr.Upgrades.TanalorrUpgradesManager;

@Getter
public final class TanalorrFishingController {

	private static final TanalorrFishingController inst = new TanalorrFishingController();
	
	private final Map<Player, ItemStack> baitingPlayers;
	private final Map<Player, TanalorrPlayerTempContainer> fishingPlayers;
	
	private final double CLICK_UPGRADE = 0.07;
	private final double PULL_FORCE = 0.35;
	
	private final Random rand = new Random();
	
	private TanalorrFishingController() {
		baitingPlayers = new ConcurrentHashMap<>();
		fishingPlayers = new ConcurrentHashMap<>();
	}
	
	public static final TanalorrFishingController get() {
		return inst;
	}
	
	public boolean isFishing(Player p) {
		return fishingPlayers.containsKey(p);
	}
	
	public boolean isBaiting(Player p) {
		return baitingPlayers.containsKey(p);
	}
	
	public void setBaitingPlayer(Player p, ItemStack it) {
		baitingPlayers.put(p, it);
	}
	
	public void removeBaitingPlayer(Player p) {
		baitingPlayers.remove(p);
	}
	
	public ItemStack getBaitingRod(Player p) {
		return baitingPlayers.get(p);
	}
	
	public void launchHook(Player p, ItemStack it) {
		TanalorrFishingController.get().removeBaitingPlayer(p);
		
		p.playSound(p.getLocation(), Sound.ENTITY_FISHING_BOBBER_THROW, 0.8f, 0.8f);
		FishHook hook = p.launchProjectile(FishHook.class);
		fishingPlayers.put(p, TanalorrPlayerTempContainer.builder()
				.player(p)
				.hook(hook)
				.fishingRod(p.getInventory().getItemInMainHand())
				.bait(it)
				.state(TanalorrFishingState.WAITING)
				.fishingStatus(0.25)
				.build());
		
		int maxTime = 600;
		if(it != null) {
			NBTItem nbt = new NBTItem(it);
			if(nbt.hasTag("time")) {
				double percent = nbt.getDouble("time");
				maxTime += maxTime * percent;
			}
		}
		hook.setMaxWaitTime(maxTime);
		hook.setMinWaitTime(maxTime/2);
	}
	
	public void removeHook(Player p) {
		if(!isFishing(p))
			return;
		
		p.playSound(p.getLocation(), Sound.ENTITY_FISHING_BOBBER_RETRIEVE, 0.8f, 0.8f);
		p.swingMainHand();
		TanalorrPlayerTempContainer cont = fishingPlayers.get(p);
		cont.getHook().remove();
		fishingPlayers.remove(p);
	}
	
	public void signalBite(Player p) {
		if(!isFishing(p))
			return;

		TanalorrPlayerTempContainer cont = fishingPlayers.get(p);
		if(!cont.getState().equals(TanalorrFishingState.WAITING))
			return;

		p.playSound(p, Sound.ENTITY_PLAYER_LEVELUP, 1.5f, 0.8f);
		p.sendTitle(" ", "§3§lCOS ZLAPALES", 5, 10, 15);
		cont.setState(TanalorrFishingState.BITING);
		new BukkitRunnable() {
			@Override
			public void run() {
				if(isCancelled())
					return;
				if(!isFishing(p) || !cont.getState().equals(TanalorrFishingState.BITING))
					return;
				
				playFishingFailEffect(p);
				removeHook(p);
			}
		}.runTaskLater(Main.getInst(), 30);
	}
	
	public void signalClick(Player p) {
		if(!isFishing(p))
			return;

		TanalorrPlayerTempContainer cont = fishingPlayers.get(p);
		switch(cont.getState()) {
			case WAITING:
				TanalorrFishingController.get().removeHook(p);
				break;
			case BITING:
				startFishingEvent(cont);
				break;
			case FISHING:
				NBTItem nbt = new NBTItem(cont.getFishingRod());
				int level = nbt.getInteger("reel");
				double upgrade = CLICK_UPGRADE * TanalorrUpgradesManager.get().getUpgradeByLevel(TanalorrUpgradeType.REEL, level).getValue();
				cont.increaseStatus(upgrade);
				p.playSound(p.getLocation(), Sound.ENTITY_FISHING_BOBBER_RETRIEVE, 1.1f, 1.2f);
				break;
		}
	}
	
	private void startFishingEvent(TanalorrPlayerTempContainer cont) {
		Player p = cont.getPlayer();
		
		Block b1 = p.getLocation().getBlock();
		Block b2 = p.getLocation().clone().add(0,1,0).getBlock();
		if(b1 != null && (b1.isLiquid() || b1.getType().equals(Material.COBWEB)))
			return;
		if(b2 != null && (b1.isLiquid() || b2.getType().equals(Material.COBWEB)))
			return;

		World w = p.getWorld();
		Location loc1 = p.getEyeLocation();
		Location loc2 = loc1.clone().add(0,-1,0);
		Location target = cont.getHook().getLocation();
		Vector vec1 = new Vector(target.getX() - loc1.getX(),
				target.getY() - loc1.getY(),
				target.getZ() - loc1.getZ()).normalize();
		Vector vec2 = new Vector(target.getX() - loc2.getX(),
				target.getY() - loc2.getY(),
				target.getZ() - loc2.getZ()).normalize();
		RayTraceResult result1 = w.rayTrace(loc1, vec1, 80, FluidCollisionMode.NEVER, false, 1, cont.getHook()::equals);
		RayTraceResult result2 = w.rayTrace(loc2, vec2, 80, FluidCollisionMode.NEVER, false, 1, cont.getHook()::equals);
		if(result1.getHitEntity() == null || result2.getHitEntity() == null)
			return;
		cont.setState(TanalorrFishingState.FISHING);
		
		BossBar bar = Bukkit.createBossBar("§9RYBI BOJ", BarColor.BLUE, BarStyle.SOLID);
		bar.setProgress(cont.getFishingStatus());
		bar.addPlayer(p);
		bar.setVisible(true);

		NBTItem nbt = new NBTItem(cont.getFishingRod());
		int level = nbt.getInteger("line");
		new BukkitRunnable() {
			Location startLoc = p.getLocation().clone();
			Location hookLoc = cont.getHook().getLocation();
			
			double multiplier = TanalorrUpgradesManager.get().getUpgradeByLevel(TanalorrUpgradeType.LINE, level).getValue();
			@Override
			public void run() {
				if(isCancelled()) {
					bar.removeAll();
					bar.setVisible(false);
					
					removeHook(p);
					return;
				}
				
				if(!p.isOnline()) {
					bar.removeAll();
					bar.setVisible(false);
					
					removeHook(p);
					cancel();
					return;
				}
				if(p.getLocation().distanceSquared(startLoc) > 4) {
					bar.removeAll();
					bar.setVisible(false);
					
					removeHook(p);
					cancel();
					return;
				}
				if(cont.getHook().isDead()) {
					bar.removeAll();
					bar.setVisible(false);
					
					removeHook(p);
					cancel();
					return;
				}
				if(p.isSneaking()) {
					bar.removeAll();
					bar.setVisible(false);
					
					removeHook(p);
					cancel();
					return;
				}
				
				cont.decreaseStatus(0.025);
				if(cont.getFishingStatus() <= 0) {
					bar.removeAll();
					bar.setVisible(false);
					
					playFishingFailEffect(p);
					cancel();
					return;
				}
				if(cont.getFishingStatus() >= 1) {
					bar.removeAll();
					bar.setVisible(false);
					
					int rodLevel = nbt.getInteger("rod");
					double variation = TanalorrUpgradesManager.get().getUpgradeByLevel(TanalorrUpgradeType.ROD, rodLevel).getValue();

					int luckyLevel = nbt.getInteger("lucky");
					double lucky = TanalorrUpgradesManager.get().getUpgradeByLevel(TanalorrUpgradeType.LUCKY, luckyLevel).getValue();
					if(cont.getBait() != null) {
						NBTItem baitNBT = new NBTItem(cont.getBait());
						if(baitNBT.hasTag("lucky"))
							lucky += baitNBT.getDouble("lucky");
					}
					
					ItemStack drop = TanalorrConfig.get().getRandomDrop(lucky, variation);
					TanalorrFishEvent event = new TanalorrFishEvent(p, drop);
					Bukkit.getPluginManager().callEvent(event);
					if(event.isCancelled())
						return;
					
					FishHook hook = cont.getHook();
					
					Item item = hookLoc.getWorld().dropItem(hookLoc, drop);
					hook.setHookedEntity(item);
					hook.pullHookedEntity();
					
					removeHook(p);
					cancel();
					return;
				}
				bar.setProgress(cont.getFishingStatus());
				
				double movementSpeed = p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getValue();
				double pullValue = PULL_FORCE * multiplier * movementSpeed * 6;
				if(pullValue < 0.1)
					pullValue = 0.1;
				
				Location loc = p.getLocation();
				Vector vec = new Vector(loc.getX() - hookLoc.getX(), 0, loc.getZ() - hookLoc.getZ()).normalize().multiply(-1);
				
				double x = rand.nextDouble(1) + 0.5 * multiplier;
				double z = rand.nextDouble(1) + 0.5 * multiplier;
				vec.multiply(new Vector(x, 0, z)).normalize().multiply(pullValue);
				
				p.setVelocity(vec);
			}
		}.runTaskTimer(Main.getInst(), 0, 5);
	
	}
	
	private void playFishingFailEffect(Player p) {
		p.playSound(p, Sound.ENTITY_VILLAGER_HURT, 1.5f, 0.8f);
		p.sendTitle(" ", "§c§lOFIARA ZERWALA SIE", 5, 10, 15);
		
		Location loc = p.getLocation().clone().add(0, 0.1, 0);
		for(int i = 0; i < 18; ++i) {
			double x = rand.nextDouble(1.5) - 0.75;
			double z = rand.nextDouble(1.5) - 0.75;
			Location tmp = loc.clone().add(x, 0, z);
			
			p.spawnParticle(Particle.SMOKE_LARGE, tmp, 0, 0, rand.nextDouble(0.25)+0.1, 0, 0.05);
		}
	}
	
}
