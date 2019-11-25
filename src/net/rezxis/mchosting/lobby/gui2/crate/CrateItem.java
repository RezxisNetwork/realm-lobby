package net.rezxis.mchosting.lobby.gui2.crate;

import net.rezxis.mchosting.databse.DBCrate;
import net.rezxis.mchosting.databse.crates.CrateTypes;
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
    private static DBCrate crate;

    public CrateItem(DBCrate crate) { super(getIcon()); }

    public static ItemStack getIcon() {
        ItemStack is = new ItemStack(Material.getMaterial(CrateTypes.getByCrate(crate).getDisplayItem()));
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&',CrateTypes.getByCrate(crate).getName()));
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
