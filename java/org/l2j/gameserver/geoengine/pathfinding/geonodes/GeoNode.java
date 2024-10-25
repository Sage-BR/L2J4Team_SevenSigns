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
package org.l2j.gameserver.geoengine.pathfinding.geonodes;

import org.l2j.gameserver.geoengine.pathfinding.AbstractNode;

/**
 * @author -Nemesiss-
 */
public class GeoNode extends AbstractNode<GeoNodeLoc>
{
	private final int _neighborsIdx;
	private short _cost;
	private GeoNode[] _neighbors;
	
	public GeoNode(GeoNodeLoc loc, int neighborsIdx)
	{
		super(loc);
		_neighborsIdx = neighborsIdx;
	}
	
	public short getCost()
	{
		return _cost;
	}
	
	public void setCost(int cost)
	{
		_cost = (short) cost;
	}
	
	public GeoNode[] getNeighbors()
	{
		return _neighbors;
	}
	
	public void attachNeighbors(GeoNode[] neighbors)
	{
		_neighbors = neighbors;
	}
	
	public int getNeighborsIdx()
	{
		return _neighborsIdx;
	}
}
