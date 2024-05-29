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
package org.l2j.gameserver.network.serverpackets.ranking;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.l2j.Config;

import org.l2j.gameserver.instancemanager.RankManager;
import org.l2j.gameserver.model.StatSet;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.network.ServerPackets;
import org.l2j.gameserver.network.serverpackets.ServerPacket;

/**
 * @author NviX
 */
public class ExRankingCharRankers extends ServerPacket
{
	private final Player _player;
	private final int _group;
	private final int _scope;
	private final int _race;
	private final int _class;
	private final Map<Integer, StatSet> _playerList;
	private final Map<Integer, StatSet> _snapshotList;
	
	public ExRankingCharRankers(Player player, int group, int scope, int race, int baseclass)
	{
		_player = player;
		_group = group;
		_scope = scope;
		_race = race;
		_class = baseclass;
		_playerList = RankManager.getInstance().getRankList();
		_snapshotList = RankManager.getInstance().getSnapshotList();
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_RANKING_CHAR_RANKERS.writeId(this);
		writeByte(_group);
		writeByte(_scope);
		writeInt(_race);
		writeInt(_player.getClassId().getId());
		if (!_playerList.isEmpty())
		{
			switch (_group)
			{
				case 0: // all
				{
					if (_scope == 0) // all
					{
						final int count = _playerList.size() > 150 ? 150 : _playerList.size();
						writeInt(count);
						for (Integer id : _playerList.keySet())
						{
							final StatSet player = _playerList.get(id);
							writeSizedString(player.getString("name"));
							writeSizedString(player.getString("clanName"));
							writeInt(Config.SERVER_ID);
							writeInt(player.getInt("level"));
							writeInt(player.getInt("classId"));
							writeInt(player.getInt("race"));
							writeInt(id); // server rank
							if (!_snapshotList.isEmpty())
							{
								for (Integer id2 : _snapshotList.keySet())
								{
									final StatSet snapshot = _snapshotList.get(id2);
									if (player.getInt("charId") == snapshot.getInt("charId"))
									{
										writeInt(id2); // server rank snapshot
										writeInt(snapshot.getInt("raceRank", 0)); // race rank snapshot
										writeInt(snapshot.getInt("classRank", 0)); // nClassRank_Snapshot
									}
								}
							}
							else
							{
								writeInt(id);
								writeInt(0);
								writeInt(0);
							}
						}
					}
					else
					{
						boolean found = false;
						for (Integer id : _playerList.keySet())
						{
							final StatSet player = _playerList.get(id);
							if (player.getInt("charId") == _player.getObjectId())
							{
								found = true;
								final int first = id > 10 ? (id - 9) : 1;
								final int last = _playerList.size() >= (id + 10) ? id + 10 : id + (_playerList.size() - id);
								if (first == 1)
								{
									writeInt(last - (first - 1));
								}
								else
								{
									writeInt(last - first);
								}
								for (int id2 = first; id2 <= last; id2++)
								{
									final StatSet plr = _playerList.get(id2);
									writeSizedString(plr.getString("name"));
									writeSizedString(plr.getString("clanName"));
									writeInt(Config.SERVER_ID);
									writeInt(plr.getInt("level"));
									writeInt(plr.getInt("classId"));
									writeInt(plr.getInt("race"));
									writeInt(id2); // server rank
									if (!_snapshotList.isEmpty())
									{
										for (Integer id3 : _snapshotList.keySet())
										{
											final StatSet snapshot = _snapshotList.get(id3);
											if (player.getInt("charId") == snapshot.getInt("charId"))
											{
												writeInt(id3); // server rank snapshot
												writeInt(snapshot.getInt("raceRank", 0));
												writeInt(snapshot.getInt("classRank", 0)); // nClassRank_Snapshot
											}
										}
									}
								}
							}
						}
						if (!found)
						{
							writeInt(0);
						}
					}
					break;
				}
				case 1: // race
				{
					if (_scope == 0) // all
					{
						int count = 0;
						for (int i = 1; i <= _playerList.size(); i++)
						{
							final StatSet player = _playerList.get(i);
							if (_race == player.getInt("race"))
							{
								count++;
							}
						}
						writeInt(count > 100 ? 100 : count);
						int i = 1;
						for (Integer id : _playerList.keySet())
						{
							final StatSet player = _playerList.get(id);
							if (_race == player.getInt("race"))
							{
								writeSizedString(player.getString("name"));
								writeSizedString(player.getString("clanName"));
								writeInt(Config.SERVER_ID);
								writeInt(player.getInt("level"));
								writeInt(player.getInt("classId"));
								writeInt(player.getInt("race"));
								writeInt(i); // server rank
								if (!_snapshotList.isEmpty())
								{
									final Map<Integer, StatSet> snapshotRaceList = new ConcurrentHashMap<>();
									int j = 1;
									for (Integer id2 : _snapshotList.keySet())
									{
										final StatSet snapshot = _snapshotList.get(id2);
										if (_race == snapshot.getInt("race"))
										{
											snapshotRaceList.put(j, _snapshotList.get(id2));
											j++;
										}
									}
									for (Integer id2 : snapshotRaceList.keySet())
									{
										final StatSet snapshot = snapshotRaceList.get(id2);
										if (player.getInt("charId") == snapshot.getInt("charId"))
										{
											writeInt(id2); // server rank snapshot
											writeInt(snapshot.getInt("raceRank", 0)); // race rank snapshot
											writeInt(snapshot.getInt("classRank", 0)); // nClassRank_Snapshot
										}
									}
								}
								else
								{
									writeInt(i);
									writeInt(i);
									writeInt(i); // nClassRank_Snapshot
								}
								i++;
							}
						}
					}
					else
					{
						boolean found = false;
						final Map<Integer, StatSet> raceList = new ConcurrentHashMap<>();
						int i = 1;
						for (Integer id : _playerList.keySet())
						{
							final StatSet set = _playerList.get(id);
							if (_player.getRace().ordinal() == set.getInt("race"))
							{
								raceList.put(i, _playerList.get(id));
								i++;
							}
						}
						for (Integer id : raceList.keySet())
						{
							final StatSet player = raceList.get(id);
							if (player.getInt("charId") == _player.getObjectId())
							{
								found = true;
								final int first = id > 10 ? (id - 9) : 1;
								final int last = raceList.size() >= (id + 10) ? id + 10 : id + (raceList.size() - id);
								if (first == 1)
								{
									writeInt(last - (first - 1));
								}
								else
								{
									writeInt(last - first);
								}
								for (int id2 = first; id2 <= last; id2++)
								{
									final StatSet plr = raceList.get(id2);
									writeSizedString(plr.getString("name"));
									writeSizedString(plr.getString("clanName"));
									writeInt(Config.SERVER_ID);
									writeInt(plr.getInt("level"));
									writeInt(plr.getInt("classId"));
									writeInt(plr.getInt("race"));
									writeInt(id2); // server rank
									writeInt(id2);
									writeInt(id2);
									writeInt(id2); // nClassRank_Snapshot
								}
							}
						}
						if (!found)
						{
							writeInt(0);
						}
					}
					break;
				}
				case 2: // clan
				{
					if (_player.getClan() != null)
					{
						final Map<Integer, StatSet> clanList = new ConcurrentHashMap<>();
						int i = 1;
						for (Integer id : _playerList.keySet())
						{
							final StatSet set = _playerList.get(id);
							if (_player.getClan().getName() == set.getString("clanName"))
							{
								clanList.put(i, _playerList.get(id));
								i++;
							}
						}
						writeInt(clanList.size());
						for (Integer id : clanList.keySet())
						{
							final StatSet player = clanList.get(id);
							writeSizedString(player.getString("name"));
							writeSizedString(player.getString("clanName"));
							writeInt(Config.SERVER_ID);
							writeInt(player.getInt("level"));
							writeInt(player.getInt("classId"));
							writeInt(player.getInt("race"));
							writeInt(id); // clan rank
							if (!_snapshotList.isEmpty())
							{
								for (Integer id2 : _snapshotList.keySet())
								{
									final StatSet snapshot = _snapshotList.get(id2);
									if (player.getInt("charId") == snapshot.getInt("charId"))
									{
										writeInt(id2); // server rank snapshot
										writeInt(snapshot.getInt("raceRank", 0)); // race rank snapshot
										writeInt(snapshot.getInt("classRank", 0)); // nClassRank_Snapshot
									}
								}
							}
							else
							{
								writeInt(id);
								writeInt(0);
								writeInt(0);
							}
						}
					}
					else
					{
						writeInt(0);
					}
					break;
				}
				case 3: // friend
				{
					if (!_player.getFriendList().isEmpty())
					{
						final Set<Integer> friendList = ConcurrentHashMap.newKeySet();
						int count = 1;
						for (int id : _player.getFriendList())
						{
							for (Integer id2 : _playerList.keySet())
							{
								final StatSet temp = _playerList.get(id2);
								if (temp.getInt("charId") == id)
								{
									friendList.add(temp.getInt("charId"));
									count++;
								}
							}
						}
						friendList.add(_player.getObjectId());
						writeInt(count);
						for (int id : _playerList.keySet())
						{
							final StatSet player = _playerList.get(id);
							if (friendList.contains(player.getInt("charId")))
							{
								writeSizedString(player.getString("name"));
								writeSizedString(player.getString("clanName"));
								writeInt(Config.SERVER_ID);
								writeInt(player.getInt("level"));
								writeInt(player.getInt("classId"));
								writeInt(player.getInt("race"));
								writeInt(id); // friend rank
								if (!_snapshotList.isEmpty())
								{
									for (Integer id2 : _snapshotList.keySet())
									{
										final StatSet snapshot = _snapshotList.get(id2);
										if (player.getInt("charId") == snapshot.getInt("charId"))
										{
											writeInt(id2); // server rank snapshot
											writeInt(snapshot.getInt("raceRank", 0)); // race rank snapshot
											writeInt(snapshot.getInt("classRank", 0)); // nClassRank_Snapshot
										}
									}
								}
								else
								{
									writeInt(id);
									writeInt(0);
									writeInt(0);
								}
							}
						}
					}
					else
					{
						writeInt(1);
						writeSizedString(_player.getName());
						if (_player.getClan() != null)
						{
							writeSizedString(_player.getClan().getName());
						}
						else
						{
							writeSizedString("");
						}
						writeInt(Config.SERVER_ID);
						writeInt(_player.getStat().getBaseLevel());
						writeInt(_player.getBaseClass());
						writeInt(_player.getRace().ordinal());
						writeInt(1); // clan rank
						if (!_snapshotList.isEmpty())
						{
							for (Integer id : _snapshotList.keySet())
							{
								final StatSet snapshot = _snapshotList.get(id);
								if (_player.getObjectId() == snapshot.getInt("charId"))
								{
									writeInt(id); // server rank snapshot
									writeInt(snapshot.getInt("raceRank", 0)); // race rank snapshot
									writeInt(snapshot.getInt("classRank", 0)); // nClassRank_Snapshot
								}
							}
						}
						else
						{
							writeInt(0);
							writeInt(0);
							writeInt(0);
						}
					}
					break;
				}
				case 4: // class
				{
					if (_scope == 0) // all
					{
						int count = 0;
						for (int i = 1; i <= _playerList.size(); i++)
						{
							final StatSet player = _playerList.get(i);
							if (_class == player.getInt("classId"))
							{
								count++;
							}
						}
						writeInt(count > 100 ? 100 : count);
						int i = 1;
						for (Integer id : _playerList.keySet())
						{
							final StatSet player = _playerList.get(id);
							if (_class == player.getInt("classId"))
							{
								writeSizedString(player.getString("name"));
								writeSizedString(player.getString("clanName"));
								writeInt(Config.SERVER_ID);
								writeInt(player.getInt("level"));
								writeInt(player.getInt("classId"));
								writeInt(player.getInt("race"));
								writeInt(i); // server rank
								if (_snapshotList.size() > 0)
								{
									final Map<Integer, StatSet> snapshotClassList = new ConcurrentHashMap<>();
									int j = 1;
									for (Integer id2 : _snapshotList.keySet())
									{
										final StatSet snapshot = _snapshotList.get(id2);
										if (_class == snapshot.getInt("classId"))
										{
											snapshotClassList.put(j, _snapshotList.get(id2));
											j++;
										}
									}
									for (Integer id2 : snapshotClassList.keySet())
									{
										final StatSet snapshot = snapshotClassList.get(id2);
										if (player.getInt("charId") == snapshot.getInt("charId"))
										{
											writeInt(id2); // server rank snapshot
											writeInt(snapshot.getInt("raceRank", 0)); // race rank snapshot
											writeInt(snapshot.getInt("classRank", 0)); // nClassRank_Snapshot
										}
									}
								}
								else
								{
									writeInt(i);
									writeInt(i);
									writeInt(i); // nClassRank_Snapshot?
								}
								i++;
							}
						}
					}
					else
					{
						boolean found = false;
						
						final Map<Integer, StatSet> classList = new ConcurrentHashMap<>();
						int i = 1;
						for (Integer id : _playerList.keySet())
						{
							final StatSet set = _playerList.get(id);
							if (_player.getBaseClass() == set.getInt("classId"))
							{
								classList.put(i, _playerList.get(id));
								i++;
							}
						}
						
						for (Integer id : classList.keySet())
						{
							final StatSet player = classList.get(id);
							if (player.getInt("charId") == _player.getObjectId())
							{
								found = true;
								final int first = id > 10 ? (id - 9) : 1;
								final int last = classList.size() >= (id + 10) ? id + 10 : id + (classList.size() - id);
								if (first == 1)
								{
									writeInt(last - (first - 1));
								}
								else
								{
									writeInt(last - first);
								}
								for (int id2 = first; id2 <= last; id2++)
								{
									final StatSet plr = classList.get(id2);
									writeSizedString(plr.getString("name"));
									writeSizedString(plr.getString("clanName"));
									writeInt(Config.SERVER_ID);
									writeInt(plr.getInt("level"));
									writeInt(plr.getInt("classId"));
									writeInt(plr.getInt("race"));
									writeInt(id2); // server rank
									writeInt(id2);
									writeInt(id2);
									writeInt(id2); // nClassRank_Snapshot?
								}
							}
						}
						if (!found)
						{
							writeInt(0);
						}
					}
					break;
				}
			}
		}
		else
		{
			writeInt(0);
		}
	}
}
