package net.rezxis.mchosting.lobby.gui2.crate;

import net.rezxis.mchosting.database.object.player.DBCrate;
import net.rezxis.mchosting.gui.GUIAction;
import net.rezxis.mchosting.gui.GUIItem;
import net.rezxis.mchosting.lobby.Lobby;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CrateItem extends GUIItem {
    private DBCrate crate;

    public CrateItem(DBCrate crate) { 
    	super(getIcon(crate));
    	this.crate = crate;
    }

    public static ItemStack getIcon(DBCrate crate) {
        ItemStack is = new ItemStack(Material.valueOf(crate.getType().getDisplayItem()));
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&',crate.getType().getName()));
        is.setItemMeta(im);
        return is;
    }

    @Override
    public GUIAction invClick(InventoryClickEvent e) {
        Lobby.instance.cTable.removeCrate(crate);
        new CrateOpenMenu(crate, (Player) e.getWhoClicked()).delayShow();
        return GUIAction.CLOSE;
    }
}