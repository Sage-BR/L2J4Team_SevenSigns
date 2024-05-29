/*
 * This file is part of the L2J 4Team project.
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
package org.l2j.gameserver.network.clientpackets.newhenna;

import java.util.Map.Entry;
import java.util.Optional;

import org.l2j.commons.network.ReadablePacket;
import org.l2j.commons.util.Rnd;
import org.l2j.gameserver.data.xml.HennaPatternPotentialData;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.holders.ItemHolder;
import org.l2j.gameserver.model.item.henna.DyePotentialFee;
import org.l2j.gameserver.model.item.henna.HennaPoten;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.newhenna.NewHennaPotenEnchant;

/**
 * @author Index, Serenitty
 */
public class RequestNewHennaPotenEnchant implements ClientPacket
{
	private int _slotId;
	private int _costItemId;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_slotId = packet.readByte();
		_costItemId = packet.readInt();
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		int dailyStep = player.getDyePotentialDailyStep();
		final DyePotentialFee currentFee = HennaPatternPotentialData.getInstance().getFee(dailyStep);
		int dailyCount = player.getDyePotentialDailyCount();
		if ((_slotId < 1) || (_slotId > 4))
		{
			return;
		}
		
		final HennaPoten hennaPattern = player.getHennaPoten(_slotId);
		int enchantExp = hennaPattern.getEnchantExp();
		final int fullExpNeeded = HennaPatternPotentialData.getInstance().getExpForLevel(hennaPattern.getEnchantLevel());
		if ((enchantExp >= fullExpNeeded) && (hennaPattern.getEnchantLevel() == 20))
		{
			player.sendPacket(new NewHennaPotenEnchant(_slotId, hennaPattern.getEnchantLevel(), hennaPattern.getEnchantExp(), dailyStep, dailyCount, hennaPattern.getActiveStep(), true));
			return;
		}
		
		if ((currentFee == null) || (dailyCount <= 0))
		{
			return;
		}
		
		final Optional<ItemHolder> itemFee = currentFee.getItems().stream().filter(ih -> ih.getId() == _costItemId).findAny();
		if (itemFee.isEmpty() || !player.destroyItemByItemId(getClass().getSimpleName(), itemFee.get().getId(), itemFee.get().getCount(), player, true))
		{
			return;
		}
		
		dailyCount -= 1;
		if ((dailyCount <= 0) && (dailyStep != HennaPatternPotentialData.getInstance().getMaxPotenEnchantStep()))
		{
			dailyStep += 1;
			final DyePotentialFee newFee = HennaPatternPotentialData.getInstance().getFee(dailyStep);
			if (newFee != null)
			{
				dailyCount = 0;
			}
			player.setDyePotentialDailyCount(dailyCount);
			// player.setDyePotentialDailyStep(dailyStep);
		}
		else
		{
			player.setDyePotentialDailyCount(dailyCount);
		}
		
		double totalChance = 0;
		double random = Rnd.nextDouble() * 100;
		for (Entry<Integer, Double> entry : currentFee.getEnchantExp().entrySet())
		{
			totalChance += entry.getValue();
			if (random <= totalChance)
			{
				
				final int increase = entry.getKey();
				int newEnchantExp = hennaPattern.getEnchantExp() + increase;
				final int PatternExpNeeded = HennaPatternPotentialData.getInstance().getExpForLevel(hennaPattern.getEnchantLevel());
				if ((newEnchantExp >= PatternExpNeeded) && (hennaPattern.getEnchantLevel() < 20))
				{
					newEnchantExp -= PatternExpNeeded;
					if (hennaPattern.getEnchantLevel() < HennaPatternPotentialData.getInstance().getMaxPotenLevel())
					{
						hennaPattern.setEnchantLevel(hennaPattern.getEnchantLevel() + 1);
						player.applyDyePotenSkills();
					}
				}
				hennaPattern.setEnchantExp(newEnchantExp);
				hennaPattern.setSlotPosition(_slotId);
				player.sendPacket(new NewHennaPotenEnchant(_slotId, hennaPattern.getEnchantLevel(), hennaPattern.getEnchantExp(), dailyStep, dailyCount, hennaPattern.getActiveStep(), true));
				return;
			}
		}
	}
}
