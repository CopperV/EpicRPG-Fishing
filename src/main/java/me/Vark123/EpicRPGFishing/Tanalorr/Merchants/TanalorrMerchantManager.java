package me.Vark123.EpicRPGFishing.Tanalorr.Merchants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import lombok.Getter;

@Getter
public final class TanalorrMerchantManager {

	private static final TanalorrMerchantManager inst = new TanalorrMerchantManager();
	
	private final Collection<TanalorrMerchant> merchants;
	
	private TanalorrMerchantManager() {
		merchants = new ArrayList<>();
	}
	
	public static final TanalorrMerchantManager get() {
		return inst;
	}
	
	public void registerMerchant(TanalorrMerchant merchant) {
		merchants.add(merchant);
	}
	
	public Optional<TanalorrMerchant> getMerchantById(String id) {
		return merchants.stream()
				.filter(merchant -> merchant.getId().equals(id))
				.findAny();
	}
	
}
