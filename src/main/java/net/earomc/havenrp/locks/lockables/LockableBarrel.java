package net.earomc.havenrp.locks.lockables;

import org.bukkit.block.Barrel;
import org.bukkit.block.BlockState;
import org.bukkit.block.Lockable;

public class LockableBarrel implements LockableContainer {

    private final Barrel barrel;

    public LockableBarrel(Barrel chest) {
        this.barrel = chest;
    }

    @Override
    public String getName() {
        return "Barrel";
    }

    public Barrel getBarrel() {
        return barrel;
    }

    @Override
    public Lockable getLockable() {
        return barrel;
    }

    @Override
    public BlockState getState() {
        return barrel;
    }
}
