package net.rezxis.mchosting.lobby;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.google.gson.Gson;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.database.Tables;
import net.rezxis.mchosting.database.object.KeyValue;
import net.rezxis.mchosting.database.object.player.DBPlayer;
import net.rezxis.mchosting.database.object.player.DBVote;
import net.rezxis.mchosting.database.object.player.DBPlayer.Rank;

public class HoloManager {

	private static Gson gson = new Gson();
	private static Hologram voteRanking;
	private static final String dbLocVoteRanking = "location_lobby_voteRanking";
	private static Hologram serverVoteRanking;
	private static final String dbLocServerVoteRanking = "location_lobby_serverVoteRanking";
	private static JavaPlugin plugin;
	
	@SuppressWarnings("deprecation")
	public static void init(JavaPlugin p) {
		plugin = p;
		KeyValue locVoteRanking = Tables.getRezxisKVTable().get(dbLocVoteRanking);
		KeyValue locServerVoteRanking = Tables.getRezxisKVTable().get(dbLocServerVoteRanking);
		if (locVoteRanking != null) {
			voteRanking = HologramsAPI.createHologram(plugin, parseLocation(locVoteRanking.getValue()));
			updateVoteRanking();
		}
		if (locServerVoteRanking != null) {
			
		}
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new BukkitRunnable() {
			@Override
			public void run() {
				updateVoteRanking();
			}}, 20 * 5, 20 * 5);
	}
	
	private static void updateVoteRanking() {
		voteRanking.clearLines();
		voteRanking.appendTextLine(ChatColor.GOLD+""+ChatColor.BOLD+"----==投票回数ランキング==----");
		int i = 1;
		for (DBVote vote : Tables.getVTable().getVoteWithLimit(10)) {
			String name = "";
			if (Bukkit.getPlayer(vote.getUuid()) != null) {
				name = Bukkit.getPlayer(vote.getUuid()).getName();
			} else {
				name = Tables.getUTable().get(vote.getUuid()).getName();
			}
			String prefix = ChatColor.RESET+""+name;
			DBPlayer player = Tables.getPTable().get(vote.getUuid());
			if (player.isSupporter() && !player.getPrefix().isEmpty()) {
				prefix = player.getPrefix()+" "+name;
			} else if (player.getRank() != Rank.NORMAL) {
				prefix = player.getRank().getPrefix()+" "+name;
			}
				voteRanking.appendTextLine(ChatColor.GREEN+String.valueOf(i)+" : "+prefix);
			i++;
		}
	}
	
	public static void handleCommand(String[] args, Player player) {
		if (args.length == 0) {
			player.sendMessage("invalid[0]");
			return;
		} else if (args[0].equalsIgnoreCase("vote")) {
			KeyValue kv = Tables.getRezxisKVTable().get(dbLocVoteRanking);
			boolean n = false;
			if (kv == null) {
				n = true;
				kv = new KeyValue(-1,dbLocVoteRanking,"");
			}
			kv.setValue(gson.toJson(player.getLocation().serialize()));
			if (n) {
				Tables.getRezxisKVTable().insert(kv);
			} else {
				kv.update();
			}
		} else {
			player.sendMessage("invalid[1]");
		}
	}
	
	@SuppressWarnings("unchecked")
	private static Location parseLocation(String val) {
		return Location.deserialize(gson.fromJson(val, Map.class));
	}
}
