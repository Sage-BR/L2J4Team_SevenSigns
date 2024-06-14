/*
 * This file is part of the L2J Mobius project.
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
package org.l2jmobius.gameserver.model;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.variables.PlayerVariables;

/**
 * @author Index
 */
public class ClientSettings
{
	private final Player _player;
	private boolean _announceEnabled;
	private boolean _partyRequestRestrictedFromOthers;
	private boolean _partyRequestRestrictedFromClan;
	private boolean _partyRequestRestrictedFromFriends;
	private boolean _friendRequestRestrictedFromOthers;
	private boolean _friendRequestRestrictedFromClan;
	private int _partyContributionType;
	
	public ClientSettings(Player player)
	{
		_player = player;
		
		final String variable = _player.getVariables().getString(PlayerVariables.CLIENT_SETTINGS, "");
		final StatSet settings = variable.isEmpty() ? new StatSet() : new StatSet(Arrays.stream(variable.split(",")).map(entry -> entry.split("=")).collect(Collectors.toMap(entry -> entry[0].replace("{", "").replace(" ", ""), entry -> entry[1].replace("}", "").replace(" ", ""))));
		_announceEnabled = settings.getBoolean("ANNOUNCE_ENABLED", true);
		_partyRequestRestrictedFromOthers = settings.getBoolean("PARTY_REQUEST_RESTRICTED_FROM_OTHERS", false);
		_partyRequestRestrictedFromClan = settings.getBoolean("PARTY_REQUEST_RESTRICTED_FROM_CLAN", false);
		_partyRequestRestrictedFromFriends = settings.getBoolean("PARTY_REQUEST_RESTRICTED_FROM_FRIENDS", false);
		_friendRequestRestrictedFromOthers = settings.getBoolean("FRIENDS_REQUEST_RESTRICTED_FROM_OTHERS", false);
		_friendRequestRestrictedFromClan = settings.getBoolean("FRIENDS_REQUEST_RESTRICTED_FROM_CLAN", false);
		_partyContributionType = settings.getInt("PARTY_CONTRIBUTION_TYPE", 0);
	}
	
	public void storeSettings()
	{
		final StatSet settings = new StatSet();
		settings.set("ANNOUNCE_ENABLED", _announceEnabled);
		settings.set("PARTY_REQUEST_RESTRICTED_FROM_OTHERS", _partyRequestRestrictedFromOthers);
		settings.set("PARTY_REQUEST_RESTRICTED_FROM_CLAN", _partyRequestRestrictedFromClan);
		settings.set("PARTY_REQUEST_RESTRICTED_FROM_FRIENDS", _partyRequestRestrictedFromFriends);
		settings.set("FRIENDS_REQUEST_RESTRICTED_FROM_OTHERS", _friendRequestRestrictedFromOthers);
		settings.set("FRIENDS_REQUEST_RESTRICTED_FROM_CLAN", _friendRequestRestrictedFromClan);
		settings.set("PARTY_CONTRIBUTION_TYPE", _partyContributionType);
		_player.getVariables().set(PlayerVariables.CLIENT_SETTINGS, settings.getSet());
	}
	
	public boolean isAnnounceEnabled()
	{
		return _announceEnabled;
	}
	
	public void setAnnounceEnabled(boolean enabled)
	{
		_announceEnabled = enabled;
		storeSettings();
	}
	
	public boolean isPartyRequestRestrictedFromOthers()
	{
		return _partyRequestRestrictedFromOthers;
	}
	
	public void setPartyRequestRestrictedFromOthers(boolean partyRequestRestrictedFromOthers)
	{
		_partyRequestRestrictedFromOthers = partyRequestRestrictedFromOthers;
	}
	
	public boolean isPartyRequestRestrictedFromClan()
	{
		return _partyRequestRestrictedFromClan;
	}
	
	public void setPartyRequestRestrictedFromClan(boolean partyRequestRestrictedFromClan)
	{
		_partyRequestRestrictedFromClan = partyRequestRestrictedFromClan;
	}
	
	public boolean isPartyRequestRestrictedFromFriends()
	{
		return _partyRequestRestrictedFromFriends;
	}
	
	public void setPartyRequestRestrictedFromFriends(boolean partyRequestRestrictedFromFriends)
	{
		_partyRequestRestrictedFromFriends = partyRequestRestrictedFromFriends;
	}
	
	public boolean isFriendRequestRestrictedFromOthers()
	{
		return _friendRequestRestrictedFromOthers;
	}
	
	public void setFriendRequestRestrictedFromOthers(boolean friendRequestRestrictedFromOthers)
	{
		_friendRequestRestrictedFromOthers = friendRequestRestrictedFromOthers;
	}
	
	public boolean isFriendRequestRestrictedFromClan()
	{
		return _friendRequestRestrictedFromClan;
	}
	
	public void setFriendRequestRestrictionFromClan(boolean friendRequestRestrictedFromClan)
	{
		_friendRequestRestrictedFromClan = friendRequestRestrictedFromClan;
	}
	
	public int getPartyContributionType()
	{
		return _partyContributionType;
	}
	
	public void setPartyContributionType(int partyContributionType)
	{
		_partyContributionType = partyContributionType;
		storeSettings();
	}
}
