package net.rezxis.mchosting.lobby.gui2.servers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.database.object.player.DBPlayer;
import net.rezxis.mchosting.database.object.server.DBServer;
import net.rezxis.mchosting.gui.GUIItem;
import net.rezxis.mchosting.gui.GUIWindow;
import net.rezxis.mchosting.lobby.Lobby;

public class ServersMenu extends GUIWindow {
	
	private int page;
	private boolean all;
	private String sort;
	
	public ServersMenu(Player player,int page, boolean all, String sort) {
		super(player,ChatColor.AQUA+"Realm List Page : "+page, 6, Lobby.instance);
		this.page = page;
		this.all = all;
		this.sort = sort;
		//21 servers per 1 page 7x3
	}

	@Override
	public HashMap<Integer, GUIItem> getOptions() {
		HashMap<Integer, GUIItem> map = new HashMap<>();
		// TODO Auto-generated method stub
		setItem(1, 5, new SortsItem(page,all,sort), map);
		ArrayList<DBServer> servers = null;
		if (all) {
			servers = Lobby.instance.sTable.getOnlineServers();
		} else {
			servers = Lobby.instance.sTable.getOnlineServersVisible();
		}
		if (!all)
			setItem(0, new ShowAllServers(sort), map);
		if (servers.size() > 21*page)
			setItem(8,5, new NextPageItem(page,all,sort), map);
		if (page > 1) {
			setItem(0,5, new BackPageItem(page,all,sort), map);
		}
		for (DBServer server : servers) {
			server.sync();
		}
		int sIndex = 0 + 21*(page-1);//=<20
		int a = 0;
		if (sort.equalsIgnoreCase("players")) {
			Collections.sort(servers, new Sort());
		} else {
			Collections.sort(servers, new ScoreSort());
		}
		if (all) {
			ArrayList<DBPlayer> ofb = Lobby.instance.pTable.ofbPlayers();
			for (DBPlayer dpp : ofb) {
				if (!dpp.isExpiredRank()) {
					DBServer sss = Lobby.instance.sTable.get(dpp.getUUID());
					if (sss != null) {
						servers.add(sss);
					}
				}
			}
		}
		if (servers.size() == 0) {
			setItem(4,2, new NoServersItem(), map);
			return map;
		} else {
			setItem(4, 5, new RandomItem(all), map);
		}
		for (int i = sIndex; i <= sIndex+20; i++) {
			if (i == servers.size())
				break;
			int x = a % 7;
			int y = (a-x)/7;
			setItem(x+1, y+1, new ServerItem(servers.get(i)), map);
			a++;
		}
		return map;
	}
	
	private class ScoreSort implements Comparator<DBServer> {

		@Override
		public int compare(DBServer o1, DBServer o2) {
			return o1.getVote() < o2.getVote() ? 1: -1;
		}
		
	}
	
	private class Sort implements Comparator<DBServer> {

		@Override
		public int compare(DBServer o1, DBServer o2) {
			return o1.getPlayers() < o2.getPlayers() ? 1: -1;
		}
		
	}
}
