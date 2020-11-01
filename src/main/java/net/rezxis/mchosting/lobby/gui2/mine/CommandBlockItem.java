package net.rezxis.mchosting.lobby.gui2.mine;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.database.object.server.DBServer;
import net.rezxis.mchosting.gui.GUIAction;
import net.rezxis.mchosting.gui.GUIItem;

public class CommandBlockItem extends GUIItem {

	private MyRealmMenu menu;
	
	public CommandBlockItem(MyRealmMenu menu) {
		super(getIcon(menu.server));
		this.menu = menu;
	}

	private static ItemStack getIcon(DBServer server) {
		ItemStack is = new ItemStack(Material.COMMAND);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.GREEN+"コマンドブロック");
		ArrayList<String> lore = new ArrayList<>();
		if (server.isCmd()) {
			lore.add(ChatColor.AQUA+"有効");
			lore.add(ChatColor.GREEN+"クリックで 無効化");
		} else {
			lore.add(ChatColor.RED+"無効");
			lore.add(ChatColor.GREEN+"クリックで 有効化");
		}
		im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}
	
	@Override
	public GUIAction invClick(InventoryClickEvent e) {
		menu.server.setCmd(!menu.server.isCmd());
		menu.server.update();
		return GUIAction.UPDATE;
	}
}
