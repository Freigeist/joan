/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openttd.network;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openttd.Client;
import org.openttd.Colour;
import org.openttd.Company;
import org.openttd.Pool;
import org.openttd.GameDate;
import org.openttd.Game;
import org.openttd.Landscape;
import org.openttd.Map;
import org.openttd.OpenTTD;

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
        try {
            while(network.getSocket().isConnected())
                receive();
        } catch (IOException ex) {
            Logger.getLogger(Network.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void send (PacketType type) throws IOException
    {
        Packet p = new Packet(type);
        this.send(p);
    }

    private void send (Packet p) throws IOException
    {
        p.send(network.getSocket());
    }

    public void receive () throws IOException
    {
        Packet p = new Packet(network.getSocket());

        try {
            this.getClass().getMethod("RECEIVE_" + p.getType(), OpenTTD.class, Packet.class).invoke(this, network.getOpenTTD(), p);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(Network.class.getName()).log(Level.SEVERE, "Method not found: {0}", ex.getMessage());
        } catch (SecurityException ex) {
            Logger.getLogger(Network.class.getName()).log(Level.SEVERE, "SECURITY {0}", p.getType());
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Network.class.getName()).log(Level.SEVERE, "ILLEGAL_ACCESS {0}", p.getType());
        } catch (InvocationTargetException ex) {
            Logger.getLogger(Network.class.getName()).log(Level.SEVERE, "INVOCATION {0}", p.getType());
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Network.class.getName()).log(Level.SEVERE, "ILLEGAL_ARGUMENT {0}", p.getType());
        } catch (ArrayIndexOutOfBoundsException ex) {
            return;
        }
    }
    
    public synchronized void POLL_DATE () throws IOException
    {
        SEND_ADMIN_PACKET_ADMIN_POLL(AdminUpdateType.ADMIN_UPDATE_DATE);
    }

    public synchronized void POLL_CLIENT_INFOS () throws IOException
    {
        POLL_CLIENT_INFO(Long.MAX_VALUE);
    }

    public synchronized void POLL_CLIENT_INFO (long client_id) throws IOException
    {
        SEND_ADMIN_PACKET_ADMIN_POLL(AdminUpdateType.ADMIN_UPDATE_CLIENT_INFO, client_id);
    }

    public synchronized void POLL_COMPANY_INFOS () throws IOException
    {
        POLL_COMPANY_INFO(Integer.MAX_VALUE);
    }

    public synchronized void POLL_COMPANY_INFO (int company_id) throws IOException
    {
        SEND_ADMIN_PACKET_ADMIN_POLL(AdminUpdateType.ADMIN_UPDATE_COMPANY_INFO, company_id);
    }

    public synchronized void POLL_COMPANY_ECONOMY () throws IOException
    {
        SEND_ADMIN_PACKET_ADMIN_POLL(AdminUpdateType.ADMIN_UPDATE_COMPANY_ECONOMY, 0);
    }

    public synchronized void POLL_COMPANY_STATS () throws IOException
    {
        SEND_ADMIN_PACKET_ADMIN_POLL(AdminUpdateType.ADMIN_UPDATE_COMPANY_STATS, 0);
    }



    public synchronized void RECEIVE_ADMIN_PACKET_SERVER_FULL (OpenTTD openttd, Packet p)
    {
        openttd.onServerFull();
    }

    public synchronized void RECEIVE_ADMIN_PACKET_SERVER_BANNED (OpenTTD openttd, Packet p)
    {
        openttd.onServerBanned();
    }

    public synchronized void RECEIVE_ADMIN_PACKET_SERVER_ERROR (OpenTTD openttd, Packet p)
    {
        NetworkErrorCode error = NetworkErrorCode.valueOf(p.recv_uint8());
        openttd.onServerError(error);
    }

    public synchronized void RECEIVE_ADMIN_PACKET_SERVER_WELCOME (OpenTTD openttd, Packet p)
    {
        Game game = new Game();
        Map  map  = new Map();

        game.name      = p.recv_string();
        game.version   = p.recv_string();
        game.dedicated = p.recv_bool();

        map.name = p.recv_string();
        map.seed = p.recv_uint32();
        map.landscape = Landscape.Get(p.recv_uint8());
        map.start_date = new GameDate(p.recv_uint32());
        map.width      = p.recv_uint16();
        map.height     = p.recv_uint16();

        game.map = map;

        openttd.onServerWelcome(game);
    }

    public synchronized void RECEIVE_ADMIN_PACKET_SERVER_DATE (OpenTTD openttd, Packet p)
    {
        GameDate date = new GameDate(p.recv_uint32());

        openttd.getGame().getMap().current_date = date;

        openttd.onServerDate(date);
    }

    public synchronized void RECEIVE_ADMIN_PACKET_SERVER_CLIENT_JOIN (OpenTTD openttd, Packet p) throws IOException
    {
        Pool pool      = openttd.getPool();
        long client_id = p.recv_uint32();

        if (pool.getClientPool().exists(client_id)) {
            Client client = pool.getClientPool().get(client_id);

            openttd.onClientJoin(client);
            return;
        }

        /* we know nothing about this client, request an update */
        POLL_CLIENT_INFO(client_id);
        Logger.getLogger(Network.class.getName()).log(Level.INFO, "Unknown client joined #{0}", client_id);
    }

    public synchronized void RECEIVE_ADMIN_PACKET_SERVER_CLIENT_INFO (OpenTTD openttd, Packet p)
    {
        Client client = new Client(p.recv_uint32());

        client.address    = p.recv_string();
        client.name       = p.recv_string();
        client.language   = NetworkLanguage.valueOf(p.recv_uint8());
        client.joindate   = new GameDate(p.recv_uint32());
        client.company_id = p.recv_uint8();

        openttd.getPool().getClientPool().add(client);

        openttd.onClientInfo(client);
    }

    public synchronized void RECEIVE_ADMIN_PACKET_SERVER_CLIENT_UPDATE (OpenTTD openttd, Packet p) throws IOException
    {
        Pool pool      = openttd.getPool();
        long client_id = p.recv_uint32();

        if (pool.getClientPool().exists(client_id)) {
            Client client = pool.getClientPool().get(client_id);

            client.name       = p.recv_string();
            client.company_id = p.recv_uint8();

            openttd.onClientUpdate(client);
            return;
        }

        /* we know nothing about this client, request an update */
        POLL_CLIENT_INFO(client_id);
        Logger.getLogger(Network.class.getName()).log(Level.INFO, "Unknown client update #{0}", client_id);
    }

    public synchronized void RECEIVE_ADMIN_PACKET_SERVER_CLIENT_QUIT (OpenTTD openttd, Packet p)
    {
        Pool pool      = openttd.getPool();
        long client_id = p.recv_uint32();

        if (pool.getClientPool().exists(client_id)) {
            Client client = pool.getClientPool().remove(client_id);

            openttd.onClientQuit(client);
            return;
        }

        /* we do not seem to have known anything about this client, but as the client left, do nothing. */
        Logger.getLogger(Network.class.getName()).log(Level.INFO, "Unknown client quit #{0}", client_id);
    }

    public synchronized void RECEIVE_ADMIN_PACKET_SERVER_CLIENT_ERROR (OpenTTD openttd, Packet p)
    {
        Pool pool      = openttd.getPool();
        long client_id = p.recv_uint32();

        NetworkErrorCode error = NetworkErrorCode.valueOf(p.recv_uint8());

        if (pool.getClientPool().exists(client_id)) {
            Client client = pool.getClientPool().remove(client_id);

            openttd.onClientError(client, error);
            return;
        }

        /* we do not seem to have known anything about this client, but as the client left, do nothing. */
        Logger.getLogger(Network.class.getName()).log(Level.INFO, "Unknown client error #{0}", client_id);
    }

    public synchronized void RECEIVE_ADMIN_PACKET_SERVER_COMPANY_NEW (OpenTTD openttd, Packet p) throws IOException
    {
        Pool pool      = openttd.getPool();
        int company_id = p.recv_uint8();

        if (pool.getCompanyPool().exists(company_id)) {
            Company company = pool.getCompanyPool().get(company_id);

            openttd.onCompanyNew(company);
            return;
        }

        /* we know nothing about this ccompany, request an update */
        POLL_COMPANY_INFO(company_id);
        Logger.getLogger(Network.class.getName()).log(Level.INFO, "Unknown company new #{0}", company_id);
    }

    public synchronized void RECEIVE_ADMIN_PACKET_SERVER_COMPANY_INFO (OpenTTD openttd, Packet p)
    {
        Company company = new Company(p.recv_uint8());

        company.name        = p.recv_string();
        company.president   = p.recv_string();
        company.colour      = Colour.valueOf(p.recv_uint8());
        company.passworded  = p.recv_bool();
        company.inaugurated = p.recv_uint32();
        company.ai          = p.recv_bool();

        openttd.getPool().getCompanyPool().add(company);

        openttd.onCompanyInfo(company);
    }

    public synchronized void RECEIVE_ADMIN_PACKET_SERVER_COMPANY_UPDATE (OpenTTD openttd, Packet p) throws IOException
    {
        Pool pool      = openttd.getPool();
        int company_id = p.recv_uint8();

        if (pool.getCompanyPool().exists(company_id)) {
            Company company = pool.getCompanyPool().get(company_id);

            company.name        = p.recv_string();
            company.president   = p.recv_string();
            company.colour      = Colour.valueOf(p.recv_uint8());
            company.passworded  = p.recv_bool();
            company.bankruptcy  = p.recv_uint8();

            for (short i = 0; i < 4; i++) {
                company.shares[i] = p.recv_uint8();
            }

            openttd.onCompanyUpdate(company);
            return;
        }

        /* we know nothing about this ccompany, request an update */
        POLL_COMPANY_INFO(company_id);
        Logger.getLogger(Network.class.getName()).log(Level.INFO, "Unknown company update #{0}", company_id);
    }

    public synchronized void RECEIVE_ADMIN_PACKET_SERVER_COMPANY_ECONOMY (OpenTTD openttd, Packet p) throws IOException
    {
        Pool pool      = openttd.getPool();
        int company_id = p.recv_uint8();

        if (pool.getCompanyPool().exists(company_id)) {
            Company company = pool.getCompanyPool().get(company_id);

            // TODO: company economy handling

            openttd.onCompanyEconomy(null);
            return;
        }

        /* we know nothing about this ccompany, request an update */
        POLL_COMPANY_INFO(company_id);
        Logger.getLogger(Network.class.getName()).log(Level.INFO, "Unknown company economy #{0}", company_id);
    }

    public synchronized void RECEIVE_ADMIN_PACKET_SERVER_COMPANY_STATS (OpenTTD openttd, Packet p) throws IOException
    {
        Pool pool      = openttd.getPool();
        int company_id = p.recv_uint8();

        if (pool.getCompanyPool().exists(company_id)) {
            Company company = pool.getCompanyPool().get(company_id);

            for (short i = 0; i < 5; i++) {
                company.vehicles[i] = p.recv_uint16();
            }

            for (short i = 0; i < 5; i++) {
                company.stations[i] = p.recv_uint16();
            }

            openttd.onCompanyStats(company);
            return;
        }

        /* we know nothing about this ccompany, request an update */
        POLL_COMPANY_INFO(company_id);
        Logger.getLogger(Network.class.getName()).log(Level.INFO, "Unknown company stats #{0}", company_id);
    }

    public synchronized void RECEIVE_ADMIN_PACKET_SERVER_COMPANY_REMOVE (OpenTTD openttd, Packet p)
    {
        Pool pool      = openttd.getPool();
        int company_id = p.recv_uint8();

        AdminCompanyRemoveReason crr = AdminCompanyRemoveReason.get(p.recv_uint8());

        if (pool.getCompanyPool().exists(company_id)) {
            Company company = pool.getCompanyPool().remove(company_id);

            openttd.onCompanyRemove(company, crr);
        }

        /* we do not seem to have known anything about this company, but as the company got closed down, do nothing. */
        Logger.getLogger(Network.class.getName()).log(Level.INFO, "Unknown company removed #{0}", company_id);
    }

    public synchronized void RECEIVE_ADMIN_PACKET_SERVER_CHAT (OpenTTD openttd, Packet p)
    {
        Pool pool            = openttd.getPool();
        NetworkAction action = NetworkAction.valueOf(p.recv_uint8());
        DestType dest        = DestType.valueOf(p.recv_uint8());
        long client_id       = p.recv_uint32();
        String message       = p.recv_string();
        BigInteger data      = p.recv_uint64();

        if (pool.getClientPool().exists(client_id)) {
            Client client = pool.getClientPool().get(client_id);

            openttd.onChat(action, dest, client, message, data);
            return;
        }

        /* we know nothing of the client who aparently sent this message, just drop it */
    }

    public synchronized void RECEIVE_ADMIN_PACKET_SERVER_NEWGAME (OpenTTD openttd, Packet p)
    {
        openttd.onNewgame();
    }

    public synchronized void RECEIVE_ADMIN_PACKET_SERVER_SHUTDOWN (OpenTTD openttd, Packet p)
    {
        openttd.onShutdown();
    }

    public synchronized void RECEIVE_ADMIN_PACKET_SERVER_RCON (OpenTTD openttd, Packet p)
    {
        Colour colour  = Colour.valueOf(p.recv_uint16());
        String message = p.recv_string();

        openttd.onRcon(colour, message);
    }

    public synchronized void RECEIVE_ADMIN_PACKET_SERVER_PROTOCOL (OpenTTD openttd, Packet p)
    {
        Protocol protocol = network.getProtocol();
        
        protocol.version = p.recv_uint8();

        while (p.recv_bool()) {
            int tIndex  = p.recv_uint16();
            int fValues = p.recv_uint16();

            /* Bitwise handling in java is ucky */
            while (fValues > 0) {
                int index = Integer.lowestOneBit(fValues);
                protocol.addSupport(tIndex, index);

                fValues -= index;
            }
        }

        network.getOpenTTD().onProtocol(protocol);
    }

    public synchronized void RECEIVE_ADMIN_PACKET_SERVER_CONSOLE (OpenTTD openttd, Packet p)
    {
        String origin  = p.recv_string();
        String message = p.recv_string();

        openttd.onConsole(origin, message);
    }

    

    public synchronized void SEND_ADMIN_PACKET_ADMIN_JOIN () throws IOException
    {
        Packet p = new Packet(PacketType.ADMIN_PACKET_ADMIN_JOIN);
        p.send_string(network.getOpenTTD().getPassword());
        p.send_string(network.getOpenTTD().getBotName());
        p.send_string(network.getOpenTTD().getBotVersion());

        this.send(p);
    }

    public synchronized void SEND_ADMIN_PACKET_ADMIN_UPDATE_FREQUENCY (AdminUpdateType type, AdminUpdateFrequency freq) throws IOException, IllegalArgumentException
    {
        if (!network.getProtocol().isSupported(type, freq))
            throw new IllegalArgumentException("The server does not support " + freq + " for " + type);

        Packet p = new Packet(PacketType.ADMIN_PACKET_ADMIN_UPDATE_FREQUENCY);
        p.send_uint16(type.getValue());
        p.send_uint16(freq.getValue());

        this.send(p);
    }

    public synchronized void SEND_ADMIN_PACKET_ADMIN_POLL (AdminUpdateType type) throws IOException, IllegalArgumentException
    {
        SEND_ADMIN_PACKET_ADMIN_POLL(type, 0);
    }

    public synchronized void SEND_ADMIN_PACKET_ADMIN_POLL (AdminUpdateType type, long data) throws IOException, IllegalArgumentException
    {
        if (!network.getProtocol().isSupported(type, AdminUpdateFrequency.ADMIN_FREQUENCY_POLL))
            throw new IllegalArgumentException("The server does not support ADMIN_FREQUENCY_POLL for " + type);

        Packet p = new Packet(PacketType.ADMIN_PACKET_ADMIN_POLL);
        p.send_uint8(type.getValue());
        p.send_uint32(data);

        this.send(p);
    }

    public synchronized void SEND_ADMIN_PACKET_ADMIN_CHAT (NetworkAction action, DestType type, long dest, String message, long data) throws IOException
    {
        Packet p = new Packet(PacketType.ADMIN_PACKET_ADMIN_CHAT);
        p.send_uint8(action.ordinal());
        p.send_uint8(type.ordinal());
        p.send_uint32(dest);

        message.trim();
        if (message.length() >= 900) {
            return;
        }

        p.send_string(message);
        p.send_uint64(data);

        this.send(p);
    }

    public synchronized void SEND_ADMIN_PACKET_ADMIN_QUIT () throws IOException
    {
        this.send(PacketType.ADMIN_PACKET_ADMIN_QUIT);
    }

    public synchronized void SEND_ADMIN_PACKET_ADMIN_RCON (String command) throws IOException
    {
        Packet p = new Packet(PacketType.ADMIN_PACKET_ADMIN_RCON);
        p.send_string(command);

        this.send(p);
    }
}
