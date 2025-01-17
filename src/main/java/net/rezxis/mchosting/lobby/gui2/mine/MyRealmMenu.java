package net.rezxis.mchosting.lobby.gui2.mine;

import java.util.HashMap;

import org.bukkit.entity.Player;

import net.rezxis.mchosting.database.Tables;
import net.rezxis.mchosting.database.object.server.DBServer;
import net.rezxis.mchosting.database.object.server.ServerStatus;
import net.rezxis.mchosting.gui.GUIItem;
import net.rezxis.mchosting.gui.GUIWindow;
import net.rezxis.mchosting.lobby.Lobby;

public class MyRealmMenu extends GUIWindow {
	
	protected DBServer server;
	
	public MyRealmMenu(Player player) {
		super(player,"Server Manager", 1, Lobby.instance);
	}

	@Override
	public HashMap<Integer, GUIItem> getOptions() {
		HashMap<Integer, GUIItem> map = new HashMap<>();
		server = Tables.getSTable().get(this.getPlayer().getUniqueId());
		if (server == null) {
			setItem(4, new CreateServerItem(), map);
		} else {
			setItem(0, new ServerStatusItem(server), map);
			if (server.getStatus() == ServerStatus.RUNNING) {
				setItem(4, new ConnectItem(), map);
			} else if (server.getStatus() == ServerStatus.STOP) {
				setItem(1, new WorldManageItem(), map);
				setItem(2, new RenameItem(this), map);
				setItem(3, new RenameMotdItem(this), map);
				setItem(4, new ServerVersionItem(this), map);
				setItem(6, new CommandBlockItem(this), map);
				setItem(7, new BackupMenuItem(), map);
				setItem(8, new DeleteServerItem(), map);
			}
		}
		return map;
	}
}
