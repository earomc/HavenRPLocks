package net.earomc.havenrp.locks;


import net.earomc.havenrp.locks.lockables.*;
import org.bukkit.Sound;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;

public class ContainerLockManager implements Listener {

    private HashMap<String, String> playersInLockMode;
    private HashMap<String, String> playersInUnlockMode;
    private ArrayList<String> playersInAdminUnlockMode;

    public ContainerLockManager() {
        this.playersInLockMode = new HashMap<>();
        this.playersInUnlockMode = new HashMap<>();
        this.playersInAdminUnlockMode = new ArrayList<>();
    }

    //Adminunlock
    public boolean isInAdminUnlockMode(Player player) {
        return playersInAdminUnlockMode.contains(player.getName());
    }

    public void setAdminUnlockMode(Player player) {
        removeLockMode(player);
        removeUnlockMode(player);
        if (!isInAdminUnlockMode(player)) {
            playersInAdminUnlockMode.add(player.getName());
            player.sendMessage("§aLeft click on a container you want to unlock.");
        }
    }

    public void removeAdminUnlockMode(Player player) {
        playersInAdminUnlockMode.remove(player.getName());
    }

    //Unlock
    public boolean isInUnlockMode(Player player) {
        return playersInUnlockMode.containsKey(player.getName());
    }

    public void setUnLockMode(Player player, String unlock) {
        removeLockMode(player);
        removeAdminUnlockMode(player);
        if (!isInUnlockMode(player)) {
            playersInUnlockMode.put(player.getName(), unlock);
            player.sendMessage("§aLeft click on a container you want to unlock.");
        }
    }

    public void removeUnlockMode(Player player) {
        playersInUnlockMode.remove(player.getName());
    }

    //Lock
    public boolean isInLockMode(Player player) {
        return playersInLockMode.containsKey(player.getName());
    }

    public void setLockMode(Player player, String lock) {
        removeUnlockMode(player);
        removeAdminUnlockMode(player);
        if (!isInLockMode(player)) {
            playersInLockMode.put(player.getName(), lock);
            player.sendMessage("§aLeft click on a container you want to lock with §9" + lock + "§a.");
        }
    }

    public void removeLockMode(Player player) {
        playersInLockMode.remove(player.getName());
    }


    //Event handling
    @EventHandler
    public void onBreakLockedContainers(BlockBreakEvent event) {
        Player player = event.getPlayer();
        BlockState state = event.getBlock().getState();
        LockableContainer lockableContainer = getLockableContainer(state);
        if (lockableContainer != null) {
            if (lockableContainer.isLocked()) {
                event.setCancelled(true);
                player.sendMessage("§cThis " + lockableContainer.getName().toLowerCase() + " is locked and cannot be broken.");
            }
        }
    }

    @EventHandler
    public void onBlowUpLockedContainers(EntityExplodeEvent event) {
        ArrayList<Block> lockedBlocks = new ArrayList<>();
        for (Block block : event.blockList()) {
            if (block.getState() instanceof Lockable) {
                Lockable lockable = (Lockable) block.getState();
                if (lockable.isLocked()) {
                    lockedBlocks.add(block);
                }
            }
        }
        event.blockList().removeAll(lockedBlocks);
    }

    @SuppressWarnings("ConstantConditions")
    @EventHandler
    public void onHopperRetrieveItems(InventoryMoveItemEvent event) {
        InventoryHolder holder = event.getSource().getHolder();
        if (holder instanceof Lockable) {
            Lockable chest = (Lockable) event.getSource().getHolder();

            if (chest != null) {
                if (chest.isLocked()) {
                    event.setCancelled(true);
                }
            }
        } else if (holder instanceof DoubleChest) {
            DoubleChest chest = (DoubleChest) event.getSource().getHolder();
            Chest chestLeft = (Chest) chest.getLeftSide();
            Chest chestRight = (Chest) chest.getRightSide();
            if (chestLeft.isLocked() || chestRight.isLocked()) {
                event.setCancelled(true);
            }

        }

    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (isInLockMode(player)) {
            removeLockMode(player);
            player.sendMessage("§cAborted locking process.");
        }
        if (isInUnlockMode(player)) {
            removeUnlockMode(player);
            player.sendMessage("§cAborted unlocking process.");
        }
        if (isInAdminUnlockMode(player)) {
            removeAdminUnlockMode(player);
            player.sendMessage("§cAborted unlocking process.");
        }

    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        removeAdminUnlockMode(player);
        removeLockMode(player);
        removeUnlockMode(player);
    }

    @EventHandler
    public void onPlayerInteractWithChest(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getClickedBlock() == null) return;
        if (event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        Block block = event.getClickedBlock();
        BlockState state = block.getState();
        LockableContainer lockableContainer = getLockableContainer(state);
        if (lockableContainer != null) {
            if (isInLockMode(player)) {
                String lock = playersInLockMode.get(player.getName());
                handleLock(player, lock, lockableContainer);
            } else if (isInUnlockMode(player)) {
                String lock = playersInUnlockMode.get(player.getName());
                handleUnlock(player, lock, lockableContainer);
            } else if (isInAdminUnlockMode(player)) {
                handleAdminUnlock(player, lockableContainer);
            }
        }
    }

    @EventHandler
    public void onMoveShulkerBoxWithPiston(BlockPistonExtendEvent event) {
        for (Block block : event.getBlocks()) {
            if (block.getState() instanceof Lockable) {
                Lockable lockable = (Lockable) block.getState();
                if (lockable.isLocked()) {
                    event.setCancelled(true);
                }
            }
        }

    }

    private void handleAdminUnlock(Player player, LockableContainer lockable) {
        lockable.unlock();
        player.sendMessage("§a" + lockable.getName() + " unlocked!");
        player.playSound(player.getLocation(), Sound.BLOCK_BARREL_OPEN, 10, 10);
        removeAdminUnlockMode(player);
    }

    private void handleUnlock(Player player, String lock, LockableContainer lockable) {
        UnlockResult result = lockable.tryUnlock(lock);
        switch (result) {
            case SUCCESS:
                player.sendMessage("§a" + lockable.getName() + " unlocked!");
                player.playSound(player.getLocation(), Sound.BLOCK_BARREL_OPEN, 10, 10);
                removeUnlockMode(player);
                break;
            case INCORRECT_LOCK:
                player.sendMessage("§cCould not unlock " + lockable.getName().toLowerCase() + ". Lock is incorrect.");
                player.playSound(player.getLocation(), Sound.BLOCK_CHEST_LOCKED, 1, 10);
                removeUnlockMode(player);
                break;
            case CONTAINER_NOT_LOCKED:
                player.sendMessage("§cThis " + lockable.getName().toLowerCase() + " is not locked.");
                break;
        }
    }

    private void handleLock(Player player, String lock, LockableContainer lockable) {
        LockResult result = lockable.tryLock(lock);
        switch (result) {
            case SUCCESS:
                player.sendMessage("§a" + lockable.getName() + " locked with §9" + lock + "§a.");
                player.playSound(player.getLocation(), Sound.BLOCK_CHEST_LOCKED, 1, 10);
                removeLockMode(player);
                break;
            case LOCK_ALREADY_SET:
                player.sendMessage("§cCannot lock this locked " + lockable.getName().toLowerCase() + ". Lock has to be removed first.");
                break;
        }
    }

    //Double chest handling

    private boolean isDoubleChestLocked(DoubleChest doubleChest) {
        Chest chestLeft = (Chest) doubleChest.getLeftSide();
        Chest chestRight = (Chest) doubleChest.getLeftSide();
        if (chestLeft != null && chestRight != null) {
            return chestLeft.isLocked() || chestRight.isLocked();
        } else return false;
    }

    private boolean isDoubleChest(Chest chest) {
        return chest.getInventory().getSize() == 54;
    }

    private DoubleChest getDoubleChestIfSo(Chest chest) {
        if (isDoubleChest(chest)) {
            return (DoubleChest) chest.getInventory().getHolder();
        } else return null;
    }

    private LockableContainer getLockableContainer(BlockState blockState) {
        if (blockState instanceof Chest) {
            Chest chest = (Chest) blockState;
            if (isDoubleChest(chest)) {
                // noinspection SingleStatementInBlock
                DoubleChest doubleChest = getDoubleChestIfSo(chest);
                //noinspection ConstantConditions
                return new LockableDoubleChest(new LockableChest((Chest) doubleChest.getLeftSide()),
                        new LockableChest((Chest) doubleChest.getRightSide()));
            } else {
                return new LockableChest(chest);
            }
        } else if (blockState instanceof Barrel) {
            return new LockableBarrel((Barrel) blockState);
        } else if (blockState instanceof ShulkerBox) {
            return new LockableShulkerBox((ShulkerBox) blockState);
        } else if (blockState instanceof Dispenser) {
            return new LockableDispenser((Dispenser) blockState);
        } else if (blockState instanceof Dropper) {
            return new LockableDropper((Dropper) blockState);
        } else if (blockState instanceof Furnace) {
            return new LockableFurnace((Furnace) blockState);
        }
        return null;
    }

    @EventHandler
    public void onChestConnect(BlockPlaceEvent event) {
        Block block = event.getBlock();
        BlockState state = block.getState();
        if (state instanceof Chest) {
            Chest chest = (Chest) state;
            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    if (isDoubleChest(chest)) {
                        //Bukkit.broadcastMessage("its a double chest.");
                        LockableDoubleChest lockableDoubleChest = (LockableDoubleChest) getLockableContainer(state);
                        @SuppressWarnings("ConstantConditions")
                        LockableChest otherChest = getOtherChest(lockableDoubleChest, chest);
                        if (otherChest.isLocked()) {
                            String lock1 = otherChest.getLock();

                            lockableDoubleChest.lock(lock1);
                        }
                    } else {
                        //Bukkit.broadcastMessage("its no double chest.");
                    }
                }
            }.runTaskLater(HavenRPLocks.getInstance(), 0);
        }
    }

    private LockableChest getOtherChest(LockableDoubleChest doubleChest, Chest chest) {
        if (chest.equals(doubleChest.getChestLeft().getChest())) {
            return doubleChest.getChestRight();
        } else if (chest.equals(doubleChest.getChestRight().getChest())) {
            return doubleChest.getChestLeft();
        } else return null;
    }

}
