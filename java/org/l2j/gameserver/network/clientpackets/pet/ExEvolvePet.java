package org.l2j.gameserver.network.clientpackets.pet;

import java.util.List;
import java.util.Map.Entry;

import org.l2j.commons.util.Rnd;
import org.l2j.gameserver.data.xml.NpcData;
import org.l2j.gameserver.data.xml.PetDataTable;
import org.l2j.gameserver.data.xml.PetTypeData;
import org.l2j.gameserver.enums.EvolveLevel;
import org.l2j.gameserver.model.PetData;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.actor.instance.Pet;
import org.l2j.gameserver.model.actor.templates.NpcTemplate;
import org.l2j.gameserver.model.holders.SkillHolder;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.network.GameClient;
import org.l2j.gameserver.network.clientpackets.ClientPacket;

/**
 * @author Berezkin Nikolay, Mobius
 */
public class ExEvolvePet implements ClientPacket
{
	@Override
	public void run(GameClient client)
	{
		final Player activeChar = client.getPlayer();
		if (activeChar == null)
		{
			return;
		}
		
		final Pet pet = activeChar.getPet();
		if (pet == null)
		{
			return;
		}
		
		if (!activeChar.isMounted() && !pet.isDead() && !activeChar.isDead() && !pet.isHungry() && !activeChar.isControlBlocked() && !activeChar.isInDuel() && !activeChar.isSitting() && !activeChar.isFishing() && !activeChar.isInCombat() && !pet.isInCombat())
		{
			final boolean isAbleToEvolveLevel1 = (pet.getLevel() >= 40) && (pet.getEvolveLevel() == EvolveLevel.None.ordinal());
			final boolean isAbleToEvolveLevel2 = (pet.getLevel() >= 76) && (pet.getEvolveLevel() == EvolveLevel.First.ordinal());
			
			if (isAbleToEvolveLevel1 && activeChar.destroyItemByItemId("PetEvolve", 94096, 1, null, true))
			{
				doEvolve(activeChar, pet, EvolveLevel.First);
			}
			else if (isAbleToEvolveLevel2 && activeChar.destroyItemByItemId("PetEvolve", 94117, 1, null, true))
			{
				doEvolve(activeChar, pet, EvolveLevel.Second);
			}
		}
		else
		{
			activeChar.sendMessage("You can't evolve in this time."); // TODO: Proper system messages.
		}
	}
	
	private void doEvolve(Player activeChar, Pet pet, EvolveLevel evolveLevel)
	{
		final Item controlItem = pet.getControlItem();
		pet.unSummon(activeChar);
		final List<PetData> pets = PetDataTable.getInstance().getPetDatasByEvolve(controlItem.getId(), evolveLevel);
		final PetData targetPet = pets.get(Rnd.get(pets.size()));
		final PetData petData = PetDataTable.getInstance().getPetData(targetPet.getNpcId());
		if ((petData == null) || (petData.getNpcId() == -1))
		{
			return;
		}
		
		final NpcTemplate npcTemplate = NpcData.getInstance().getTemplate(evolveLevel == EvolveLevel.Second ? pet.getId() + 2 : petData.getNpcId());
		final Pet evolved = Pet.spawnPet(npcTemplate, activeChar, controlItem);
		if (evolved == null)
		{
			return;
		}
		
		if (evolveLevel == EvolveLevel.First)
		{
			final Entry<Integer, SkillHolder> skillType = PetTypeData.getInstance().getRandomSkill();
			final String name = PetTypeData.getInstance().getNamePrefix(skillType.getKey()) + " " + PetTypeData.getInstance().getRandomName();
			evolved.addSkill(skillType.getValue().getSkill());
			evolved.setName(name);
			PetDataTable.getInstance().setPetName(controlItem.getObjectId(), name);
		}
		
		activeChar.setPet(evolved);
		evolved.setShowSummonAnimation(true);
		evolved.setEvolveLevel(evolveLevel);
		evolved.setRunning();
		evolved.storeEvolvedPets(evolveLevel.ordinal(), evolved.getPetData().getIndex(), controlItem.getObjectId());
		controlItem.setEnchantLevel(evolved.getLevel());
		evolved.spawnMe(pet.getX(), pet.getY(), pet.getZ());
		evolved.startFeed();
	}
}
