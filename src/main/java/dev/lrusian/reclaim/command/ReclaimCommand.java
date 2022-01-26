package dev.lrusian.reclaim.command;

import dev.lrusian.reclaim.Reclaim;
import dev.lrusian.reclaim.listener.ReclaimHandler;
import lombok.AllArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class ReclaimCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return false;
        Player player = (Player)sender;
        if(args.length == 0){
            new ReclaimHandler().performCommand(player);
            //Reclaim.get().getReclaimHandler().performCommand(player);
            return false;
        }


        return false;
    }

}