package org.l2jmobius.gameserver.network.clientpackets.pet;

import java.util.Optional;

import org.l2jmobius.gameserver.data.xml.PetAcquireList;
import org.l2jmobius.gameserver.data.xml.SkillData;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.actor.instance.Pet;
import org.l2jmobius.gameserver.model.holders.PetSkillAcquireHolder;
import org.l2jmobius.gameserver.model.skill.Skill;
import org.l2jmobius.gameserver.network.clientpackets.ClientPacket;
import org.l2jmobius.gameserver.network.serverpackets.pet.ExPetSkillList;

/**
 * @author Berezkin Nikolay
 */
public class RequestExAcquirePetSkill extends ClientPacket
{
	private int skillId, skillLevel;
	
	@Override
	protected void readImpl()
	{
		skillId = readInt();
		skillLevel = readInt();
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
		final Skill skill = SkillData.getInstance().getSkill(skillId, skillLevel);
		if (skill == null)
		{
			return;
		}
		final Optional<PetSkillAcquireHolder> reqItem = PetAcquireList.getInstance().getSkills(pet.getPetData().getType()).stream().filter(it -> (it.getSkillId() == skillId) && (it.getSkillLevel() == skillLevel)).findFirst();
		if (reqItem.isPresent())
		{
			if (reqItem.get().getItem() != null)
			{
				if (player.destroyItemByItemId("PetAcquireSkill", reqItem.get().getItem().getId(), reqItem.get().getItem().getCount(), null, true))
				{
					pet.addSkill(skill);
					pet.storePetSkills(skillId, skillLevel);
					player.sendPacket(new ExPetSkillList(false, pet));
				}
			}
			else
			{
				pet.addSkill(skill);
				player.sendPacket(new ExPetSkillList(false, pet));
			}
		}
	}
}
