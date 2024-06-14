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
package org.l2j.gameserver.network.clientpackets.captcha;

import org.l2j.gameserver.data.BotReportTable;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.actor.request.CaptchaRequest;
import org.l2j.gameserver.model.captcha.Captcha;
import org.l2j.gameserver.model.captcha.CaptchaGenerator;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.SystemMessage;
import org.l2j.gameserver.network.serverpackets.captcha.ReceiveBotCaptchaImage;
import org.l2j.gameserver.network.serverpackets.captcha.ReceiveBotCaptchaResult;

public class RequestCaptchaAnswer extends ClientPacket
{
	private int _answer;
	
	@Override
	protected void readImpl()
	{
		readLong(); // captchaId not needed since we store the information on CaptchaRequest
		_answer = readInt();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		CaptchaRequest request = player.getRequest(CaptchaRequest.class);
		if (request != null)
		{
			if (_answer == request.getCaptcha().getCode())
			{
				player.sendPacket(ReceiveBotCaptchaResult.SUCCESS);
				player.sendPacket(SystemMessageId.IDENTIFICATION_COMPLETED_HAVE_A_GOOD_TIME_WITH_LINEAGE_II_THANK_YOU);
				request.cancelTimeout();
				player.removeRequest(CaptchaRequest.class);
			}
			else
			{
				onWrongCode(player, request);
			}
		}
		else
		{
			final Captcha captcha = CaptchaGenerator.getInstance().next();
			request = new CaptchaRequest(player, captcha);
			player.addRequest(request);
			player.sendPacket(new ReceiveBotCaptchaImage(captcha, request.getRemainingTime()));
		}
	}
	
	private void onWrongCode(Player player, CaptchaRequest request)
	{
		if (request.isLimitReached())
		{
			request.cancelTimeout();
			BotReportTable.getInstance().punishBotDueUnsolvedCaptcha(player);
		}
		else
		{
			final Captcha captcha = CaptchaGenerator.getInstance().next();
			request.newRequest(captcha);
			player.sendPacket(new ReceiveBotCaptchaImage(captcha, request.getRemainingTime()));
			final SystemMessage msg = new SystemMessage(SystemMessageId.WRONG_AUTHENTICATION_CODE_IF_YOU_ENTER_THE_WRONG_CODE_S1_TIME_S_THE_SYSTEM_WILL_QUALIFY_YOU_AS_A_PROHIBITED_SOFTWARE_USER_AND_CHARGE_A_PENALTY_ATTEMPTS_LEFT_S2);
			msg.addInt(request.maxAttemps());
			msg.addInt(request.remainingAttemps());
			player.sendPacket(msg);
		}
	}
}
