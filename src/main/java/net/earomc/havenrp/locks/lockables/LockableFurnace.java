package net.earomc.havenrp.locks.lockables;

import org.bukkit.block.BlockState;
import org.bukkit.block.Furnace;
import org.bukkit.block.Lockable;

public class LockableFurnace implements LockableContainer {

    private Furnace furnace;

    public LockableFurnace(Furnace furnace) {
        this.furnace = furnace;
    }

    @Override
    public Lockable getLockable() {
        return furnace;
    }

    @Override
    public BlockState getState() {
        return furnace;
    }

    @Override
    public String getName() {
        return "Furnace";
    }

}
