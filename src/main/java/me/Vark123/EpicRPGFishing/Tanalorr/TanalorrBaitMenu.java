package me.Vark123.EpicRPGFishing.Tanalorr;

import org.bukkit.Material;
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
public final class TanalorrBaitMenu {

	private static final TanalorrBaitMenu inst = new TanalorrBaitMenu();
	
	private final InventoryProvider provider;
	
	private final ItemStack empty;
	private final ItemStack confirm;
	
	private final int[] freeSlots = new int[] {4};
	
	private TanalorrBaitMenu() {
		empty = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);{
			ItemMeta im = empty.getItemMeta();
			im.setDisplayName(" ");
			empty.setItemMeta(im);
		}
		confirm = new ItemStack(Material.FISHING_ROD);{
			ItemMeta im = confirm.getItemMeta();
			im.setDisplayName("§bZARZUC WEDKE");
			confirm.setItemMeta(im);
		}
		
		provider = new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				for(int i = 0; i < 9; ++i) {
					if(i == 4)
						continue;
					contents.set(i, empty);
				}
				
				contents.set(8, IntelligentItem.of(confirm, e -> {
					ItemStack it = e.getInventory().getItem(4);
					if(it == null || it.getType().equals(Material.AIR)) {
						TanalorrFishingController.get().launchHook(player, null);
						player.closeInventory();
						return;
					}
					
					NBTItem nbt = new NBTItem(it);
					if(nbt.hasTag("type") 
							&& nbt.getString("type").equalsIgnoreCase("bait-tanalorr")) {
						if(it.getAmount() < 2)
							e.getInventory().setItem(4, null);
						else
							it.setAmount(it.getAmount() - 1);
					}

					player.closeInventory();
					TanalorrFishingController.get().launchHook(player, it);
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
	
	public static final TanalorrBaitMenu get() {
		return inst;
	}
	
	public void openMenu(Player p) {
		RyseInventory.builder()
			.title("§3§lZALOZ PRZYNETE")
			.rows(1)
			.disableUpdateTask()
			.ignoredSlots(freeSlots)
			.enableAction(Action.MOVE_TO_OTHER_INVENTORY)
			.ignoreClickEvent(DisabledInventoryClick.BOTTOM)
			.ignoreEvents(DisabledEvents.INVENTORY_DRAG)
			.provider(provider)
			.build(Main.getInst())
			.open(p);
	}
	
}
