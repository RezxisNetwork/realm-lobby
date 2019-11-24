package net.rezxis.mchosting.lobby;

import java.net.URI;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.google.gson.Gson;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.databse.DBPlayer;
import net.rezxis.mchosting.databse.DBServer;
import net.rezxis.mchosting.databse.Database;
import net.rezxis.mchosting.databse.tables.FilesTable;
import net.rezxis.mchosting.databse.tables.PlayersTable;
import net.rezxis.mchosting.databse.tables.ServersTable;
import net.rezxis.mchosting.network.WSClient;
import net.rezxis.mchosting.network.packet.sync.SyncPlayerSendPacket;

public class Lobby extends JavaPlugin {

	public static Lobby instance;
	public WSClient ws;
	public ServersTable sTable;
	public PlayersTable pTable;
	public FilesTable fTable;
	public Props props;
	public HashMap<UUID,DBPlayer> players = new HashMap<>();
	public HashMap<UUID,Scoreboard> boards = new HashMap<>();
	
	@Override
	public void onEnable() {
		instance = this;
		props = new Props("hosting.propertis");
		//connect DB and get servers which online from DB
		Database.init();
		sTable = new ServersTable();
		pTable = new PlayersTable();
		fTable = new FilesTable();
		Bukkit.getPluginManager().registerEvents(new ServerListener(),this);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable() {
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					DBPlayer player = players.get(p.getUniqueId());
					player.sync();
					Scoreboard board = boards.get(p.getUniqueId());
					board.getObjective("info").unregister();;
					Objective obj = board.registerNewObjective("info", "dummy");
					BoardListeners.updateBoard(obj, player);
					p.setScoreboard(board);
				}
			}
		}, 0L, 20L*4);
		
		new Thread(()->{
				try {
					ws = new WSClient(new URI(props.SYNC_ADDRESS), new WSClientHandler());
				} catch (Exception e) {
					e.printStackTrace();
				}
				ws.connect();
			
		}).start();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		return CommandHandler.onCommand(sender, cmd, commandLabel, args);
	}
	
	public void connect(Player player, DBServer dest) {
		player.sendMessage(ChatColor.AQUA+"接続中");
		SyncPlayerSendPacket sPacket = new SyncPlayerSendPacket(player.getUniqueId().toString(),dest.getDisplayName());
		ws.send(new Gson().toJson(sPacket));
	}
	
	public void connect(Player player) {
		player.sendMessage(ChatColor.AQUA+"接続中");
		DBServer server = sTable.get(player.getUniqueId());
		SyncPlayerSendPacket sPacket = new SyncPlayerSendPacket(player.getUniqueId().toString(),server.getDisplayName());
		ws.send(new Gson().toJson(sPacket));
	}
}
