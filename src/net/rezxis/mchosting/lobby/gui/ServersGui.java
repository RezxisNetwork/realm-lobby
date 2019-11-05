package net.rezxis.mchosting.lobby.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.gson.Gson;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.databse.DBServer;
import net.rezxis.mchosting.gui.EmptyItem;
import net.rezxis.mchosting.gui.ExecutableItemStack;
import net.rezxis.mchosting.gui.GuiPresenter;
import net.rezxis.mchosting.gui.ItemExecution.ExecReturn;
import net.rezxis.mchosting.lobby.Lobby;

public class ServersGui implements GuiPresenter {
	
	@Override
	public List<ExecutableItemStack> getOptions(Player player) {
		ArrayList<ExecutableItemStack> items = new ArrayList<>();
		if (Lobby.instance.sTable.getOnlineServers().size() == 0) {
			items.add(new EmptyItem());
			items.add(new EmptyItem());
			items.add(new EmptyItem());
			items.add(new EmptyItem());
			items.add(new NoServers());
		} else {
			int players = Lobby.instance.sTable.getOnlinePlayers();
			ArrayList<DBServer> serverss = Lobby.instance.sTable.getOnlineServers();
			int servers = serverss.size();
			items.add(new Status(getIconServerStatus(players,servers)));
			for (DBServer e : serverss) {
				items.add(new Server(e));
				System.out.println(e.getDisplayName()+":"+e.getPlayers());
			}
		}
		return items;
	}

	@Override
	public String getLabel() {
		return "Servers";
	}

	@Override
	public String getEmptyMessage() {
		return "Something went to worng";
	}
	
	public static ItemStack getIconNoServers() {
		ItemStack is = new ItemStack(Material.BARRIER);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.RED+"オンラインのサーバーはありません");
		ArrayList<String> list = new ArrayList<>();
		im.setLore(list);
		is.setItemMeta(im);
		return is;
	}
	
	public static ItemStack getIconServerStatus(int players, int servers) {
		ItemStack is = new ItemStack(Material.PAPER);
		ItemMeta im = is.getItemMeta();
		is.setAmount(1);
		im.setDisplayName(ChatColor.GOLD+""+ChatColor.BOLD+"Servers Status");
		ArrayList<String> list = new ArrayList<>();
		list.add(ChatColor.AQUA+"Online players : "+players);
		list.add(ChatColor.AQUA+"Online servers : "+servers);
		im.setLore(list);
		is.setItemMeta(im);
		return is;
	}
	
	public static ItemStack getIconServer(DBServer server) {
		server.sync();
		ItemStack is = new ItemStack(Material.DIAMOND_BLOCK);
		ItemMeta im = is.getItemMeta();
		if (server.getPlayers() == 0) {
			is.setAmount(1);
		} else {
			is.setAmount(server.getPlayers());
		}
		im.setDisplayName(ChatColor.AQUA+server.getDisplayName());
		ArrayList<String> list = new ArrayList<>();
		list.add(server.getMotd());
		list.add(ChatColor.AQUA+"================");
		list.add(ChatColor.AQUA+"Online : "+server.getPlayers());
		list.add(ChatColor.AQUA+"Owner : "+Bukkit.getOfflinePlayer(server.getOwner()).getName());
		//ADD MOTD
		im.setLore(list);
		is.setItemMeta(im);
		return is;
	}
	
	private class Status extends ExecutableItemStack {
		public Status(ItemStack is) {
			super(is);
		}
		@Override
		public ExecReturn Execute(Player player, ClickType click) {
			return ExecReturn.NO_ACTION;
		}
	}
	
	private class Server extends ExecutableItemStack {
		private DBServer s;
		public Server(DBServer server) {
			super(getIconServer(server));
			this.s = server;
		}

		@Override
		public ExecReturn Execute(Player player, ClickType click) {
			Lobby.instance.connect(player,s);
			return ExecReturn.CLOSE;
		}
	}
	
	private class NoServers extends ExecutableItemStack {
		public NoServers() {
			super(getIconNoServers());
		}
		@Override
		public ExecReturn Execute(Player player, ClickType click) {
			return ExecReturn.NO_ACTION;
		}
	}
}
