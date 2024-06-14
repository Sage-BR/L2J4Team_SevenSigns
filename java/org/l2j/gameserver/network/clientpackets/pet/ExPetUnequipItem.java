package org.l2j.gameserver.network.clientpackets.pet;

import org.l2j.gameserver.ai.CtrlEvent;
import org.l2j.gameserver.ai.CtrlIntention;
import org.l2j.gameserver.ai.NextAction;
import org.l2j.gameserver.instancemanager.FortManager;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.actor.instance.Pet;
import org.l2j.gameserver.model.item.ItemTemplate;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.model.zone.ZoneId;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.ActionFailed;
import org.l2j.gameserver.network.serverpackets.SystemMessage;
import org.l2j.gameserver.network.serverpackets.pet.ExPetSkillList;
import org.l2j.gameserver.network.serverpackets.pet.PetSummonInfo;

/**
 * @author Berezkin Nikolay
 */
public class ExPetUnequipItem extends ClientPacket
{
	private int _objectId;
	private int _itemId;
	
	@Override
	protected void readImpl()
	{
		_objectId = readInt();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		final Pet pet = player.getPet();
		if (pet == null)
		{
			return;
		}
		
		// Flood protect UseItem
		if (!getClient().getFloodProtectors().canUseItem())
		{
			return;
		}
		
		if (player.isInsideZone(ZoneId.JAIL))
		{
			player.sendMessage("You cannot use items while jailed.");
			return;
		}
		
		if (player.getActiveTradeList() != null)
		{
			player.cancelActiveTrade();
		}
		
		if (player.isInStoreMode())
		{
			player.sendPacket(SystemMessageId.WHILE_OPERATING_A_PRIVATE_STORE_OR_WORKSHOP_YOU_CANNOT_DISCARD_DESTROY_OR_TRADE_AN_ITEM);
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		final Item item = pet.getInventory().getItemByObjectId(_objectId);
		
		// No UseItem is allowed while the player is in special conditions
		if (player.hasBlockActions() || player.isControlBlocked() || player.isAlikeDead())
		{
			return;
		}
		
		// Char cannot use item when dead
		if (player.isDead() || pet.isDead() || !player.getInventory().canManipulateWithItemId(item.getId()))
		{
			final SystemMessage sm = new SystemMessage(SystemMessageId.S1_CANNOT_BE_USED_THE_REQUIREMENTS_ARE_NOT_MET);
			sm.addItemName(item);
			player.sendPacket(sm);
			return;
		}
		
		if (!item.isEquipable())
		{
			return;
		}
		
		_itemId = item.getId();
		if (player.isFishing() && ((_itemId < 6535) || (_itemId > 6540)))
		{
			// You cannot do anything else while fishing
			player.sendPacket(SystemMessageId.YOU_CANNOT_DO_THAT_WHILE_FISHING_3);
			return;
		}
		
		player.onActionRequest();
		
		if (item.isEquipable())
		{
			if (pet.getInventory().isItemSlotBlocked(item.getTemplate().getBodyPart()))
			{
				player.sendPacket(SystemMessageId.YOU_DO_NOT_MEET_THE_REQUIRED_CONDITION_TO_EQUIP_THAT_ITEM);
				return;
			}
			// Prevent players to equip weapon while wearing combat flag
			// Don't allow weapon/shield equipment if a cursed weapon is equipped.
			if ((item.getTemplate().getBodyPart() == ItemTemplate.SLOT_LR_HAND) || (item.getTemplate().getBodyPart() == ItemTemplate.SLOT_L_HAND) || (item.getTemplate().getBodyPart() == ItemTemplate.SLOT_R_HAND))
			{
				if ((player.getActiveWeaponItem() != null) && (player.getActiveWeaponItem().getId() == FortManager.ORC_FORTRESS_FLAG))
				{
					player.sendPacket(SystemMessageId.YOU_DO_NOT_MEET_THE_REQUIRED_CONDITION_TO_EQUIP_THAT_ITEM);
					return;
				}
			}
			else if (item.getTemplate().getBodyPart() == ItemTemplate.SLOT_DECO)
			{
				if (!item.isEquipped() && (player.getInventory().getTalismanSlots() == 0))
				{
					player.sendPacket(SystemMessageId.YOU_DO_NOT_MEET_THE_REQUIRED_CONDITION_TO_EQUIP_THAT_ITEM);
					return;
				}
			}
			else if (item.getTemplate().getBodyPart() == ItemTemplate.SLOT_BROOCH_JEWEL)
			{
				if (!item.isEquipped() && (player.getInventory().getBroochJewelSlots() == 0))
				{
					final SystemMessage sm = new SystemMessage(SystemMessageId.YOU_CANNOT_EQUIP_S1_WITHOUT_EQUIPPING_A_BROOCH);
					sm.addItemName(item);
					player.sendPacket(sm);
					return;
				}
			}
			else if (item.getTemplate().getBodyPart() == ItemTemplate.SLOT_AGATHION)
			{
				if (!item.isEquipped() && (player.getInventory().getAgathionSlots() == 0))
				{
					player.sendPacket(SystemMessageId.YOU_DO_NOT_MEET_THE_REQUIRED_CONDITION_TO_EQUIP_THAT_ITEM);
					return;
				}
			}
			else if (item.getTemplate().getBodyPart() == ItemTemplate.SLOT_ARTIFACT)
			{
				if (!item.isEquipped() && (player.getInventory().getArtifactSlots() == 0))
				{
					final SystemMessage sm = new SystemMessage(SystemMessageId.YOU_DO_NOT_MEET_THE_REQUIRED_CONDITION_TO_EQUIP_THAT_ITEM);
					sm.addItemName(item);
					player.sendPacket(sm);
					return;
				}
			}
			
			if (player.isCastingNow())
			{
				// Create and Bind the next action to the AI.
				player.getAI().setNextAction(new NextAction(CtrlEvent.EVT_FINISH_CASTING, CtrlIntention.AI_INTENTION_CAST, () ->
				{
					pet.transferItem("UnequipFromPet", item.getObjectId(), 1, player.getInventory(), player, null);
					sendInfos(pet, player);
				}));
			}
			else if (player.isAttackingNow())
			{
				// Equip or unEquip.
				pet.transferItem("UnequipFromPet", item.getObjectId(), 1, player.getInventory(), player, null);
				sendInfos(pet, player);
			}
			else
			{
				pet.transferItem("UnequipFromPet", item.getObjectId(), 1, player.getInventory(), player, null);
				sendInfos(pet, player);
			}
		}
	}
	
	private void sendInfos(Pet pet, Player player)
	{
		pet.getStat().recalculateStats(true);
		player.sendPacket(new PetSummonInfo(pet, 1));
		player.sendPacket(new ExPetSkillList(false, pet));
	}
}
