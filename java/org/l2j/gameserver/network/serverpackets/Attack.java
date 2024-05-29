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
import java.util.Iterator;
import java.util.List;

import org.l2j.gameserver.enums.BroochJewel;
import org.l2j.gameserver.model.Hit;
import org.l2j.gameserver.model.Location;
import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.ServerPackets;

public class Attack extends ServerPacket
{
	private final int _attackerObjId;
	private final Location _attackerLoc;
	private final Location _targetLoc;
	private final List<Hit> _hits = new ArrayList<>();
	private final int _soulshotVisualSubstitute;
	
	/**
	 * @param attacker
	 * @param target
	 */
	public Attack(Creature attacker, Creature target)
	{
		_attackerObjId = attacker.getObjectId();
		_attackerLoc = new Location(attacker);
		_targetLoc = new Location(target);
		final Player player = attacker.getActingPlayer();
		if (player == null)
		{
			_soulshotVisualSubstitute = 0;
		}
		else
		{
			final BroochJewel activeRuby = player.getActiveRubyJewel();
			final BroochJewel activeShappire = player.getActiveShappireJewel();
			if (activeRuby != null)
			{
				_soulshotVisualSubstitute = activeRuby.getItemId();
			}
			else if (activeShappire != null)
			{
				_soulshotVisualSubstitute = activeShappire.getItemId();
			}
			else
			{
				_soulshotVisualSubstitute = 0;
			}
		}
	}
	
	/**
	 * Adds hit to the attack (Attacks such as dual dagger/sword/fist has two hits)
	 * @param hit
	 */
	public void addHit(Hit hit)
	{
		_hits.add(hit);
	}
	
	public List<Hit> getHits()
	{
		return _hits;
	}
	
	/**
	 * @return {@code true} if current attack contains at least 1 hit.
	 */
	public boolean hasHits()
	{
		return !_hits.isEmpty();
	}
	
	/**
	 * Writes current hit
	 * @param hit
	 */
	private void writeHit(Hit hit)
	{
		writeInt(hit.getTargetId());
		writeInt(hit.getDamage());
		writeInt(hit.getFlags());
		writeInt(hit.getGrade()); // GOD
	}
	
	@Override
	public void write()
	{
		final Iterator<Hit> it = _hits.iterator();
		final Hit firstHit = it.next();
		ServerPackets.ATTACK.writeId(this);
		writeInt(_attackerObjId);
		writeInt(firstHit.getTargetId());
		writeInt(_soulshotVisualSubstitute); // Ertheia
		writeInt(firstHit.getDamage());
		writeInt(firstHit.getFlags());
		writeInt(firstHit.getGrade()); // GOD
		writeInt(_attackerLoc.getX());
		writeInt(_attackerLoc.getY());
		writeInt(_attackerLoc.getZ());
		writeShort(_hits.size() - 1);
		while (it.hasNext())
		{
			writeHit(it.next());
		}
		writeInt(_targetLoc.getX());
		writeInt(_targetLoc.getY());
		writeInt(_targetLoc.getZ());
	}
}
