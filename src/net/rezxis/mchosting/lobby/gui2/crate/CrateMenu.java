package net.rezxis.mchosting.lobby.gui2.crate;

import net.rezxis.mchosting.database.Tables;
import net.rezxis.mchosting.database.object.player.DBCrate;
import net.rezxis.mchosting.gui.GUIAction;
import net.rezxis.mchosting.gui.GUIItem;
import net.rezxis.mchosting.gui.GUIWindow;
import net.rezxis.mchosting.lobby.Lobby;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

public class CrateMenu extends GUIWindow {
    private List<DBCrate> crates;

    public CrateMenu(Player player) {
        super(player, "Crates", 5, Lobby.instance);
        crates = Tables.getCTable().getCrates(player.getUniqueId(),45);
    }

    @Override
    public HashMap<Integer, GUIItem> getOptions() {
        HashMap<Integer, GUIItem> map = new HashMap<>();
        setItem(8, 4, new OpenAllItem(), map);
        if(crates.size() == 0) {
            ItemStack is = new ItemStack(Material.BARRIER);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(ChatColor.RED+"あなたは箱を持っていません");
            is.setItemMeta(im);
            setItem(22, new GUIItem(is) {
                @Override
                public GUIAction invClick(InventoryClickEvent e) {
                    return GUIAction.CLOSE;
                }
            }, map);
            return map;
        }
        int i = 0;
        for(DBCrate crate : crates) {
        	if (i == 31) {
        		setItem(8, 4, new OpenAllItem(), map);
        		return map;
        	}
            setItem(i, new CrateItem(crate), map);
            i++;
        }
        return map;
    }
}
