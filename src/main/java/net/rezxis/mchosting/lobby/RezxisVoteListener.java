package net.rezxis.mchosting.lobby;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.vexsoftware.votifier.Votifier;
import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VoteListener;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.database.Tables;
import net.rezxis.mchosting.database.crates.CrateTypes;
import net.rezxis.mchosting.database.object.player.DBPlayer;
import net.rezxis.mchosting.database.object.player.DBPlayer.Rank;
import net.rezxis.mchosting.lobby.vote.VoteBonus;
import net.rezxis.mchosting.database.object.player.DBVote;

public class RezxisVoteListener implements VoteListener {
	
	public static RezxisVoteListener i;
	
	public RezxisVoteListener() {
		i = this;
	}
	
	@Override
	public void voteMade(Vote vote) {
		OfflinePlayer player = Bukkit.getOfflinePlayer(vote.getUsername());
		DBVote dv = Tables.getVTable().getVoteByUUID(player.getUniqueId());
		boolean exists = true;
		if (dv == null) {
			Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Japan"),Locale.JAPANESE);
			c.setTime(new Date());
			c.add(Calendar.DAY_OF_MONTH, -2);
			dv = new DBVote(-1,player.getUniqueId(),0,0,new Date(), c.getTime());
		}
		//add
		int type = dv.getTotal() % 7;
		VoteBonus.give(player, dv, type);
		dv.setTotal(dv.getTotal()+1);
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Japan"), Locale.JAPANESE);
		c.add(Calendar.DAY_OF_WEEK, -1);
		if (dv.getLastVote().after(c.getTime())) {
			dv.setStreak(dv.getStreak()+1);
		} else {
			dv.setStreak(1);
		}
		dv.setLastVote(Calendar.getInstance(TimeZone.getTimeZone("Japan"), Locale.JAPANESE).getTime());
		if (player.isOnline()) {
			Player p = Bukkit.getPlayer(player.getUniqueId());
			p.sendMessage(ChatColor.AQUA+""+ChatColor.BOLD+"投票ありがとう互いざいます。");
			p.sendMessage(ChatColor.AQUA+""+ChatColor.BOLD+"投票報酬を獲得しました。詳細は/voteinfo で確認できます。");
		}
		if (!exists) {
			Tables.getVTable().insert(dv);
		} else {
			dv.update();
		}
	}
	
	
	
	/*@Override
	public void voteMade(Vote vote) {
		@SuppressWarnings("deprecation")
		OfflinePlayer player = Bukkit.getOfflinePlayer(vote.getUsername());
		if (Tables.getPTable().get(player.getUniqueId()) == null)
			Tables.getPTable().insert(new DBPlayer(-1, player.getUniqueId(), Rank.NORMAL, 0, false, new Date(), new Date(), false, false ,"",false,false,new Date(),"",0,"",-1));
		DBPlayer dp = Tables.getPTable().get(player.getUniqueId());
		dp.addCoin(1000);
		dp.update();
		Tables.getCTable().giveCrate(player.getUniqueId(), CrateTypes.VOTE);
		Votifier.getInstance().getLogger().log(Level.INFO, vote.getUsername()+" has voted!");
		if (player.isOnline()) {
			Player p = Bukkit.getPlayer(vote.getUsername());
			p.sendMessage(ChatColor.AQUA+"投票ありがとうございました。報酬として1000RealmCoinと報酬箱を受け取りました。");
		}
	}*/
}
