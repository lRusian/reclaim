package dev.lrusian.reclaim;

import dev.lrusian.reclaim.command.ReclaimCommand;
import dev.lrusian.reclaim.listener.ReclaimHandler;
import dev.lrusian.reclaim.util.FileConfig;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Reclaim extends JavaPlugin {

    FileConfig defaultConfig;
    ReclaimHandler reclaimHandler;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        loadConfigs();

        try {
            getServer().getPluginManager().registerEvents(new ReclaimHandler(), this);
            this.getCommand("reclaim").setExecutor(new ReclaimCommand());
        } catch(Exception e) {
            e.printStackTrace();
            //Bukkit.getPluginManager().disablePlugin(this);
        }


    }


    @Override
    public void onDisable() {
    }


    private void loadConfigs() {
        this.defaultConfig = new FileConfig(this, "config.yml");
    }

    public static Reclaim get() {
        return Reclaim.getPlugin(Reclaim.class);
    }

}
