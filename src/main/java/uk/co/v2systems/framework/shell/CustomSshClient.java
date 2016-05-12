package uk.co.v2systems.framework.shell;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;

/**
 * Created by Pankaj Buchade on 26/06/2015.
 */
public class CustomSshClient {
    static Connection connection;
    static String hostname=null;
    static int port=-1;
    static String username=null;
    static String password=null;
    static Session session = null;
    static String lastCommandStatus;
    static String lastCommandOutput;
    static boolean isAuthenticated=false;
    static Logger logger = LogManager.getLogger(CustomSshClient.class);

    public static int connect(){
        if(hostname!=null && port!=-1 && username!=null && password !=null )
            return connect(hostname,port,username,password);
        return -1;
    }
//Connect using Secure Shell using username password
    public static int connect (String hostname, int port, String username, String password) {
        try{
            CustomSshClient.port=port;
            CustomSshClient.hostname=hostname;
            CustomSshClient.username=username;
            CustomSshClient.password=password;
            connection = new Connection(hostname,CustomSshClient.port);
            connection.connect();
            isAuthenticated = connection.authenticateWithPassword(username, password);
            if (!isAuthenticated)
                throw new IOException("authenticateWithPassword failed.");
            logger.info("ssh successfully connected to host: " + hostname);
            return 0;
        }catch(Exception e){
            logger.error("\nFailed to connect host: " + hostname +"  "+ e.toString());
            return 1; //Exception
        }
    }
//Connect using Secure Shell using securekey
    public static int connect(String hostname, int port, String username, String keyfilePath, String keyfilePass){
        try{
            File keyfile = new File(keyfilePath); // or "~/.ssh/id_dsa"
            connection = new Connection(hostname, port);
            //connection.connect;
            connection.connect(null,10000,10000);
            isAuthenticated = connection.authenticateWithPublicKey(username, keyfile, keyfilePass);
            if (!isAuthenticated){
                logger.debug("authenticateWithPublicKey failed.");
                logger.debug(connection.getRemainingAuthMethods(username).clone()[0]);
            }
            logger.info("SSH successfully connected to host: " + hostname);
            return 0;
        }catch(Exception e){
            logger.error("Failed to connect host: " + hostname +"  "+ e.toString());
            return 1; //Exception
        }
    }
//Execute Command on connected host no output displayed
    public static String executeCommand(String command) {
        return CustomSshClient.executeCommand(command, false);
    }
//Execute Command on connected host verbose
    public static String executeCommand(String command, boolean verbose) {
        try{
            /* Create a session */
            if(isAuthenticated) {
                session = connection.openSession();
                command.trim();
                if(command != ""){
                    StringBuilder completeString = new StringBuilder();
                    session.execCommand(command);
                    Thread.sleep(2000);
                    InputStream stdout = new StreamGobbler(session.getStdout());
                    BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
                    String line;
                    while((line = br.readLine()) != null){
                        completeString.append("\n"+line);
                    }
                    lastCommandOutput = completeString.toString();
                    logger.debug(lastCommandOutput);
                    lastCommandStatus = session.getExitStatus().toString();
                    logger.info("command: " + command + " exited with status code = " + lastCommandStatus);
                    CustomSshClient.close();
                    return lastCommandOutput;
                }
            }
            else
                logger.error("Not connected to any server...! Please use method CustomSshClient.connect()");
        }catch(Exception e){
            CustomSshClient.close();
            return ("Exception in CustomSshClient.executeCommand: " + e);
        }
        CustomSshClient.close();
        return "No command to execute";
    }
// Returns output of last executed command
    public static String getLastCommandOutput() {
        return lastCommandOutput;
    }
// Returns command status of last executed command
    public static String getLastCommandStatus() {
        return CustomSshClient.lastCommandStatus;
    }
// Returns close shell connection
    public static int close() {
        try{
            if(session!=null)
                session.close();
            return 0;
        }catch(Exception  e){
            logger.error("Unable to close ssh channel " + e.toString());
            return 1;
        }
    }
// Set Connection host
    public static void setHostname(String hostname) {
        CustomSshClient.hostname = hostname; isAuthenticated=false;
    }
// Set ssh port for ssh Connection
    public static void setPort(int port) {
        CustomSshClient.port = port; isAuthenticated=false;
    }
// Set User Name for ssh Connection
    public static void setUsername(String username) {
        CustomSshClient.username = username; isAuthenticated=false;
    }
// Set Password for ssh Connection
    public static void setPassword(String password) {
        CustomSshClient.password = password; isAuthenticated=false;
    }
}
