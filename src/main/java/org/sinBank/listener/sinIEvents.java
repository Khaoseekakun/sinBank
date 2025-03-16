package org.sinBank.listener;

import com.Zrips.CMI.events.CMIUserBalanceChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@SuppressWarnings("UnstableApiUsage")
public class sinIEvents implements Listener {

    @EventHandler
    public void onCMIPlayerEvent(CMIUserBalanceChangeEvent event){
        // Deposit, Withdraw
        String changeType = event.getActionType();
        if(changeType.equals("Deposit")){

        }

        if(changeType.equals("Withdraw")){

        }
    }
}
