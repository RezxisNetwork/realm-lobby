package net.rezxis.mchosting.lobby.gui2.main;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.gui.GUIAction;
import net.rezxis.mchosting.gui.GUIItem;
import net.rezxis.mchosting.lobby.gui2.servers.ServersMenu;

public class ServersItem extends GUIItem {

	public ServersItem() {
		super(getIcon());
	}
	
	private static ItemStack getIcon() {
		ItemStack is = new ItemStack(Material.GOLD_NUGGET);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.LIGHT_PURPLE+"他のレールムを"+ChatColor.AQUA+"表示");
		is.setItemMeta(im);
		return is;
	}

	@Override
	public GUIAction invClick(InventoryClickEvent e) {
		new ServersMenu((Player)e.getWhoClicked(),1,false,"players").delayShow();
		return GUIAction.CLOSE;
	}
}
