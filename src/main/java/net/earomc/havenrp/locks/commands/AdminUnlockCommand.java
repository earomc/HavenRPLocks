package net.earomc.havenrp.locks.commands;


import net.earomc.havenrp.locks.ContainerLockManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminUnlockCommand implements CommandExecutor {

    private ContainerLockManager containerLockManager;

    public AdminUnlockCommand(ContainerLockManager containerLockManager) {
        this.containerLockManager = containerLockManager;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (player.hasPermission("chestlocker.adminunlock")) {
                if (args.length == 0) {
                    containerLockManager.setAdminUnlockMode(player);
                } else {
                    player.sendMessage("§cPlease use /adminunlock");
                }
            }
        }
        return true;
    }
}
