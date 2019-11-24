package net.rezxis.mchosting.lobby.gui2.mine.world;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.gson.Gson;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.databse.DBFile;
import net.rezxis.mchosting.databse.DBFile.Type;
import net.rezxis.mchosting.gui.GUIAction;
import net.rezxis.mchosting.gui.GUIItem;
import net.rezxis.mchosting.lobby.Lobby;
import net.rezxis.mchosting.network.packet.sync.SyncWorldPacket;
import net.rezxis.mchosting.network.packet.sync.SyncWorldPacket.Action;

public class WorldUploadItem extends GUIItem {
	
	public WorldUploadItem(Player player) {
		super(getIcon(player));
	}
	
	private static ItemStack getIcon(Player player) {
		ItemStack is = new ItemStack(Material.GRASS);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.LIGHT_PURPLE+"ワールドをアップロード");
		ArrayList<String> lore = new ArrayList<>();
		DBFile file = Lobby.instance.fTable.get2(player.getUniqueId().toString(), Type.WORLD);
		if (file == null) {
			lore.add(ChatColor.AQUA+"クリックでアップロード");
		} else {
			file.sync();
			if (!file.getUploaded()) {
				lore.add(ChatColor.AQUA+"ワールドをアップロードしてください");
				lore.add(ChatColor.GREEN+"クリックでアップロードリンクを表示");
			} else if (file.getUploaded()) {
				lore.add(ChatColor.AQUA+"クリックで適応");
			}
		}
		im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}

	@Override
	public GUIAction invClick(InventoryClickEvent e) {
		Player player = (Player) e.getWhoClicked();
		DBFile file = Lobby.instance.fTable.get2(player.getUniqueId().toString(), Type.WORLD);
		if (file == null) {
			file = new DBFile("", player.getUniqueId().toString(),
					RandomStringUtils.randomAlphabetic(10),
					false, new Date(), Type.WORLD);
			Lobby.instance.fTable.insert(file);
		} else {
			file.sync();
			if (!file.getUploaded()) {
				String url = "https://world.rezxis.net/upload.php?uuid="+file.getUUID()+"&secretKey="+file.getSecret();
				player.sendMessage(ChatColor.YELLOW+"ここからWorld.zipをアップロードしてください。");
				player.sendMessage(ChatColor.YELLOW+url);
			} else {
				player.sendMessage(ChatColor.GREEN+"ワールドを更新します。");
				HashMap<String, String> map = new HashMap<>();
				map.put("uuid", player.getUniqueId().toString());
				map.put("secret", file.getSecret());
				Lobby.instance.ws.send(new Gson().toJson(new SyncWorldPacket(map, Action.UPLOAD)));
			}
		}
		return GUIAction.UPDATE;
	}
}
