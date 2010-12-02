/* $Id$ */
package org.openttd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openttd.enums.*;

/**
 * Sample Bot Class, demonstrates a remote console implementation
 * @author Nathanael Rebsch
 */
public class SimpleConsole extends OpenTTD
{
    private Properties properties;

    public static void main (String[] args)
    {
        if (args.length < 1) {
            System.out.println("Please provide a properties file as first argument.");
            System.exit(1);
        }

        File file = new File(args[0]);

        if (!file.exists()) {
            System.out.println("File not found.");
            System.exit(1);
        }

        System.out.println("starting console...");

        SimpleConsole bot = new SimpleConsole(file);
    }

    /**
     * Simple Console constructor
     * @param file The config file.
     */
    public SimpleConsole (File file)
    {
        loadProperties(file);

        this.botName = "Simple Console";
        this.botVersion = "0.1";

        try {
            this.hostname = properties.getProperty("host");
            this.port     = Integer.valueOf(properties.getProperty("port"));
            this.password = properties.getProperty("pass");

            try {
                this.loglevel = Level.parse(properties.getProperty("loglevel"));
            } catch (Exception ex) {
                System.out.println("No 'loglevel' key found in config file.");
                System.out.println("  Possible values: OFF, INFO, SEVERE, WARNING");
                System.out.println("  Loglevel OFF will be used.");
                this.loglevel = Level.OFF;
            }

            this.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String str = "";
            while (network.isConnected()) {
                str = in.readLine();
                network.SEND_ADMIN_PACKET_ADMIN_RCON(str);
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(SimpleConsole.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SimpleConsole.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void shutdownBot ()
    {
        System.exit(0);
    }

    /**
     * Load properties from a simple file containing:
     * <key> = <value> pairs.
     * @param file The config file.
     */
    private void loadProperties (File file)
    {
        properties = new Properties();
        try {
            properties.load(new FileInputStream(file));
        } catch (IOException ex) {
            Logger.getLogger(SimpleConsole.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            System.exit(1);
        }
    }

    /**
     * Lets do something when we receive the "welcome" packet
     */
    @Override
    public void onServerWelcome(Game game)
    {
        /* register for console updates */
        this.registerUpdateFrequency(AdminUpdateType.ADMIN_UPDATE_CONSOLE, AdminUpdateFrequency.ADMIN_FREQUENCY_AUTOMATIC);

        System.out.println("... ready.");
    }

    /**
     * We have received console output, print it.
     */
    @Override
    public void onConsole(String origin, String message)
    {
        origin = String.format("[%s]", origin);
        System.out.printf("%-10s %s\n", origin, message);
    }

    /**
     * Result from an Rcon command we entered.
     */
    @Override
    public void onRcon(Colour colour, String message)
    {
        System.out.println(StringFunc.stripColour(message));
    }

    @Override
    public void onShutdown ()
    {
        System.out.println("Server Shutdown");
        shutdownBot();
    }

    @Override
    public void onServerError (NetworkErrorCode error)
    {
        System.out.println(error);
        shutdownBot();
    }

    @Override
    public void onServerFull ()
    {
        System.out.println("The server is full.");
        shutdownBot();
    }

    @Override
    public void onServerBanned ()
    {
        System.out.println("The server has you banned.");
        shutdownBot();
    }
}
