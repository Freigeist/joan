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

import org.openttd.enums.PacketType;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Arrays;
import java.net.Socket;
import java.net.SocketException;

public class Packet
{
    public static final int SEND_MTU = 1460;
    private static final int POS_PACKET_TYPE = 3;

    private final Socket socket;

    private PacketType type = null;
    private byte[] buf;
    private int pos = 0;

    /**
     * Constructor. Creates a new Packet of Type type.
     * @param socket The Socket this is related to.
     * @param type PacketType of this Packet.
     */
    public Packet (final Socket socket, PacketType type)
    {
        this.socket = socket;
        this.buf    = new byte[SEND_MTU];
        this.pos    = POS_PACKET_TYPE + 1;
        this.setPacketType(type);
    }

    /**
     * Constructor. Read the next Packet from the Socket.
     * @param socket
     * @throws IOException
     * @throws IndexOutOfBoundsException
     * @throws RuntimeException
     */
    public Packet (final Socket socket) throws IOException, IndexOutOfBoundsException, RuntimeException
    {
        this.socket = socket;
        this.buf    = new byte[2];

        DataInputStream in = new DataInputStream(socket.getInputStream());

        in.read(this.buf, 0, 2);
        int length = this.length();

        this.buf = Arrays.copyOf(this.buf, length);

        if (socket.isClosed()) {
            throw new RuntimeException("Socket closed");
        }

        if (length > SEND_MTU) {
            throw new IndexOutOfBoundsException("Packet length claims to be greater than SEND_MTU");
        }

        if (length == 0) {
            throw new SocketException("Empty packet received");
        }

        in.readFully(this.buf, 2, this.length() - 2);
        this.pos = POS_PACKET_TYPE + 1;
    }

    protected final Socket getSocket ()
    {
        return this.socket;
    }

    public void append (byte[] buf)
    {
        System.arraycopy(buf, 0, this.buf, this.buf.length, buf.length);
    }

    private void setPacketType (PacketType type)
    {
        this.buf[POS_PACKET_TYPE] = (byte) type.getValue().byteValue();
        this.type = type;
    }

    public void writeBool (boolean b)
    {
        this.buf[this.pos++] = (byte) (b ? 1 : 0);
    }

    public void writeString (String s) throws IOException
    {
        byte b[] = s.getBytes();

        for (int i = 0; i < b.length; i++) {
            this.buf[this.pos++] = b[i];
        }

        this.buf[this.pos++] = '\0';
    }

    public void writeUint8 (short n)
    {
        this.buf[this.pos++] = (byte) n;
    }

    public void writeUint8 (int n)
    {
        this.writeUint8((short) n);
    }

    public void writeUint16 (int n)
    {
        this.buf[this.pos++] = (byte) n;
        this.buf[this.pos++] = (byte) (n >> 8);
    }

    public void writeUint32 (long n)
    {
        this.buf[this.pos++] = (byte) n;
        this.buf[this.pos++] = (byte) (n >> 8);
        this.buf[this.pos++] = (byte) (n >> 16);
        this.buf[this.pos++] = (byte) (n >> 24);
    }

    public void writeUint64 (long n)
    {
        this.buf[this.pos++] = (byte) n;
        this.buf[this.pos++] = (byte) (n >> 8);
        this.buf[this.pos++] = (byte) (n >> 16);
        this.buf[this.pos++] = (byte) (n >> 24);
        this.buf[this.pos++] = (byte) (n >> 32);
        this.buf[this.pos++] = (byte) (n >> 40);
        this.buf[this.pos++] = (byte) (n >> 48);
        this.buf[this.pos++] = (byte) (n >> 56);
    }

    public void writeUint64 (BigInteger n)
    {
        this.writeUint64(n.longValue());
    }

    public boolean readBool ()
    {
        return (this.buf[this.pos++] & 0xFF) > 0;
    }

    public int readUint8 ()
    {
        return (this.buf[this.pos++] & 0xFF);
    }

    public int readUint16 ()
    {
        int n = this.buf[this.pos++] & 0xFF;
        n += (this.buf[this.pos++] & 0xFF) << 8;

        return n;
    }

    public long readUint32 ()
    {
        long n = this.buf[this.pos++] & 0xFF;
        n += (this.buf[this.pos++] & 0xFF) << 8;
        n += (this.buf[this.pos++] & 0xFF) << 16;
        n += (this.buf[this.pos++] & 0xFF) << 24;

        return n;
    }

    public BigInteger readUint64 ()
    {
        long l = 0;
        l += (long)(this.buf[this.pos++] & 0xFF);
        l += (long)(this.buf[this.pos++] & 0xFF) << 8;
        l += (long)(this.buf[this.pos++] & 0xFF) << 16;
        l += (long)(this.buf[this.pos++] & 0xFF) << 24;
        l += (long)(this.buf[this.pos++] & 0xFF) << 32;
        l += (long)(this.buf[this.pos++] & 0xFF) << 40;
        l += (long)(this.buf[this.pos++] & 0xFF) << 48;
        l += (long)(this.buf[this.pos++] & 0xFF) << 56;

        BigInteger b = BigInteger.valueOf(l);
        
        return b;
    }

    public String readString ()
    {
        String out = "";
        int startIdx = this.pos;

        while (this.buf[this.pos++] != (byte) '\0');

        int endIdx = this.pos - startIdx - 1;

        try {
            out = new String(this.buf, startIdx, endIdx, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            /* as "UTF-8" is a supported encoding, we will ignore this part! */
        }

        return out;
    }

    public PacketType getType () throws ArrayIndexOutOfBoundsException
    {
        if (this.type == null) {
            this.type = PacketType.valueOf(this.buf[POS_PACKET_TYPE] & 0xFF);
        }

        return this.type;
    }

    public void send () throws IOException
    {
        this.buf[0] = (byte) this.pos;
        this.buf[1] = (byte) (this.pos >> 8);

        this.socket.getOutputStream().write(Arrays.copyOf(this.buf, this.pos));
    }

    public final int length ()
    {
        int b1 = this.buf[0] & 0xFF;
        int b2 = this.buf[1] & 0xFF;

        int r = (b1 + (b2 << 8));

        return r;
    }

    public void dump ()
    {
        System.out.printf("%s", this.buf.toString());
    }
}
