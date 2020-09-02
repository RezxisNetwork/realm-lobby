package net.rezxis.mchosting.lobby;

import java.net.URI;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.google.gson.Gson;
import com.mattmalec.pterodactyl4j.PteroBuilder;
import com.mattmalec.pterodactyl4j.application.entities.PteroApplication;
import com.vexsoftware.votifier.Votifier;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.database.Database;
import net.rezxis.mchosting.database.Tables;
import net.rezxis.mchosting.database.object.ServerWrapper;
import net.rezxis.mchosting.database.object.player.DBPlayer;
import net.rezxis.mchosting.database.object.server.DBServer;
import net.rezxis.mchosting.network.WSClient;
import net.rezxis.mchosting.network.packet.sync.SyncPlayerSendPacket;

public class Lobby extends JavaPlugin {

	public static Lobby instance;
	public WSClient ws;
	public Props props;
	public HashMap<UUID,DBPlayer> players = new HashMap<>();
	public HashMap<UUID,Scoreboard> boards = new HashMap<>();
	public HashMap<UUID, PermissionAttachment> perms = new HashMap<UUID, PermissionAttachment>();
	public PteroApplication api = new PteroBuilder().setApplicationUrl("https://panel.rezxis.net").setToken("NcLXBrShRAGu54z74NEAdueNGhcSp4lRQfHZ5KZ6ptFlgCH4").build().asApplication();
	
	@Override
	public void onEnable() {
		instance = this;
		props = new Props("hosting.propertis");
		//connect DB and get servers which online from DB
		//Database.init(props.DB_HOST,props.DB_USER,props.DB_PASS,props.DB_PORT,props.DB_NAME);
		Bukkit.getPluginManager().registerEvents(new ServerListener(),this);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable() {
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					DBPlayer player = players.get(p.getUniqueId());
					player.sync();
					Scoreboard board = boards.get(p.getUniqueId());
					if (board == null)
						continue;
					board.getObjective("info").unregister();;
					Objective obj = board.registerNewObjective("info", "dummy");
					ServerListener.updateBoard(obj, player);
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
		Votifier.getInstance().getListeners().add(new RezxisVoteListener());
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		return CommandHandler.onCommand(sender, cmd, commandLabel, args);
	}
	
	public void connect(Player player, ServerWrapper dest) {
		player.sendMessage(ChatColor.AQUA+"接続中");
		SyncPlayerSendPacket sPacket = new SyncPlayerSendPacket(player.getUniqueId().toString(),dest.getDisplayName());
		ws.send(new Gson().toJson(sPacket));
	}
	
	public void connect(Player player, DBServer dest) {
		player.sendMessage(ChatColor.AQUA+"接続中");
		SyncPlayerSendPacket sPacket = new SyncPlayerSendPacket(player.getUniqueId().toString(),dest.getDisplayName());
		ws.send(new Gson().toJson(sPacket));
	}
	
	public void connect(Player player) {
		player.sendMessage(ChatColor.AQUA+"接続中");
		DBServer server = Tables.getSTable().get(player.getUniqueId());
		SyncPlayerSendPacket sPacket = new SyncPlayerSendPacket(player.getUniqueId().toString(),server.getDisplayName());
		ws.send(new Gson().toJson(sPacket));
	}
}
