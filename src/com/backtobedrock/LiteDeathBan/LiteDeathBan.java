package com.backtobedrock.LiteDeathBan;

import java.io.File;
import com.backtobedrock.LiteDeathBan.eventHandlers.LiteDeathBanEventHandlers;
import com.backtobedrock.LiteDeathBan.helperClasses.UpdateChecker;
import com.backtobedrock.LiteDeathBan.runnables.PartsOnPlaytime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class LiteDeathBan extends JavaPlugin implements Listener {

    private boolean oldVersion = false;

    private LiteDeathBanConfig config;
    private LiteDeathBanMessages messages;
    private LiteDeathBanCommands commands;

    private final List<UUID> usedRevive = new ArrayList<>();
    private final TreeMap<UUID, Integer> tagList = new TreeMap<>();
    private final TreeMap<UUID, BossBar> bars = new TreeMap<>();
    private final TreeMap<UUID, String> confirmationList = new TreeMap<>();
    private final TreeMap<UUID, Integer> confirmationRunners = new TreeMap<>();
    private final TreeMap<UUID, Long> playtimeLastLifeOnlinePlayers = new TreeMap<>();

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        File dir = new File(this.getDataFolder() + "/userdata");
        dir.mkdirs();

        this.config = new LiteDeathBanConfig(this);
        this.messages = new LiteDeathBanMessages(this);
        this.commands = new LiteDeathBanCommands(this);

        if (this.getLDBConfig().isUpdateChecker()) {
            new UpdateChecker(this, 71483).getVersion(version -> {
                this.oldVersion = !this.getDescription().getVersion().equalsIgnoreCase(version);
            });
        }

        getServer().getPluginManager().registerEvents(new LiteDeathBanEventHandlers(this), this);

        if (this.getLDBConfig().isGetPartOfLifeOnPlaytime()) {
            new PartsOnPlaytime(this).runTaskTimer(this, 0, this.getLDBConfig().getPlaytimeCheck() * 60 * 20);
        }

        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        return this.commands.onCommand(cs, cmnd, alias, args);
    }

    public LiteDeathBanConfig getLDBConfig() {
        return config;
    }

    public LiteDeathBanMessages getMessages() {
        return this.messages;
    }

    public void addToTagList(UUID plyrID, int id) {
        this.tagList.put(plyrID, id);

    }

    public void removeFromTagList(UUID plyrID) {
        this.tagList.remove(plyrID);
        if (this.config.getCombatTagWarningStyle().equalsIgnoreCase("bossbar")) {
            BossBar bar = this.bars.remove(plyrID);
            bar.setVisible(false);
        }
    }

    public int getFromTagList(UUID plyrID) {
        return this.tagList.get(plyrID);
    }

    public boolean doesTagListContain(UUID plyrID) {
        return this.tagList.containsKey(plyrID);
    }

    public void addBar(UUID id, BossBar bar) {
        this.bars.put(id, bar);
    }

    public void addToConfirmation(UUID plyrID, String name, int id) {
        this.confirmationList.put(plyrID, name);
        this.confirmationRunners.put(plyrID, id);

    }

    public void removeFromConfirmation(UUID plyrID) {
        this.confirmationList.remove(plyrID);
        this.confirmationRunners.remove(plyrID);
    }

    public String getFromConfirmation(UUID plyrID) {
        return this.confirmationList.get(plyrID);
    }

    public boolean doesConfirmationContain(UUID plyrID) {
        return this.confirmationList.containsKey(plyrID);
    }

    public void addToUsedRevive(UUID plyrID) {
        this.usedRevive.add(plyrID);
    }

    public void removeFromUsedRevive(UUID plyrID) {
        this.usedRevive.remove(plyrID);
    }

    public boolean doesUsedReviveContain(UUID plyrID) {
        return this.usedRevive.contains(plyrID);
    }

    public void addToPlaytimeLastLifeOnlinePlayers(UUID plyrID, long playtime) {
        this.playtimeLastLifeOnlinePlayers.put(plyrID, playtime);
    }

    public void removeFromPlaytimeLastLifeOnlinePlayers(UUID plyrID) {
        this.playtimeLastLifeOnlinePlayers.remove(plyrID);
    }

    public long getFromPlaytimeLastLifeOnlinePlayers(UUID plyrID) {
        return this.playtimeLastLifeOnlinePlayers.get(plyrID);
    }

    public boolean isOldVersion() {
        return this.oldVersion;
    }
}
