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
package org.l2j.gameserver.model;

/**
 * @author malyelfik
 */
public class CropProcure extends SeedProduction
{
	private final int _rewardType;
	
	public CropProcure(int id, long amount, int type, long startAmount, long price)
	{
		super(id, amount, price, startAmount);
		_rewardType = type;
	}
	
	public int getReward()
	{
		return _rewardType;
	}
}