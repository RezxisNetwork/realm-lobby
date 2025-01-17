package net.rezxis.mchosting.lobby.gui2.crate;

import net.rezxis.mchosting.database.Tables;
import net.rezxis.mchosting.database.crates.CrateTypes;
import net.rezxis.mchosting.database.object.player.DBCrate;
import net.rezxis.mchosting.database.object.player.DBPlayer;
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
import java.util.Map.Entry;
import java.util.Random;

public class CrateOpenMenu extends GUIWindow {
    private DBCrate crate;
    private HashMap<Integer, GUIItem> map;

    public CrateOpenMenu(DBCrate crate, Player player) {
        super(player, crate.getType().getName(), 6, Lobby.instance);
        this.crate = crate;
    }

    @Override
    public HashMap<Integer, GUIItem> getOptions() {
    	if (map == null) {
    		map = new HashMap<>();
            int won = 0;
            Random r = new Random();
            if(crate.getType() == CrateTypes.NORMAL) {
                won = 35 + r.nextInt(51);
            }else if(crate.getType() == CrateTypes.RARE) {
                won = 65 + r.nextInt(75);
            }else if(crate.getType() == CrateTypes.VOTE) {
                won = 500;
            }
            int add = 0;
            while(won > 0) {
                int amount = r.nextInt(Math.min(won, 266304));
                amount = Math.max(amount, 4);
                won -= amount;
                int slot = r.nextInt(9*6);
                setItem(slot,addCoins(slot, amount),map);
                add += amount;
            }
            DBPlayer player = Tables.getPTable().get(getPlayer().getUniqueId());
            player.addCoin(add);
            player.update();
    	}
        return map;
    }

    private GUIItem addCoins(int slot, int amount) {
    	CrateOpenMenu com = this;
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
                getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                int key = -1;
                for (Entry<Integer,GUIItem> eee: com.map.entrySet()) {
                	if (eee.getValue().equals(this)) {
                		key = eee.getKey();
                	}
                }
                if (key != -1) {
                	com.map.remove(key);
                }
                if (com.map.size() == 0) {
                	new CrateMenu((Player) e.getWhoClicked()).delayShow();
                	return GUIAction.CLOSE;
                }
                return GUIAction.UPDATE;
            }
        };
        return guiItem;
    }
}
