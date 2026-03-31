package me.veqtora.featherUtils;

import net.digitalingot.feather.serverapi.api.event.player.PlayerHelloEvent;
import net.digitalingot.feather.serverapi.api.meta.DiscordActivity;
import net.digitalingot.feather.serverapi.api.meta.MetaService;
import net.digitalingot.feather.serverapi.api.meta.ServerListBackground;
import net.digitalingot.feather.serverapi.api.meta.exception.ServerListBackgroundException;
import net.digitalingot.feather.serverapi.api.player.FeatherPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

public final class FeatherUtils extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadServerListBackground();

        Bukkit.getPluginManager().registerEvents(this, this);

        getLogger().info("FeatherUtils Integration enabled.");

        // Feather client event (still useful for logs)
        net.digitalingot.feather.serverapi.api.FeatherAPI.getEventService().subscribe(PlayerHelloEvent.class, (event) -> {
            FeatherPlayer featherPlayer = event.getPlayer();
            String playerName = featherPlayer.getName();

            getLogger().info(playerName + " is using Feather!");

            // optional join message
            FileConfiguration config = getConfig();
            if (config.getBoolean("join-message.enabled", true)) {

                String rawMessage = config.getString("join-message.message", "&a%player%&7 joined with Feather!");
                String formatted = rawMessage.replace("%player%", playerName).replace("&", "§");

                String permission = config.getString("join-message.permission");

                for (Player online : Bukkit.getOnlinePlayers()) {
                    if (permission == null || permission.isBlank() || online.hasPermission(permission)) {
                        online.sendMessage(formatted);
                    }
                }
            }

            // update Discord once for safety
            updateDiscordActivity();
        });

        // keep Discord synced every 5 seconds
        Bukkit.getScheduler().runTaskTimer(this, this::updateDiscordActivity, 20L, 100L);
    }

    @Override
    public void onDisable() {
        getLogger().info("FeatherUtils disabled.");
    }


    public void updateDiscordActivity() {
        FileConfiguration config = getConfig();

        int online = Bukkit.getOnlinePlayers().size();
        int max = Bukkit.getMaxPlayers();

        String state = config.getString("discord-activity.state", "%player_count%/%max_players% Players")
                .replace("%player_count%", String.valueOf(online))
                .replace("%max_players%", String.valueOf(max));

        String details = config.getString("discord-activity.details", "")
                .replace("%player_count%", String.valueOf(online))
                .replace("%max_players%", String.valueOf(max));

        DiscordActivity activity = DiscordActivity.builder()
                .withImage(config.getString("discord-activity.image"))
                .withImageText(config.getString("discord-activity.image-text"))
                .withState(state)
                .withDetails(details)
                .build();

        for (FeatherPlayer fp : net.digitalingot.feather.serverapi.api.FeatherAPI.getPlayerService().getPlayers()) {
            MetaService meta = net.digitalingot.feather.serverapi.api.FeatherAPI.getMetaService();
            meta.updateDiscordActivity(fp, activity);
        }
    }

    // update instantly when players join/leave (faster sync)
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Bukkit.getScheduler().runTaskLater(this, this::updateDiscordActivity, 20L);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Bukkit.getScheduler().runTaskLater(this, this::updateDiscordActivity, 20L);
    }

    private void loadServerListBackground() {
        Path backgroundsDir = getDataFolder().toPath().resolve("backgrounds");

        try {
            Files.createDirectories(backgroundsDir);
        } catch (IOException e) {
            getLogger().severe("Could not create backgrounds directory.");
            e.printStackTrace();
            return;
        }

        String backgroundFile = getConfig().getString("background", "server-background.png");
        Path imagePath = backgroundsDir.resolve(backgroundFile);

        if (!Files.exists(imagePath, new LinkOption[0])) {
            getLogger().warning("Background file not found: " + backgroundFile);
            return;
        }

        try {
            MetaService metaService = net.digitalingot.feather.serverapi.api.FeatherAPI.getMetaService();
            ServerListBackground background =
                    metaService.getServerListBackgroundFactory().byPath(imagePath);

            metaService.setServerListBackground(background);

            getLogger().info("Feather server list background applied.");
        } catch (IOException | ServerListBackgroundException e) {
            getLogger().severe("Failed to load server background:");
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        String prefix = getConfig().getString("prefix", "&6[FeatherUtils] &f")
                .replace("&", "§");

        if (!command.getName().equalsIgnoreCase("FeatherUtils")) {
            return false;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(prefix + "Commands:");
            sender.sendMessage("§e/featherutils reload §7- reload config + background");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            reloadConfig();
            loadServerListBackground();
            sender.sendMessage(prefix + "§aReloaded.");
            return true;
        }

        sender.sendMessage(prefix + "§cUnknown command.");
        return true;
    }
}

