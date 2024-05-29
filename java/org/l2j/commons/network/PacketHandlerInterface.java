package org.l2j.commons.network;

/**
 * @author Pantelis Andrianakis
 * @since October 4th 2022
 * @param <E> extends NetClient
 */
public interface PacketHandlerInterface<E extends NetClient>
{
	default void handle(E client, ReadablePacket packet)
	{
	}
}
