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

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.net.Socket;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handling of sending packets to an OpenTTD server in a separate thread.
 * @author Nathanael Rebsch
 */
public class NetworkInputThread implements Runnable
{
    private static final NetworkInputThread singleton;
    private static final HashMap<Socket, BlockingQueue<Packet>> queues;

    private final Logger log = LoggerFactory.getLogger(NetworkInputThread.class);

    static {
        singleton = new NetworkInputThread();
        queues = new HashMap<Socket, BlockingQueue<Packet>>();
    }

    /**
     * Constructor kept private for a good reason.
     */
    private NetworkInputThread ()
    {
        new Thread(this).start();
    }

    /**
     * Create a queue if there should not be one already.
     * @param socket Socket to bind the queue to.
     */
    private static void instanciateQueue (Socket socket)
    {
        if (!queues.containsKey(socket)) {
            queues.put(socket, new ArrayBlockingQueue<Packet>(100, false));
        }
    }

    /**
     * Get the queue bound to the given Socket.
     * @param socket Socket for which a queue should be retrieved.
     * @return The queue bound to the Socket.
     */
    protected static BlockingQueue<Packet> getQueue (Socket socket)
    {
        instanciateQueue(socket);
        return queues.get(socket);
    }

    /**
     * Append a packet to the appropriate queue.
     * @param p Packet to append to the queue.
     */
    protected static void append (Packet p)
    {
        getQueue(p.getSocket()).add(p);
    }

    /**
     * Get the next packet from the top of the queue.
     * @param socket Socket of which queue to get the next Packet.
     * @return Packet from the top of the queue.
     * @throws InterruptedException
     */
    protected static synchronized Packet getNext (Socket socket) throws InterruptedException
    {
        return getQueue(socket).take();
    }

    @Override
    public void run ()
    {
        while (true) {
            for (Socket socket : queues.keySet()) {
                try {
                    if (socket.isClosed()) {
                        queues.remove(socket);
                        log.info("Socket closed: {}", socket.getRemoteSocketAddress().toString());
                        continue;
                    }
                    
                    Packet p = new Packet(socket);
                    
                    /* make sure it's not a close packet, in case we need to shutdown the socket */
                    if (p.getType().isSocketCloseIndicator()) {
                        socket.close();
                    }
                    
                    append(p);
                    log.trace("Received Packet {}", p.getType());
                } catch (IOException ex) {
                    log.error("Failed reading packet", ex);
                } catch (IndexOutOfBoundsException ex) {
                    log.error("Packet size > SEND_MTU?", ex);
                }
            }
        }
    }
}
