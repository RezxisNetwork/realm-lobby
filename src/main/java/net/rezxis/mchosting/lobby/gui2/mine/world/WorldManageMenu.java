package net.rezxis.mchosting.lobby.gui2.mine.world;

import java.util.HashMap;

import org.bukkit.entity.Player;

import net.rezxis.mchosting.gui.GUIItem;
import net.rezxis.mchosting.gui.GUIWindow;
import net.rezxis.mchosting.lobby.Lobby;

public class WorldManageMenu extends GUIWindow {

	private Player player;
	
	public WorldManageMenu(Player player) {
		super(player, "World Manager", 1, Lobby.instance);
		this.player = player;
	}

	@Override
	public HashMap<Integer, GUIItem> getOptions() {
		HashMap<Integer, GUIItem> items = new HashMap<>();
		items.put(0, new WorldUploadItem(player));
		return items;
	}
}
