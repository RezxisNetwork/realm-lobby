package net.rezxis.mchosting.lobby.gui2.mine;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.database.DBServer;
import net.rezxis.mchosting.gui.GUIAction;
import net.rezxis.mchosting.gui.GUIItem;

public class CommandBlockItem extends GUIItem {

	private DBServer server;
	
	public CommandBlockItem(DBServer server) {
		super(getIcon(server));
		this.server = server;
	}

	private static ItemStack getIcon(DBServer server) {
		ItemStack is = new ItemStack(Material.COMMAND);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.GREEN+"コマンドブロック");
		ArrayList<String> lore = new ArrayList<>();
		if (server.getCmd()) {
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
		server.setCmd(!server.getCmd());
		server.update();
		return GUIAction.UPDATE;
	}
}
