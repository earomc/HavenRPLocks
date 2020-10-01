package net.earomc.havenrp.locks.lockables;

import org.bukkit.block.ShulkerBox;

public class LockableShulkerBox implements LockableContainer {

    private ShulkerBox shulkerBox;
    public LockableShulkerBox(ShulkerBox shulkerBox) {
        this.shulkerBox = shulkerBox;
    }

    @Override
    public LockResult tryLock(String lock) {
        if (!shulkerBox.isLocked()) {
            lock(lock);
            return LockResult.SUCCESS;
        } else {
            return LockResult.LOCK_ALREADY_SET;
        }
    }

    @Override
    public UnlockResult tryUnlock(String lock) {
        if (isLocked()) {
            if (getLock().equals(lock)) {
                unlock();
                return UnlockResult.SUCCESS;
            } else {
                return UnlockResult.INCORRECT_LOCK;
            }
        } else {
            return UnlockResult.CONTAINER_NOT_LOCKED;
        }
    }

    @Override
    public void lock(String lock) {
        shulkerBox.setLock(lock);
        shulkerBox.update();
    }

    @Override
    public void unlock() {
        shulkerBox.setLock("");
        shulkerBox.update();
    }

    @Override
    public String getName() {
        return "Shulker box";
    }

    @Override
    public String getLock() {
        return shulkerBox.getLock();
    }

    @Override
    public boolean isLocked() {
        return shulkerBox.isLocked();
    }
}
