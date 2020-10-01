package net.earomc.havenrp.locks.lockables;

public class LockableDoubleChest implements LockableContainer {

    private LockableChest chestLeft;
    private LockableChest chestRight;

    public LockableDoubleChest(LockableChest chestLeft, LockableChest chestRight) {
        this.chestLeft = chestLeft;
        this.chestRight = chestRight;
        //Bukkit.broadcastMessage(this.toString());
    }


    private LockableChest getOtherChest(LockableChest chest) {
        if (chest.equals(chestLeft)) {
            return chestRight;
        } else if (chest.equals(chestRight)) {
            return chestLeft;
        }
        else return null;
    }

    @Override
    public LockResult tryLock(String lock) {
        if (!isLocked()) {
            this.lock(lock);
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
        chestLeft.lock(lock);
        chestRight.lock(lock);
        //Bukkit.broadcastMessage("Double chest locked!!!!");
    }

    @Override
    public void unlock() {
        chestLeft.unlock();
        chestRight.unlock();
    }

    @Override
    public String getName() {
        return "Double chest";
    }

    public LockableChest getChestLeft() {
        return chestLeft;
    }

    public LockableChest getChestRight() {
        return chestRight;
    }

    @Override
    public String getLock() {
        if (chestLeft.getLock().equals(chestRight.getLock())) {
            return chestLeft.getLock();
        }
        else return "";
    }

    @Override
    public boolean isLocked() {
        return chestRight.isLocked() && chestLeft.isLocked();
    }

    @Override
    public String toString() {
        return this.getClass().getName() + " lock: " + getLock() + ", chestRightLock: " + chestRight.getLock()
                + ", chestLeftLock: " + chestLeft.getLock() + ", @" + hashCode();
    }
}
