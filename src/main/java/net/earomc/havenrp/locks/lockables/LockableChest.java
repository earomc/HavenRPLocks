package net.earomc.havenrp.locks.lockables;

import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Lockable;

public class LockableChest implements LockableContainer {

    private final Chest chest;

    public LockableChest(Chest chest) {
        this.chest = chest;
    }

    @Override
    public BlockState getState() {
        return chest;
    }

    @Override
    public Lockable getLockable() {
        return chest;
    }

    @Override
    public String getName() {
        return "Chest";
    }

    @Override
    public String getLock() {
        return chest.getLock();
    }

    public Chest getChest() {
        return chest;
    }
}
