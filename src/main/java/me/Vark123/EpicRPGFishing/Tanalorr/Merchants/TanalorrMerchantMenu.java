package me.Vark123.EpicRPGFishing.Tanalorr.Merchants;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.lang3.mutable.MutableInt;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.enums.Action;
import io.github.rysefoxx.inventory.plugin.enums.DisabledEvents;
import io.github.rysefoxx.inventory.plugin.enums.DisabledInventoryClick;
import io.github.rysefoxx.inventory.plugin.other.EventCreator;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import lombok.Getter;
import me.Vark123.EpicRPG.Core.CoinsSystem;
import me.Vark123.EpicRPG.Core.MoneySystem;
import me.Vark123.EpicRPG.Core.RudaSystem;
import me.Vark123.EpicRPG.Core.StygiaSystem;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Utils.Utils;
import me.Vark123.EpicRPGFishing.Main;
import me.Vark123.EpicRPGFishing.Tanalorr.Merchants.Prices.Impl.CoinsPrice;
import me.Vark123.EpicRPGFishing.Tanalorr.Merchants.Prices.Impl.MoneyPrice;
import me.Vark123.EpicRPGFishing.Tanalorr.Merchants.Prices.Impl.RudaPrice;
import me.Vark123.EpicRPGFishing.Tanalorr.Merchants.Prices.Impl.StygiaPrice;

@Getter
public final class TanalorrMerchantMenu {

	private static final TanalorrMerchantMenu inst = new TanalorrMerchantMenu();
	
	private final ItemStack empty;
	private final ItemStack sell;
	private final ItemStack confirm;
	private final ItemStack reject;

	private final ItemStack nullItem;
	
	private final int[] freeSlots = new int[] {
			0,1,2,3,4,5,6,7,8, 
			9,10,11,12,13,14,15,16,17, 
			18,19,20,21,22,23,24,25,26};
	
	private TanalorrMerchantMenu() {
		empty = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);{
			ItemMeta im = empty.getItemMeta();
			im.setDisplayName(" ");
			empty.setItemMeta(im);
		}
		sell = new ItemStack(Material.EMERALD);{
			ItemMeta im = sell.getItemMeta();
			im.setDisplayName("§6§lSPRZEDAJ");
			sell.setItemMeta(im);
		}
		confirm = new ItemStack(Material.EMERALD);{
			ItemMeta im = confirm.getItemMeta();
			im.setDisplayName("§2§lPOTWIERDZ");
			confirm.setItemMeta(im);
		}
		reject = new ItemStack(Material.REDSTONE);{
			ItemMeta im = reject.getItemMeta();
			im.setDisplayName("§c§lODRZUC");
			reject.setItemMeta(im);
		}
		
		nullItem = new ItemStack(Material.AIR);
	}
	
	public static final TanalorrMerchantMenu get() {
		return inst;
	}
	
	public void openMenu(Player p, TanalorrMerchant merchant) {
		RyseInventory.builder()
			.title(merchant.getDisplay())
			.rows(4)
			.disableUpdateTask()
			.ignoredSlots(freeSlots)
			.enableAction(Action.MOVE_TO_OTHER_INVENTORY)
			.ignoreClickEvent(DisabledInventoryClick.BOTTOM)
			.ignoreEvents(DisabledEvents.INVENTORY_DRAG)
			.listener(getClickEvent())
			.provider(getProvider(p, merchant))
			.build(Main.getInst())
			.open(p);
	}
	
	private InventoryProvider getProvider(Player p, TanalorrMerchant merchant) {
		return new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				generateSellMenu(p, merchant, contents);
			}
			@Override
			public void close(Player player, RyseInventory inventory) {
				Inventory inv = inventory.getInventory();
				for(int i = 0; i < 27; ++i) {
					ItemStack it = inv.getItem(i);
					if(it == null || it.getType().equals(Material.AIR))
						continue;
					
					Utils.dropItemStack(player, it);
				}
			}
		};
	}
	
	private void generateSellMenu(Player p, TanalorrMerchant merchant, InventoryContents contents) {
		for(int i = 27; i < 36; ++i)
			contents.updateOrSet(i, empty);
		
		contents.updateOrSet(31, IntelligentItem.of(sell, e -> {
			generateConfirmationMenu(p, merchant, contents);
		}));
		
	}
	
	private void generateConfirmationMenu(Player p, TanalorrMerchant merchant, InventoryContents contents) {
		for(int i = 27; i < 36; ++i)
			contents.updateOrSet(i, empty);
		
		MutableInt moneyValue = new MutableInt();
		MutableInt stygiaValue = new MutableInt();
		MutableInt coinsValue = new MutableInt();
		MutableInt rudaValue = new MutableInt();
		Map<Integer, ItemStack> correctItems = new LinkedHashMap<>();
		Inventory inv = contents.pagination().inventory().getInventory();
		for(int i = 0; i < 27; ++i) {
			ItemStack it = inv.getItem(i);
			if(it == null || it.getType().equals(Material.AIR))
				continue;			
			
			NBTItem nbt = new NBTItem(it);
			if(!nbt.hasTag("MYTHIC_TYPE"))
				continue;
			
			String mmId = nbt.getString("MYTHIC_TYPE");
			if(!merchant.getOffers().containsKey(mmId))
				continue;

			correctItems.put(i, it);
			merchant.getOffers().get(mmId).forEach(price -> {
				if(price instanceof MoneyPrice)
					moneyValue.add(price.getAmount() * it.getAmount());
				else if(price instanceof StygiaPrice)
					stygiaValue.add(price.getAmount() * it.getAmount());
				else if(price instanceof CoinsPrice)
					coinsValue.add(price.getAmount() * it.getAmount());
				else if(price instanceof RudaPrice)
					rudaValue.add(price.getAmount() * it.getAmount());
			});
		}
		
		ItemStack info = new ItemStack(Material.PAPER);{
			ItemMeta im = info.getItemMeta();
			im.setDisplayName("§7§oZa to wszystko moge Ci dac:");
			List<String> lore = new LinkedList<>();
			lore.add(" ");
			if(moneyValue.getValue() > 0)
				lore.add("§c§l》 §e§o"+moneyValue.getValue()+" §7zlota");
			if(stygiaValue.getValue() > 0)
				lore.add("§c§l》 §3§o"+stygiaValue.getValue()+" §7stygii");
			if(coinsValue.getValue() > 0)
				lore.add("§c§l》 §4§o"+coinsValue.getValue()+" §7smoczych monet");
			if(rudaValue.getValue() > 0)
				lore.add("§c§l》 §9§o"+rudaValue.getValue()+" §7brylek rudy");
			im.setLore(lore);
			info.setItemMeta(im);
		}
		contents.updateOrSet(31, info);
		
		contents.updateOrSet(28, IntelligentItem.of(confirm, e -> {
			correctItems.keySet().forEach(i -> contents.updateOrSet(i, nullItem));
			
			RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
			if(moneyValue.getValue() > 0)
				MoneySystem.getInstance().addMoney(rpg, moneyValue.getValue(), "sale");
			if(stygiaValue.getValue() > 0)
				StygiaSystem.getInstance().addStygia(rpg, stygiaValue.getValue(), "sale");
			if(coinsValue.getValue() > 0)
				CoinsSystem.getInstance().addCoins(rpg, coinsValue.getValue(), "sale");
			if(rudaValue.getValue() > 0)
				RudaSystem.getInstance().addRuda(rpg, rudaValue.getValue(), "sale");
			
			p.closeInventory();
		}));
		contents.updateOrSet(34, IntelligentItem.of(reject, e -> {
			generateSellMenu(p, merchant, contents);
		}));
	}
	
	private EventCreator<InventoryClickEvent> getClickEvent() {
		Consumer<InventoryClickEvent> event = e -> {
			Inventory inv = e.getView().getTopInventory();
			if(!inv.getItem(31).getType().equals(Material.PAPER))
				return;

			if(e.isShiftClick()) {
				e.setResult(Result.DENY);
				e.setCancelled(true);
				return;
			}

			List<Integer> freeSlotList = Utils.intArrayToList(freeSlots);
			int slot = e.getSlot();
			if(!freeSlotList.contains(slot))
				return;

			e.setCancelled(true);
		};
		
		EventCreator<InventoryClickEvent> creator = new EventCreator<>(InventoryClickEvent.class, event);
		return creator;
	}
	
}
