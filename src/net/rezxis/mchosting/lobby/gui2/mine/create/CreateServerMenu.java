package net.rezxis.mchosting.lobby.gui2.mine.create;

import java.util.HashMap;

import org.bukkit.entity.Player;

import net.rezxis.mchosting.gui.GUIItem;
import net.rezxis.mchosting.gui.GUIWindow;
import net.rezxis.mchosting.lobby.Lobby;

public class CreateServerMenu extends GUIWindow {
	
	private String name;
	private String type;
	
	public CreateServerMenu(Player player, String name, String type) {
		super(player,"Create Server",1,Lobby.instance);
		this.name = name;
		this.type = type;
	}

	@Override
	public HashMap<Integer, GUIItem> getOptions() {
		HashMap<Integer, GUIItem> map = new HashMap<>();
		setItem(0, new ServerNameItem(name,type), map);
		setItem(1, new WorldTypeItem(name,type), map);
		setItem(4, new DoCreateServerItem(name,type), map);
		return map;
	}
}
