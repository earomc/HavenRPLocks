package net.earomc.havenrp.locks.lockables;

import org.bukkit.block.Barrel;

public class LockableBarrel implements LockableContainer {

    private Barrel barrel;

    public LockableBarrel(Barrel chest) {
        this.barrel = chest;
    }


    @Override
    public LockResult tryLock(String lock) {
        if (!barrel.isLocked()) {
            lock(lock);
            return LockResult.SUCCESS;
        } else {
            return LockResult.LOCK_ALREADY_SET;
        }
    }

    @Override
    public UnlockResult tryUnlock(String lock) {
        if (barrel.isLocked()) {
            String currentLock = barrel.getLock();
            if (currentLock.equals(lock)) {
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
        barrel.setLock(lock);
        barrel.update();
    }

    @Override
    public void unlock() {
        barrel.setLock("");
        barrel.update();
    }

    @Override
    public String getName() {
        return "Barrel";
    }

    @Override
    public String getLock() {
        return barrel.getLock();
    }

    @Override
    public boolean isLocked() {
        return barrel.isLocked();
    }

    public Barrel getBarrel() {
        return barrel;
    }
}
