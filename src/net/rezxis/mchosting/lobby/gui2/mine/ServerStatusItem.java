package net.rezxis.mchosting.lobby.gui2.mine;

import java.util.ArrayList;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.gson.Gson;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.databse.DBServer;
import net.rezxis.mchosting.databse.ServerStatus;
import net.rezxis.mchosting.gui.GUIAction;
import net.rezxis.mchosting.gui.GUIItem;
import net.rezxis.mchosting.lobby.Lobby;
import net.rezxis.mchosting.network.packet.sync.SyncStartServer;
import net.rezxis.mchosting.network.packet.sync.SyncStopServer;

public class ServerStatusItem extends GUIItem {

	private DBServer server;
	
	public ServerStatusItem(DBServer server) {
		super(getIcon(server));
		this.server = server;
	}
	
	@SuppressWarnings("deprecation")
	private static ItemStack getIcon(DBServer server) {
		ItemStack is = null;
		ItemMeta im = null;
		if (server.getStatus() == ServerStatus.RUNNING) {
			is = new ItemStack(Material.EMERALD_BLOCK);
			im = is.getItemMeta();
			im.setDisplayName(ChatColor.GREEN+"オンライン");
			ArrayList<String> list = new ArrayList<>();
			list.add(ChatColor.RED+"サーバーを停止する");
			im.setLore(list);
		} else if (server.getStatus() == ServerStatus.STOP){
			is = new ItemStack(Material.REDSTONE_BLOCK);
			im = is.getItemMeta();
			im.setDisplayName(ChatColor.RED+"オフライン");
			ArrayList<String> list = new ArrayList<>();
			list.add(ChatColor.GREEN+"サーバーを起動する");
			im.setLore(list);
		} else if (server.getStatus() == ServerStatus.STARTING) {
			is = new ItemStack(Material.WOOL,1,DyeColor.ORANGE.getWoolData());
			im = is.getItemMeta();
			im.setDisplayName(ChatColor.GREEN+"起動中");
		} else if (server.getStatus() == ServerStatus.STOPPING) {
			is = new ItemStack(Material.WOOL,1,DyeColor.RED.getWoolData());
			im = is.getItemMeta();
			im.setDisplayName(ChatColor.GREEN+"停止中");
		} else if (server.getStatus() == ServerStatus.REBOOTING) {
			is = new ItemStack(Material.WOOL,1,DyeColor.ORANGE.getWoolData());
			im = is.getItemMeta();
			im.setDisplayName(ChatColor.GREEN+"再起動中");
		}
		is.setItemMeta(im);
		return is;
	}

	@Override
	public GUIAction invClick(InventoryClickEvent e) {
		if (server.getStatus() == ServerStatus.RUNNING) {
			//stop
			SyncStopServer packet = new SyncStopServer(e.getWhoClicked().getUniqueId().toString());
			Lobby.instance.ws.send(new Gson().toJson(packet));
			e.getWhoClicked().sendMessage(ChatColor.RED+"停止中。");
		} else if (server.getStatus() == ServerStatus.STOP) {
			//start
			SyncStartServer packet = new SyncStartServer(e.getWhoClicked().getUniqueId().toString());
			Lobby.instance.ws.send(new Gson().toJson(packet));
			e.getWhoClicked().sendMessage(ChatColor.GREEN+"起動中！");
		}
		return GUIAction.CLOSE;
	}
}
