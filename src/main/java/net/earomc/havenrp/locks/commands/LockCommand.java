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
        commandSender.sendMessage("This command is not correctly implemented yet.");
        //rework it so that it will lock the container and then give you a lock.
        return true;
    }
}
