package net.rezxis.mchosting.lobby.gui2.crate;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.database.Tables;
import net.rezxis.mchosting.database.crates.CrateTypes;
import net.rezxis.mchosting.database.object.player.DBCrate;
import net.rezxis.mchosting.database.object.player.DBPlayer;
import net.rezxis.mchosting.gui.GUIAction;
import net.rezxis.mchosting.gui.GUIItem;
import net.rezxis.mchosting.lobby.Lobby;

public class OpenAllItem extends GUIItem {

	public OpenAllItem() {
		super(getIcon());
	}
	
	private static ItemStack getIcon() {
		ItemStack is = new ItemStack(Material.ENDER_PORTAL_FRAME);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.GREEN+"一斉開封");
		ArrayList<String> lore = new ArrayList<>();
		lore.add(ChatColor.GREEN+"全ランクで使用可能になりました。");
		im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}

	@Override
	public GUIAction invClick(InventoryClickEvent e) {
		DBPlayer player = Tables.getPTable().get(e.getWhoClicked().getUniqueId());
		int add = 0;
		for (DBCrate crt : Tables.getCTable().getCrates(e.getWhoClicked().getUniqueId(), -1)) {
            Random r = new Random();
            CrateTypes type = crt.getType();
            int won = 0;
            if(type == CrateTypes.NORMAL) {
                won += 35 + r.nextInt(51);
            }else if(type == CrateTypes.RARE) {
            	won += 65 + r.nextInt(75);
            }else if(type == CrateTypes.VOTE) {
            	won += 500;
            }
            while(won > 0) {
                int amount = r.nextInt(Math.min(won, 266304));
                amount = Math.max(amount, 4);
                won -= amount;
                add += amount;
            }
            crt.remove();
		}
		player.addCoin(add);
		player.update();
		Lobby.instance.getServer().getPlayer(e.getWhoClicked().getUniqueId()).sendMessage(ChatColor.LIGHT_PURPLE+""+add+"Realm"+ChatColor.GOLD+"Coins"+"を獲得しました。");
		return GUIAction.CLOSE;
	}
}
