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
package org.l2j.gameserver.network.clientpackets.pet;

import org.l2j.commons.network.ReadablePacket;
import org.l2j.gameserver.data.xml.NpcData;
import org.l2j.gameserver.data.xml.PetDataTable;
import org.l2j.gameserver.data.xml.PetExtractData;
import org.l2j.gameserver.model.PetData;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.actor.instance.Pet;
import org.l2j.gameserver.model.actor.templates.NpcTemplate;
import org.l2j.gameserver.model.holders.PetExtractionHolder;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.model.itemcontainer.PetInventory;
import org.l2j.gameserver.model.itemcontainer.PlayerInventory;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.SystemMessageId;
import org.l2j.gameserver.network.clientpackets.ClientPacket;
import org.l2j.gameserver.network.serverpackets.pet.ResultPetExtractSystem;

/**
 * @author Geremy
 */
public class ExTryPetExtractSystem implements ClientPacket
{
	private int _itemObjId;
	
	@Override
	public void read(ReadablePacket packet)
	{
		_itemObjId = packet.readInt();
	}
	
	@Override
	public void run(GameClient client)
	{
		final Player player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		final Item petItem = player.getInventory().getItemByObjectId(_itemObjId);
		if ((petItem == null) || ((player.getPet() != null) && (player.getPet().getControlItem() == petItem)))
		{
			player.sendPacket(new ResultPetExtractSystem(false));
			return;
		}
		
		final PetData petData = PetDataTable.getInstance().getPetDataByItemId(petItem.getId());
		final NpcTemplate npcTemplate = NpcData.getInstance().getTemplate(petData.getNpcId());
		final Pet pet = new Pet(npcTemplate, player, petItem);
		final PetInventory petInventory = pet.getInventory();
		final PlayerInventory playerInventory = player.getInventory();
		if ((petInventory == null) || (playerInventory == null))
		{
			player.sendPacket(new ResultPetExtractSystem(false));
			return;
		}
		
		if (!playerInventory.validateWeight(petInventory.getTotalWeight()) || !playerInventory.validateCapacity(petInventory.getSize()))
		{
			player.sendPacket(SystemMessageId.THERE_ARE_ITEMS_IN_THE_PET_S_INVENTORY_TAKE_THEM_OUT_FIRST);
			player.sendPacket(new ResultPetExtractSystem(false));
			return;
		}
		
		petInventory.transferItemsToOwner();
		
		final Pet petInfo = Pet.restore(petItem, NpcData.getInstance().getTemplate(petData.getNpcId()), player);
		final int petId = PetDataTable.getInstance().getPetDataByItemId(petItem.getId()).getType();
		final int petLevel = petInfo.getLevel();
		final PetExtractionHolder holder = PetExtractData.getInstance().getExtraction(petId, petLevel);
		if (holder != null)
		{
			final int extractItemId = holder.getExtractItem();
			final int extractItemCount = (int) (petInfo.getStat().getExp() / holder.getExtractExp());
			final int extractCostId = holder.getExtractCost().getId();
			final long extractCostCount = holder.getExtractCost().getCount() * extractItemCount;
			final int defaultCostId = holder.getDefaultCost().getId();
			final long defaultCostCount = holder.getDefaultCost().getCount();
			if ((player.getInventory().getInventoryItemCount(extractCostId, -1) >= extractCostCount) && (player.getInventory().getInventoryItemCount(defaultCostId, -1) >= defaultCostCount))
			{
				if (player.destroyItemByItemId("Pet Extraction", extractCostId, extractCostCount, player, true) && player.destroyItemByItemId("Pet Extraction", defaultCostId, defaultCostCount, player, true) && player.destroyItem("Pet Extraction", petItem, player, true))
				{
					player.addItem("Pet Extraction", extractItemId, extractItemCount, player, true);
					player.sendPacket(new ResultPetExtractSystem(true));
				}
			}
			else
			{
				player.sendPacket(SystemMessageId.INCORRECT_ITEM_COUNT);
				player.sendPacket(new ResultPetExtractSystem(false));
			}
			return;
		}
		
		player.sendPacket(new ResultPetExtractSystem(false));
	}
}
