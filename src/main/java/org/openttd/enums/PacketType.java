/*
 *  Copyright (C) 2011 Nathanael Rebsch
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package org.openttd.enums;

/**
 *
 * @author Nathanael Rebsch
 */
public enum PacketType implements Reversible<Integer>
{
    ADMIN_PACKET_ADMIN_JOIN              (0),    ///< The admin announces and authenticates itself to the server.
    ADMIN_PACKET_ADMIN_QUIT              (1),    ///< The admin tells the server that it is quitting.
    ADMIN_PACKET_ADMIN_UPDATE_FREQUENCY  (2),    ///< The admin tells the server the update frequency of a particular piece of information.
    ADMIN_PACKET_ADMIN_POLL              (3),    ///< The admin explicitly polls for a piece of information.
    ADMIN_PACKET_ADMIN_CHAT              (4),    ///< The admin sends a chat message to be distributed.
    ADMIN_PACKET_ADMIN_RCON              (5),    ///< The admin sends a remote console command.

    ADMIN_PACKET_SERVER_FULL             (100),  ///< The server tells the admin it cannot accept the admin.
    ADMIN_PACKET_SERVER_BANNED           (101),  ///< The server tells the admin it is banned.
    ADMIN_PACKET_SERVER_ERROR            (102),  ///< The server tells the admin an error has occurred.
    ADMIN_PACKET_SERVER_PROTOCOL         (103),  ///< The server tells the admin its protocol version.
    ADMIN_PACKET_SERVER_WELCOME          (104),  ///< The server welcomes the admin to a game.
    ADMIN_PACKET_SERVER_NEWGAME          (105),  ///< The server tells the admin its going to start a new game.
    ADMIN_PACKET_SERVER_SHUTDOWN         (106),  ///< The server tells the admin its shutting down.

    ADMIN_PACKET_SERVER_DATE             (107),  ///< The server tells the admin what the current game date is.
    ADMIN_PACKET_SERVER_CLIENT_JOIN      (108),  ///< The server tells the admin that a client has joined.
    ADMIN_PACKET_SERVER_CLIENT_INFO      (109),  ///< The server gives the admin information about a client.
    ADMIN_PACKET_SERVER_CLIENT_UPDATE    (110),  ///< The server gives the admin an information update on a client.
    ADMIN_PACKET_SERVER_CLIENT_QUIT      (111),  ///< The server tells the admin that a client quit.
    ADMIN_PACKET_SERVER_CLIENT_ERROR     (112),  ///< The server tells the admin that a client caused an error.
    ADMIN_PACKET_SERVER_COMPANY_NEW      (113),  ///< The server tells the admin that a new company has started.
    ADMIN_PACKET_SERVER_COMPANY_INFO     (114),  ///< The server gives the admin information about a company.
    ADMIN_PACKET_SERVER_COMPANY_UPDATE   (115),  ///< The server gives the admin an information update on a company.
    ADMIN_PACKET_SERVER_COMPANY_REMOVE   (116),  ///< The server tells the admin that a company was removed.
    ADMIN_PACKET_SERVER_COMPANY_ECONOMY  (117),  ///< The server gives the admin some economy related company information.
    ADMIN_PACKET_SERVER_COMPANY_STATS    (118),  ///< The server gives the admin some statistics about a company.
    ADMIN_PACKET_SERVER_CHAT             (119),  ///< The server received a chat message and relays it.
    ADMIN_PACKET_SERVER_RCON             (120),  ///< The server's reply to a remove console command.
    ADMIN_PACKET_SERVER_CONSOLE          (121),  ///< The server gives the admin the data that got printed to its console.
    ADMIN_PACKET_SERVER_CMD_NAMES        (122),  ///< The server gives the admin names of all DoCommands.
    ADMIN_PACKET_SERVER_CMD_LOGGING      (123),  ///< The server gives the admin DoCommand information (for logging purposes only).
    ADMIN_PACKET_SERVER_END              (124),  ///< For internal reference only, mark the end.
    ADMIN_PACKET_SERVER_GAMESCRIPT       (125),  ///< The server gives the admin information from the GameScript in JSON.

    INVALID_ADMIN_PACKET                 (0xFF); ///< An invalid marker for admin packets.

    private final Integer value;
    private final String dispatchName;

    private static final ReverseLookup<Integer, PacketType> lookup;

    static {
        lookup = new ReverseLookup<Integer, PacketType>(PacketType.class);
    }

    PacketType (int i)
    {
        value = i;
        dispatchName = this.dispatchName();
    }

    /**
     * Turn the name into a valid method name.
     * Example:
     *   ADMIN_PACKET_SERVER_CLIENT_INFO becomes receiveServerClientInfo
     *   ADMIN_PACKET_ADMIN_RCON becomes sendAdminRcon
     * @return Valid method name to use for receiving.
     */
    private String dispatchName ()
    {
        StringBuilder result;
        String name = this.name().replaceFirst("ADMIN_PACKET_", "").toLowerCase();

        /* receive packets start at 100 */
        if (this.value < 100) {
            result = new StringBuilder("send");
        } else {
            result = new StringBuilder("receive");
        }

        for (String part : name.split("_")) {
            result.append(part.substring(0,1).toUpperCase());
            result.append(part.substring(1));
        }

        return result.toString();
    }

    /**
     * Get the dispatch name of the enum.
     * Example:
     *   ADMIN_PACKET_SERVER_CLIENT_INFO becomes receiveServerClientInfo
     *   ADMIN_PACKET_ADMIN_RCON becomes sendAdminRcon
     * @return Cached name of the enum ready to use in a receive method.
     */
    public String getDispatchName ()
    {
        return this.dispatchName;
    }

    public static boolean isValid (int i)
    {
        return valueOf(i) != null;
    }

    @Override
    public Integer getValue ()
    {
        return value;
    }

    public static PacketType valueOf (int i)
    {
        return lookup.get(i);
    }
}
