package net.earomc.havenrp.locks;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.UUID;

public class Lock {

    private UUID uuid;
    private ItemStack lockItem;
    private ItemStack keyItem;

    public Lock() {
        this.uuid = UUID.randomUUID();
        this.lockItem = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta itemMeta = this.lockItem.getItemMeta();
        if (itemMeta == null) return;
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        itemMeta.setLore(Collections.singletonList(uuid.toString()));

        this.keyItem = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta keyMeta = this.keyItem.getItemMeta();
        if (keyMeta == null) return;
        keyMeta.setDisplayName("§7§lKey");
        keyMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        keyMeta.setLore(Collections.singletonList(uuid.toString()));


        keyItem.setItemMeta(keyMeta);
        lockItem.setItemMeta(itemMeta);
    }



    public void giveKey(Player player) {

    }
}
