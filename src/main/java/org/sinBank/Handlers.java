package org.sinBank;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

@SuppressWarnings("deprecation")
public class Handlers {
    public static void SendMessage(Player player, String message){
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f"+message));
    }

    public static String ChangeColor(String message){
        return ChatColor.translateAlternateColorCodes('&',"&f"+message);
    }

    public static void LoadConfig(){
        File configFile = new File(Bukkit.getPluginsFolder(), "config.yml");
        SinBank.config = YamlConfiguration.loadConfiguration(configFile);
        File configMessage = new File(Bukkit.getPluginsFolder(), "messages.yml");
        SinBank.messages = YamlConfiguration.loadConfiguration(configMessage);
    }
}
