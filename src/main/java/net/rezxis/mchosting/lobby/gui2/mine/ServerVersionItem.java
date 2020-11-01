package net.rezxis.mchosting.lobby.gui2.mine;

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
	
	private MyRealmMenu menu;
	
	public ServerVersionItem(MyRealmMenu menu) {
		super(getIcon(menu));
		this.menu = menu;
	}
	
	public static ItemStack getIcon(MyRealmMenu menu) {
		ItemStack is = new ItemStack(Material.GLASS);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.GREEN+"Version : "+menu.server.getVersion().getFriendly());
		ArrayList<String> lore = new ArrayList<String>();
		for (Version v : Version.values()) {
			if (menu.server.getVersion() == v) {
				lore.add(ChatColor.AQUA+v.getFriendly());
			} else {
				lore.add(ChatColor.GRAY+v.getFriendly());
			}
		}
		im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}

	@Override
	public GUIAction invClick(InventoryClickEvent e) {
		int index = menu.server.getVersion().ordinal();
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
		menu.server.setVersion(Version.values()[index]);
		menu.server.update();
		return GUIAction.UPDATE;
	}
}
