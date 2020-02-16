package net.rezxis.mchosting.lobby.gui2.servers;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.gson.Gson;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.database.Tables;
import net.rezxis.mchosting.database.object.ServerWrapper;
import net.rezxis.mchosting.database.object.player.DBPlayer;
import net.rezxis.mchosting.database.object.server.ServerStatus;
import net.rezxis.mchosting.gui.GUIAction;
import net.rezxis.mchosting.gui.GUIItem;
import net.rezxis.mchosting.lobby.Lobby;
import net.rezxis.mchosting.network.packet.sync.SyncStartServer;

public class ServerItem extends GUIItem {

	public ServerWrapper server;
	
	public ServerItem(ServerWrapper server) {
		super(getIcon(server));
		this.server = server;
	}
	
	private static ItemStack getIcon(ServerWrapper server) {
		DBPlayer dp = Tables.getPTable().get(server.getOwner());
		ItemStack is = new ItemStack(Material.BARRIER);
		try {
			Material m = Material.valueOf(server.getIcon());
			is = new ItemStack(m);
		} catch (Exception ex) {}
		if (server.getPlayers() > 0)
			is.setAmount(server.getPlayers());
		else 
			is.setAmount(1);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(server.getDisplayName());
		ArrayList<String> lore = new ArrayList<>();
		lore.add(server.getMotd());
		lore.add("");
		int maxp = -1;
		if (server.isDBServer()) {
			maxp = dp.getRank().getMaxPlayers();
		} else {
			maxp = server.getDBThirdParty().getMax();
		}
		lore.add(ChatColor.GRAY+"オンラインのプレイヤー "+ChatColor.GREEN+String.valueOf(server.getPlayers())+ChatColor.GRAY+"/"+maxp);
		lore.add(ChatColor.WHITE+"主: "+Bukkit.getOfflinePlayer(server.getOwner()).getName());
		lore.add(ChatColor.WHITE+"投票: "+ChatColor.AQUA+server.getVote());
		if (server.isDBServer()) {
			if (dp.getRank().getOfflineBoot() && server.getDBServer().getStatus() == ServerStatus.STOP && dp.isOfflineBoot()) {
				lore.add(ChatColor.LIGHT_PURPLE+"クリックで起動");
			}
		}
		im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}

	@Override
	public GUIAction invClick(InventoryClickEvent e) {
		DBPlayer dp = Tables.getPTable().get(server.getOwner());
		if (server.isDBServer()) {
			if (dp.getRank().getOfflineBoot() && server.getDBServer().getStatus() == ServerStatus.STOP && dp.isOfflineBoot()) {
				Lobby.instance.ws.send(new Gson().toJson(new SyncStartServer(server.getOwner().toString())));
				e.getWhoClicked().sendMessage(ChatColor.AQUA+"起動中");
			}
		}
		Lobby.instance.connect((Player) e.getWhoClicked(),server);
		return GUIAction.CLOSE;
	}
}
