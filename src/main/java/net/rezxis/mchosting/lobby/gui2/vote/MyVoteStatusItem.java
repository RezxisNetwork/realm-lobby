package net.rezxis.mchosting.lobby.gui2.vote;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.rezxis.mchosting.database.object.player.DBVote;
import net.rezxis.mchosting.gui.GUIAction;
import net.rezxis.mchosting.gui.GUIItem;

public class MyVoteStatusItem extends GUIItem {

	private Player player;
	
	public MyVoteStatusItem(Player player, DBVote dbVote) {
		super(getIcon(dbVote));
		this.player = player;
	}
	
	private static ItemStack getIcon(DBVote dbVote) {
		ItemStack is = new ItemStack(Material.ENDER_CHEST);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"投票ステータス");
		ArrayList<String> lores = new ArrayList<>();
		lores.add(ChatColor.GOLD+""+ChatColor.BOLD+"ストリーク : "+dbVote.getStreak());
		lores.add(ChatColor.GREEN+""+ChatColor.BOLD+"合計投票回数 : "+dbVote.getTotal());
		lores.add(ChatColor.AQUA+""+ChatColor.BOLD+"最終投票日時 : "+dbVote.getLastVote().toLocaleString());
		im.setLore(lores);
		is.setItemMeta(im);
		return is;
	}

	@Override
	public GUIAction invClick(InventoryClickEvent e) {
		TextComponent tc = new TextComponent(ChatColor.AQUA+""+ChatColor.BOLD+"投票はこちらからできます。 Click here");
		tc.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://minecraft.jp/servers/play.rezxis.net/vote"));
		player.spigot().sendMessage(tc);
		return GUIAction.NO_ACTION;
	}
}
