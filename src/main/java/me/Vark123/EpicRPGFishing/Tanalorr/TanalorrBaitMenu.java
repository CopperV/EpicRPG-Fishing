package me.Vark123.EpicRPGFishing.Tanalorr;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import lombok.Getter;
import me.Vark123.EpicInventory.Content.IntelligentItem;
import me.Vark123.EpicInventory.Content.InventoryContents;
import me.Vark123.EpicInventory.Content.InventoryProvider;
import me.Vark123.EpicInventory.Enums.DisabledEvents;
import me.Vark123.EpicInventory.Enums.DisabledInventoryClick;
import me.Vark123.EpicInventory.Pagination.EpicInventory;
import me.Vark123.EpicRPG.Utils.Utils;
import me.Vark123.EpicRPGFishing.Main;

@Getter
public final class TanalorrBaitMenu {

	private static final TanalorrBaitMenu inst = new TanalorrBaitMenu();
	
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
	}
	
	public static final TanalorrBaitMenu get() {
		return inst;
	}
	
	public void openMenu(Player p, ItemStack fishingRod) {
		EpicInventory.builder()
			.title("§3§lZALOZ PRZYNETE")
			.rows(1)
			.disableUpdateTask()
			.ignoredSlots(freeSlots)
//			.enableAction(Action.MOVE_TO_OTHER_INVENTORY)
			.ignoreClickEvent(DisabledInventoryClick.BOTTOM)
			.ignoreEvents(DisabledEvents.INVENTORY_DRAG)
			.provider(getProvider(fishingRod))
			.build(Main.getInst())
			.open(p);
	}
	
	private InventoryProvider getProvider(ItemStack fishingRod) {
		return new InventoryProvider() {
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
							&& nbt.getString("type").equalsIgnoreCase("bait-tanalorr")
							&& nbt.hasTag("hook")) {
						
						NBTItem fishingRodNbt2 = new NBTItem(fishingRod);
						int hookLevel = fishingRodNbt2.getInteger("hook");
						int requiredHook = nbt.getInteger("hook");
						if(requiredHook > hookLevel) {
							player.closeInventory();
							TanalorrFishingController.get().launchHook(player, null);
							return;
						}
						
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
			public void close(Player player, EpicInventory inventory) {
				ItemStack it = inventory.getInventory().getItem(4);
				if(it == null || it.getType().equals(Material.AIR))
					return;
				
				Utils.dropItemStack(player, it);
				inventory.getInventory().clear();
			}
		};
	}
	
}
