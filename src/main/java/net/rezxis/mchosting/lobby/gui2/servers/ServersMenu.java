package net.rezxis.mchosting.lobby.gui2.servers;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.database.object.ServerWrapper;
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
		ArrayList<ServerWrapper> servers = ServerWrapper.getServers(all, sort);
		if (!all)
			setItem(0, new ShowAllServers(sort), map);
		if (servers.size() > 21*page)
			setItem(8,5, new NextPageItem(page,all,sort), map);
		if (page > 1) {
			setItem(0,5, new BackPageItem(page,all,sort), map);
		}
		int sIndex = 0 + 21*(page-1);//=<20]
		int a = 0;
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
}
