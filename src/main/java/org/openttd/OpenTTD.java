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

package org.openttd;

import java.io.IOException;
import java.math.BigInteger;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openttd.enums.*;
import org.openttd.network.Network;
import org.openttd.network.Protocol;

/**
 * The OpenTTD Admin Network Bot Framework
 *
 * Extend this Class and implement the methods you wish to make use of.
 * @see OpenTTDBotExample
 *
 * @author Nathanael Rebsch
 */
public abstract class OpenTTD
{
    protected Game game;
    protected Pool pool;
    protected Network network;

    protected String botName;
    protected String botVersion;

    protected String hostname = "localhost";
    protected int    port     = 3977;
    protected String password = "";

    public    Level  loglevel = Level.INFO;

    public OpenTTD ()
    {
        game = new Game();
        pool = new Pool();
        network = new Network(this);
    }

    /**
     * Connect the bot to the specified server
     * @param host Hostname or IP of the server.
     * @param port Port number of the server's admin network.
     * @param password Password required to join the admin network.
     * @throws UnknownHostException
     * @throws IOException
     */
    public void connect (String host, int port, String password) throws UnknownHostException, IOException
    {
        this.hostname = host;
        this.port     = port;
        this.password = password;

        this.connect();
    }

    /**
     * Connect to a server.
     * @throws UnknownHostException
     * @throws IOException
     */
    public void connect () throws UnknownHostException, IOException
    {
        network.connect(this.hostname, this.port);
        network.receive();
    }

    /**
     * @return Name of the bot.
     */
    public String getBotName ()
    {
        return botName;
    }

    /**
     * @return Version of the bot.
     */
    public String getBotVersion ()
    {
        return botVersion;
    }

    /**
     * @return Current representation of the Game.
     */
    public Game getGame ()
    {
        return game;
    }

    /**
     * @return Network handler of this bot.
     */
    public Network getNetwork ()
    {
        return network;
    }

    /**
     * @return The password required to join the admin network.
     */
    public String getPassword ()
    {
        return this.password;
    }

    public String getHostname ()
    {
        return hostname;
    }

    /**
     * Set the Hostname or IP of the server to join.
     * @param hostname
     */
    public void setHostname (String hostname)
    {
        this.hostname = hostname;
    }

    /**
     * @return The port.
     */
    public int getPort ()
    {
        return port;
    }

    /**
     * Set the port of the server.
     * @param port
     */
    public void setPort (int port)
    {
        this.port = port;
    }

    /**
     * Set the password needed to join the admin network.
     * @param password
     */
    public void setPassword (String password)
    {
        this.password = password;
    }

    /**
     * @return The pool handling.
     */
    public Pool getPool ()
    {
        return pool;
    }

    /**
     * Set the name of this bot.
     * @param name
     */
    public void setBotName (String name)
    {
        this.botName = name;
    }

    /**
     * Set the version of this bot.
     * @param version
     */
    public void setBotVersion (String version)
    {
        this.botVersion = version;
    }

    /**
     * Register an AdminUpdateType at AdminUpdateFrequency with the server.
     * @param type AdminUpdateType to be registered
     * @param freq AdminUpdateFrequency the type should be registered for
     */
    protected final void registerUpdateFrequency (AdminUpdateType type, AdminUpdateFrequency freq)
    {
        try {
            network.SEND_ADMIN_PACKET_ADMIN_UPDATE_FREQUENCY(type, freq);
        } catch (IOException ex) {
            Logger.getLogger(OpenTTD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Send poll requests for everything (order is important).
     */
    public final void pollAll ()
    {
        this.pollDate();
        this.pollClientInfos();
        this.pollCompanyInfos();
        this.pollCompanyStats();
        this.pollCompanyEconomy();
    }

    /**
     * Poll the server for the Date.
     */
    public final void pollDate ()
    {
        try {
            network.POLL_DATE();
        } catch (IOException ex) {
            Logger.getLogger(OpenTTD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Poll the server for all Clients
     */
    public final void pollClientInfos ()
    {
        try {
            network.POLL_CLIENT_INFOS();
        } catch (IOException ex) {
            Logger.getLogger(OpenTTD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * poll the server for a certain client.
     * @param clientId The Id of the client
     */
    public final void pollClientInfo (long clientId)
    {
        try {
            network.POLL_CLIENT_INFO(clientId);
        } catch (IOException ex) {
            Logger.getLogger(OpenTTD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Poll the server for all companies.
     */
    public final void pollCompanyInfos ()
    {
        try {
            network.POLL_COMPANY_INFOS();
        } catch (IOException ex) {
            Logger.getLogger(OpenTTD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Poll the server for a certain company.
     * @param companyId The Id of the company
     */
    public final void pollCompanyInfo (int companyId)
    {
        try {
            network.POLL_COMPANY_INFO(companyId);
        } catch (IOException ex) {
            Logger.getLogger(OpenTTD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Poll the server for all company stats.
     */
    public final void pollCompanyStats ()
    {
        try {
            network.POLL_COMPANY_STATS();
        } catch (IOException ex) {
            Logger.getLogger(OpenTTD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * poll the server for all company economy data.
     */
    public final void pollCompanyEconomy ()
    {
        try {
            network.POLL_COMPANY_ECONOMY();
        } catch (IOException ex) {
            Logger.getLogger(OpenTTD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Called when the server tells us that it is full.
     */
    public void onServerFull () {}

    /**
     * Called when the server tells us this IP is banned.
     */
    public void onServerBanned () {}

    /**
     * This bot has caused an Error.
     * @param error The error that was caused by this bot.
     */
    public void onServerError(NetworkErrorCode error) {}


    /**
     * The server has Welcomed us to the game.
     * @param game Game details such as Server name and various Map details.
     */
    public void onServerWelcome (Game game) {}

    /**
     * Current game Date.
     * @param date
     */
    public void onServerDate (GameDate date) {}


    /**
     * A client has joined the game.
     * @param client
     */
    public void onClientJoin (Client client) {}

    /**
     * Client information.
     * @param client
     */
    public void onClientInfo (Client client) {}

    /**
     * Client has changed something, e.g. name, or has moved into another company.
     * @param client
     */
    public void onClientUpdate (Client client) {}

    /**
     * The client has quit / left the game.
     * @param client
     */
    public void onClientQuit (Client client) {}

    /**
     * The client has caused an error and was disconnected.
     * @param client
     * @param error
     */
    public void onClientError (Client client, NetworkErrorCode error) {}


    /**
     * A new company has been created.
     * @param company
     */
    public void onCompanyNew (Company company) {}

    /**
     * Information on a company has been received.
     * @param company
     */
    public void onCompanyInfo (Company company) {}

    /**
     * Some company details have changed, e.g. password status, name, etc.
     * @param company
     */
    public void onCompanyUpdate (Company company) {}

    /**
     * Update on company stats (vehicle and station numbers).
     * @param company
     */
    public void onCompanyStats (Company company) {}

    /**
     * Economy data on the company has arrived.
     * @param company
     */
    public void onCompanyEconomy (Company company) {}

    /**
     * A company has been removed.
     * @param company
     * @param crr Reason for the companies removal (e.g. Bankruptcy).
     */
    public void onCompanyRemove (Company company, AdminCompanyRemoveReason crr) {}


    /**
     * We have sent an Rcon command, this is the answer.
     * @param colour  Colour of the message.
     * @param message The output of our Rcon command.
     */
    public void onRcon (Colour colour, String message) {}

    /**
     * Someone in the game is chatting.
     * To distinguish if this chat was send to everybody, or was a private chat to the server
     * Check action and desttype.
     * @param action The action of this chat message (e.g. give money).
     * @param desttype The destination type of this chat message.
     * @param client The client that sent this message.
     * @param message The actual message
     * @param data Money (in case this was a 'give money' action).
     */
    public void onChat (NetworkAction action, DestType desttype, Client client, String message, BigInteger data) {}

    /**
     * The server is going to load a new map / saved map.
     * During this process the bot stays connected to the server.
     */
    public void onNewgame () {}

    /**
     * The server is going to shutdown.
     */
    public void onShutdown () {}


    /**
     * The server is notifying us of it's protocol version and further details.
     * @param protocol Protocol details of what we cover.
     */
    public void onProtocol (Protocol protocol) {}

    /**
     * Output on the server console is being sent to the bot.
     * @param origin  e.g. console, net, ai, ...
     * @param message the message as printed on the dedicated server console.
     */
    public void onConsole (String origin, String message) {}
}
