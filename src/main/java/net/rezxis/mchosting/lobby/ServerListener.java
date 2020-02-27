package net.rezxis.mchosting.lobby;

import net.rezxis.mchosting.database.Tables;
import net.rezxis.mchosting.database.object.player.DBPlayer;
import net.rezxis.mchosting.database.object.player.DBPlayer.Rank;
import net.rezxis.mchosting.lobby.gui2.crate.CrateMenu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.yapzhenyie.GadgetsMenu.api.GadgetsMenuAPI;
import com.yapzhenyie.GadgetsMenu.player.PlayerManager;

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
	public void onMove(PlayerMoveEvent event) {
		if (event.getTo().getY() < 0) {
			event.getPlayer().teleport(event.getPlayer().getLocation().getWorld().getSpawnLocation());
		}
	}
	
	@EventHandler
	public void onLeft(PlayerQuitEvent event) {
		event.setQuitMessage(null);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		event.setJoinMessage(null);
		event.getPlayer().getInventory().setItem(4, menu);
		DBPlayer player = Tables.getPTable().get(event.getPlayer().getUniqueId());
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
		PermissionAttachment attachment = event.getPlayer().addAttachment(Lobby.instance);
		PlayerManager gadPlayer = GadgetsMenuAPI.getPlayerManager(event.getPlayer());
		if (player.getVault() > 0) {
			gadPlayer.giveMysteryBoxes(System.currentTimeMillis()+(24 * 3600 * 1000 * 7), true, null, player.getVault());
			player.setVault(0);
			player.update();
		}
		Lobby.instance.perms.put(event.getPlayer().getUniqueId(), attachment);
		attachment.setPermission("gadgetsmenu.animations.*", true);
		attachment.setPermission("gadgetsmenu.mysteryboxes.open.1", true);
		attachment.setPermission("gadgetsmenu.mysteryboxes.open.2", true);
		attachment.setPermission("gadgetsmenu.mysteryboxes.open.3", true);
		if (player.isSupporter()) {
			attachment.setPermission("gadgetsmenu.mysteryboxes.open.4", true);
			attachment.setPermission("gadgetsmenu.mysteryboxes.open.5", true);
			attachment.setPermission("gadgetsmenu.multipleboxes.*", true);
		} else {
			attachment.setPermission("gadgetsmenu.mysteryboxes.open.4", false);
			attachment.setPermission("gadgetsmenu.mysteryboxes.open.5", false);
			attachment.setPermission("gadgetsmenu.multipleboxes.*", false);
		}
		attachment.setPermission("gadgetsmenu.menuselector", true);
		gadPlayer.giveMenuSelector();
		
		for (Player pp : Bukkit.getOnlinePlayers()) {
			DBPlayer dp = Tables.getPTable().get(pp.getUniqueId());
			Team team = board.getTeam(dp.getRank().name());
			team.addEntry(pp.getName());
		}
		GadgetsMenuAPI.getPlayerManager(event.getPlayer()).giveMenuSelector();
		Objective obj = board.registerNewObjective("info", "dummy");
		updateBoard(obj, player);
		event.getPlayer().setScoreboard(board);
		Lobby.instance.boards.put(event.getPlayer().getUniqueId(), board);
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		DBPlayer player = Lobby.instance.players.get(event.getPlayer().getUniqueId());
		if (player == null) {
			player = Tables.getPTable().get(event.getPlayer().getUniqueId());
			Lobby.instance.players.put(event.getPlayer().getUniqueId(), player);
		}
		if (player.isSupporter() && !player.getPrefix().isEmpty()) {
			event.setFormat(player.getPrefix()+" "+event.getPlayer().getName()+ChatColor.WHITE+" : "+event.getMessage());
			return;
		}
		if (player.getRank() != Rank.NORMAL) {
			event.setFormat(player.getRank().getPrefix()+" "+ChatColor.RESET+event.getPlayer().getName()+ChatColor.WHITE+" : "+event.getMessage());
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