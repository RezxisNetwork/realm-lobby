package net.rezxis.mchosting.lobby.gui2.mine.create;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.database.object.server.Version;
import net.rezxis.mchosting.gui.GUIAction;
import net.rezxis.mchosting.gui.GUIItem;

public class ServerVersionItem extends GUIItem {
	
	private CreateServerMenu menu;
	
	public ServerVersionItem(CreateServerMenu menu) {
		super(getIcon(menu));
		this.menu = menu;
	}
	
	public static ItemStack getIcon(CreateServerMenu menu) {
		ItemStack is = new ItemStack(Material.GLASS);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.GREEN+"Version : "+menu.version.getFriendly());
		ArrayList<String> lore = new ArrayList<String>();
		for (Version v : Version.values()) {
			if (menu.version == v) {
				lore.add(ChatColor.AQUA+v.name());
			} else {
				lore.add(ChatColor.GRAY+v.name());
			}
		}
		im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}

	@Override
	public GUIAction invClick(InventoryClickEvent e) {
		int index = menu.version.ordinal();
		if (e.isShiftClick()) {
			index = 0;
		} else if (e.isLeftClick()) {
			index += 1;
		} else if (e.isRightClick()) {
			index -= 1;
		}
		if (index < 0) {
			index = Version.values().length - 1;
		} else if (index >= Version.values().length){
			index = 0;
		}
		menu.version = Version.values()[index];
		return GUIAction.UPDATE;
	}
}
