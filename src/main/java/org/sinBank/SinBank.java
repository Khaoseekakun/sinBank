package org.sinBank;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.Connection;
import java.util.Objects;

import org.sinBank.commands.bank;
import org.sinBank.listener.clickInventory;
import org.sinBank.listener.sinIEvents;

public final class SinBank extends JavaPlugin {
    public static FileConfiguration config;
    public static FileConfiguration messages;
    public static Connection sql;
    public final SQLiteManager sqlManager = new SQLiteManager();
    public File messageFile;
    @Override
    public void onEnable() {
        saveDefaultConfig();
        sqlManager.connect();
        sql = sqlManager.getConnection();

        this.messageFile = new File(getDataFolder(), "messages.yml");
        if (!messageFile.exists()) {
            saveResource("messages.yml", false);
        }
        messages = YamlConfiguration.loadConfiguration(messageFile);
        config = getConfig();

        Bukkit.getPluginManager().registerEvents(new sinIEvents(), this);
        Bukkit.getPluginManager().registerEvents(new clickInventory(), this);

        Objects.requireNonNull(getCommand("bank")).setExecutor(new bank());
    }

    @Override
    public void onDisable() {
        sqlManager.disconnect();
    }
}
