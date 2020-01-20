package net.rezxis.mchosting.lobby.gui2.mine.delete;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.entity.Player;

import net.rezxis.mchosting.gui.GUIItem;
import net.rezxis.mchosting.gui.GUIWindow;
import net.rezxis.mchosting.lobby.Lobby;

public class DeleteConfirmMenu extends GUIWindow {

	private int phase;
	
	public DeleteConfirmMenu(Player player, int phase) {
		super(player, "DeleteConfirm ("+phase+"/3)", 6, Lobby.instance);
		this.phase = phase;
	}

	@Override
	public HashMap<Integer, GUIItem> getOptions() {
		HashMap<Integer, GUIItem> items = new HashMap<Integer, GUIItem>();
		int z = new Random().nextInt(53);
		for (int i = 0; i < 53; i++) {
			if (i == z) {
				items.put(i, new ConfirmItem(phase));
			} else {
				items.put(i, new CancelItem());
			}
		}
		return items;
	}
}
