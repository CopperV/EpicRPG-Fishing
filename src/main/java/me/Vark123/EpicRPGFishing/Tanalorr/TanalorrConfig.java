package me.Vark123.EpicRPGFishing.Tanalorr;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.Getter;
import me.Vark123.EpicRPGFishing.FileManager;
import me.Vark123.EpicRPGFishing.Tanalorr.Merchants.TanalorrMerchant;
import me.Vark123.EpicRPGFishing.Tanalorr.Merchants.TanalorrMerchantManager;
import me.Vark123.EpicRPGFishing.Tanalorr.Merchants.Prices.IPrice;
import me.Vark123.EpicRPGFishing.Tanalorr.Merchants.Prices.Impl.CoinsPrice;
import me.Vark123.EpicRPGFishing.Tanalorr.Merchants.Prices.Impl.MoneyPrice;
import me.Vark123.EpicRPGFishing.Tanalorr.Merchants.Prices.Impl.RudaPrice;
import me.Vark123.EpicRPGFishing.Tanalorr.Merchants.Prices.Impl.StygiaPrice;
import me.Vark123.EpicRPGFishing.Tanalorr.Upgrades.TanalorrUpgrade;
import me.Vark123.EpicRPGFishing.Tanalorr.Upgrades.TanalorrUpgradeType;
import me.Vark123.EpicRPGFishing.Tanalorr.Upgrades.TanalorrUpgradesManager;
import me.Vark123.EpicRPGFishing.Tanalorr.Upgrades.Costs.ICost;
import me.Vark123.EpicRPGFishing.Tanalorr.Upgrades.Costs.Impl.DragonCoinsCost;
import me.Vark123.EpicRPGFishing.Tanalorr.Upgrades.Costs.Impl.MoneyCost;
import me.Vark123.EpicRPGFishing.Tanalorr.Upgrades.Costs.Impl.RudaCost;
import me.Vark123.EpicRPGFishing.Tanalorr.Upgrades.Costs.Impl.StygiaCost;

@Getter
public final class TanalorrConfig {

	private static final TanalorrConfig conf = new TanalorrConfig();
	
	private boolean init;
	
	private final Collection<String> allowedWorlds;
	private final TreeMap<Double, String> fishingDrops;
	
	private final Random rand = new Random();
	
	private TanalorrConfig() {
		allowedWorlds = new HashSet<>();
		fishingDrops = new TreeMap<>();
	}
	
	public static final TanalorrConfig get() {
		return conf;
	}
	
	public ItemStack getRandomDrop(double lucky, double variation) {
		double chance = rand.nextDouble(variation) + lucky;
		double max = fishingDrops.lastKey();
		if(chance > max)
			chance = max;
		
		String mmId = fishingDrops.ceilingEntry(chance).getValue();
		return MythicBukkit.inst().getItemManager().getItemStack(mmId);
	}
	
	public void load() {
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(FileManager.getTanalorrConfig());
		
		this.allowedWorlds.addAll(fYml.getStringList("worlds"));
		
		ConfigurationSection upgrades = fYml.getConfigurationSection("upgrades");
		upgrades.getKeys(false).forEach(key -> {
			ConfigurationSection upgrade = upgrades.getConfigurationSection(key);
			
			final String display = ChatColor.translateAlternateColorCodes('&', upgrade.getString("display"));
			final TanalorrUpgradeType type = TanalorrUpgradeType.valueOf(key.toUpperCase());
			
			ConfigurationSection levels = upgrade.getConfigurationSection("levels");
			levels.getKeys(false).stream()
				.filter(StringUtils::isNumeric)
				.map(Integer::parseInt)
				.forEach(level -> {
					ConfigurationSection section = levels.getConfigurationSection(""+level);
					
					final String id = section.getString("id");
					final String valueDisplay = ChatColor.translateAlternateColorCodes('&', section.getString("value-display"));
					double value = section.getDouble("value");

					Collection<ICost> costs;
					if(section.getConfigurationSection("costs") != null)
						costs = getCosts(section.getConfigurationSection("costs"));
					else
						costs = new LinkedList<>();
					
					TanalorrUpgradesManager.get().registerUpgrade(TanalorrUpgrade.builder()
							.id(id)
							.level(level)
							.type(type)
							.display(display)
							.valueDisplay(valueDisplay)
							.value(value)
							.costs(costs)
							.build());
				});
		});
		
		MutableDouble value = new MutableDouble();
		ConfigurationSection drops = fYml.getConfigurationSection("drops");
		drops.getKeys(false).stream()
			.map(key -> drops.getConfigurationSection(key))
			.forEach(dropSection -> {
				String mmId = dropSection.getString("id");
				double chance = dropSection.getDouble("chance");
				
				fishingDrops.put(value.addAndGet(chance), mmId);
			});
		
		ConfigurationSection merchants = fYml.getConfigurationSection("merchants");
		merchants.getKeys(false).stream()
			.map(key -> merchants.getConfigurationSection(key))
			.forEach(merchantSection -> {
				String id = merchantSection.getString("id");
				String display = ChatColor.translateAlternateColorCodes('&', 
						merchantSection.getString("display"));
				
				Map<String, Collection<IPrice>> offers = new LinkedHashMap<>();
				ConfigurationSection offersSection = merchantSection.getConfigurationSection("offers");
				offersSection.getKeys(false).stream()
					.forEach(mmId -> {
						ConfigurationSection offerSection = offersSection.getConfigurationSection(mmId);
						
						Collection<IPrice> prices = new HashSet<>();
						if(offerSection.contains("money"))
							prices.add(new MoneyPrice(offerSection.getInt("money")));
						if(offerSection.contains("stygia"))
							prices.add(new StygiaPrice(offerSection.getInt("stygia")));
						if(offerSection.contains("ruda"))
							prices.add(new RudaPrice(offerSection.getInt("ruda")));
						if(offerSection.contains("coins"))
							prices.add(new CoinsPrice(offerSection.getInt("coins")));
					
						offers.put(mmId, prices);
					});
				
				TanalorrMerchant merchant = TanalorrMerchant.builder()
						.id(id)
						.display(display)
						.offers(offers)
						.build();
				TanalorrMerchantManager.get().registerMerchant(merchant);
			});
	}
	
	private Collection<ICost> getCosts(ConfigurationSection section) {
		Collection<ICost> costs = new LinkedList<>();
		
		if(section.contains("money"))
			costs.add(new MoneyCost(section.getInt("money")));
		if(section.contains("stygia"))
			costs.add(new StygiaCost(section.getInt("stygia")));
		if(section.contains("ruda"))
			costs.add(new RudaCost(section.getInt("ruda")));
		if(section.contains("coins"))
			costs.add(new DragonCoinsCost(section.getInt("coins")));
		
		return costs;
	}
	
}
