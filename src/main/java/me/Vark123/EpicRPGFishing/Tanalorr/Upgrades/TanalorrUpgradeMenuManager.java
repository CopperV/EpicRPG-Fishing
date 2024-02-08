package me.Vark123.EpicRPGFishing.Tanalorr.Upgrades;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableObject;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.enums.Action;
import io.github.rysefoxx.inventory.plugin.enums.DisabledEvents;
import io.github.rysefoxx.inventory.plugin.enums.DisabledInventoryClick;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import lombok.Getter;
import me.Vark123.EpicRPG.Utils.Utils;
import me.Vark123.EpicRPGFishing.Main;

@Getter
public final class TanalorrUpgradeMenuManager {

	private static final TanalorrUpgradeMenuManager inst = new TanalorrUpgradeMenuManager();

	private final ItemStack empty;
	private final ItemStack upgrade;

	private final int[] freeSlots = new int[] {4};
	
	private final InventoryProvider baseProvider;
	
	private TanalorrUpgradeMenuManager() {
		empty = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);{
			ItemMeta im = empty.getItemMeta();
			im.setDisplayName(" ");
			empty.setItemMeta(im);
		}
		upgrade = new ItemStack(Material.FISHING_ROD);{
			ItemMeta im = upgrade.getItemMeta();
			im.setDisplayName("§6ULEPSZ");
			upgrade.setItemMeta(im);
		}
		
		baseProvider = new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				for(int i = 0; i < 9; ++i) {
					if(i == 4)
						continue;
					contents.set(i, empty);
				}
				
				contents.set(8, IntelligentItem.of(upgrade, e -> {
					ItemStack it = e.getClickedInventory().getItem(4);
					if(it == null || it.getType().equals(Material.AIR)) {
						player.closeInventory();
						return;
					}
					
					NBTItem nbt = new NBTItem(it);
					if(!nbt.hasTag("type") 
							|| !nbt.getString("type").equalsIgnoreCase("fish-tanalorr")) {
						player.closeInventory();
						return;
					}
					
					e.getClickedInventory().setItem(4, null);
					openMenu(player, new MutableObject<>(it));
				}));
			}
			@Override
			public void close(Player player, RyseInventory inventory) {
				ItemStack it = inventory.getInventory().getItem(4);
				if(it == null || it.getType().equals(Material.AIR))
					return;
				
				Utils.dropItemStack(player, it);
			}
		};
	}
	
	public static final TanalorrUpgradeMenuManager get() {
		return inst;
	}
	
	public void openBaseMenu(Player p) {
		RyseInventory.builder()
			.title("§6§lULEPSZ WEDKE")
			.rows(1)
			.disableUpdateTask()
			.ignoredSlots(freeSlots)
			.enableAction(Action.MOVE_TO_OTHER_INVENTORY)
			.ignoreClickEvent(DisabledInventoryClick.BOTTOM)
			.ignoreEvents(DisabledEvents.INVENTORY_DRAG)
			.provider(baseProvider)
			.build(Main.getInst())
			.open(p);
	}
	
	private void openMenu(Player p, MutableObject<ItemStack> fishingRod) {
		RyseInventory.builder()
			.title("§6§lULEPSZ WEDKE")
			.rows(1)
			.disableUpdateTask()
			.provider(new InventoryProvider() {
				@Override
				public void init(Player player, InventoryContents contents) {
					for(int i = 0; i < 9; ++i)
						contents.set(i, empty);

					ItemStack _it = fishingRod.getValue();
					NBTItem nbt = new NBTItem(_it);
					int lucky = nbt.getInteger("lucky");
					int rod = nbt.getInteger("rod");
					int reel = nbt.getInteger("reel");
					int line = nbt.getInteger("line");
					int hook = nbt.getInteger("hook");
					
					TanalorrUpgrade luckyUpgrade = TanalorrUpgradesManager.get()
							.getUpgradeByLevel(TanalorrUpgradeType.LUCKY, lucky + 1);
					TanalorrUpgrade rodUpgrade = TanalorrUpgradesManager.get()
							.getUpgradeByLevel(TanalorrUpgradeType.ROD, rod + 1);
					TanalorrUpgrade reelUpgrade = TanalorrUpgradesManager.get()
							.getUpgradeByLevel(TanalorrUpgradeType.REEL, reel + 1);
					TanalorrUpgrade lineUpgrade = TanalorrUpgradesManager.get()
							.getUpgradeByLevel(TanalorrUpgradeType.LINE, line + 1);
					TanalorrUpgrade hookUpgrade = TanalorrUpgradesManager.get()
							.getUpgradeByLevel(TanalorrUpgradeType.HOOK, hook + 1);
					
					if(luckyUpgrade != null){
						ItemStack it = new ItemStack(Material.GOLD_NUGGET);
						ItemMeta im = it.getItemMeta();
						im.setDisplayName(luckyUpgrade.getDisplay()+luckyUpgrade.getValueDisplay());
						List<String> lore = new LinkedList<>();
						lore.add(" ");
						luckyUpgrade.getCosts().forEach(cost -> lore.add(cost.display()));
						im.setLore(lore);
						it.setItemMeta(im);
						contents.set(0, IntelligentItem.of(it, e -> {
							tryUpgradeFishingRod(p, luckyUpgrade, fishingRod, lucky + 1, rod, reel, line, hook);
						}));
					}
					if(rodUpgrade != null){
						ItemStack it = new ItemStack(Material.STICK);
						ItemMeta im = it.getItemMeta();
						im.setDisplayName(rodUpgrade.getDisplay()+rodUpgrade.getValueDisplay());
						List<String> lore = new LinkedList<>();
						lore.add(" ");
						rodUpgrade.getCosts().forEach(cost -> lore.add(cost.display()));
						im.setLore(lore);
						it.setItemMeta(im);
						contents.set(2, IntelligentItem.of(it, e -> {
							tryUpgradeFishingRod(p, rodUpgrade, fishingRod, lucky, rod + 1, reel, line, hook);
						}));
					}
					if(reelUpgrade != null){
						ItemStack it = new ItemStack(Material.CONDUIT);
						ItemMeta im = it.getItemMeta();
						im.setDisplayName(reelUpgrade.getDisplay()+reelUpgrade.getValueDisplay());
						List<String> lore = new LinkedList<>();
						lore.add(" ");
						reelUpgrade.getCosts().forEach(cost -> lore.add(cost.display()));
						im.setLore(lore);
						it.setItemMeta(im);
						contents.set(4, IntelligentItem.of(it, e -> {
							tryUpgradeFishingRod(p, reelUpgrade, fishingRod, lucky, rod, reel + 1, line, hook);
						}));
					}
					if(lineUpgrade != null){
						ItemStack it = new ItemStack(Material.STRING);
						ItemMeta im = it.getItemMeta();
						im.setDisplayName(lineUpgrade.getDisplay()+lineUpgrade.getValueDisplay());
						List<String> lore = new LinkedList<>();
						lore.add(" ");
						lineUpgrade.getCosts().forEach(cost -> lore.add(cost.display()));
						im.setLore(lore);
						it.setItemMeta(im);
						contents.set(6, IntelligentItem.of(it, e -> {
							tryUpgradeFishingRod(p, lineUpgrade, fishingRod, lucky, rod, reel, line + 1, hook);
						}));
					}
					if(hookUpgrade != null){
						ItemStack it = new ItemStack(Material.TRIPWIRE_HOOK);
						ItemMeta im = it.getItemMeta();
						im.setDisplayName(hookUpgrade.getDisplay()+hookUpgrade.getValueDisplay());
						List<String> lore = new LinkedList<>();
						lore.add(" ");
						hookUpgrade.getCosts().forEach(cost -> lore.add(cost.display()));
						im.setLore(lore);
						it.setItemMeta(im);
						contents.set(8, IntelligentItem.of(it, e -> {
							tryUpgradeFishingRod(p, hookUpgrade, fishingRod, lucky, rod, reel, line, hook + 1);
						}));
					}
				}
				@Override
				public void close(Player player, RyseInventory inventory) {
					if(fishingRod.getValue() == null)
						return;
					
					Utils.dropItemStack(player, fishingRod.getValue());
				}
			})
			.build(Main.getInst())
			.open(p);
	}
	
	private void tryUpgradeFishingRod(Player p, TanalorrUpgrade upgrade, MutableObject<ItemStack> fishingRod, int lucky, int rod, int reel, int line, int hook) {
		MutableBoolean canUpgrade = new MutableBoolean(true);
		upgrade.getCosts().stream()
			.filter(cost -> !cost.check(p))
			.findAny()
			.ifPresent(cost -> canUpgrade.setFalse());
		
		if(canUpgrade.isFalse()) {
			p.closeInventory();
			return;
		}
		
		upgrade.getCosts().forEach(cost -> cost.spend(p));
		ItemStack newFishingRod = generateUpgradedFishingRod(fishingRod.getValue(), lucky, rod, reel, line, hook);
		fishingRod.setValue(newFishingRod);

		p.closeInventory();
		p.playSound(p, Sound.BLOCK_ANVIL_USE, 1, 0.85f);
		p.spawnParticle(Particle.VILLAGER_HAPPY, p.getLocation().clone().add(0, 1.25, 0), 24, .6f, .6f, .6f, .15f);
	}
	
	private ItemStack generateUpgradedFishingRod(ItemStack it, int lucky, int rod, int reel, int line, int hook) {
		TanalorrUpgrade luckyUpgrade = TanalorrUpgradesManager.get()
				.getUpgradeByLevel(TanalorrUpgradeType.LUCKY, lucky);
		TanalorrUpgrade rodUpgrade = TanalorrUpgradesManager.get()
				.getUpgradeByLevel(TanalorrUpgradeType.ROD, rod);
		TanalorrUpgrade reelUpgrade = TanalorrUpgradesManager.get()
				.getUpgradeByLevel(TanalorrUpgradeType.REEL, reel);
		TanalorrUpgrade lineUpgrade = TanalorrUpgradesManager.get()
				.getUpgradeByLevel(TanalorrUpgradeType.LINE, line);
		TanalorrUpgrade hookUpgrade = TanalorrUpgradesManager.get()
				.getUpgradeByLevel(TanalorrUpgradeType.HOOK, hook);
		
		ItemStack toReturn = it.clone();
		
		ItemMeta im = toReturn.getItemMeta();
		List<String> lore = new LinkedList<>();
		lore.add(" ");
		lore.add(luckyUpgrade.getDisplay()+luckyUpgrade.getValueDisplay());
		lore.add(rodUpgrade.getDisplay()+rodUpgrade.getValueDisplay());
		lore.add(reelUpgrade.getDisplay()+reelUpgrade.getValueDisplay());
		lore.add(lineUpgrade.getDisplay()+lineUpgrade.getValueDisplay());
		lore.add(hookUpgrade.getDisplay()+hookUpgrade.getValueDisplay());
		lore.add(" ");
		im.setLore(lore);
		toReturn.setItemMeta(im);
		
		NBTItem nbt = new NBTItem(toReturn);
		nbt.setInteger("lucky", lucky);
		nbt.setInteger("rod", rod);
		nbt.setInteger("reel", reel);
		nbt.setInteger("line", line);
		nbt.setInteger("hook", hook);
		nbt.applyNBT(toReturn);
		
		return toReturn;
	}
	
}
