package net.earomc.havenrp.locks.lockables;

import org.bukkit.block.Furnace;

public class LockableFurnace implements LockableContainer {

    private Furnace furnace;

    public LockableFurnace(Furnace furnace) {
        this.furnace = furnace;
    }

    @Override
    public LockResult tryLock(String lock) {
        if (!furnace.isLocked()) {
            lock(lock);
            return LockResult.SUCCESS;
        } else {
            return LockResult.LOCK_ALREADY_SET;
        }
    }

    @Override
    public void lock(String lock) {
        furnace.setLock(lock);
        furnace.update();
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
    public void unlock() {
        furnace.setLock("");
        furnace.update();
    }

    @Override
    public String getName() {
        return "Furnace";
    }

    @Override
    public String getLock() {
        return furnace.getLock();
    }

    @Override
    public boolean isLocked() {
        return furnace.isLocked();
    }
}
