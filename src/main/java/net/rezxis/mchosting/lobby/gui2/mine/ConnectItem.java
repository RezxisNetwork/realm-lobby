package net.rezxis.mchosting.lobby.gui2.mine;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.gui.GUIAction;
import net.rezxis.mchosting.gui.GUIItem;
import net.rezxis.mchosting.lobby.Lobby;

public class ConnectItem extends GUIItem {

	public ConnectItem() {
		super(getIcon());
	}
	
	private static ItemStack getIcon() {
		ItemStack is = new ItemStack(Material.MINECART);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.GREEN+"接続");
		ArrayList<String> list = new ArrayList<>();
		im.setLore(list);
		is.setItemMeta(im);
		return is;
	}

	@Override
	public GUIAction invClick(InventoryClickEvent e) {
		Lobby.instance.connect((Player) e.getWhoClicked());
		return GUIAction.CLOSE;
	}
}
