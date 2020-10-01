package net.earomc.havenrp.locks.commands;

import net.earomc.havenrp.locks.ContainerLockManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LockCommand implements CommandExecutor {

    private ContainerLockManager containerLockManager;

    public LockCommand(ContainerLockManager containerLockManager) {
        this.containerLockManager = containerLockManager;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (args.length == 1) {
                String lock = args[0];
                containerLockManager.setLockMode(player, lock);
            } else {
                player.sendMessage("§cPlease use /lock <lockName>");
            }
        }
        return true;
    }
}
