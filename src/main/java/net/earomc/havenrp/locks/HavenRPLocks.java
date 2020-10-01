package net.earomc.havenrp.locks;


import net.earomc.havenrp.locks.commands.AdminUnlockCommand;
import net.earomc.havenrp.locks.commands.LockCommand;
import net.earomc.havenrp.locks.commands.UnlockCommand;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class HavenRPLocks extends JavaPlugin {

    @Override
    public void onEnable() {
        instance = this;
        this.containerLockManager = new ContainerLockManager();
        registerEvents(containerLockManager);
        registerCommands();
    }

    public static HavenRPLocks getInstance() {
        return instance;
    }

    private static HavenRPLocks instance;

    private ContainerLockManager containerLockManager;

    private void registerEvents(Listener... listeners) {
        PluginManager pluginManager = Bukkit.getPluginManager();
        for (Listener listener : listeners) {
            pluginManager.registerEvents(listener, this);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void registerCommands() {
        getCommand("lock").setExecutor(new LockCommand(containerLockManager));
        getCommand("unlock").setExecutor(new UnlockCommand(containerLockManager));
        getCommand("adminunlock").setExecutor(new AdminUnlockCommand(containerLockManager));
    }
}
