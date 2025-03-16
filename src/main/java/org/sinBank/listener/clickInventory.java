package org.sinBank.listener;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.sinBank.Handlers;
import org.sinBank.SinBank;

public class clickInventory implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        String titleName = SinBank.messages.getString("gui.name");

        if (event.getView().getTitle().equals(Handlers.ChangeColor(titleName))) {
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();
            Inventory clickedInventory = event.getClickedInventory(); // Get clicked inventory

            ItemStack clickedItem = event.getCurrentItem();
            if(clickedItem == null) return;

            if (clickedInventory != null && clickedInventory.equals(player.getInventory())) {
                return;
            }

            NBTItem nbtItem = new NBTItem(clickedItem);
            String checkLoanId = nbtItem.getString("loanId");

            if(checkLoanId.equals("type_amount")){
                return;
            }
        }
    }
}
