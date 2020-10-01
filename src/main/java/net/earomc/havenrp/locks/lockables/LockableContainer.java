package net.earomc.havenrp.locks.lockables;

public interface LockableContainer {
    LockResult tryLock(String lock);
    void lock(String lock);
    UnlockResult tryUnlock(String lock);
    void unlock();
    String getName();
    String getLock();
    boolean isLocked();
}
