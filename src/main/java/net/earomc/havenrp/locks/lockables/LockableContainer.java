package net.earomc.havenrp.locks.lockables;

import org.bukkit.block.BlockState;
import org.bukkit.block.Lockable;

public interface LockableContainer {
    default void lock(String lock) {
        getLockable().setLock(lock);
        getState().update();
    }
    default void unlock() {
        getLockable().setLock("");
        getState().update();
    }

    default LockResult tryLock(String lock) {
        if (!getLockable().isLocked()) {
            lock(lock);
            return LockResult.SUCCESS;
        } else {
            return LockResult.LOCK_ALREADY_SET;
        }
    }

    default UnlockResult tryUnlock(String lock) {
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

    default String getLock() {
        return getLockable().getLock();
    }
    default boolean isLocked() {
        return getLockable().isLocked();
    }

    Lockable getLockable();
    BlockState getState();
    String getName();


}
