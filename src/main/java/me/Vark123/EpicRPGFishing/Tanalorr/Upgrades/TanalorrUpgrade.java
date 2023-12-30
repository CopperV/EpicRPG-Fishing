package me.Vark123.EpicRPGFishing.Tanalorr.Upgrades;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import me.Vark123.EpicRPGFishing.Tanalorr.Upgrades.Costs.ICost;

@Builder
@Getter
@AllArgsConstructor
public class TanalorrUpgrade {

	private String id;
	private int level;
	private TanalorrUpgradeType type;
	
	private String display;
	private String valueDisplay;
	private double value;
	
	private Collection<ICost> costs;
	
}
