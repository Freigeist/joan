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

    
    public synchronized void sendAdminUpdateFrequency (AdminUpdateType type, AdminUpdateFrequency freq) throws IOException
    {
        networkClient.sendAdminUpdateFrequency(type, freq);
    }

    public synchronized void sendAdminRcon (String command) throws IOException
    {
        if (!command.isEmpty())
            networkClient.sendAdminRcon(command);
    }

    public synchronized void sendAdminQuit () throws IOException
    {
        networkClient.sendAdminQuit();
    }

    public synchronized void sendAdminChat (NetworkAction action, DestType type, long dest, String message, long data) throws IOException
    {
        networkClient.sendAdminChat(action, type, dest, message, data);
    }

    public synchronized void pollDate () throws IOException
    {
        networkClient.pollDate();
    }

    public synchronized void pollCompanyStats () throws IOException
    {
        networkClient.pollCompanyStats();
    }

    public synchronized void pollCompanyInfos () throws IOException
    {
        networkClient.pollCompanyInfos();
    }

    public synchronized void pollCompanyInfo (int company_id) throws IOException
    {
        networkClient.pollCompanyInfo(company_id);
    }

    public synchronized void pollCompanyEconomy () throws IOException
    {
        networkClient.pollCompanyEconomy();
    }

    public synchronized void pollClientInfos () throws IOException
    {
        networkClient.pollClientInfos();
    }

    public synchronized void pollClientInfo (long clientId) throws IOException
    {
        networkClient.pollClientInfo(clientId);
    }

    public synchronized void pollCmdNames () throws IOException
    {
        networkClient.pollCmdNames();
    }
}
