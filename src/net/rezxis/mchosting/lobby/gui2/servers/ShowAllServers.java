package net.rezxis.mchosting.lobby.gui2.servers;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.gui.GUIAction;
import net.rezxis.mchosting.gui.GUIItem;

public class ShowAllServers extends GUIItem {
	
	private String sort;
	
	public ShowAllServers(String sort) {
		super (getIcon());
		this.sort = sort;
	}
	
	private static ItemStack getIcon() {
		ItemStack is = new ItemStack(Material.ENDER_CHEST);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.AQUA+"全てのレールムを表示");
		is.setItemMeta(im);
		return is;
	}

	@Override
	public GUIAction invClick(InventoryClickEvent e) {
		new ServersMenu((Player)e.getWhoClicked(),1, true,sort).delayShow();
		return GUIAction.CLOSE;
	}
}
