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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.l2j.Config;
import org.l2j.commons.database.DatabaseFactory;
import org.l2j.gameserver.data.sql.ClanTable;
import org.l2j.gameserver.data.xml.ExperienceData;
import org.l2j.gameserver.model.CharSelectInfoPackage;
import org.l2j.gameserver.model.VariationInstance;
import org.l2j.gameserver.model.World;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.clan.Clan;
import org.l2j.gameserver.model.itemcontainer.Inventory;
import org.l2j.gameserver.model.olympiad.Hero;
import org.l2j.gameserver.network.Disconnection;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.ServerPackets;

public class CharSelectionInfo extends ServerPacket
{
	private static final Logger LOGGER = Logger.getLogger(CharSelectionInfo.class.getName());
	
	private static final int[] PAPERDOLL_ORDER = new int[]
	{
		Inventory.PAPERDOLL_UNDER,
		Inventory.PAPERDOLL_REAR,
		Inventory.PAPERDOLL_LEAR,
		Inventory.PAPERDOLL_NECK,
		Inventory.PAPERDOLL_RFINGER,
		Inventory.PAPERDOLL_LFINGER,
		Inventory.PAPERDOLL_HEAD,
		Inventory.PAPERDOLL_RHAND,
		Inventory.PAPERDOLL_LHAND,
		Inventory.PAPERDOLL_GLOVES,
		Inventory.PAPERDOLL_CHEST,
		Inventory.PAPERDOLL_LEGS,
		Inventory.PAPERDOLL_FEET,
		Inventory.PAPERDOLL_CLOAK,
		Inventory.PAPERDOLL_RHAND,
		Inventory.PAPERDOLL_HAIR,
		Inventory.PAPERDOLL_HAIR2,
		Inventory.PAPERDOLL_RBRACELET,
		Inventory.PAPERDOLL_LBRACELET,
		Inventory.PAPERDOLL_AGATHION1, // 152
		Inventory.PAPERDOLL_AGATHION2, // 152
		Inventory.PAPERDOLL_AGATHION3, // 152
		Inventory.PAPERDOLL_AGATHION4, // 152
		Inventory.PAPERDOLL_AGATHION5, // 152
		Inventory.PAPERDOLL_DECO1,
		Inventory.PAPERDOLL_DECO2,
		Inventory.PAPERDOLL_DECO3,
		Inventory.PAPERDOLL_DECO4,
		Inventory.PAPERDOLL_DECO5,
		Inventory.PAPERDOLL_DECO6,
		Inventory.PAPERDOLL_BELT,
		Inventory.PAPERDOLL_BROOCH,
		Inventory.PAPERDOLL_BROOCH_JEWEL1,
		Inventory.PAPERDOLL_BROOCH_JEWEL2,
		Inventory.PAPERDOLL_BROOCH_JEWEL3,
		Inventory.PAPERDOLL_BROOCH_JEWEL4,
		Inventory.PAPERDOLL_BROOCH_JEWEL5,
		Inventory.PAPERDOLL_BROOCH_JEWEL6,
		Inventory.PAPERDOLL_ARTIFACT_BOOK, // 152
		Inventory.PAPERDOLL_ARTIFACT1, // 152
		Inventory.PAPERDOLL_ARTIFACT2, // 152
		Inventory.PAPERDOLL_ARTIFACT3, // 152
		Inventory.PAPERDOLL_ARTIFACT4, // 152
		Inventory.PAPERDOLL_ARTIFACT5, // 152
		Inventory.PAPERDOLL_ARTIFACT6, // 152
		Inventory.PAPERDOLL_ARTIFACT7, // 152
		Inventory.PAPERDOLL_ARTIFACT8, // 152
		Inventory.PAPERDOLL_ARTIFACT9, // 152
		Inventory.PAPERDOLL_ARTIFACT10, // 152
		Inventory.PAPERDOLL_ARTIFACT11, // 152
		Inventory.PAPERDOLL_ARTIFACT12, // 152
		Inventory.PAPERDOLL_ARTIFACT13, // 152
		Inventory.PAPERDOLL_ARTIFACT14, // 152
		Inventory.PAPERDOLL_ARTIFACT15, // 152
		Inventory.PAPERDOLL_ARTIFACT16, // 152
		Inventory.PAPERDOLL_ARTIFACT17, // 152
		Inventory.PAPERDOLL_ARTIFACT18, // 152
		Inventory.PAPERDOLL_ARTIFACT19, // 152
		Inventory.PAPERDOLL_ARTIFACT20, // 152
		Inventory.PAPERDOLL_ARTIFACT21 // 152
	};
	private static final int[] PAPERDOLL_ORDER_VISUAL_ID = new int[]
	{
		Inventory.PAPERDOLL_RHAND,
		Inventory.PAPERDOLL_LHAND,
		Inventory.PAPERDOLL_GLOVES,
		Inventory.PAPERDOLL_CHEST,
		Inventory.PAPERDOLL_LEGS,
		Inventory.PAPERDOLL_FEET,
		Inventory.PAPERDOLL_RHAND,
		Inventory.PAPERDOLL_HAIR,
		Inventory.PAPERDOLL_HAIR2,
	};
	
	private final String _loginName;
	private final int _sessionId;
	private int _activeId;
	private final List<CharSelectInfoPackage> _characterPackages;
	
	/**
	 * Constructor for CharSelectionInfo.
	 * @param loginName
	 * @param sessionId
	 */
	public CharSelectionInfo(String loginName, int sessionId)
	{
		_sessionId = sessionId;
		_loginName = loginName;
		_characterPackages = loadCharacterSelectInfo(_loginName);
		_activeId = -1;
	}
	
	public CharSelectionInfo(String loginName, int sessionId, int activeId)
	{
		_sessionId = sessionId;
		_loginName = loginName;
		_characterPackages = loadCharacterSelectInfo(_loginName);
		_activeId = activeId;
	}
	
	public List<CharSelectInfoPackage> getCharInfo()
	{
		return _characterPackages;
	}
	
	@Override
	public void write()
	{
		ServerPackets.CHARACTER_SELECTION_INFO.writeId(this);
		final int size = _characterPackages.size();
		writeInt(size); // Created character count
		writeInt(Config.MAX_CHARACTERS_NUMBER_PER_ACCOUNT); // Can prevent players from creating new characters (if 0); (if 1, the client will ask if chars may be created (0x13) Response: (0x0D) )
		writeByte(size == Config.MAX_CHARACTERS_NUMBER_PER_ACCOUNT); // if 1 can't create new char
		writeByte(1); // 0=can't play, 1=can play free until level 85, 2=100% free play
		writeInt(2); // if 1, Korean client
		writeByte(0); // Gift message for inactive accounts // 152
		writeByte(0); // Balthus Knights, if 1 suggests premium account
		
		long lastAccess = 0;
		if (_activeId == -1)
		{
			for (int i = 0; i < size; i++)
			{
				if (lastAccess < _characterPackages.get(i).getLastAccess())
				{
					lastAccess = _characterPackages.get(i).getLastAccess();
					_activeId = i;
				}
			}
		}
		
		for (int i = 0; i < size; i++)
		{
			final CharSelectInfoPackage charInfoPackage = _characterPackages.get(i);
			writeString(charInfoPackage.getName()); // Character name
			writeInt(charInfoPackage.getObjectId()); // Character ID
			writeString(_loginName); // Account name
			writeInt(_sessionId); // Account ID
			writeInt(0); // Clan ID
			writeInt(0); // Builder level
			writeInt(charInfoPackage.getSex()); // Sex
			writeInt(charInfoPackage.getRace()); // Race
			writeInt(charInfoPackage.getBaseClassId());
			writeInt(Config.SERVER_ID);
			writeInt(charInfoPackage.getX());
			writeInt(charInfoPackage.getY());
			writeInt(charInfoPackage.getZ());
			writeDouble(charInfoPackage.getCurrentHp());
			writeDouble(charInfoPackage.getCurrentMp());
			writeLong(charInfoPackage.getSp());
			writeLong(charInfoPackage.getExp());
			writeDouble((float) (charInfoPackage.getExp() - ExperienceData.getInstance().getExpForLevel(charInfoPackage.getLevel())) / (ExperienceData.getInstance().getExpForLevel(charInfoPackage.getLevel() + 1) - ExperienceData.getInstance().getExpForLevel(charInfoPackage.getLevel())));
			writeInt(charInfoPackage.getLevel());
			writeInt(charInfoPackage.getReputation());
			writeInt(charInfoPackage.getPkKills());
			writeInt(charInfoPackage.getPvPKills());
			writeInt(0);
			writeInt(0);
			writeInt(0);
			writeInt(0);
			writeInt(0);
			writeInt(0);
			writeInt(0);
			writeInt(0); // Ertheia
			writeInt(0); // Ertheia
			for (int slot : getPaperdollOrder())
			{
				writeInt(charInfoPackage.getPaperdollItemId(slot));
			}
			for (int slot : getPaperdollOrderVisualId())
			{
				writeInt(charInfoPackage.getPaperdollItemVisualId(slot));
			}
			writeShort(charInfoPackage.getEnchantEffect(Inventory.PAPERDOLL_CHEST)); // Upper Body enchant level
			writeShort(charInfoPackage.getEnchantEffect(Inventory.PAPERDOLL_LEGS)); // Lower Body enchant level
			writeShort(charInfoPackage.getEnchantEffect(Inventory.PAPERDOLL_HEAD)); // Headgear enchant level
			writeShort(charInfoPackage.getEnchantEffect(Inventory.PAPERDOLL_GLOVES)); // Gloves enchant level
			writeShort(charInfoPackage.getEnchantEffect(Inventory.PAPERDOLL_FEET)); // Boots enchant level
			writeInt(charInfoPackage.getHairStyle());
			writeInt(charInfoPackage.getHairColor());
			writeInt(charInfoPackage.getFace());
			writeDouble(charInfoPackage.getMaxHp()); // Maximum HP
			writeDouble(charInfoPackage.getMaxMp()); // Maximum MP
			writeInt(charInfoPackage.getDeleteTimer() > 0 ? (int) ((charInfoPackage.getDeleteTimer() - System.currentTimeMillis()) / 1000) : 0);
			writeInt(charInfoPackage.getClassId());
			writeInt(i == _activeId);
			writeByte(charInfoPackage.getEnchantEffect(Inventory.PAPERDOLL_RHAND) > 127 ? 127 : charInfoPackage.getEnchantEffect(Inventory.PAPERDOLL_RHAND));
			writeInt(charInfoPackage.getAugmentation() != null ? charInfoPackage.getAugmentation().getOption1Id() : 0);
			writeInt(charInfoPackage.getAugmentation() != null ? charInfoPackage.getAugmentation().getOption2Id() : 0);
			writeInt(0); // Transformation: Currently on retail when you are on character select you don't see your transformation.
			writeInt(0); // Pet NpcId
			writeInt(0); // Pet level
			writeInt(0); // Pet Food
			writeInt(0); // Pet Food Level
			writeDouble(0); // Current pet HP
			writeDouble(0); // Current pet MP
			writeInt(charInfoPackage.getVitalityPoints()); // Vitality
			writeInt((int) Config.RATE_VITALITY_EXP_MULTIPLIER * 100); // Vitality Percent
			writeInt(charInfoPackage.getVitalityItemsUsed()); // Remaining vitality item uses
			writeInt(charInfoPackage.getAccessLevel() != -100); // Char is active or not
			writeByte(charInfoPackage.isNoble());
			writeByte(Hero.getInstance().isHero(charInfoPackage.getObjectId()) ? 2 : 0); // Hero glow
			writeByte(charInfoPackage.isHairAccessoryEnabled()); // Show hair accessory if enabled
			writeInt(0); // 235 - ban time left
			writeInt((int) (charInfoPackage.getLastAccess() / 1000)); // 235 - last play time
			writeByte(0); // 338
			writeInt(charInfoPackage.getHairColor() + 1); // 338 - DK color.
			writeByte(charInfoPackage.getClassId() == 217 ? 1 : charInfoPackage.getClassId() == 218 ? 2 : charInfoPackage.getClassId() == 219 ? 3 : charInfoPackage.getClassId() == 220 ? 4 : 0); // 362
		}
	}
	
	private static List<CharSelectInfoPackage> loadCharacterSelectInfo(String loginName)
	{
		CharSelectInfoPackage charInfopackage;
		final List<CharSelectInfoPackage> characterList = new LinkedList<>();
		try (Connection con = DatabaseFactory.getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT * FROM characters WHERE account_name=? ORDER BY createDate"))
		{
			statement.setString(1, loginName);
			try (ResultSet charList = statement.executeQuery())
			{
				while (charList.next()) // fills the package
				{
					charInfopackage = restoreChar(charList);
					if (charInfopackage != null)
					{
						characterList.add(charInfopackage);
						
						// Disconnect offline trader.
						if (Config.OFFLINE_DISCONNECT_SAME_ACCOUNT)
						{
							final Player player = World.getInstance().getPlayer(charInfopackage.getObjectId());
							if (player != null)
							{
								Disconnection.of(player).storeMe().deleteMe();
							}
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.log(Level.WARNING, "Could not restore char info: " + e.getMessage(), e);
		}
		return characterList;
	}
	
	private static void loadCharacterSubclassInfo(CharSelectInfoPackage charInfopackage, int objectId, int activeClassId)
	{
		try (Connection con = DatabaseFactory.getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT exp, sp, level, vitality_points FROM character_subclasses WHERE charId=? AND class_id=? ORDER BY charId"))
		{
			statement.setInt(1, objectId);
			statement.setInt(2, activeClassId);
			try (ResultSet charList = statement.executeQuery())
			{
				if (charList.next())
				{
					charInfopackage.setExp(charList.getLong("exp"));
					charInfopackage.setSp(charList.getLong("sp"));
					charInfopackage.setLevel(charList.getInt("level"));
					charInfopackage.setVitalityPoints(charList.getInt("vitality_points"));
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.log(Level.WARNING, "Could not restore char subclass info: " + e.getMessage(), e);
		}
	}
	
	private static CharSelectInfoPackage restoreChar(ResultSet chardata) throws Exception
	{
		final int objectId = chardata.getInt("charId");
		final String name = chardata.getString("char_name");
		
		// See if the char must be deleted
		final long deletetime = chardata.getLong("deletetime");
		if ((deletetime > 0) && (System.currentTimeMillis() > deletetime))
		{
			final Clan clan = ClanTable.getInstance().getClan(chardata.getInt("clanid"));
			if (clan != null)
			{
				clan.removeClanMember(objectId, 0);
			}
			GameClient.deleteCharByObjId(objectId);
			return null;
		}
		
		final CharSelectInfoPackage charInfopackage = new CharSelectInfoPackage(objectId, name);
		charInfopackage.setAccessLevel(chardata.getInt("accesslevel"));
		charInfopackage.setLevel(chardata.getInt("level"));
		charInfopackage.setMaxHp(chardata.getInt("maxhp"));
		charInfopackage.setCurrentHp(chardata.getDouble("curhp"));
		charInfopackage.setMaxMp(chardata.getInt("maxmp"));
		charInfopackage.setCurrentMp(chardata.getDouble("curmp"));
		charInfopackage.setReputation(chardata.getInt("reputation"));
		charInfopackage.setPkKills(chardata.getInt("pkkills"));
		charInfopackage.setPvPKills(chardata.getInt("pvpkills"));
		charInfopackage.setFace(chardata.getInt("face"));
		charInfopackage.setHairStyle(chardata.getInt("hairstyle"));
		charInfopackage.setHairColor(chardata.getInt("haircolor"));
		charInfopackage.setSex(chardata.getInt("sex"));
		charInfopackage.setExp(chardata.getLong("exp"));
		charInfopackage.setSp(chardata.getLong("sp"));
		charInfopackage.setVitalityPoints(chardata.getInt("vitality_points"));
		charInfopackage.setClanId(chardata.getInt("clanid"));
		charInfopackage.setRace(chardata.getInt("race"));
		final int baseClassId = chardata.getInt("base_class");
		final int activeClassId = chardata.getInt("classid");
		charInfopackage.setX(chardata.getInt("x"));
		charInfopackage.setY(chardata.getInt("y"));
		charInfopackage.setZ(chardata.getInt("z"));
		final int faction = chardata.getInt("faction");
		if (faction == 1)
		{
			charInfopackage.setGood();
		}
		if (faction == 2)
		{
			charInfopackage.setEvil();
		}
		if (Config.MULTILANG_ENABLE)
		{
			String lang = chardata.getString("language");
			if (!Config.MULTILANG_ALLOWED.contains(lang))
			{
				lang = Config.MULTILANG_DEFAULT;
			}
			charInfopackage.setHtmlPrefix("data/lang/" + lang + "/");
		}
		// if is in subclass, load subclass exp, sp, level info
		if (baseClassId != activeClassId)
		{
			loadCharacterSubclassInfo(charInfopackage, objectId, activeClassId);
		}
		charInfopackage.setClassId(activeClassId);
		// Get the augmentation id for equipped weapon
		int weaponObjId = charInfopackage.getPaperdollObjectId(Inventory.PAPERDOLL_RHAND);
		if (weaponObjId < 1)
		{
			weaponObjId = charInfopackage.getPaperdollObjectId(Inventory.PAPERDOLL_RHAND);
		}
		if (weaponObjId > 0)
		{
			try (Connection con = DatabaseFactory.getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT mineralId,option1,option2 FROM item_variations WHERE itemId=?"))
			{
				statement.setInt(1, weaponObjId);
				try (ResultSet result = statement.executeQuery())
				{
					if (result.next())
					{
						final int mineralId = result.getInt("mineralId");
						final int option1 = result.getInt("option1");
						final int option2 = result.getInt("option2");
						if ((option1 != -1) && (option2 != -1))
						{
							charInfopackage.setAugmentation(new VariationInstance(mineralId, option1, option2));
						}
					}
				}
			}
			catch (Exception e)
			{
				LOGGER.log(Level.WARNING, "Could not restore augmentation info: " + e.getMessage(), e);
			}
		}
		// Check if the base class is set to zero and also doesn't match with the current active class, otherwise send the base class ID. This prevents chars created before base class was introduced from being displayed incorrectly.
		if ((baseClassId == 0) && (activeClassId > 0))
		{
			charInfopackage.setBaseClassId(activeClassId);
		}
		else
		{
			charInfopackage.setBaseClassId(baseClassId);
		}
		charInfopackage.setDeleteTimer(deletetime);
		charInfopackage.setLastAccess(chardata.getLong("lastAccess"));
		charInfopackage.setNoble(chardata.getInt("nobless") == 1);
		return charInfopackage;
	}
	
	@Override
	public int[] getPaperdollOrder()
	{
		return PAPERDOLL_ORDER;
	}
	
	@Override
	public int[] getPaperdollOrderVisualId()
	{
		return PAPERDOLL_ORDER_VISUAL_ID;
	}
}
