package dev.lrusian.reclaim.listener;

import dev.lrusian.reclaim.Reclaim;
import dev.lrusian.reclaim.util.CC;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class ReclaimHandler implements Listener {

    private final List<ReclaimData> reclaims;

    public ReclaimHandler() {
        this.reclaims = new ArrayList<>();

        this.loadReclaimData();
    }

    public void disable() {
        this.reclaims.clear();
    }

    private void loadReclaimData() {
        ConfigurationSection section = Reclaim.get().getConfig()
                .getConfigurationSection("REWARDS");

        section.getKeys(false).forEach(key -> {
            ReclaimData reclaim = new ReclaimData();

            reclaim.setRankName(CC.translate(section.getString(key + ".RANK_NAME", "")));
            reclaim.setPermission(section.getString(key + ".PERMISSION"));
            reclaim.setCommands(section.getStringList(key + ".COMMANDS"));

            this.reclaims.add(reclaim);
        });
    }

    private boolean hasReclaimPermission(Player player) {
        return this.reclaims.stream().anyMatch(reclaim -> player.hasPermission(reclaim.getPermission()));
    }

    private ReclaimData getReclaim(Player player) {
        return this.reclaims.stream().filter(reclaim -> player.hasPermission(reclaim.getPermission()))
                .findFirst().orElse(null);
    }

    public void performCommand(Player player) {
        if(!this.hasReclaimPermission(player)) {
            player.sendMessage(CC.translate(Reclaim.get().getConfig().getString("RECLAIM.NO-PERMISSION")));
            return;
        }

        if (Reclaim.get().getConfig().contains("DATA." + player.getUniqueId())) {
            player.sendMessage(CC.translate(Reclaim.get().getConfig().getString("RECLAIM.ALREADY_USED")));
            return;
        }

        Reclaim.get().getConfig().set("DATA." + player.getUniqueId(), true);

        ReclaimData reclaim = this.getReclaim(player);

        reclaim.getCommands().forEach(command -> Bukkit.dispatchCommand(Bukkit
                .getConsoleSender(), command.replace("<player>", player.getName())
                .replace("<rank>", reclaim.getRankName())));


        if (Reclaim.get().getConfig().getBoolean("RECLAIM.BROADCAST.ENABLED")) {
            for (String reclaimmessage : Reclaim.get().getConfig().getStringList("RECLAIM.BROADCAST.MESSAGE")) {

                reclaimmessage = reclaimmessage.replace("<rank>", reclaim.getRankName());
                reclaimmessage = reclaimmessage.replace("<player>", player.getName());


                Bukkit.getConsoleSender().getServer().broadcastMessage(CC.translate(reclaimmessage));
            }
        }

        if (Reclaim.get().getConfig().getBoolean("RECLAIM.PLAYER.ENABLED")) {
            for (String reclaimmessage : Reclaim.get().getConfig().getStringList("RECLAIM.PLAYER.MESSAGE")) {

                reclaimmessage = reclaimmessage.replace("<rank>", reclaim.getRankName());
                reclaimmessage = reclaimmessage.replace("<player>", player.getName());


                player.sendMessage(CC.translate(reclaimmessage));
            }
        }
        Reclaim.get().saveConfig();
    }

    @Getter
    @Setter
    private static class ReclaimData {

        private String rankName;
        private String permission;
        private List<String> commands;
    }
}
