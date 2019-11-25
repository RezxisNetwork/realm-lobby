package net.rezxis.mchosting.lobby.gui2.crate;

import net.rezxis.mchosting.databse.DBCrate;
import net.rezxis.mchosting.databse.DBPlayer;
import net.rezxis.mchosting.databse.crates.CrateTypes;
import net.rezxis.mchosting.gui.GUIAction;
import net.rezxis.mchosting.gui.GUIItem;
import net.rezxis.mchosting.gui.GUIWindow;
import net.rezxis.mchosting.lobby.Lobby;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Random;

public class CrateOpenMenu extends GUIWindow {
    private DBCrate crate;
    private CrateTypes type;

    public CrateOpenMenu(DBCrate crate, Player player) {
        super(player, crate.getType(), 9*6, Lobby.instance);
    }

    @Override
    public HashMap<Integer, GUIItem> getOptions() {
        HashMap<Integer, GUIItem> map = new HashMap<>();
        int won = 0;
        Random r = new Random();
        type = CrateTypes.getByCrate(crate);
        if(type == CrateTypes.NORMAL) {
            won = 35 + r.nextInt(51);
        }else if(type == CrateTypes.RARE) {
            won = 65 + r.nextInt(75);
        }else if(type == CrateTypes.VOTE) {
            won = 500;
        }
        while(won > 0) {
            int amount = r.nextInt(Math.min(won, 266304));
            amount = Math.max(amount, 4);
            won -= amount;
            int slot = r.nextInt(9*6);
            setItem(slot,addCoins(slot, amount, map),map);
        }
        return map;
    }

    private GUIItem addCoins(int slot, int amount, HashMap map) {
        Material m = Material.GOLD_NUGGET;
        int displayAmount = amount;
        if(displayAmount > 64) {
            displayAmount /= 64;
            m = Material.GOLD_INGOT;
            if(displayAmount > 64) {
                displayAmount /= 64;
                m = Material.GOLD_BLOCK;
            }
        }
        displayAmount = Math.min(displayAmount, 64);
        ItemStack is = new ItemStack(m);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.GRAY.toString()+amount+" "+ChatColor.AQUA+"Realm"+ChatColor.GOLD+"Coins");
        is.setAmount(displayAmount);
        is.setItemMeta(im);
        GUIItem guiItem = new GUIItem(is) {
            @Override
            public GUIAction invClick(InventoryClickEvent e) {
                DBPlayer player = Lobby.instance.pTable.get(getPlayer().getUniqueId());
                player.addCoin(amount);
                getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                boolean empty = true;
                for (Object itmp : map.values()) {
                    GUIItem item = (GUIItem) itmp;
                    if(item != null && item.getBukkitItem().getType() != Material.AIR) {
                        empty = false;
                    }
                }
                if(empty) {
                    return GUIAction.CLOSE;
                }
                return GUIAction.UPDATE;
            }
        };
        return guiItem;
    }
}
