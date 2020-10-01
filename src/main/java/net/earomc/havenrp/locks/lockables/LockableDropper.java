package net.earomc.havenrp.locks.lockables;

import org.bukkit.block.BlockState;
import org.bukkit.block.Dropper;
import org.bukkit.block.Lockable;

public class LockableDropper implements LockableContainer {

    private final Dropper dropper;

    public LockableDropper(Dropper dropper) {
        this.dropper = dropper;
    }

    @Override
    public Lockable getLockable() {
        return dropper;
    }

    @Override
    public BlockState getState() {
        return dropper;
    }

    @Override
    public String getName() {
        return "Dropper";
    }
}
