package uk.co.v2systems.framework.shell;

import org.apache.commons.net.telnet.TelnetClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.v2systems.framework.utils.Methods;

import java.io.DataInputStream;
import java.io.PrintStream;

/**
 * Created by Pankaj Buchade on 26/06/2015.
 *
 * Note: Still to handle process which doesn't return prompt
 */
public class CustomTelnetClient {
    static TelnetClient telnetClient;
    static DataInputStream dataInputStream;
    static PrintStream printStream;
    static String lastCommandStatus;
    static String lastCommandOutput;
    static String [] possiblePrompts = {"#","$"};
    static String hostname;
    static String username;
    static String password;
    static int port;
    static Logger slf4jLogger = LoggerFactory.getLogger(CustomTelnetClient.class);

    public void connect(String hostname, int port, String username, String password){
        try{
            this.hostname=hostname;
            this.port=port;
            telnetClient = new TelnetClient();
            telnetClient.connect(hostname,port);
            dataInputStream = new DataInputStream(telnetClient.getInputStream());
            printStream = new PrintStream(telnetClient.getOutputStream());
            //Authentication
            readUntil( "login: " );
            write(username);
            readUntil(possiblePrompts);
            write(password);
            readUntil(possiblePrompts);
        }catch(Exception e){
            slf4jLogger.error("Exception in TelnetClient.connect ::" + e);
        }
    }
//Execute Command
    public String sendCommand( String command ) {
        try {
            write(command);
            return readUntil(possiblePrompts);
        }catch(Exception e){
            slf4jLogger.error("Exception in TelNetClient.sendCommand: " + e);
        }
        return null;
    }
//Disconnect from the telnet session
    public void disconnect(){
        try {
            if (telnetClient != null)
                telnetClient.disconnect();
        }catch (Exception e){
            slf4jLogger.error("Exception in TelNetClient.disconnect: " + e);
        }
    }
//Read the command output
    public String readUntil( String pattern ) {
        try {
            char lastChar = pattern.charAt( pattern.length() - 1 );
            StringBuffer sb = new StringBuffer();
            boolean found = false;
            char ch = ( char )dataInputStream.read();
            while( true ) {
                System.out.print( ch );
                sb.append( ch );
                if( ch == lastChar ) {
                    if( sb.toString().endsWith( pattern ) ) {
                        return sb.toString();
                    }
                }
                ch = ( char )dataInputStream.read();
            }
        }
        catch( Exception e ) {
            e.printStackTrace();
        }
        return null;
    }

    public String readUntil( String [] pattern ) {
        try {
            char lastChars[];
            boolean found = false;
            lastChars = new char[pattern.length];
            for(int i=0; i<pattern.length; i++)
            {
                lastChars[i] = pattern[i].charAt(pattern[i].length()-1);
            }
            StringBuffer sb = new StringBuffer();
            char ch = ( char )dataInputStream.read();
            while( true ) {
                System.out.print(ch);
                sb.append( ch );
                for(int i=0; i<pattern.length; i++){
                    if( ch == lastChars[i] ) {
                        if( sb.toString().endsWith( pattern[i] ) ) {
                            return sb.toString();
                        }
                    }
                }
                ch = ( char )dataInputStream.read();
            }
        }
        catch( Exception e ) {
            e.printStackTrace();
        }
        return null;
    }

    public void write( String value ) {
        try {
            printStream.println( value );
            printStream.flush();
        }
        catch( Exception e ) {
            e.printStackTrace();
        }
    }
}
