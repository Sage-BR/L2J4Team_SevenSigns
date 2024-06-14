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
package org.l2jmobius.gameserver.network.clientpackets.storereview;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.l2jmobius.gameserver.data.xml.CollectionData;
import org.l2jmobius.gameserver.data.xml.EnsoulData;
import org.l2jmobius.gameserver.data.xml.HennaData;
import org.l2jmobius.gameserver.data.xml.VariationData;
import org.l2jmobius.gameserver.enums.PrivateStoreType;
import org.l2jmobius.gameserver.handler.IItemHandler;
import org.l2jmobius.gameserver.handler.ItemHandler;
import org.l2jmobius.gameserver.instancemanager.PrivateStoreHistoryManager;
import org.l2jmobius.gameserver.instancemanager.PrivateStoreHistoryManager.ItemHistoryTransaction;
import org.l2jmobius.gameserver.model.ItemInfo;
import org.l2jmobius.gameserver.model.TradeItem;
import org.l2jmobius.gameserver.model.World;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.holders.CollectionDataHolder;
import org.l2jmobius.gameserver.model.holders.ItemEnchantHolder;
import org.l2jmobius.gameserver.model.item.EtcItem;
import org.l2jmobius.gameserver.model.item.ItemTemplate;
import org.l2jmobius.gameserver.network.clientpackets.ClientPacket;
import org.l2jmobius.gameserver.network.serverpackets.storereview.ExPrivateStoreSearchHistory;
import org.l2jmobius.gameserver.network.serverpackets.storereview.ExPrivateStoreSearchItem;

/**
 * @author Mobius
 */
public class ExRequestPrivateStoreSearchList extends ClientPacket
{
	public static final int MAX_ITEM_PER_PAGE = 120;
	
	private String _searchWord;
	private StoreType _storeType;
	private StoreItemType _itemType;
	private StoreSubItemType _itemSubtype;
	private int _searchCollection;
	
	@Override
	protected void readImpl()
	{
		_searchWord = readSizedString();
		_storeType = StoreType.findById(readByte());
		_itemType = StoreItemType.findById(readByte());
		_itemSubtype = StoreSubItemType.findById(readByte());
		_searchCollection = readByte();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getPlayer();
		if (player == null)
		{
			return;
		}
		
		final Collection<Player> stores = World.getInstance().getSellingOrBuyingPlayers();
		final List<ShopItem> items = new ArrayList<>();
		final List<Integer> itemIds = new ArrayList<>();
		stores.forEach(vendor ->
		{
			for (TradeItem item : vendor.getPrivateStoreType() == PrivateStoreType.BUY ? vendor.getBuyList().getItems() : vendor.getSellList().getItems())
			{
				// Filter by storeType.
				if ((_storeType == StoreType.ALL) || ((_storeType == StoreType.SELL) && ((vendor.getPrivateStoreType() == PrivateStoreType.SELL) || (vendor.getPrivateStoreType() == PrivateStoreType.PACKAGE_SELL))) || ((_storeType == StoreType.BUY) && (vendor.getPrivateStoreType() == PrivateStoreType.BUY)))
				{
					if (isItemVisibleForShop(item))
					{
						// Filter by Word if supplied.
						if (_searchWord.equals("") || (!_searchWord.equals("") && (item.getItem().getName().toLowerCase().contains(_searchWord.toLowerCase()))))
						{
							items.add(new ShopItem(item, vendor, vendor.getPrivateStoreType()));
							itemIds.add(item.getItem().getId());
						}
					}
				}
			}
		});
		
		int nSize = items.size();
		int maxPage = Math.max(1, (nSize / (MAX_ITEM_PER_PAGE * 1F)) > (nSize / MAX_ITEM_PER_PAGE) ? (nSize / MAX_ITEM_PER_PAGE) + 1 : nSize / MAX_ITEM_PER_PAGE);
		for (int page = 1; page <= maxPage; page++)
		{
			final int nsize = page == maxPage ? ((nSize % MAX_ITEM_PER_PAGE) > 0) || (nSize == 0) ? nSize % MAX_ITEM_PER_PAGE : MAX_ITEM_PER_PAGE : MAX_ITEM_PER_PAGE;
			player.sendPacket(new ExPrivateStoreSearchItem(page, maxPage, nsize, items));
		}
		
		final List<ItemHistoryTransaction> history = new ArrayList<>();
		final List<ItemHistoryTransaction> historyTemp = new ArrayList<>();
		PrivateStoreHistoryManager.getInstance().getHistory().forEach(transaction ->
		{
			if (itemIds.contains(transaction.getItemId()))
			{
				history.add(transaction);
			}
		});
		
		int page = 1;
		maxPage = Math.max(1, (history.size() / (MAX_ITEM_PER_PAGE * 1F)) > (history.size() / MAX_ITEM_PER_PAGE) ? (history.size() / MAX_ITEM_PER_PAGE) + 1 : history.size() / MAX_ITEM_PER_PAGE);
		
		for (int index = 0; index < history.size(); index++)
		{
			historyTemp.add(history.get(index));
			
			if ((index == (history.size() - 1)) || (index == (MAX_ITEM_PER_PAGE - 1)) || ((index > 0) && ((index % (MAX_ITEM_PER_PAGE - 1)) == 0)))
			{
				player.sendPacket(new ExPrivateStoreSearchHistory(page, maxPage, historyTemp));
				page++;
				historyTemp.clear();
			}
		}
		
		if (page == 1)
		{
			player.sendPacket(new ExPrivateStoreSearchHistory(1, 1, historyTemp));
		}
	}
	
	private boolean isItemVisibleForShop(TradeItem item)
	{
		/**
		 * Equipement
		 */
		if ((_itemType == StoreItemType.EQUIPMENT) && (_itemSubtype == StoreSubItemType.ALL) && (_searchCollection == 0))
		{
			// Equipment - All
			return item.getItem().isEquipable();
		}
		if ((_itemType == StoreItemType.EQUIPMENT) && (_itemSubtype == StoreSubItemType.WEAPON) && (_searchCollection == 0))
		{
			// Equipment - Weapon
			return item.getItem().isEquipable() && item.getItem().isWeapon();
		}
		if ((_itemType == StoreItemType.EQUIPMENT) && (_itemSubtype == StoreSubItemType.ARMOR) && (_searchCollection == 0))
		{
			// Equipment - Armor
			return item.getItem().isEquipable() && isEquipmentArmor(item.getItem());
		}
		if ((_itemType == StoreItemType.EQUIPMENT) && (_itemSubtype == StoreSubItemType.ACCESSORY) && (_searchCollection == 0))
		{
			// Equipment - Accessory
			return item.getItem().isEquipable() && isAccessory(item.getItem());
		}
		if ((_itemType == StoreItemType.EQUIPMENT) && (_itemSubtype == StoreSubItemType.EQUIPMENT_MISC) && (_searchCollection == 0))
		{
			// Equipment - Misc
			return item.getItem().isEquipable() && !item.getItem().isWeapon() && !isEquipmentArmor(item.getItem()) && !isAccessory(item.getItem());
		}
		
		/**
		 * Exping / Enhancement
		 */
		if ((_itemType == StoreItemType.ENHANCEMENT_OR_EXPING) && (_itemSubtype == StoreSubItemType.ALL) && (_searchCollection == 0))
		{
			// Exping / Enhancement - All
			return isEnhancementItem(item.getItem());
		}
		if ((_itemType == StoreItemType.ENHANCEMENT_OR_EXPING) && (_itemSubtype == StoreSubItemType.ENCHANT_SCROLL) && (_searchCollection == 0))
		{
			// Exping / Enhancement - Enchant Scroll
			return isEnchantScroll(item.getItem());
		}
		if ((_itemType == StoreItemType.ENHANCEMENT_OR_EXPING) && (_itemSubtype == StoreSubItemType.CRYSTAL) && (_searchCollection == 0))
		{
			// Exping / Enhancement - Crystal
			return isCrystal(item.getItem());
		}
		if ((_itemType == StoreItemType.ENHANCEMENT_OR_EXPING) && (_itemSubtype == StoreSubItemType.LIFE_STONE) && (_searchCollection == 0))
		{
			// Exping / Enhancement - Life Stone
			return isLifeStone(item.getItem());
		}
		if ((_itemType == StoreItemType.ENHANCEMENT_OR_EXPING) && (_itemSubtype == StoreSubItemType.DYES) && (_searchCollection == 0))
		{
			// Exping / Enhancement - Dyes
			return isDye(item.getItem());
		}
		if ((_itemType == StoreItemType.ENHANCEMENT_OR_EXPING) && (_itemSubtype == StoreSubItemType.SPELLBOOK) && (_searchCollection == 0))
		{
			// Exping / Enhancement - SpellBooks
			return isSpellBook(item.getItem());
		}
		if ((_itemType == StoreItemType.ENHANCEMENT_OR_EXPING) && (_itemSubtype == StoreSubItemType.ENHANCEMENT_MISC) && (_searchCollection == 0))
		{
			// Exping / Enhancement - Misc
			return isEnhancementMisc(item.getItem());
		}
		
		/**
		 * Groceries
		 */
		if ((_itemType == StoreItemType.GROCERY_OR_COLLECTION_MISC) && (_itemSubtype == StoreSubItemType.ALL) && (_searchCollection == 0))
		{
			// Groceries - All
			return item.getItem().isPotion() || item.getItem().isScroll() || isTicket(item.getItem()) || isPackOrCraft(item.getItem()) || isGroceryMisc(item.getItem());
		}
		if ((_itemType == StoreItemType.GROCERY_OR_COLLECTION_MISC) && (_itemSubtype == StoreSubItemType.POTION_SCROLL) && (_searchCollection == 0))
		{
			// Groceries - Potion/Scroll
			return item.getItem().isPotion() || item.getItem().isScroll();
		}
		if ((_itemType == StoreItemType.GROCERY_OR_COLLECTION_MISC) && (_itemSubtype == StoreSubItemType.TICKET) && (_searchCollection == 0))
		{
			// Groceries - Ticket
			return isTicket(item.getItem());
		}
		if ((_itemType == StoreItemType.GROCERY_OR_COLLECTION_MISC) && (_itemSubtype == StoreSubItemType.PACK_CRAFT) && (_searchCollection == 0))
		{
			// Groceries - Pack/Craft
			return isPackOrCraft(item.getItem());
		}
		if ((_itemType == StoreItemType.GROCERY_OR_COLLECTION_MISC) && (_itemSubtype == StoreSubItemType.GROCERY_MISC) && (_searchCollection == 0))
		{
			// Groceries - Misc
			return isGroceryMisc(item.getItem());
		}
		
		/**
		 * Collections
		 */
		if ((_itemType == StoreItemType.ALL) && (_searchCollection == 1))
		{
			// Collections - All
			return isCollection(item.getItem());
		}
		if ((_itemType == StoreItemType.EQUIPMENT) && (_searchCollection == 1))
		{
			// Collections - Equipement
			return isCollectionEquipement(item.getItem());
		}
		if ((_itemType == StoreItemType.ENHANCEMENT_OR_EXPING) && (_searchCollection == 1))
		{
			// Collections - Enchanted Item
			return isCollectionEnchanted(item.getItem());
		}
		if ((_itemType == StoreItemType.GROCERY_OR_COLLECTION_MISC) && (_searchCollection == 1))
		{
			// Collections - Misc
			return isCollectionMisc(item.getItem());
		}
		
		return true;
	}
	
	private boolean isEquipmentArmor(ItemTemplate item)
	{
		return item.isArmor() && ((item.getBodyPart() == ItemTemplate.SLOT_CHEST) || (item.getBodyPart() == ItemTemplate.SLOT_FULL_ARMOR) || (item.getBodyPart() == ItemTemplate.SLOT_HEAD) || (item.getBodyPart() == ItemTemplate.SLOT_LEGS) || (item.getBodyPart() == ItemTemplate.SLOT_FEET) || (item.getBodyPart() == ItemTemplate.SLOT_GLOVES) || (item.getBodyPart() == (ItemTemplate.SLOT_CHEST | ItemTemplate.SLOT_LEGS)));
	}
	
	private boolean isAccessory(ItemTemplate item)
	{
		return item.isArmor() && ((item.getBodyPart() == (ItemTemplate.SLOT_L_BRACELET | ItemTemplate.SLOT_R_BRACELET | ItemTemplate.SLOT_BROOCH)) || (item.getBodyPart() == (ItemTemplate.SLOT_R_FINGER | ItemTemplate.SLOT_L_FINGER)) || (item.getBodyPart() == ItemTemplate.SLOT_NECK) || (item.getBodyPart() == (ItemTemplate.SLOT_R_EAR | ItemTemplate.SLOT_L_EAR)));
	}
	
	private boolean isEnchantScroll(ItemTemplate item)
	{
		if (!(item instanceof EtcItem))
		{
			return false;
		}
		
		final IItemHandler ih = ItemHandler.getInstance().getHandler((EtcItem) item);
		
		return (ih != null) && ih.getClass().getSimpleName().equals("EnchantScrolls");
	}
	
	private boolean isCrystal(ItemTemplate item)
	{
		return EnsoulData.getInstance().getStone(item.getId()) != null;
	}
	
	private boolean isLifeStone(ItemTemplate item)
	{
		return VariationData.getInstance().hasVariation(item.getId());
	}
	
	private boolean isDye(ItemTemplate item)
	{
		return HennaData.getInstance().getHennaByItemId(item.getId()) != null;
	}
	
	private boolean isSpellBook(ItemTemplate item)
	{
		return item.getName().contains("Spellbook: ");
	}
	
	private boolean isEnhancementMisc(ItemTemplate item)
	{
		return (item.getId() >= 91031) && (item.getId() <= 91038);
	}
	
	private boolean isEnhancementItem(ItemTemplate item)
	{
		return isEnchantScroll(item) || isCrystal(item) || isLifeStone(item) || isDye(item) || isSpellBook(item) || isEnhancementMisc(item);
	}
	
	private boolean isTicket(ItemTemplate item)
	{
		return (item.getId() == 90045) || (item.getId() == 91462) || (item.getId() == 91463) || (item.getId() == 91972) || (item.getId() == 93903);
	}
	
	private boolean isPackOrCraft(ItemTemplate item)
	{
		if ((item.getId() == 92477) || (item.getId() == 91462) || (item.getId() == 92478) || (item.getId() == 92479) || (item.getId() == 92480) || (item.getId() == 92481) || (item.getId() == 49756) || (item.getId() == 93906) || (item.getId() == 93907) || (item.getId() == 93908) || (item.getId() == 93909) || (item.getId() == 93910) || (item.getId() == 91076))
		{
			return true;
		}
		
		if (!(item instanceof EtcItem))
		{
			return false;
		}
		
		final IItemHandler ih = ItemHandler.getInstance().getHandler((EtcItem) item);
		return (ih != null) && ih.getClass().getSimpleName().equals("ExtractableItems");
	}
	
	private boolean isGroceryMisc(ItemTemplate item)
	{
		// Kinda fallback trash category to ensure no skipping any items.
		return !item.isEquipable() && !isEnhancementItem(item) && !isCollection(item) && !item.isPotion() && !item.isScroll() && !isTicket(item) && !isPackOrCraft(item);
	}
	
	private boolean isCollection(ItemTemplate item)
	{
		for (CollectionDataHolder collectionHolder : CollectionData.getInstance().getCollections())
		{
			for (ItemEnchantHolder itemData : collectionHolder.getItems())
			{
				if (itemData.getId() == item.getId())
				{
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean isCollectionEquipement(ItemTemplate item)
	{
		return isCollection(item) && item.isEquipable();
	}
	
	private boolean isCollectionEnchanted(ItemTemplate item)
	{
		return isCollection(item) && item.getName().contains("Spellbook: ");
	}
	
	private boolean isCollectionMisc(ItemTemplate item)
	{
		return (item.getId() >= 93906) && (item.getId() <= 93910);
	}
	
	private enum StoreType
	{
		SELL((byte) 0x00),
		BUY((byte) 0x01),
		ALL((byte) 0x03);
		
		private final byte _storeType;
		
		StoreType(byte storeType)
		{
			_storeType = storeType;
		}
		
		public static StoreType findById(int id)
		{
			for (StoreType storeType : values())
			{
				if (storeType.getValue() == id)
				{
					return storeType;
				}
			}
			return null;
		}
		
		public byte getValue()
		{
			return _storeType;
		}
	}
	
	private enum StoreItemType
	{
		ALL((byte) 0xFF),
		EQUIPMENT((byte) 0x00),
		ENHANCEMENT_OR_EXPING((byte) 0x02),
		GROCERY_OR_COLLECTION_MISC((byte) 0x04);
		
		private final byte _storeItemType;
		
		StoreItemType(byte storeItemType)
		{
			_storeItemType = storeItemType;
		}
		
		public static StoreItemType findById(int id)
		{
			for (StoreItemType storeItemType : values())
			{
				if (storeItemType.getValue() == id)
				{
					return storeItemType;
				}
			}
			return null;
		}
		
		public byte getValue()
		{
			return _storeItemType;
		}
	}
	
	private enum StoreSubItemType
	{
		ALL((byte) 0xFF),
		WEAPON((byte) 0x00),
		ARMOR((byte) 0x01),
		ACCESSORY((byte) 0x02),
		EQUIPMENT_MISC((byte) 0x03),
		ENCHANT_SCROLL((byte) 0x08),
		LIFE_STONE((byte) 0x0F),
		DYES((byte) 0x10),
		CRYSTAL((byte) 0x11),
		SPELLBOOK((byte) 0x12),
		ENHANCEMENT_MISC((byte) 0x13),
		POTION_SCROLL((byte) 0x14),
		TICKET((byte) 0x15),
		PACK_CRAFT((byte) 0x16),
		GROCERY_MISC((byte) 0x18);
		
		private final byte _storeSubItemType;
		
		StoreSubItemType(byte storeSubItemType)
		{
			_storeSubItemType = storeSubItemType;
		}
		
		public static StoreSubItemType findById(int id)
		{
			for (StoreSubItemType storeSubItemType : values())
			{
				if (storeSubItemType.getValue() == id)
				{
					return storeSubItemType;
				}
			}
			return null;
		}
		
		public byte getValue()
		{
			return _storeSubItemType;
		}
	}
	
	public static class ShopItem
	{
		private final TradeItem _item;
		private final Player _owner;
		private final PrivateStoreType _storeType;
		
		public ShopItem(TradeItem item, Player owner, PrivateStoreType storeType)
		{
			_item = item;
			_owner = owner;
			_storeType = storeType;
		}
		
		public long getCount()
		{
			return _item.getCount();
		}
		
		public ItemInfo getItemInfo()
		{
			return new ItemInfo(_item);
		}
		
		public Player getOwner()
		{
			return _owner;
		}
		
		public PrivateStoreType getStoreType()
		{
			return _storeType;
		}
		
		public long getPrice()
		{
			return _item.getPrice();
		}
	}
}
