package net.rezxis.mchosting.lobby.gui2.mine.backup;

import java.util.HashMap;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.database.DBBackup;
import net.rezxis.mchosting.gui.GUIItem;
import net.rezxis.mchosting.gui.GUIWindow;
import net.rezxis.mchosting.lobby.Lobby;

public class BackupMenu extends GUIWindow {

	private int select;
	private Player player;
	
	public BackupMenu(Player player, int select) {
		super(player, ChatColor.LIGHT_PURPLE+"バックアップ", 6, Lobby.instance);
		this.select = select;
		this.player = player;
	}

	@Override
	public HashMap<Integer, GUIItem> getOptions() {
		HashMap<Integer, GUIItem> items = new HashMap<Integer, GUIItem>();
		for (int i = 0; i < 9; i++) {
			setItem(i,4,new SplitItem(),items);
		}
		setItem(0,5,new CreateBackupItem(),items);
		int num = 0;
		DBBackup selected = null;
		for (DBBackup obj : Lobby.instance.bTable.getBackups(player.getUniqueId().toString())) {
			setItem(num,0,new BackupObjectItem(obj, num == select),items);
			if (num == select) {
				selected = obj;
			}
			num++;
		}
		if (selected != null) {
			setItem(4,5,new PatchItem(selected),items);
			setItem(5,5,new RenameItem(selected),items);
			setItem(8,5,new DeleteItem(selected),items);
		}
		return items;
	}
}
