package net.earomc.havenrp.locks;

import net.earomc.havenrp.locks.util.UUIDDataType;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class CraftListener implements Listener {


    public CraftListener() {}

    @EventHandler
    public void onCraft(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        if (event.getSlotType() == InventoryType.SlotType.RESULT) {
            Player player = (Player) event.getWhoClicked();
            ItemMeta itemMeta = event.getCurrentItem().getItemMeta();
            if (itemMeta == null) return;
            NamespacedKey lockUUIDKey = new NamespacedKey(HavenRPLocks.getInstance(), "lockUUID");
            UUID uuid = itemMeta.getPersistentDataContainer().get(lockUUIDKey, UUIDDataType.INSTANCE);
            if (uuid != null) {
                if (player.getInventory().firstEmpty() == -1) {
                    event.setCancelled(true);
                    player.sendMessage("§cYou need space in your inventory for the key to craft a lock!");
                } else {
                    Lock lock = new Lock(uuid);
                    player.getInventory().addItem(lock.getKeyItem());
                }
            }
        }
    }

    @EventHandler
    public void onPrepareCraftingLock(PrepareItemCraftEvent event) {
        ItemStack result = event.getInventory().getResult();
        InventoryHolder holder = event.getInventory().getHolder();
        if (result == null) return;
        if (holder instanceof Player) {
            Player player = (Player) holder;
            if (result.isSimilar(Lock.getDefaultLockItem())) {
                Lock lock = new Lock();
                event.getInventory().setResult(lock.getLockItem());

            }
        }

    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        //event.getWhoClicked().sendMessage("raw: " + event.getRawSlot() + ", not raw:" + event.getSlot() + ", slottype: " + event.getSlotType());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        Lock lock = new Lock();
        PlayerInventory inventory = event.getPlayer().getInventory();
        if (message.equalsIgnoreCase("lockpls")) {
            inventory.addItem(lock.getLockItem());
        } else if (message.equalsIgnoreCase("keypls")) {
            inventory.addItem(lock.getKeyItem());
        }

        if (message.equalsIgnoreCase("item")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    ItemStack item = inventory.getItem(0);
                    if (item == null) return;
                    event.getPlayer().sendMessage(item.toString());
                }
            }.runTaskTimer(HavenRPLocks.getInstance(), 0, 1);
        }
    }
}
