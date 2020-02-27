package net.rezxis.mchosting.lobby.gui2.servers;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.database.Tables;
import net.rezxis.mchosting.database.object.server.DBServer;
import net.rezxis.mchosting.gui.GUIAction;
import net.rezxis.mchosting.gui.GUIItem;
import net.rezxis.mchosting.lobby.Lobby;

public class RandomItem extends GUIItem {

	private boolean all;
	
	public RandomItem(boolean all) {
		super(getIcon());
		this.all = all;
	}
	
	private static ItemStack getIcon() {
		ItemStack is = new ItemStack(Material.ENDER_PEARL);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.LIGHT_PURPLE+"ランダム");
		ArrayList<String> lore = new ArrayList<>();
		lore.add(ChatColor.GREEN+"ランダムなサーバーに接続");
		im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}

	@Override
	public GUIAction invClick(InventoryClickEvent e) {
		ArrayList<DBServer> servers = null;
		if (all) {
			servers = Tables.getSTable().getOnlineServers();
		} else {
			servers = Tables.getSTable().getOnlineServersVisible();
		}
		DBServer server = servers.get(new Random().nextInt(servers.size()-1));
		Lobby.instance.connect((Player) e.getWhoClicked(), server);
		return GUIAction.CLOSE;
	}
}
