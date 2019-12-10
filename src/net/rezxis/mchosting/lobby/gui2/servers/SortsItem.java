package net.rezxis.mchosting.lobby.gui2.servers;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.gui.GUIAction;
import net.rezxis.mchosting.gui.GUIItem;

public class SortsItem extends GUIItem {

	private int page;
	private boolean all;
	private String type;
	
	public SortsItem(int page, boolean all, String type) {
		super(getIcon(type));
		this.page = page;
		this.all = all;
		this.type = type;
	}
	
	private static ItemStack getIcon(String type) {
		ItemStack is = new ItemStack(Material.SIGN);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.AQUA+"並べ替え");
		ArrayList<String> lore = new ArrayList<>();
		if (type.equalsIgnoreCase("score")) {
			lore.add(ChatColor.AQUA+"投票数");
			lore.add(ChatColor.GRAY+"プレイヤー数");
		} else {
			lore.add(ChatColor.GRAY+"投票数");
			lore.add(ChatColor.AQUA+"プレイヤー数");
		}
		im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}

	@Override
	public GUIAction invClick(InventoryClickEvent e) {
		String next = null;
		if (this.type.equalsIgnoreCase("score")) {
			next = "players";
		} else {
			next = "score";
		}
		new ServersMenu((Player) e.getWhoClicked(), page, all, next).delayShow();
		return GUIAction.CLOSE;
	}
}
