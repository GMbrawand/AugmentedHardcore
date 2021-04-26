package com.backtobedrock.augmentedhardcore.guis.clickActions;

import com.backtobedrock.augmentedhardcore.domain.data.PlayerData;
import com.backtobedrock.augmentedhardcore.guis.GuiDeathBans;
import com.backtobedrock.augmentedhardcore.utils.PlayerUtils;
import org.bukkit.entity.Player;

public class ClickActionOpenBansGui extends AbstractClickAction {

    private final PlayerData playerData;

    public ClickActionOpenBansGui(PlayerData playerData) {
        this.playerData = playerData;
    }

    @Override
    public void execute(Player player) {
        if (this.playerData.getBanCount() > 0) {
            PlayerUtils.openInventory(player, new GuiDeathBans(this.playerData));
        } else {
            player.sendMessage("§cThere are currently no death bans recorded yet.");
        }
    }
}
