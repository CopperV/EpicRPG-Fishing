package me.Vark123.EpicRPGFishing.QTESystem.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import me.Vark123.EpicRPGFishing.QTESystem.QTEHolder;
import me.Vark123.EpicRPGFishing.QTESystem.QTEManager;

public class QTEInvClickListener implements Listener {
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.isCancelled())
			return;
		
		Inventory inv = e.getView().getTopInventory();
		if(e.getClickedInventory() == null 
				|| !e.getClickedInventory().equals(inv))
			return;
		
		InventoryHolder holder = e.getView().getTopInventory().getHolder();
		if(!(holder instanceof QTEHolder))
			return;
		
		e.setCancelled(true);
		
		ItemStack it = e.getCurrentItem();
		if(it == null)
			return;
		
		QTEHolder qteHolder = (QTEHolder) holder;
		if(!it.equals(qteHolder.getCorrect())) {
			QTEManager.get().cancelQTE(qteHolder);
			return;
		}

		double avrTime = (qteHolder.getReflex() - qteHolder.getTimer()) / 20.0;
		qteHolder.addAvrTime(avrTime);
		qteHolder.getVariance().add(avrTime);
		qteHolder.setProgress(qteHolder.getProgress()+1);
		if(qteHolder.getProgress() <= qteHolder.getComplexity()) {
			QTEManager.get().generateQTE(qteHolder);
			return;
		}
		QTEManager.get().finishQTE(qteHolder);
	}

}
