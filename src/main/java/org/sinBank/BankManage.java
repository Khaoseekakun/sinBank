package org.sinBank;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.*;

@SuppressWarnings({"deprecation", "DataFlowIssue"})
public class BankManage {

    private static final List<LoanItem> loanItems = new ArrayList<>();
    static DecimalFormat df = new DecimalFormat("#,###.##");
    public static Inventory MainGUI(Player player) {

        String guiName = SinBank.messages.getString("gui.name");
        Inventory mainInv = Bukkit.createInventory(null, 54, Handlers.ChangeColor(guiName));
        List<Map<?, ?>> guiSlot = SinBank.config.getMapList("loan-gui");

        String loan_name = SinBank.messages.getString("gui.loan-detail.name");
        List<String> loan_lore = SinBank.messages.getStringList("gui.loan-detail.lore");

        String loan_accept = SinBank.messages.getString("loan_status.accept");
        String loan_deny = SinBank.messages.getString("loan_status.deny");

        String check_accept = SinBank.messages.getString("check_require.pass");
        String check_deny = SinBank.messages.getString("check_require.dis_pass");


        for (Map<?, ?> itemData : guiSlot) {
            if (itemData.containsKey("item")) {
                Map<?, ?> itemMap = (Map<?, ?>) itemData.get("item");

                int slot = (int) itemMap.get("slot");
                String itemType = itemMap.get("item_type").toString();
                int itemModel = (int) itemMap.get("item_model");

                boolean isSystem = itemMap.containsKey("system") && (boolean) itemMap.get("system");

                String loanId = isSystem ? "type_amount" : itemMap.get("loanId").toString();

                String itemName = itemMap.containsKey("item_name") ? itemMap.get("item_name").toString() : null;

                loanItems.add(new LoanItem(slot, loanId, itemType, itemModel, isSystem, itemName));

                int amount = SinBank.config.getInt("loan-list." + loanId + ".amount");
                double interest_rate = SinBank.config.getDouble("loan-list." + loanId + ".interest_rate");
                int repayment_days = SinBank.config.getInt("loan-list." + loanId + ".repayment_days");
                double penalty_rate = SinBank.config.getDouble("loan-list." + loanId + ".penalty_rate");
                double repayment_total = amount * (interest_rate+1);
                double total_penalty = (penalty_rate+1)*repayment_total;
                int min_income_per_day = SinBank.config.getInt("loan-list." + loanId + ".min_income_per_day");
                int min_account_age = SinBank.config.getInt("loan-list." + loanId + ".min_account_age");
                boolean check_require = true;
                boolean loan_status = true;

                Material material = Material.getMaterial(itemType);

                if (material == null) continue;

                ItemStack new_item = new ItemStack(material, 1);

                NBTItem itemNBT = new NBTItem(new_item);

                itemNBT.setString("loanId", loanId);

                new_item = itemNBT.getItem();
                ItemMeta meta = new_item.getItemMeta();

                meta.setItemName(Handlers.ChangeColor(loan_name.replace("{amount}", df.format(amount))));

                if (itemModel != 0) {
                    meta.setCustomModelData(itemModel);
                }

                if (!isSystem) {
                    List<String> lore_set = new ArrayList<>();

                    for (String lore : loan_lore) {
                        lore = lore.replace("{repayment_days}", String.valueOf(repayment_days));
                        lore = lore.replace("{interest_rate}", df.format(interest_rate*100));
                        lore = lore.replace("{penalty_rate}", df.format(penalty_rate*100));;
                        lore = lore.replace("{repayment_total}", df.format(repayment_total));
                        lore = lore.replace("{penalty_total}", df.format(total_penalty));
                        lore = lore.replace("{min_income_per_day}", df.format(min_income_per_day));
                        lore = lore.replace("{min_account_age}", String.valueOf(min_account_age));
                        lore = lore.replace("{itemName}", String.valueOf(itemName));
                        lore = lore.replace("{amount}", df.format(amount));
                        lore = lore.replace("{loan_status}", loan_status ? loan_accept : loan_deny);
                        lore = lore.replace("{check_require}", check_require ? check_accept : check_deny);
                        lore_set.add(Handlers.ChangeColor(lore));
                    }

                    meta.setLore(lore_set);

                    new_item.setItemMeta(meta);

                    mainInv.setItem(slot, new_item);
                }
            }
        }

        return mainInv;
    }

    public LoanItem getItemByIndex(int index) {
        if (index < 0 || index >= loanItems.size()) return null;
        return loanItems.get(index);
    }

    public LoanItem getItemByLoanId(String loanId) {
        for (LoanItem item : loanItems) {
            if (item.getLoanId() != null && item.getLoanId().equals(loanId)) {
                return item;
            }
        }
        return null;
    }

    public List<LoanItem> getAllItems() {
        return loanItems;
    }

    public static class LoanItem {
        private final int slot;
        private final String loanId;
        private final String itemType;
        private final int itemModel;
        private final boolean isSystem;
        private final String itemName;

        public LoanItem(int slot, String loanId, String itemType, int itemModel, boolean isSystem, String itemName) {
            this.slot = slot;
            this.loanId = loanId;
            this.itemType = itemType;
            this.itemModel = itemModel;
            this.isSystem = isSystem;
            this.itemName = itemName;
        }

        public int getSlot() {
            return slot;
        }

        public String getLoanId() {
            return loanId;
        }

        public String getItemType() {
            return itemType;
        }

        public int getItemModel() {
            return itemModel;
        }

        public boolean isSystem() {
            return isSystem;
        }

        public String getItemName() {
            return itemName;
        }
    }

    public static void OpenMainGUI(Player player) {
        Inventory inv = MainGUI(player);
        player.openInventory(inv);
    }
}
