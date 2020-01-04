package net.rezxis.mchosting.lobby.gui2.mine.backup;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.database.object.internal.DBBackup;
import net.rezxis.mchosting.gui.GUIAction;
import net.rezxis.mchosting.gui.GUIItem;

public class BackupObjectItem extends GUIItem {

	public BackupObjectItem(DBBackup obj, boolean selected) {
		super(getIcon(obj, selected));
	}
	
	private static ItemStack getIcon(DBBackup obj, boolean selected) {
		ItemStack is = new ItemStack(Material.ENDER_CHEST);
		ItemMeta im = is.getItemMeta();
		ArrayList<String> lore = new ArrayList<>();
		lore.add(ChatColor.GREEN+"作成日時 : "+obj.getCreation().toLocaleString());
		if (selected) {
			lore.add(ChatColor.AQUA+"選択されています。");
		} else {
			lore.add(ChatColor.AQUA+"クリックで選択。");
		}
		im.setLore(lore);
		im.setDisplayName(obj.getName());
		if (selected) {
			im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			im.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
		}
		is.setItemMeta(im);
		return is;
	}

	@Override
	public GUIAction invClick(InventoryClickEvent e) {
		new BackupMenu((Player) e.getWhoClicked(),e.getRawSlot()).delayShow();
		return GUIAction.CLOSE;
	}
}
