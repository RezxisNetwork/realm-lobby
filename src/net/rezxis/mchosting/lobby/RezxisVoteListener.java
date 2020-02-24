package net.rezxis.mchosting.lobby;

import java.util.Date;
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

public class RezxisVoteListener implements VoteListener {
	
	public RezxisVoteListener() {
	}
	
	@Override
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
	}
}
