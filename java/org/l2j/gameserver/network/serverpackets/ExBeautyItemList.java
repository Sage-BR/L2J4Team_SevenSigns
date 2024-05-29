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
package org.l2j.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.l2j.gameserver.data.xml.BeautyShopData;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.beautyshop.BeautyData;
import org.l2j.gameserver.model.beautyshop.BeautyItem;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author Sdw
 */
public class ExBeautyItemList extends ServerPacket
{
	private static final int HAIR_TYPE = 0;
	private static final int FACE_TYPE = 1;
	private static final int COLOR_TYPE = 2;
	
	private int _colorCount;
	private final BeautyData _beautyData;
	private final Map<Integer, List<BeautyItem>> _colorData = new HashMap<>();
	
	public ExBeautyItemList(Player player)
	{
		_beautyData = BeautyShopData.getInstance().getBeautyData(player.getRace(), player.getAppearance().getSexType());
		for (BeautyItem hair : _beautyData.getHairList().values())
		{
			final List<BeautyItem> colors = new ArrayList<>();
			for (BeautyItem color : hair.getColors().values())
			{
				colors.add(color);
				_colorCount++;
			}
			_colorData.put(hair.getId(), colors);
		}
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_BEAUTY_ITEM_LIST.writeId(this);
		writeInt(HAIR_TYPE);
		writeInt(_beautyData.getHairList().size());
		for (BeautyItem hair : _beautyData.getHairList().values())
		{
			writeInt(0); // ?
			writeInt(hair.getId());
			writeInt(hair.getAdena());
			writeInt(hair.getResetAdena());
			writeInt(hair.getBeautyShopTicket());
			writeInt(1); // Limit
		}
		writeInt(FACE_TYPE);
		writeInt(_beautyData.getFaceList().size());
		for (BeautyItem face : _beautyData.getFaceList().values())
		{
			writeInt(0); // ?
			writeInt(face.getId());
			writeInt(face.getAdena());
			writeInt(face.getResetAdena());
			writeInt(face.getBeautyShopTicket());
			writeInt(1); // Limit
		}
		writeInt(COLOR_TYPE);
		writeInt(_colorCount);
		for (Entry<Integer, List<BeautyItem>> entry : _colorData.entrySet())
		{
			for (BeautyItem color : entry.getValue())
			{
				writeInt(entry.getKey());
				writeInt(color.getId());
				writeInt(color.getAdena());
				writeInt(color.getResetAdena());
				writeInt(color.getBeautyShopTicket());
				writeInt(1);
			}
		}
	}
}
