/*
 * This file is part of the L2J 4Team Project.
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
package org.l2j.gameserver.network.serverpackets.enchant.single;

import java.util.ArrayList;
import java.util.List;

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author Serenitty
 */
public class ExChangedEnchantTargetItemProbList extends ServerPacket
{
	private final List<EnchantProbInfo> _probList;
	
	public ExChangedEnchantTargetItemProbList(List<EnchantProbInfo> probList)
	{
		_probList = probList;
	}
	
	public ExChangedEnchantTargetItemProbList(EnchantProbInfo probInfo)
	{
		_probList = new ArrayList<>();
		_probList.add(probInfo);
	}
	
	@Override
	public void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.EX_CHANGED_ENCHANT_TARGET_ITEM_PROB_LIST.writeId(this, buffer);
		
		buffer.writeInt(_probList.size()); // vProbList;
		for (EnchantProbInfo info : _probList)
		{
			buffer.writeInt(info.itemObjId); // nItemServerId;
			buffer.writeInt(info.totalSuccessProb);// nTotalSuccessProbPermyriad;
			buffer.writeInt(info.baseProb);// nBaseProbPermyriad;
			buffer.writeInt(info.supportProb);// nSupportProbPermyriad;
			buffer.writeInt(info.itemSkillProb);// nItemSkillProbPermyriad;
		}
	}
	
	public static class EnchantProbInfo
	{
		int itemObjId;
		int totalSuccessProb;
		int baseProb;
		int supportProb;
		int itemSkillProb;
		
		public EnchantProbInfo(int itemObjId, int totalSuccessProb, int baseProb, int supportProb, int itemSkillProb)
		{
			this.itemObjId = itemObjId;
			this.totalSuccessProb = Math.min(10000, totalSuccessProb);
			this.baseProb = baseProb;
			this.supportProb = supportProb;
			this.itemSkillProb = itemSkillProb;
		}
	}
}