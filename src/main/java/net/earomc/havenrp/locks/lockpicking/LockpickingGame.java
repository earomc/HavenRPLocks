package net.earomc.havenrp.locks.lockpicking;

import net.earomc.havenrp.locks.lockables.LockableContainer;
import org.bukkit.entity.Player;

public class LockpickingGame {
    public LockableContainer loseGame(Player player) {
        player.sendMessage("§cYou lost the game");
        //TODO: not finished yet.
        return null;
    }

    public LockableContainer winGame(Player player) {
        //TODO: not finished yet.
        return null;
    }
}
