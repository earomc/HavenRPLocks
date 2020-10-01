package net.earomc.havenrp.locks.lockables;

import org.bukkit.block.BlockState;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Lockable;

public class LockableDispenser implements LockableContainer {

    private final Dispenser dispenser;
    public LockableDispenser(Dispenser dispenser) {
        this.dispenser = dispenser;
    }

    @Override
    public Lockable getLockable() {
        return dispenser;
    }

    @Override
    public BlockState getState() {
        return dispenser;
    }

    @Override
    public String getName() {
        return "Dispenser";
    }

}
