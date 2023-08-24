package me.Vark123.EpicRPGFishing.QTESystem.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;

import me.Vark123.EpicRPGFishing.QTESystem.QTEHolder;
import me.Vark123.EpicRPGFishing.QTESystem.QTEManager;

public class QTEInvCloseListener implements Listener {

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		InventoryHolder holder = e.getView().getTopInventory().getHolder();
		if(!(holder instanceof QTEHolder))
			return;
		QTEHolder qteHoler = (QTEHolder) holder;
		QTEManager.get().cancelQTE(qteHoler);
	}
	
}
