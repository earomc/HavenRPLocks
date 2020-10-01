package net.earomc.havenrp.locks.lockables;

import org.bukkit.block.Dropper;

public class LockableDropper implements LockableContainer {

    private Dropper dropper;

    public LockableDropper(Dropper dropper) {
        this.dropper = dropper;
    }

    @Override
    public LockResult tryLock(String lock) {
        if (!dropper.isLocked()) {
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
        dropper.setLock(lock);
        dropper.update();
    }


    @Override
    public void unlock() {
        dropper.setLock("");
        dropper.update();
    }

    @Override
    public String getName() {
        return "Dropper";
    }

    @Override
    public String getLock() {
        return dropper.getLock();
    }

    @Override
    public boolean isLocked() {
        return dropper.isLocked();
    }
}
