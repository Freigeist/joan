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

package org.openttd.network;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openttd.OpenTTD;
import org.openttd.enums.*;

public class Network
{
    private Socket        socket;
    private NetworkClient networkClient;
    private OpenTTD       openttd;
    private Protocol      protocol;
    
    public Network (OpenTTD openttd)
    {
        this.openttd       = openttd;
        this.protocol      = new Protocol();
        this.networkClient = new NetworkClient(this);

        Logger.getLogger(Network.class.getName()).setLevel(openttd.loglevel);
    }

    public boolean connect (String host, int port) throws UnknownHostException, IOException
    {
        if (openttd.getPassword().isEmpty()) {
            Logger.getLogger(Network.class.getName()).log(Level.SEVERE, "Cannot connect with empty password");
            System.exit(2);
        }

        this.socket = new Socket(host, port);

        this.socket.setTcpNoDelay(true);
        this.socket.setKeepAlive(false);

        this.networkClient.sendAdminJoin();
        return true;
    }

    public boolean isConnected ()
    {
        return this.socket.isConnected() && !this.socket.isClosed();
    }

    public void disconnect ()
    {
        try {
            this.socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Network.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    protected OpenTTD getOpenTTD ()
    {
        return openttd;
    }

    protected Protocol getProtocol ()
    {
        return protocol;
    }

    protected Socket getSocket ()
    {
        return socket;
    }

    public void receive () throws IOException
    {
        networkClient.start();
    }
    
    public void serverMessagePublic (String msg) throws IOException
    {
        networkClient.sendAdminChat(NetworkAction.NETWORK_ACTION_SERVER_MESSAGE, DestType.DESTTYPE_BROADCAST, 0, msg, 0);
    }

    public void serverMessagePrivate (long client, String msg) throws IOException
    {
        networkClient.sendAdminChat(NetworkAction.NETWORK_ACTION_SERVER_MESSAGE, DestType.DESTTYPE_CLIENT, client, msg, 0);
    }

    public void chatPublic (String msg) throws IOException
    {
        networkClient.sendAdminChat(NetworkAction.NETWORK_ACTION_CHAT, DestType.DESTTYPE_BROADCAST, 0, msg, 0);
    }

    public void chatPrivate (long client, String msg) throws IOException
    {
        networkClient.sendAdminChat(NetworkAction.NETWORK_ACTION_CHAT_CLIENT, DestType.DESTTYPE_CLIENT, client, msg, 0);
    }

    public void chatTeam (int company, String msg) throws IOException
    {
        networkClient.sendAdminChat(NetworkAction.NETWORK_ACTION_SERVER_MESSAGE, DestType.DESTTYPE_TEAM, company, msg, 0);
    }

    
    public synchronized void SEND_ADMIN_PACKET_ADMIN_UPDATE_FREQUENCY (AdminUpdateType type, AdminUpdateFrequency freq) throws IOException
    {
        networkClient.sendAdminUpdateFrequency(type, freq);
    }

    public synchronized void SEND_ADMIN_PACKET_ADMIN_RCON (String command) throws IOException
    {
        if (!command.isEmpty())
            networkClient.sendAdminRcon(command);
    }

    public synchronized void SEND_ADMIN_PACKET_ADMIN_QUIT () throws IOException
    {
        networkClient.sendAdminQuit();
    }

    public synchronized void SEND_ADMIN_PACKET_ADMIN_CHAT (NetworkAction action, DestType type, long dest, String message, long data) throws IOException
    {
        networkClient.sendAdminChat(action, type, dest, message, data);
    }

    public synchronized void POLL_DATE () throws IOException
    {
        networkClient.POLL_DATE();
    }

    public synchronized void POLL_COMPANY_STATS () throws IOException
    {
        networkClient.POLL_COMPANY_STATS();
    }

    public synchronized void POLL_COMPANY_INFOS () throws IOException
    {
        networkClient.POLL_COMPANY_INFOS();
    }

    public synchronized void POLL_COMPANY_INFO (int company_id) throws IOException
    {
        networkClient.POLL_COMPANY_INFO(company_id);
    }

    public synchronized void POLL_COMPANY_ECONOMY () throws IOException
    {
        networkClient.POLL_COMPANY_ECONOMY();
    }

    public synchronized void POLL_CLIENT_INFOS () throws IOException
    {
        networkClient.POLL_CLIENT_INFOS();
    }

    public synchronized void POLL_CLIENT_INFO (long client_id) throws IOException
    {
        networkClient.POLL_CLIENT_INFO(client_id);
    }

    public synchronized void POLL_CMD_NAMES () throws IOException
    {
        networkClient.POLL_CMD_NAMES();
    }
}
