package me.Vark123.EpicRPGFishing.Tanalorr.Merchants;

import java.util.Collection;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import me.Vark123.EpicRPGFishing.Tanalorr.Merchants.Prices.IPrice;

@Getter
@AllArgsConstructor
@Builder
public class TanalorrMerchant {

	private String id;
	private String display;
	
	private Map<String, Collection<IPrice>> offers;
	
}
