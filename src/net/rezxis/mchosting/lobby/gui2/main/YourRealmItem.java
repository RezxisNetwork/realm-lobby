package net.rezxis.mchosting.lobby.gui2.main;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.gui.GUIAction;
import net.rezxis.mchosting.gui.GUIItem;
import net.rezxis.mchosting.lobby.gui2.mine.MyRealmMenu;

public class YourRealmItem extends GUIItem {

	public YourRealmItem() {
		super(getIcon());
	}
	
	public static ItemStack getIcon() {
		ItemStack is = new ItemStack(Material.CHEST);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.AQUA+"あなたの"+ChatColor.LIGHT_PURPLE+"レールム");
		ArrayList<String> lore = new ArrayList<>();
		lore.add(ChatColor.GRAY+"鯖の制定を変えます");
		is.setItemMeta(im);
		return is;
	}

	
	@Override
	public GUIAction invClick(InventoryClickEvent e) {
		new MyRealmMenu((Player) e.getWhoClicked()).delayShow();
		return GUIAction.CLOSE;
	}
}
