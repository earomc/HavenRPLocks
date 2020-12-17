package net.earomc.havenrp.locks;


import net.earomc.havenrp.locks.commands.AdminUnlockCommand;
import net.earomc.havenrp.locks.commands.LockCommand;
import net.earomc.havenrp.locks.commands.TestCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class HavenRPLocks extends JavaPlugin {

    private static HavenRPLocks instance;
    private ContainerLockManager containerLockManager;

    public static HavenRPLocks getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        this.containerLockManager = new ContainerLockManager();
        registerEvents(containerLockManager, new CraftListener());
        registerCommands();
        registerRecipes();
    }

    private void registerRecipes() {

        ShapedRecipe recipeLock = new ShapedRecipe(new NamespacedKey(this, "lock"), LockKeyPair.getDefaultLockItem());
        recipeLock.shape(
                "#n#",
                "n#n",
                "#i#");
        recipeLock.setIngredient('#', Material.AIR);
        recipeLock.setIngredient('n', Material.IRON_NUGGET);
        recipeLock.setIngredient('i', Material.IRON_INGOT);
        Bukkit.getServer().addRecipe(recipeLock);

        ShapelessRecipe recipeKey = new ShapelessRecipe(new NamespacedKey(this, "key"), LockKeyPair.getDefaultKeyItem());
        recipeKey.addIngredient(Material.IRON_NUGGET);
        recipeKey.addIngredient(Material.ENCHANTED_BOOK);
        Bukkit.getServer().addRecipe(recipeKey);
    }

    private void registerEvents(Listener... listeners) {
        PluginManager pluginManager = Bukkit.getPluginManager();
        for (Listener listener : listeners) {
            pluginManager.registerEvents(listener, this);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void registerCommands() {
        getCommand("lock").setExecutor(new LockCommand(containerLockManager));
        getCommand("adminunlock").setExecutor(new AdminUnlockCommand(containerLockManager));
        getCommand("testi").setExecutor(new TestCommand());
    }
}
