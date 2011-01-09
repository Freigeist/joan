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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openttd.Client;
import org.openttd.Company;
import org.openttd.Pool;
import org.openttd.GameDate;
import org.openttd.Game;
import org.openttd.Map;
import org.openttd.OpenTTD;
import org.openttd.enums.*;

/**
 *
 * @author nathanael
 */
public class NetworkClient extends Thread
{
    private Network network;

    protected NetworkClient (Network network)
    {
        this.network = network;
        Logger.getLogger(Network.class.getName()).setLevel(network.getOpenTTD().loglevel);
    }

    @Override
    public void run ()
    {
        while (network.isConnected())
            receive();
    }

    public void send (PacketType type) throws IOException
    {
        Packet p = new Packet(network.getSocket(), type);
        NetworkOutputThread.append(p);
    }

    public void receive ()
    {
        try {
            Packet p = NetworkInputThread.getNext(network.getSocket());
            delegatePacket(p);
        } catch (InterruptedException ex) {
            Logger.getLogger(NetworkClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void delegatePacket (Packet p)
    {
        try {
            this.getClass().getMethod(p.getType().getDispatchName(), OpenTTD.class, Packet.class).invoke(this, network.getOpenTTD(), p);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(Network.class.getName()).log(Level.SEVERE, ex.getCause().getMessage(), ex.getCause());
        } catch (SecurityException ex) {
            Logger.getLogger(Network.class.getName()).log(Level.SEVERE, ex.getCause().getMessage(), ex.getCause());
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Network.class.getName()).log(Level.SEVERE, ex.getCause().getMessage(), ex.getCause());
        } catch (InvocationTargetException ex) {
            Logger.getLogger(Network.class.getName()).log(Level.SEVERE, ex.getCause().getMessage(), ex.getCause());
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Network.class.getName()).log(Level.SEVERE, ex.getCause().getMessage(), ex.getCause());
        } catch (ArrayIndexOutOfBoundsException ex) {
            Logger.getLogger(Network.class.getName()).log(Level.SEVERE, ex.getCause().getMessage(), ex.getCause());
            return;
        }
    }
    
    public synchronized void pollDate () throws IOException
    {
        sendAdminPoll(AdminUpdateType.ADMIN_UPDATE_DATE);
    }

    public synchronized void pollClientInfos () throws IOException
    {
        pollClientInfo(Long.MAX_VALUE);
    }

    public synchronized void pollClientInfo (long clientId) throws IOException
    {
        sendAdminPoll(AdminUpdateType.ADMIN_UPDATE_CLIENT_INFO, clientId);
    }

    public synchronized void pollCompanyInfos () throws IOException
    {
        sendAdminPoll(AdminUpdateType.ADMIN_UPDATE_COMPANY_INFO, Long.MAX_VALUE);
    }

    public synchronized void pollCompanyInfo (int companyId) throws IOException
    {
        sendAdminPoll(AdminUpdateType.ADMIN_UPDATE_COMPANY_INFO, companyId);
    }

    public synchronized void pollCompanyEconomy () throws IOException
    {
        sendAdminPoll(AdminUpdateType.ADMIN_UPDATE_COMPANY_ECONOMY, 0);
    }

    public synchronized void pollCompanyStats () throws IOException
    {
        sendAdminPoll(AdminUpdateType.ADMIN_UPDATE_COMPANY_STATS, 0);
    }

    public synchronized void pollCmdNames ()  throws IOException
    {
        sendAdminPoll(AdminUpdateType.ADMIN_UPDATE_CMD_NAMES);
    }



    public synchronized void receiveServerFull (OpenTTD openttd, Packet p)
    {
        network.disconnect();
        openttd.onServerFull();
    }

    public synchronized void receiveServerBanned (OpenTTD openttd, Packet p)
    {
        network.disconnect();
        openttd.onServerBanned();
    }

    public synchronized void receiveServerError (OpenTTD openttd, Packet p)
    {
        network.disconnect();
        NetworkErrorCode error = NetworkErrorCode.valueOf(p.readUint8());
        openttd.onServerError(error);
    }

    public synchronized void receiveServerWelcome (OpenTTD openttd, Packet p)
    {
        Game game = new Game();
        Map  map  = new Map();

        game.name      = p.readString();
        game.version   = p.readString();
        game.dedicated = p.readBool();

        map.name = p.readString();
        map.seed = p.readUint32();
        map.landscape = Landscape.valueOf(p.readUint8());
        map.start_date = new GameDate(p.readUint32());
        map.width      = p.readUint16();
        map.height     = p.readUint16();

        game.map = map;

        openttd.onServerWelcome(game);
    }

    public synchronized void receiveServerDate (OpenTTD openttd, Packet p)
    {
        GameDate date = new GameDate(p.readUint32());

        openttd.getGame().getMap().current_date = date;

        openttd.onServerDate(date);
    }

    public synchronized void receiveServerClientJoin (OpenTTD openttd, Packet p) throws IOException
    {
        Pool pool     = openttd.getPool();
        long clientId = p.readUint32();

        if (pool.getClientPool().exists(clientId)) {
            Client client = pool.getClientPool().get(clientId);

            openttd.onClientJoin(client);
            return;
        }

        /* we know nothing about this client, request an update */
        pollClientInfo(clientId);
        Logger.getLogger(Network.class.getName()).log(Level.INFO, "Unknown client joined #{0}", clientId);
    }

    public synchronized void receiveServerClientInfo (OpenTTD openttd, Packet p)
    {
        Client client = new Client(p.readUint32());

        client.address   = p.readString();
        client.name      = p.readString();
        client.language  = NetworkLanguage.valueOf(p.readUint8());
        client.joindate  = new GameDate(p.readUint32());
        client.companyId = p.readUint8();

        openttd.getPool().getClientPool().add(client);

        openttd.onClientInfo(client);
    }

    public synchronized void receiveServerClientUpdate (OpenTTD openttd, Packet p) throws IOException
    {
        Pool pool     = openttd.getPool();
        long clientId = p.readUint32();

        if (pool.getClientPool().exists(clientId)) {
            Client client = pool.getClientPool().get(clientId);

            client.name      = p.readString();
            client.companyId = p.readUint8();

            openttd.onClientUpdate(client);
            return;
        }

        /* we know nothing about this client, request an update */
        pollClientInfo(clientId);
        Logger.getLogger(Network.class.getName()).log(Level.INFO, "Unknown client update #{0}", clientId);
    }

    public synchronized void receiveServerClientQuit (OpenTTD openttd, Packet p)
    {
        Pool pool     = openttd.getPool();
        long clientId = p.readUint32();

        if (pool.getClientPool().exists(clientId)) {
            Client client = pool.getClientPool().remove(clientId);

            openttd.onClientQuit(client);
            return;
        }

        /* we do not seem to have known anything about this client, but as the client left, do nothing. */
        Logger.getLogger(Network.class.getName()).log(Level.INFO, "Unknown client quit #{0}", clientId);
    }

    public synchronized void receiveServerClientError (OpenTTD openttd, Packet p)
    {
        Pool pool     = openttd.getPool();
        long clientId = p.readUint32();

        NetworkErrorCode error = NetworkErrorCode.valueOf(p.readUint8());

        if (pool.getClientPool().exists(clientId)) {
            Client client = pool.getClientPool().remove(clientId);

            openttd.onClientError(client, error);
            return;
        }

        /* we do not seem to have known anything about this client, but as the client left, do nothing. */
        Logger.getLogger(Network.class.getName()).log(Level.INFO, "Unknown client error #{0}", clientId);
    }

    public synchronized void receiveServerCompanyNew (OpenTTD openttd, Packet p) throws IOException
    {
        Pool pool     = openttd.getPool();
        int companyId = p.readUint8();

        if (pool.getCompanyPool().exists(companyId)) {
            Company company = pool.getCompanyPool().get(companyId);

            openttd.onCompanyNew(company);
            return;
        }

        /* we know nothing about this ccompany, request an update */
        pollCompanyInfo(companyId);
        Logger.getLogger(Network.class.getName()).log(Level.INFO, "Unknown company new #{0}", companyId);
    }

    public synchronized void receiveServerCompanyInfo (OpenTTD openttd, Packet p)
    {
        Company company = new Company(p.readUint8());

        company.name        = p.readString();
        company.president   = p.readString();
        company.colour      = Colour.valueOf(p.readUint8());
        company.passworded  = p.readBool();
        company.inaugurated = p.readUint32();
        company.ai          = p.readBool();

        openttd.getPool().getCompanyPool().add(company);

        openttd.onCompanyInfo(company);
    }

    public synchronized void receiveServerCompanyUpdate (OpenTTD openttd, Packet p) throws IOException
    {
        Pool pool     = openttd.getPool();
        int companyId = p.readUint8();

        if (pool.getCompanyPool().exists(companyId)) {
            Company company = pool.getCompanyPool().get(companyId);

            company.name        = p.readString();
            company.president   = p.readString();
            company.colour      = Colour.valueOf(p.readUint8());
            company.passworded  = p.readBool();
            company.bankruptcy  = p.readUint8();

            for (short i = 0; i < 4; i++) {
                company.shares[i] = p.readUint8();
            }

            openttd.onCompanyUpdate(company);
            return;
        }

        /* we know nothing about this ccompany, request an update */
        pollCompanyInfo(companyId);
        Logger.getLogger(Network.class.getName()).log(Level.INFO, "Unknown company update #{0}", companyId);
    }

    public synchronized void receiveServerCompanyEconomy (OpenTTD openttd, Packet p) throws IOException
    {
        Pool pool     = openttd.getPool();
        int companyId = p.readUint8();

        if (pool.getCompanyPool().exists(companyId)) {
            Company company = pool.getCompanyPool().get(companyId);

            // TODO: company economy handling

            openttd.onCompanyEconomy(null);
            return;
        }

        /* we know nothing about this ccompany, request an update */
        pollCompanyInfo(companyId);
        Logger.getLogger(Network.class.getName()).log(Level.INFO, "Unknown company economy #{0}", companyId);
    }

    public synchronized void receiveServerCompanyStats (OpenTTD openttd, Packet p) throws IOException
    {
        Pool pool     = openttd.getPool();
        int companyId = p.readUint8();

        if (pool.getCompanyPool().exists(companyId)) {
            Company company = pool.getCompanyPool().get(companyId);

            for (VehicleType vt : VehicleType.values()) {
                company.vehicles.put(vt, p.readUint16());
            }

            for (VehicleType vt : VehicleType.values()) {
                company.stations.put(vt, p.readUint16());
            }

            openttd.onCompanyStats(company);
            return;
        }

        /* we know nothing about this ccompany, request an update */
        pollCompanyInfo(companyId);
        Logger.getLogger(Network.class.getName()).log(Level.INFO, "Unknown company stats #{0}", companyId);
    }

    public synchronized void receiveServerCompanyRemove (OpenTTD openttd, Packet p)
    {
        Pool pool     = openttd.getPool();
        int companyId = p.readUint8();

        AdminCompanyRemoveReason crr = AdminCompanyRemoveReason.valueOf(p.readUint8());

        if (pool.getCompanyPool().exists(companyId)) {
            Company company = pool.getCompanyPool().remove(companyId);

            openttd.onCompanyRemove(company, crr);
        }

        /* we do not seem to have known anything about this company, but as the company got closed down, do nothing. */
        Logger.getLogger(Network.class.getName()).log(Level.INFO, "Unknown company removed #{0}", companyId);
    }

    public synchronized void receiveServerChat (OpenTTD openttd, Packet p)
    {
        Pool pool            = openttd.getPool();
        NetworkAction action = NetworkAction.valueOf(p.readUint8());
        DestType dest        = DestType.valueOf(p.readUint8());
        long clientId        = p.readUint32();
        String message       = p.readString();
        BigInteger data      = p.readUint64();

        if (pool.getClientPool().exists(clientId)) {
            Client client = pool.getClientPool().get(clientId);

            openttd.onChat(action, dest, client, message, data);
            return;
        }

        /* we know nothing of the client who aparently sent this message, just drop it */
    }

    public synchronized void receiveServerNewgame (OpenTTD openttd, Packet p)
    {
        openttd.onNewgame();
    }

    public synchronized void receiveServerShutdown (OpenTTD openttd, Packet p)
    {
        network.disconnect();
        openttd.onShutdown();
    }

    public synchronized void receiveServerRcon (OpenTTD openttd, Packet p)
    {
        Colour colour  = Colour.valueOf(p.readUint16());
        String message = p.readString();

        openttd.onRcon(colour, message);
    }

    public synchronized void receiveServerProtocol (OpenTTD openttd, Packet p)
    {
        Protocol protocol = network.getProtocol();
        
        protocol.version = p.readUint8();

        while (p.readBool()) {
            int tIndex  = p.readUint16();
            int fValues = p.readUint16();

            /* Bitwise handling in java is ucky */
            while (fValues > 0) {
                int index = Integer.lowestOneBit(fValues);
                protocol.addSupport(tIndex, index);

                fValues -= index;
            }
        }

        network.getOpenTTD().onProtocol(protocol);
    }

    public synchronized void receiveServerConsole (OpenTTD openttd, Packet p)
    {
        String origin  = p.readString();
        String message = p.readString();

        openttd.onConsole(origin, message);
    }

    public synchronized void receiveServerCmdNames (OpenTTD openttd, Packet p)
    {
        while(p.readBool()) {
            int cmdId = p.readUint16();
            String cmdName = p.readString();

            new DoCommandName(cmdName, cmdId);
        }
    }

    public synchronized void receiveServerCmdLogging (OpenTTD openttd, Packet p) throws IOException
    {
        Pool pool = openttd.getPool();

        long clientId  = p.readUint32();
        int companyId  = p.readUint8();
        int command_id = p.readUint16();
        long p1        = p.readUint32();
        long p2        = p.readUint32();
        long tile      = p.readUint32();
        String text    = p.readString();
        long frame     = p.readUint32();

        Client client = pool.getClientPool().get(clientId);

        if (client == null) {
            this.pollClientInfo(clientId);
            return;
        }

        Company company = pool.getCompanyPool().get(companyId);

        if (company == null) {
            this.pollCompanyInfo(companyId);
            return;
        }

        DoCommandName command = DoCommandName.valueOf(command_id);

        if (command == null) {
            this.pollCmdNames();
            return;
        }

        openttd.onCmdLogging(client, company, command, p1, p2, tile, text, frame);
    }



    public synchronized void sendAdminJoin () throws IOException
    {
        Packet p = new Packet(network.getSocket(), PacketType.ADMIN_PACKET_ADMIN_JOIN);
        p.writeString(network.getOpenTTD().getPassword());
        p.writeString(network.getOpenTTD().getBotName());
        p.writeString(network.getOpenTTD().getBotVersion());

        NetworkOutputThread.append(p);
    }

    public synchronized void sendAdminUpdateFrequency (AdminUpdateType type, AdminUpdateFrequency freq) throws IOException, IllegalArgumentException
    {
        if (!network.getProtocol().isSupported(type, freq))
            throw new IllegalArgumentException("The server does not support " + freq + " for " + type);

        Packet p = new Packet(network.getSocket(), PacketType.ADMIN_PACKET_ADMIN_UPDATE_FREQUENCY);
        p.writeUint16(type.getValue());
        p.writeUint16(freq.getValue());

        NetworkOutputThread.append(p);
    }

    public synchronized void sendAdminPoll (AdminUpdateType type) throws IOException, IllegalArgumentException
    {
        sendAdminPoll(type, 0);
    }

    public synchronized void sendAdminPoll (AdminUpdateType type, long data) throws IOException, IllegalArgumentException
    {
        if (!network.getProtocol().isSupported(type, AdminUpdateFrequency.ADMIN_FREQUENCY_POLL))
            throw new IllegalArgumentException("The server does not support ADMIN_FREQUENCY_POLL for " + type);

        Packet p = new Packet(network.getSocket(), PacketType.ADMIN_PACKET_ADMIN_POLL);
        p.writeUint8(type.getValue());
        p.writeUint32(data);

        NetworkOutputThread.append(p);
    }

    public synchronized void sendAdminChat (NetworkAction action, DestType type, long dest, String message, long data) throws IOException
    {
        Packet p = new Packet(network.getSocket(), PacketType.ADMIN_PACKET_ADMIN_CHAT);
        p.writeUint8(action.ordinal());
        p.writeUint8(type.ordinal());
        p.writeUint32(dest);

        message.trim();
        if (message.length() >= 900) {
            return;
        }

        p.writeString(message);
        p.writeUint64(data);

        NetworkOutputThread.append(p);
    }

    public synchronized void sendAdminQuit () throws IOException
    {
        this.send(PacketType.ADMIN_PACKET_ADMIN_QUIT);
        network.disconnect();
    }

    public synchronized void sendAdminRcon (String command) throws IOException
    {
        Packet p = new Packet(network.getSocket(), PacketType.ADMIN_PACKET_ADMIN_RCON);
        p.writeString(command);

        NetworkOutputThread.append(p);
    }
}
