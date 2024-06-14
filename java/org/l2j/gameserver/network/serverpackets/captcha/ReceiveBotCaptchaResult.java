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
package org.l2j.gameserver.network.serverpackets.captcha;

import org.l2j.commons.network.WritableBuffer;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

public class ReceiveBotCaptchaResult extends ServerPacket
{
	public static final ReceiveBotCaptchaResult SUCCESS = new ReceiveBotCaptchaResult(1);
	public static final ReceiveBotCaptchaResult FAILED = new ReceiveBotCaptchaResult(0);
	
	private final int _answer;
	
	private ReceiveBotCaptchaResult(int answer)
	{
		_answer = answer;
	}
	
	@Override
	protected void writeImpl(GameClient client, WritableBuffer buffer)
	{
		ServerPackets.RECEIVE_VIP_BOT_CAPTCHA_ANSWER_RESULT.writeId(this, buffer);
		buffer.writeInt(_answer);
	}
}
