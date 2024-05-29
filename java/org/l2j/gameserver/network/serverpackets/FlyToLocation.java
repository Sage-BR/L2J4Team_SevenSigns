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

import org.l2j.gameserver.enums.FlyType;
import org.l2j.gameserver.model.actor.Creature;
import org.l2j.gameserver.model.interfaces.ILocational;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author KenM
 */
public class FlyToLocation extends ServerPacket
{
	private final int _destX;
	private final int _destY;
	private final int _destZ;
	private final int _chaObjId;
	private final int _orgX;
	private final int _orgY;
	private final int _orgZ;
	private final FlyType _type;
	private int _flySpeed;
	private int _flyDelay;
	private int _animationSpeed;
	
	public FlyToLocation(Creature creature, int destX, int destY, int destZ, FlyType type)
	{
		_chaObjId = creature.getObjectId();
		_orgX = creature.getX();
		_orgY = creature.getY();
		_orgZ = creature.getZ();
		_destX = destX;
		_destY = destY;
		_destZ = destZ;
		_type = type;
		if (creature.isPlayer())
		{
			creature.getActingPlayer().setBlinkActive(true);
		}
	}
	
	public FlyToLocation(Creature creature, int destX, int destY, int destZ, FlyType type, int flySpeed, int flyDelay, int animationSpeed)
	{
		_chaObjId = creature.getObjectId();
		_orgX = creature.getX();
		_orgY = creature.getY();
		_orgZ = creature.getZ();
		_destX = destX;
		_destY = destY;
		_destZ = destZ;
		_type = type;
		_flySpeed = flySpeed;
		_flyDelay = flyDelay;
		_animationSpeed = animationSpeed;
		if (creature.isPlayer())
		{
			creature.getActingPlayer().setBlinkActive(true);
		}
	}
	
	public FlyToLocation(Creature creature, ILocational dest, FlyType type)
	{
		this(creature, dest.getX(), dest.getY(), dest.getZ(), type);
	}
	
	public FlyToLocation(Creature creature, ILocational dest, FlyType type, int flySpeed, int flyDelay, int animationSpeed)
	{
		this(creature, dest.getX(), dest.getY(), dest.getZ(), type, flySpeed, flyDelay, animationSpeed);
	}
	
	@Override
	public void write()
	{
		ServerPackets.FLY_TO_LOCATION.writeId(this);
		writeInt(_chaObjId);
		writeInt(_destX);
		writeInt(_destY);
		writeInt(_destZ);
		writeInt(_orgX);
		writeInt(_orgY);
		writeInt(_orgZ);
		writeInt(_type.ordinal());
		writeInt(_flySpeed);
		writeInt(_flyDelay);
		writeInt(_animationSpeed);
	}
}
