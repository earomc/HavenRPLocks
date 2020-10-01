package net.earomc.havenrp.locks.lockables;

import org.bukkit.block.Chest;

public class LockableChest implements LockableContainer {

    private Chest chest;

    public LockableChest(Chest chest) {
        this.chest = chest;
    }

    @Override
    public LockResult tryLock(String lock) {
        if (!chest.isLocked()) {
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
        chest.setLock(lock);
        chest.update();

    }

    @Override
    public void unlock() {
        chest.setLock("");
        chest.update();
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

    @Override
    public boolean isLocked() {
        return chest.isLocked();
    }
}
