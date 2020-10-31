package net.rezxis.mchosting.lobby.gui2.mine.create;

import java.util.HashMap;

import org.bukkit.entity.Player;

import net.rezxis.mchosting.database.object.server.Version;
import net.rezxis.mchosting.gui.GUIItem;
import net.rezxis.mchosting.gui.GUIWindow;
import net.rezxis.mchosting.lobby.Lobby;

public class CreateServerMenu extends GUIWindow {
	
	protected String name;
	protected String type;
	protected String stype;
	protected Version version;
	
	public CreateServerMenu(Player player, String name, String type, String stype, Version version) {
		super(player,"Create Server",1,Lobby.instance);
		this.name = name;
		this.type = type;
		this.stype = stype;
		this.version = version;
	}

	@Override
	public HashMap<Integer, GUIItem> getOptions() {
		HashMap<Integer, GUIItem> map = new HashMap<>();
		setItem(0, new ServerNameItem(this), map);
		setItem(1, new WorldTypeItem(this), map);
		setItem(2, new ServerTypeItem(this), map);
		setItem(4, new DoCreateServerItem(this), map);
		return map;
	}
}
