package net.earomc.havenrp.locks.lockables;

import org.bukkit.block.Dispenser;

public class LockableDispenser implements LockableContainer {

    private Dispenser dispenser;
    public LockableDispenser(Dispenser dispenser) {
        this.dispenser = dispenser;
    }

    @Override
    public LockResult tryLock(String lock) {
        if (!dispenser.isLocked()) {
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
        dispenser.setLock(lock);
        dispenser.update();
    }

    @Override
    public void unlock() {
        dispenser.setLock("");
        dispenser.update();
    }

    @Override
    public String getName() {
        return "Dispenser";
    }

    @Override
    public String getLock() {
        return dispenser.getLock();
    }

    @Override
    public boolean isLocked() {
        return dispenser.isLocked();
    }
}
