package net.rezxis.mchosting.lobby.gui2.mine.backup;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.database.object.internal.DBBackup;
import net.rezxis.mchosting.gui.GUIAction;
import net.rezxis.mchosting.gui.GUIItem;
import net.wesjd.anvilgui.AnvilGUI;
import net.rezxis.mchosting.lobby.Lobby;

public class RenameItem extends GUIItem {

	private DBBackup obj;
	
	public RenameItem(DBBackup obj) {
		super(getIcon());
		this.obj = obj;
	}
	
	private static ItemStack getIcon() {
		ItemStack is = new ItemStack(Material.WRITTEN_BOOK);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.GREEN+"名前を変更");
		is.setItemMeta(im);
		return is;
	}

	@SuppressWarnings("deprecation")
	@Override
	public GUIAction invClick(InventoryClickEvent e) {
		Bukkit.getScheduler().scheduleAsyncDelayedTask(Lobby.instance, new Runnable() {
			public void run() {
				new AnvilGUI.Builder()
				.onClose(pl -> {
				})
				.onComplete((pl,text) -> {
					if (text == null) {
						return AnvilGUI.Response.text(obj.getName());
					}
					obj.setName(text);
					obj.update();
					new BackupMenu(pl,-1).delayShow();
					return AnvilGUI.Response.close();
				})
				.text(obj.getName())
				.plugin(Lobby.instance)
				.open((Player) e.getWhoClicked());
			}
		}, 2);
		return GUIAction.CLOSE;
	}
}
