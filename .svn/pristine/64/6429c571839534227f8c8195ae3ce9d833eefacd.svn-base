/*
 * This file is part of the L2J Mobius project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.l2jmobius.gameserver.network.clientpackets.newhenna;

import org.l2jmobius.commons.util.Rnd;
import org.l2jmobius.gameserver.data.xml.HennaCombinationData;
import org.l2jmobius.gameserver.data.xml.HennaData;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.holders.ItemHolder;
import org.l2jmobius.gameserver.model.item.combination.CombinationItemType;
import org.l2jmobius.gameserver.model.item.henna.CombinationHenna;
import org.l2jmobius.gameserver.model.item.henna.CombinationHennaReward;
import org.l2jmobius.gameserver.model.item.henna.Henna;
import org.l2jmobius.gameserver.model.itemcontainer.Inventory;
import org.l2jmobius.gameserver.network.PacketLogger;
import org.l2jmobius.gameserver.network.clientpackets.ClientPacket;
import org.l2jmobius.gameserver.network.serverpackets.InventoryUpdate;
import org.l2jmobius.gameserver.network.serverpackets.newhenna.NewHennaPotenCompose;

/**
 * @author Index, Serenitty
 */
public class RequestNewHennaCompose extends ClientPacket
{
	private int _slotOneIndex;
	private int _slotOneItemId;
	private int _slotTwoItemId;
	
	@Override
	protected void readImpl()
	{
		_slotOneIndex = readInt();
		_slotOneItemId = readInt();
		_slotTwoItemId = readInt();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		final Inventory inventory = player.getInventory();
		if ((player.getHenna(_slotOneIndex) == null) || ((_slotOneItemId != -1) && (inventory.getItemByObjectId(_slotOneItemId) == null)) || ((_slotTwoItemId != -1) && (inventory.getItemByObjectId(_slotTwoItemId) == null)))
		{
			return;
		}
		
		final Henna henna = player.getHenna(_slotOneIndex);
		final CombinationHenna combinationHennas = HennaCombinationData.getInstance().getByHenna(henna.getDyeId());
		if (combinationHennas == null)
		{
			player.sendPacket(new NewHennaPotenCompose(henna.getDyeId(), -1, false));
			return;
		}
		
		if (((_slotOneItemId != -1) && (combinationHennas.getItemOne() != inventory.getItemByObjectId(_slotOneItemId).getId())) || ((_slotTwoItemId != -1) && (combinationHennas.getItemTwo() != inventory.getItemByObjectId(_slotTwoItemId).getId())))
		{
			PacketLogger.info(getClass().getSimpleName() + ": player " + player.getName() + " - " + player.getObjectId() + " have modified client or combination data is outdated!");
		}
		
		final long commission = combinationHennas.getCommission();
		if (commission > player.getAdena())
		{
			return;
		}
		
		final ItemHolder one = new ItemHolder(combinationHennas.getItemOne(), combinationHennas.getCountOne());
		final ItemHolder two = new ItemHolder(combinationHennas.getItemTwo(), combinationHennas.getCountTwo());
		if (((_slotOneItemId != -1) && (inventory.getItemByItemId(one.getId()) == null) && (inventory.getItemByItemId(one.getId()).getCount() < one.getCount())) || ((_slotTwoItemId != -1) && (inventory.getItemByItemId(two.getId()) == null) && (inventory.getItemByItemId(two.getId()).getCount() < two.getCount())))
		{
			player.sendPacket(new NewHennaPotenCompose(henna.getDyeId(), -1, false));
			return;
		}
		
		final InventoryUpdate iu = new InventoryUpdate();
		if (_slotOneItemId != -1)
		{
			iu.addModifiedItem(inventory.getItemByItemId(one.getId()));
		}
		if (_slotTwoItemId != -1)
		{
			iu.addModifiedItem(inventory.getItemByItemId(two.getId()));
		}
		iu.addModifiedItem(inventory.getItemByItemId(Inventory.ADENA_ID));
		
		if (((_slotOneItemId != -1) && (inventory.destroyItemByItemId("Henna Improving", one.getId(), one.getCount(), player, null) == null)) || ((_slotTwoItemId != -1) && (inventory.destroyItemByItemId("Henna Improving", two.getId(), two.getCount(), player, null) == null)) || (inventory.destroyItemByItemId("Henna Improving", Inventory.ADENA_ID, commission, player, null) == null))
		{
			player.sendPacket(new NewHennaPotenCompose(henna.getDyeId(), -1, false));
			return;
		}
		
		if (Rnd.get(0, 100) <= combinationHennas.getChance())
		{
			final CombinationHennaReward reward = combinationHennas.getReward(CombinationItemType.ON_SUCCESS);
			player.removeHenna(_slotOneIndex, false);
			player.addHenna(_slotOneIndex, HennaData.getInstance().getHenna(reward.getHennaId()));
			player.addItem("Henna Improving", reward.getId(), reward.getCount(), null, false);
			player.sendPacket(new NewHennaPotenCompose(reward.getHennaId(), reward.getId() == 0 ? -1 : reward.getId(), true));
		}
		else
		{
			final CombinationHennaReward reward = combinationHennas.getReward(CombinationItemType.ON_FAILURE);
			if (henna.getDyeId() != reward.getHennaId())
			{
				player.removeHenna(_slotOneIndex, false);
				player.addHenna(_slotOneIndex, HennaData.getInstance().getHenna(reward.getHennaId()));
			}
			player.addItem("Henna Improving", reward.getId(), reward.getCount(), null, false);
			player.sendPacket(new NewHennaPotenCompose(reward.getHennaId(), reward.getId() == 0 ? -1 : reward.getId(), false));
		}
		player.sendPacket(iu);
	}
}
