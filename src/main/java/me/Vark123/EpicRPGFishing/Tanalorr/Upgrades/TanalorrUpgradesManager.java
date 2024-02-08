package me.Vark123.EpicRPGFishing.Tanalorr.Upgrades;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;

@Getter
public final class TanalorrUpgradesManager {

	private static final TanalorrUpgradesManager inst = new TanalorrUpgradesManager();
	
	private final Map<Integer, TanalorrUpgrade> luckyUpgrades;
	private final Map<Integer, TanalorrUpgrade> rodUpgrades;
	private final Map<Integer, TanalorrUpgrade> reelUpgrades;
	private final Map<Integer, TanalorrUpgrade> lineUpgrades;
	private final Map<Integer, TanalorrUpgrade> hookUpgrades;
	
	private TanalorrUpgradesManager() {
		luckyUpgrades = new LinkedHashMap<>();
		rodUpgrades = new LinkedHashMap<>();
		reelUpgrades = new LinkedHashMap<>();
		lineUpgrades = new LinkedHashMap<>();
		hookUpgrades = new LinkedHashMap<>();
	}
	
	public static final TanalorrUpgradesManager get() {
		return inst;
	}
	
	public void registerUpgrade(TanalorrUpgrade upgrade) {
		int level = upgrade.getLevel();
		switch(upgrade.getType()) {
			case HOOK:
				hookUpgrades.put(level, upgrade);
				break;
			case LINE:
				lineUpgrades.put(level, upgrade);
				break;
			case LUCKY:
				luckyUpgrades.put(level, upgrade);
				break;
			case REEL:
				reelUpgrades.put(level, upgrade);
				break;
			case ROD:
				rodUpgrades.put(level, upgrade);
				break;
		}
	}
	
	public TanalorrUpgrade getUpgradeByLevel(TanalorrUpgradeType type, int level) {
		switch(type) {
			case HOOK:
				return hookUpgrades.get(level);
			case LINE:
				return lineUpgrades.get(level);
			case LUCKY:
				return luckyUpgrades.get(level);
			case REEL:
				return reelUpgrades.get(level);
			case ROD:
				return rodUpgrades.get(level);
			default:
				return null;
		}
	}
	
}
