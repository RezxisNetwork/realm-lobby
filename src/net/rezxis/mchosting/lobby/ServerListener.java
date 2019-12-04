package net.rezxis.mchosting.lobby;

import net.rezxis.mchosting.databse.DBPlayer;
import net.rezxis.mchosting.databse.DBPlayer.Rank;
import net.rezxis.mchosting.lobby.gui2.crate.CrateMenu;

import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import net.rezxis.mchosting.lobby.gui2.main.MainMenu;

public class ServerListener implements Listener {

	public static ItemStack menu;
	
	public ServerListener() {
		menu = new ItemStack(Material.NETHER_STAR);
		ItemMeta im = menu.getItemMeta();
		im.setDisplayName(ChatColor.GOLD+""+ChatColor.BOLD+"Main Menu");
		menu.setItemMeta(im);
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		event.getPlayer().getInventory().setItem(4, menu);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		event.getPlayer().getInventory().setItem(4, menu);
		DBPlayer player = Lobby.instance.pTable.get(event.getPlayer().getUniqueId());
		if (player == null) {
			player = new DBPlayer(-1, event.getPlayer().getUniqueId(), Rank.NORMAL, 0, false, new Date());
			Lobby.instance.pTable.insert(player);
		}
		if (player.isExpiredRank()) {
			player.setRank(Rank.NORMAL);
			player.setOfflineBoot(false);
			player.update();
			event.getPlayer().sendMessage(ChatColor.RED+"Rankの期限が切れました。");
		}
		for (Scoreboard board : Lobby.instance.boards.values()) {
			board.getTeam(player.getRank().name()).addEntry(event.getPlayer().getName());
		}
		Lobby.instance.players.put(event.getPlayer().getUniqueId(), player);
		Scoreboard board = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
		for (Rank rank : Rank.values()) {
			if (board.getTeam(rank.name()) == null) {
				Team team = board.registerNewTeam(rank.name());
				team.setPrefix(rank.getPrefix());
			}
		}
		for (Player pp : Bukkit.getOnlinePlayers()) {
			DBPlayer dp = Lobby.instance.pTable.get(pp.getUniqueId());
			Team team = board.getTeam(dp.getRank().name());
			team.addEntry(pp.getName());
		}
		
		Objective obj = board.registerNewObjective("info", "dummy");
		updateBoard(obj, player);
		event.getPlayer().setScoreboard(board);
		Lobby.instance.boards.put(event.getPlayer().getUniqueId(), board);
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		DBPlayer player = Lobby.instance.players.get(event.getPlayer().getUniqueId());
		if (player == null) {
			player = Lobby.instance.pTable.get(event.getPlayer().getUniqueId());
			Lobby.instance.players.put(event.getPlayer().getUniqueId(), player);
		}
		if (player.getRank() != Rank.NORMAL) {
			event.setFormat(player.getRank().getPrefix()+" "+event.getPlayer().getName()+ChatColor.WHITE+" : "+event.getMessage());
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if (event.getClickedBlock() != null) {
				if (event.getClickedBlock().getType() == Material.TRAPPED_CHEST) {
					new CrateMenu(event.getPlayer()).delayShow();
				}
			}
		}
		if (event.getItem() != null)
			if (event.getItem().equals(menu))
				new MainMenu(event.getPlayer()).delayShow();
			
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (event.getWhoClicked().getGameMode() != GameMode.CREATIVE) {
			event.setCancelled(true);
		}
	}
	
	public static Objective updateBoard(Objective obj, DBPlayer player) {
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(ChatColor.AQUA+"Rezxis Network");
		obj.getScore(" ").setScore(6);
		obj.getScore("  ").setScore(5);
		if (player.getRank() != Rank.NORMAL) {
			obj.getScore(ChatColor.GOLD+"Rank "+ChatColor.WHITE+": "+player.getRank().getPrefix()).setScore(4);
		} else {
			obj.getScore(ChatColor.GOLD+"Rank "+ChatColor.WHITE+": [NORMAL]").setScore(4);
		}
		obj.getScore("   ").setScore(3);
		obj.getScore(ChatColor.AQUA+"RealmCoin "+ChatColor.WHITE+": "+ChatColor.AQUA+player.getCoin()).setScore(2);
		obj.getScore("    ").setScore(1);
		obj.getScore(ChatColor.GOLD+"PLAY.REZXIS.NET").setScore(0);
		return obj;
	}
}