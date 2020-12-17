package net.earomc.havenrp.locks;

import net.earomc.havenrp.locks.util.SimpleSlot;
import net.earomc.havenrp.locks.util.UUIDDataType;
import org.bukkit.Bukkit;
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

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.UUID;

public class CraftListener implements Listener {

    private HashMap<Player, SimpleSlot> playerToSlotMap;

    public CraftListener() {
        playerToSlotMap = new HashMap<>();
    }

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
                    LockKeyPair lockKeyPair = new LockKeyPair(uuid);
                    player.getInventory().addItem(lockKeyPair.getKeyItem());
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
            if (result.isSimilar(LockKeyPair.getDefaultLockItem())) {
                LockKeyPair lockKeyPair = new LockKeyPair();
                event.getInventory().setResult(lockKeyPair.getLockItem());

            }
        }

    }

    @EventHandler
    public void onPrepareCraftingKey(PrepareItemCraftEvent event) {
        CraftingInventory inventory = event.getInventory();
        ItemStack result = inventory.getResult();
        if (result == null) return;
        if (result.isSimilar(LockKeyPair.getDefaultKeyItem())) {
            InventoryHolder holder = inventory.getHolder();
            ItemStack[] matrix = inventory.getMatrix();


            if (holder instanceof Player) {
                Player player = (Player) holder;
                SimpleSlot slotWithLockItem = getSlotWithLockItem(inventory);

                LockKeyPair lockKeyPair = null;

                if (slotWithLockItem != null) {
                    lockKeyPair = LockKeyPair.getFromItem(slotWithLockItem.getItemStack());
                    inventory.setResult(lockKeyPair.getKeyItem());
                    playerToSlotMap.put(player, slotWithLockItem);
                } else {
                    inventory.setResult(null);
                    playerToSlotMap.remove(player);
                }
            }
        }
    }



    @Nullable
    private SimpleSlot getSlotWithLockItem(CraftingInventory craftingInventory) {
        LockKeyPair lockKeyPairFromItem = null;
        ItemStack lockItem = null;
        int lockSlot = -1;
        ItemStack[] matrix = craftingInventory.getMatrix();
        for (int i = 0; i < matrix.length; i++) {
            ItemStack current = matrix[i];
            lockKeyPairFromItem = LockKeyPair.getFromItem(current);
            if (lockKeyPairFromItem != null) {
                lockSlot = i;
                Bukkit.broadcastMessage("Lock slot: " + lockSlot);
                lockItem = current;
                break;
            }
        }
        if (lockSlot != -1) {
            return new SimpleSlot(lockItem, lockSlot, craftingInventory);
        }
        return null;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        //event.getWhoClicked().sendMessage("raw: " + event.getRawSlot() + ", not raw:" + event.getSlot() + ", slottype: " + event.getSlotType());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        LockKeyPair lockKeyPair = new LockKeyPair();
        PlayerInventory inventory = event.getPlayer().getInventory();
        if (message.equalsIgnoreCase("lockpls")) {
            inventory.addItem(lockKeyPair.getLockItem());
        } else if (message.equalsIgnoreCase("keypls")) {
            inventory.addItem(lockKeyPair.getKeyItem());
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
