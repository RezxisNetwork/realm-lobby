package net.rezxis.mchosting.lobby.gui2.mine;

import java.util.HashMap;

import org.bukkit.entity.Player;

import net.rezxis.mchosting.database.object.server.DBServer;
import net.rezxis.mchosting.database.object.server.ServerStatus;
import net.rezxis.mchosting.database.object.server.DBServer.GameType;
import net.rezxis.mchosting.gui.GUIItem;
import net.rezxis.mchosting.gui.GUIWindow;
import net.rezxis.mchosting.lobby.Lobby;

public class MyRealmMenu extends GUIWindow {
	public MyRealmMenu(Player player) {
		super(player,"Server Manager", 1, Lobby.instance);
	}

	@Override
	public HashMap<Integer, GUIItem> getOptions() {
		HashMap<Integer, GUIItem> map = new HashMap<>();
		DBServer server = Lobby.instance.sTable.get(this.getPlayer().getUniqueId());
		if (server == null) {
			setItem(4, new CreateServerItem(), map);
		} else {
			setItem(0, new ServerStatusItem(server), map);
			if (server.getStatus() == ServerStatus.RUNNING) {
				setItem(4, new ConnectItem(), map);
			} else if (server.getStatus() == ServerStatus.STOP) {
				setItem(1, new WorldManageItem(), map);
				setItem(2, new RenameItem(server), map);
				setItem(3, new RenameMotdItem(server), map);
				setItem(6, new CommandBlockItem(server), map);
				setItem(7, new BackupMenuItem(), map);
				setItem(8, new DeleteServerItem(), map);
			}
		}
		return map;
	}
}
