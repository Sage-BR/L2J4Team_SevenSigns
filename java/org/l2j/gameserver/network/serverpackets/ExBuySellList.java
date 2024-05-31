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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.l2j.Config;
import org.l2j.gameserver.enums.TaxType;
import org.l2j.gameserver.instancemanager.CastleManager;
import org.l2j.gameserver.model.actor.Player;
import org.l2j.gameserver.model.actor.Summon;
import org.l2j.gameserver.model.buylist.Product;
import org.l2j.gameserver.model.buylist.ProductList;
import org.l2j.gameserver.model.item.instance.Item;
import org.l2j.gameserver.network.PacketLogger;
import org.l2j.gameserver.network.ServerPackets;

/**
 * @author ShanSoft, Index
 */
public class ExBuySellList extends AbstractItemPacket
{
	public static final int BUY_SELL_LIST_BUY = 0;
	public static final int BUY_SELL_LIST_SELL = 1;
	public static final int BUY_SELL_LIST_UNK = 2;
	public static final int BUY_SELL_LIST_TAX = 3;
	
	public static final int UNK_SELECT_FIRST_TAB = 0;
	public static final int UNK_SHOW_PURCHASE_LIST = 1;
	public static final int UNK_SEND_NOT_ENOUGH_ADENA_MESSAGE = 2;
	public static final int UNK_SEND_INCORRECT_ITEM_MESSAGE = 3;
	
	private static final int[] CASTLES =
	{
		3, // Giran
		7, // Goddart
		5, // Aden
	};
	
	private final int _inventorySlots;
	private final int _type;
	
	// buy type - BUY
	private long _money;
	private double _castleTaxRate;
	private Collection<Product> _list;
	private int _listId;
	
	// buy type - SELL
	private final List<Item> _sellList = new ArrayList<>();
	private final Collection<Item> _refundList = new ArrayList<>();
	private boolean _done;
	
	// buy type = unk
	private int _unkType;
	
	// buy type - send tax
	private boolean _applyTax;
	
	public ExBuySellList(ProductList list, Player player, double castleTaxRate)
	{
		_type = BUY_SELL_LIST_BUY;
		_listId = list.getListId();
		_list = list.getProducts();
		_money = player.isGM() && (player.getAdena() == 0) && (list.getNpcsAllowed() == null) ? 1000000000 : player.getAdena();
		_inventorySlots = player.getInventory().getNonQuestSize();
		_castleTaxRate = castleTaxRate;
	}
	
	public ExBuySellList(Player player, boolean done)
	{
		_type = BUY_SELL_LIST_SELL;
		final Summon pet = player.getPet();
		for (Item item : player.getInventory().getItems())
		{
			if (!item.isEquipped() && item.isSellable() && ((pet == null) || (item.getObjectId() != pet.getControlObjectId())))
			{
				_sellList.add(item);
			}
		}
		_inventorySlots = player.getInventory().getNonQuestSize();
		if (player.hasRefund())
		{
			_refundList.addAll(player.getRefund().getItems());
		}
		_done = done;
	}
	
	public ExBuySellList(int type)
	{
		_type = BUY_SELL_LIST_UNK;
		_unkType = type;
		_inventorySlots = 0;
	}
	
	public ExBuySellList()
	{
		_type = BUY_SELL_LIST_TAX;
		_inventorySlots = 0;
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_BUY_SELL_LIST.writeId(this);
		writeInt(_type);
		switch (_type)
		{
			case BUY_SELL_LIST_BUY:
			{
				sendBuyList();
				break;
			}
			case BUY_SELL_LIST_SELL:
			{
				sendSellList();
				break;
			}
			case BUY_SELL_LIST_UNK:
			{
				sendUnk();
				break;
			}
			case BUY_SELL_LIST_TAX:
			{
				sendCurrentTax();
				break;
			}
			default:
			{
				PacketLogger.warning(getClass().getSimpleName() + ": unknown type " + _type);
				break;
			}
		}
	}
	
	private void sendBuyList()
	{
		writeLong(_money); // current money
		writeInt(_listId);
		writeInt(_inventorySlots);
		writeShort(_list.size());
		for (Product product : _list)
		{
			if ((product.getCount() > 0) || !product.hasLimitedStock())
			{
				writeItem(product);
				writeLong((long) (product.getPrice() * (1.0 + _castleTaxRate + product.getBaseTaxRate())));
			}
		}
	}
	
	private void sendSellList()
	{
		writeInt(_inventorySlots);
		if (!_sellList.isEmpty())
		{
			writeShort(_sellList.size());
			for (Item item : _sellList)
			{
				writeItem(item);
				writeLong(Config.MERCHANT_ZERO_SELL_PRICE ? 0 : item.getTemplate().getReferencePrice() / 2);
			}
		}
		else
		{
			writeShort(0);
		}
		if (!_refundList.isEmpty())
		{
			writeShort(_refundList.size());
			int i = 0;
			for (Item item : _refundList)
			{
				writeItem(item);
				writeInt(i++);
				writeLong(Config.MERCHANT_ZERO_SELL_PRICE ? 0 : (item.getTemplate().getReferencePrice() / 2) * item.getCount());
			}
		}
		else
		{
			writeShort(0);
		}
		writeByte(_done ? 1 : 0);
	}
	
	private void sendUnk()
	{
		writeByte(_unkType);
	}
	
	private void sendCurrentTax()
	{
		writeInt(CASTLES.length);
		for (int id : CASTLES)
		{
			writeInt(id); // residence id
			try
			{
				writeInt(_applyTax ? CastleManager.getInstance().getCastleById(id).getTaxPercent(TaxType.BUY) : 0); // residence tax
			}
			catch (NullPointerException ignored)
			{
				writeInt(0);
			}
		}
	}
}
