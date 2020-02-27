package net.rezxis.mchosting.lobby.gui2.main;

import java.util.HashMap;

import org.bukkit.entity.Player;

import net.rezxis.mchosting.gui.GUIItem;
import net.rezxis.mchosting.gui.GUIWindow;
import net.rezxis.mchosting.lobby.Lobby;

public class MainMenu extends GUIWindow {

	public MainMenu(Player player) {
		super(player, "RezxisRealm Navigator", 3, Lobby.instance);
	}

	@Override
	public HashMap<Integer, GUIItem> getOptions() {
		HashMap<Integer, GUIItem> map = new HashMap<>();
		setItem(11, new ServersItem(), map);
		setItem(13, new YourRealmItem(), map);
		setItem(15, new CrateMenuItem(), map);
		return map;
	}
}
