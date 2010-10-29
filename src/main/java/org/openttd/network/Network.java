package org.openttd.network;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openttd.OpenTTD;

public class Network
{
    private Socket        socket;
    private NetworkClient network_client;
    private OpenTTD       openttd;
    
    public Network (OpenTTD openttd)
    {
        this.openttd       = openttd;
        this.network_client = new NetworkClient(this);

        Logger.getLogger(Network.class.getName()).setLevel(openttd.loglevel);
    }

    public boolean connect(String host, int port) throws UnknownHostException, IOException
    {
        if (openttd.getPassword().isEmpty()) {
            Logger.getLogger(Network.class.getName()).log(Level.SEVERE, "Cannot connect with empty password");
            System.exit(2);
        }

        this.socket = new Socket(host, port);

        this.socket.setTcpNoDelay(true);
        this.socket.setKeepAlive(false);

        this.network_client.SEND_ADMIN_PACKET_ADMIN_JOIN();
        return true;
    }

    public boolean isConnected ()
    {
        return this.socket.isConnected();
    }

    public void disconnect ()
    {
        try {
            this.socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Network.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    protected OpenTTD getOpenTTD()
    {
        return openttd;
    }

    protected Socket getSocket()
    {
        return socket;
    }

    public void receive () throws IOException
    {
        network_client.start();
    }
    
    public void serverMessagePublic(String msg) throws IOException
    {
        network_client.SEND_ADMIN_PACKET_ADMIN_CHAT(NetworkAction.NETWORK_ACTION_SERVER_MESSAGE, DestType.DESTTYPE_BROADCAST, 0, msg, 0);
    }

    public void serverMessagePrivate(long client, String msg) throws IOException
    {
        network_client.SEND_ADMIN_PACKET_ADMIN_CHAT(NetworkAction.NETWORK_ACTION_SERVER_MESSAGE, DestType.DESTTYPE_CLIENT, client, msg, 0);
    }

    public void chatPublic(String msg) throws IOException
    {
        network_client.SEND_ADMIN_PACKET_ADMIN_CHAT(NetworkAction.NETWORK_ACTION_CHAT, DestType.DESTTYPE_BROADCAST, 0, msg, 0);
    }

    public void chatPrivate(long client, String msg) throws IOException
    {
        network_client.SEND_ADMIN_PACKET_ADMIN_CHAT(NetworkAction.NETWORK_ACTION_CHAT_CLIENT, DestType.DESTTYPE_CLIENT, client, msg, 0);
    }

    public void chatTeam(int company, String msg) throws IOException
    {
        network_client.SEND_ADMIN_PACKET_ADMIN_CHAT(NetworkAction.NETWORK_ACTION_SERVER_MESSAGE, DestType.DESTTYPE_TEAM, company, msg, 0);
    }

    
    public synchronized void SEND_ADMIN_PACKET_ADMIN_UPDATE_FREQUENCY (AdminUpdateType type, AdminUpdateFrequency freq) throws IOException
    {
        network_client.SEND_ADMIN_PACKET_ADMIN_UPDATE_FREQUENCY(type, freq);
    }

    public synchronized void SEND_ADMIN_PACKET_ADMIN_RCON (String command) throws IOException
    {
        if (!command.isEmpty())
            network_client.SEND_ADMIN_PACKET_ADMIN_RCON(command);
    }

    public synchronized void SEND_ADMIN_PACKET_ADMIN_QUIT () throws IOException
    {
        network_client.SEND_ADMIN_PACKET_ADMIN_QUIT();
    }

    public synchronized void SEND_ADMIN_PACKET_ADMIN_CHAT (NetworkAction action, DestType type, long dest, String message, long data) throws IOException
    {
        network_client.SEND_ADMIN_PACKET_ADMIN_CHAT(action, type, dest, message, data);
    }

    public synchronized void POLL_DATE () throws IOException
    {
        network_client.POLL_DATE();
    }

    public synchronized void POLL_COMPANY_STATS () throws IOException
    {
        network_client.POLL_COMPANY_STATS();
    }

    public synchronized void POLL_COMPANY_INFOS () throws IOException
    {
        network_client.POLL_COMPANY_INFOS();
    }

    public synchronized void POLL_COMPANY_INFO (int company_id) throws IOException
    {
        network_client.POLL_COMPANY_INFO(company_id);
    }

    public synchronized void POLL_COMPANY_ECONOMY () throws IOException
    {
        network_client.POLL_COMPANY_ECONOMY();
    }

    public synchronized void POLL_CLIENT_INFOS () throws IOException
    {
        network_client.POLL_CLIENT_INFOS();
    }

    public synchronized void POLL_CLIENT_INFO (long client_id) throws IOException
    {
        network_client.POLL_CLIENT_INFO(client_id);
    }
}