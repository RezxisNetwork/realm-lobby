package net.rezxis.mchosting.lobby.gui2.vote;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.rezxis.mchosting.gui.GUIAction;
import net.rezxis.mchosting.gui.GUIItem;

public class GlassPaneItem extends GUIItem {

	public GlassPaneItem(short colorData) {
		super(getIcon(colorData));
	}
	
	private static ItemStack getIcon(short colorData) {
		ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE,1,colorData);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName("");
		is.setItemMeta(im);
		return is;
	}

	@Override
	public GUIAction invClick(InventoryClickEvent e) {
		return GUIAction.NO_ACTION;
	}
}
