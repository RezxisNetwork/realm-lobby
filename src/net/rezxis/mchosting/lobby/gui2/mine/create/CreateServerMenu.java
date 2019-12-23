package net.rezxis.mchosting.lobby.gui2.mine.create;

import java.util.HashMap;

import org.bukkit.entity.Player;

import net.rezxis.mchosting.gui.GUIItem;
import net.rezxis.mchosting.gui.GUIWindow;
import net.rezxis.mchosting.lobby.Lobby;

public class CreateServerMenu extends GUIWindow {
	
	private String name;
	private String type;
	private String stype;
	
	public CreateServerMenu(Player player, String name, String type, String stype) {
		super(player,"Create Server",1,Lobby.instance);
		this.name = name;
		this.type = type;
		this.stype = stype;
	}

	@Override
	public HashMap<Integer, GUIItem> getOptions() {
		HashMap<Integer, GUIItem> map = new HashMap<>();
		setItem(0, new ServerNameItem(name,type,stype), map);
		setItem(1, new WorldTypeItem(name,type,stype), map);
		setItem(2, new ServerTypeItem(name,type,stype), map);
		setItem(4, new DoCreateServerItem(name,type,stype), map);
		return map;
	}
}
