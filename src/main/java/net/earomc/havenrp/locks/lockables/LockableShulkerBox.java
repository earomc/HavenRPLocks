package net.earomc.havenrp.locks.lockables;

import org.bukkit.block.BlockState;
import org.bukkit.block.Lockable;
import org.bukkit.block.ShulkerBox;

public class LockableShulkerBox implements LockableContainer {

    private ShulkerBox shulkerBox;
    public LockableShulkerBox(ShulkerBox shulkerBox) {
        this.shulkerBox = shulkerBox;
    }


    @Override
    public String getName() {
        return "Shulker box";
    }

    @Override
    public Lockable getLockable() {
        return shulkerBox;
    }

    @Override
    public BlockState getState() {
        return shulkerBox;
    }
}
