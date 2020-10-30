package net.earomc.havenrp.locks;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import net.earomc.havenrp.locks.util.UUIDDataType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class Lock {

    private UUID uuid;
    private ItemStack lockItem;
    private ItemStack keyItem;
    private static ItemStack DEFAULT_LOCK_ITEM;
    private static ItemStack DEFAULT_KEY_ITEM;

    private static final int LOCK_CUSTOM_MODEL_DATA = 452920;
    private static final int KEY_CUSTOM_MODEL_DATA = 452921;

    static {
        Lock lock = new Lock();
        DEFAULT_LOCK_ITEM = lock.getLockItem();
        DEFAULT_KEY_ITEM = lock.getKeyItem();
    }
    public Lock(UUID uuid) {
        this.uuid = uuid;
    }

    public Lock() {
        this(UUID.randomUUID());
    }


    public void giveKey(Player player) {
        player.getInventory().addItem(keyItem);
    }

    public static ItemStack getDefaultLockItem() {
        return DEFAULT_LOCK_ITEM;
    }

    public static ItemStack getDefaultKeyItem() {
        return DEFAULT_KEY_ITEM;
    }

    public ItemStack getLockItem() {
        if (lockItem == null) {
            this.lockItem = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta lockMeta = this.lockItem.getItemMeta();
            if (lockMeta == null) return null;
            lockMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
            NBTEditor.set(lockItem, uuid.toString(), "lockUUID");
            lockMeta.setDisplayName("§7§lLock");



            NamespacedKey lockUUIDKey = new NamespacedKey(HavenRPLocks.getInstance(), "lockUUID");
            lockMeta.getPersistentDataContainer().set(lockUUIDKey, UUIDDataType.INSTANCE, uuid);
            Bukkit.broadcastMessage("lockItem UUID: " + lockMeta.getPersistentDataContainer().get(lockUUIDKey, UUIDDataType.INSTANCE));

            //Lock Model
            lockMeta.setCustomModelData(LOCK_CUSTOM_MODEL_DATA);
            NBTEditor.set(lockItem, (byte) 1, "lock");

            lockItem.setItemMeta(lockMeta);
        }
        return lockItem;
    }

    public ItemStack getKeyItem() {
        if (keyItem == null) {
            //Key Item
            this.keyItem = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta keyMeta = this.keyItem.getItemMeta();
            if (keyMeta == null) return null;
            keyMeta.setDisplayName("§7§lKey");
            keyMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
            NamespacedKey keyUUIDKey = new NamespacedKey(HavenRPLocks.getInstance(), "keyUUID");
            keyMeta.getPersistentDataContainer().set(keyUUIDKey, UUIDDataType.INSTANCE, uuid);

            Bukkit.broadcastMessage("keyItem UUID: " + keyMeta.getPersistentDataContainer().get(keyUUIDKey, UUIDDataType.INSTANCE));
            //Key Model
            keyMeta.setCustomModelData(KEY_CUSTOM_MODEL_DATA);
            NBTEditor.set(keyItem, (byte) 1, "key");

            keyItem.setItemMeta(keyMeta);
        }
        return keyItem;
    }

    public UUID getUuid() {
        return uuid;
    }

    public static Lock getFromItem(ItemStack itemStack) {
        if (itemStack == null) return null;
        Lock lock = null;
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return null;
        UUID lockUUID = itemMeta.getPersistentDataContainer().get(new NamespacedKey(HavenRPLocks.getInstance(), "lockUUID"), UUIDDataType.INSTANCE);
        UUID keyUUID = itemMeta.getPersistentDataContainer().get(new NamespacedKey(HavenRPLocks.getInstance(), "keyUUID"), UUIDDataType.INSTANCE);

        if (lockUUID != null && keyUUID != null) throw new IllegalStateException("Can't have item with keyUUID and lockUUID");

        if (lockUUID != null) {
            lock = new Lock(lockUUID);
        }
        if (keyUUID != null) {
            lock = new Lock(keyUUID);
        }

        return lock;
    }
}
