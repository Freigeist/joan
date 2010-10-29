package org.openttd.network;

import java.io.*;
import java.net.*;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Packet
{

    final static int SEND_MTU = 1460;
    private PacketType type = null;
    byte[] buf;
    int pos = 0;

    public Packet () throws IOException
    {
        this.buf = new byte[SEND_MTU];
        this.pos = 2;
    }

    public Packet (PacketType type)
    {
        this.buf = new byte[SEND_MTU];
        this.pos = 3;
        this.setPacketType(type);
    }

    public Packet (byte[] buf)
    {
        this.buf = buf;
    }

    public Packet (Socket s) throws IOException
    {
        this.buf = new byte[2];
        DataInputStream in = new DataInputStream(s.getInputStream());

        in.read(this.buf, 0, 2);
        int length = this.length();

        this.buf = Arrays.copyOf(this.buf, length);

        if (length > SEND_MTU) {
            Logger.getLogger(Network.class.getName()).log(Level.INFO, "SEND_MTU exceeded");
        }

        // TODO: throw an exception to handle
        if (length == 0) {
            Logger.getLogger(Network.class.getName()).log(Level.INFO, "Empty packet received");
            System.exit(1);
        }

        in.readFully(this.buf, 2, this.length() - 2);
        this.pos = 3;

        Logger.getLogger(Network.class.getName()).log(Level.INFO, "Receiving {0}", this.getType());
    }

    public void append (byte[] buf)
    {
        System.arraycopy(buf, 0, this.buf, this.buf.length, buf.length);
    }

    public void setPacketType (PacketType type)
    {
        this.buf[2] = (byte) type.getValue().byteValue();
        this.type = type;
    }

    public void send_bool (boolean b)
    {
        this.buf[this.pos++] = (byte) (b ? 1 : 0);
    }

    public void send_string (String s) throws IOException
    {
        byte b[] = s.getBytes();

        for (int i = 0; i < b.length; i++) {
            this.buf[this.pos++] = b[i];
        }

        this.buf[this.pos++] = '\0';
    }

    public void send_uint8 (short n)
    {
        this.buf[this.pos++] = (byte) n;
    }

    public void send_uint8 (int n)
    {
        this.send_uint8((short) n);
    }

    public void send_uint16 (int n)
    {
        this.buf[this.pos++] = (byte) n;
        this.buf[this.pos++] = (byte) (n >> 8);
    }

    public void send_uint32 (long n)
    {
        this.buf[this.pos++] = (byte) n;
        this.buf[this.pos++] = (byte) (n >> 8);
        this.buf[this.pos++] = (byte) (n >> 16);
        this.buf[this.pos++] = (byte) (n >> 24);
    }

    public void send_uint64 (long n)
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

    public void send_uint64 (BigInteger n)
    {
        this.send_uint64(n.longValue());
    }

    public boolean recv_bool ()
    {
        return (this.buf[this.pos++] & 0xFF) > 0;
    }

    public int recv_uint8 ()
    {
        return (this.buf[this.pos++] & 0xFF);
    }

    public int recv_uint16 ()
    {
        int n = this.buf[this.pos++] & 0xFF;
        n += (this.buf[this.pos++] & 0xFF) << 8;

        return n;
    }

    public long recv_uint32 ()
    {
        long n = this.buf[this.pos++] & 0xFF;
        n += (this.buf[this.pos++] & 0xFF) << 8;
        n += (this.buf[this.pos++] & 0xFF) << 16;
        n += (this.buf[this.pos++] & 0xFF) << 24;

        return n;
    }

    public BigInteger recv_uint64 ()
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

    public String recv_string ()
    {
        String out = "";
        int startIdx = this.pos;

        while (this.buf[this.pos++] != (byte) '\0');

        int endIdx = this.pos - startIdx - 1;

        try {
            out = new String(this.buf, startIdx, endIdx, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            /* as utf8 is a supported encoding, we will ignore this part! */
        }

        return out;
    }

    public PacketType getType () throws ArrayIndexOutOfBoundsException
    {
        if (this.type == null) {
            this.type = PacketType.valueOf(this.buf[2] & 0xFF);
        }

        return this.type;
    }

    public void send (Socket s) throws IOException
    {
        Logger.getLogger(Network.class.getName()).log(Level.INFO, "Sending {0}", this.getType());
        
        this.buf[0] = (byte) this.pos;
        this.buf[1] = (byte) (this.pos >> 8);

        s.getOutputStream().write(Arrays.copyOf(this.buf, this.pos));
    }

    public int length ()
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
