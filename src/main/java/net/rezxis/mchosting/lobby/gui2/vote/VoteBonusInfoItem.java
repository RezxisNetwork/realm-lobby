package net.rezxis.mchosting.lobby.gui2.vote;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.TimeZone;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.rezxis.mchosting.database.object.player.DBVote;
import net.rezxis.mchosting.gui.GUIAction;
import net.rezxis.mchosting.gui.GUIItem;
import net.rezxis.mchosting.lobby.vote.VoteBonus;
import net.rezxis.mchosting.lobby.vote.VoteBonus.Action;

public class VoteBonusInfoItem extends GUIItem {

	public VoteBonusInfoItem(DBVote dv, int kind) {
		super(getIcon(dv, kind));
	}
	
	private static ItemStack getIcon(DBVote dv, int kind) {
		String name;
		short damage = 0;
		if (dv.getTotal() == 0) {
			damage = 14;
			name = ChatColor.RED+"";
		} else {
			if ((dv.getTotal() % 7) -1 >= kind) {
				damage = 5;
				name = ChatColor.GREEN+"";
			} else {
				if (dv.getTotal() != 0 && dv.getTotal() % 7 == 0) {
					damage = 5;
					name = ChatColor.GREEN+"";
				} else {
					damage = 14;
					name = ChatColor.RED+"";
				}
			}
		}
		ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE,1,damage);
		ItemMeta im = is.getItemMeta();
		ArrayList<String> lore = new ArrayList<>();
		lore.add(ChatColor.AQUA+""+ChatColor.BOLD+"報酬");
		for (Entry<Action,Integer> e : VoteBonus.bonuses.get(kind).action.entrySet()) {
			if (e.getKey() == Action.VOTEBOX) {
				lore.add(ChatColor.GREEN+"- 投票箱 x "+e.getValue());
			} else if (e.getKey() == Action.MYSTERY4) {
				lore.add(ChatColor.GREEN+"- MysteryBox 4Stars x "+e.getValue());
			} else if (e.getKey() == Action.MYSTERY5) {
				lore.add(ChatColor.GREEN+"- MysteryBox 5Stars x "+e.getValue());
			} else if (e.getKey() == Action.RANK) {
				lore.add(ChatColor.GREEN+"- 一日間報酬二倍");
				if (dv.hasRank()) {
					Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Japan"), Locale.JAPANESE);
					c.setTime(dv.getRank());
					lore.add(ChatColor.RED+"有効期限 : "+c.getTime().toLocaleString());
				}
			}
		}
		im.setDisplayName(name+""+(kind+1)+"日目");
		im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}

	@Override
	public GUIAction invClick(InventoryClickEvent e) {
		return GUIAction.NO_ACTION;
	}
}
