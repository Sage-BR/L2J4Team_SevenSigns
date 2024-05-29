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
package org.l2j.gameserver.network.clientpackets;

import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.data.xml.BeautyShopData;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.beautyshop.BeautyData;
import org.l2j.gameserver.model.beautyshop.BeautyItem;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.serverpackets.ExResponseBeautyRegistReset;

/**
 * @author Sdw
 */
public class RequestShowResetShopList implements ClientPacket
{
	private int _hairId;
	private int _faceId;
	private int _colorId;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_hairId = packet.readInt();
		_faceId = packet.readInt();
		_colorId = packet.readInt();
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		final BeautyData beautyData = BeautyShopData.getInstance().getBeautyData(player.getRace(), player.getAppearance().getSexType());
		int requiredAdena = 0;
		if (_hairId > 0)
		{
			final BeautyItem hair = beautyData.getHairList().get(_hairId);
			if (hair == null)
			{
				player.sendPacket(new ExResponseBeautyRegistReset(player, ExResponseBeautyRegistReset.RESTORE, ExResponseBeautyRegistReset.FAILURE));
				return;
			}
			
			requiredAdena += hair.getResetAdena();
			if (_colorId > 0)
			{
				final BeautyItem color = hair.getColors().get(_colorId);
				if (color == null)
				{
					player.sendPacket(new ExResponseBeautyRegistReset(player, ExResponseBeautyRegistReset.RESTORE, ExResponseBeautyRegistReset.FAILURE));
					return;
				}
				
				requiredAdena += color.getResetAdena();
			}
		}
		
		if (_faceId > 0)
		{
			final BeautyItem face = beautyData.getFaceList().get(_faceId);
			if (face == null)
			{
				player.sendPacket(new ExResponseBeautyRegistReset(player, ExResponseBeautyRegistReset.RESTORE, ExResponseBeautyRegistReset.FAILURE));
				return;
			}
			
			requiredAdena += face.getResetAdena();
		}
		
		if ((player.getAdena() < requiredAdena))
		{
			player.sendPacket(new ExResponseBeautyRegistReset(player, ExResponseBeautyRegistReset.RESTORE, ExResponseBeautyRegistReset.FAILURE));
			return;
		}
		
		if ((requiredAdena > 0) && !player.reduceAdena(getClass().getSimpleName(), requiredAdena, null, true))
		{
			player.sendPacket(new ExResponseBeautyRegistReset(player, ExResponseBeautyRegistReset.RESTORE, ExResponseBeautyRegistReset.FAILURE));
			return;
		}
		
		player.getVariables().remove("visualHairId");
		player.getVariables().remove("visualHairColorId");
		player.getVariables().remove("visualFaceId");
		
		player.sendPacket(new ExResponseBeautyRegistReset(player, ExResponseBeautyRegistReset.RESTORE, ExResponseBeautyRegistReset.SUCCESS));
	}
}