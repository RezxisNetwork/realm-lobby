package net.rezxis.mchosting.lobby.gui2.mine.create;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.gui.GUIAction;
import net.rezxis.mchosting.gui.GUIItem;

public class WorldTypeItem extends GUIItem {

	private CreateServerMenu menu;
	
	public WorldTypeItem(CreateServerMenu menu) {
		super(getIconWorldType(new String[] {"DEFAULT","FLAT","VOID"},menu.type));
		this.menu = menu;
	}
	
	private static ItemStack getIconWorldType(String[] all, String current) {
		ItemStack is = new ItemStack(Material.GRASS);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.AQUA+"ワールドの種類");
		ArrayList<String> lore = new ArrayList<>();
		for (String line : all) {
			if (line.contains(current)) {
				lore.add(ChatColor.AQUA+line);
			} else {
				lore.add(ChatColor.GRAY+line);
			}
		}
		im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}

	@Override
	public GUIAction invClick(InventoryClickEvent e) {
		if (menu.type.equalsIgnoreCase("DEFAULT")) {
			menu.type = "FLAT";
		} else if (menu.type.equalsIgnoreCase("FLAT")) {
			menu.type = "VOID";
		} else if (menu.type.equalsIgnoreCase("VOID")) {
			menu.type = "DEFAULT";
		}
		new CreateServerMenu((Player)e.getWhoClicked(), menu.name, menu.type, menu.stype, menu.version).delayShow();
		return GUIAction.CLOSE;
	}
}
