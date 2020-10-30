package net.earomc.havenrp.locks.util;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SimpleSlot {
    private ItemStack itemStack;
    private int id;
    private Inventory inventory;

    public SimpleSlot(ItemStack itemStack, int id, Inventory inventory) {
        this.itemStack = itemStack;
        this.id = id;
        this.inventory = inventory;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public int getId() {
        return id;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
