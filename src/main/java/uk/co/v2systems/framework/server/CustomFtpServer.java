package uk.co.v2systems.framework.server;

import org.apache.ftpserver.ConnectionConfigFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.v2systems.framework.utils.Methods;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by I&T Lab User on 26/06/2015.
 */
public class CustomFtpServer implements Runnable {
        static int ftpPort= 2121;
        static String FTPServerIp = Methods.getLocalIP();
        static FtpServer server;
        static String homeDir="c:/";
        public static volatile boolean stopRequest = false;
        static Logger slf4jLogger = LoggerFactory.getLogger(CustomFtpServer.class);

        @Override
        public void run() {
            this.start();
        }

        public static void start(){
            try{
                FtpServerFactory serverFactory = new FtpServerFactory();
                ListenerFactory factory = new ListenerFactory();
                ConnectionConfigFactory connectionConfigFactory = new ConnectionConfigFactory();
                connectionConfigFactory.setAnonymousLoginEnabled(true);
                serverFactory.setConnectionConfig(connectionConfigFactory.createConnectionConfig());

                BaseUser user = new BaseUser();
                user.setName("anonymous");
                user.setHomeDirectory(homeDir);

                List<Authority> authorities = new ArrayList<Authority>();
                authorities.add(new WritePermission());
                user.setAuthorities(authorities);
                serverFactory.getUserManager().save(user);

                // set the port of the listener
                factory.setPort(ftpPort);
                // replace the default listener
                serverFactory.addListener("default", factory.createListener());
                // start the server
                server = serverFactory.createServer();
                //serverFactory.createServer();
                server.start();
            }catch(Exception e){
                slf4jLogger.error("Error in method FTPServer.start :- " + e);
            }
        }

        public static void stop(){
            try{
                if(server!=null && stopRequest==true) {
                    server.stop();
                    System.out.println("FTP Server Stopped!");
                }

            }catch(Exception e){
                slf4jLogger.error("Error in method FTPServer.stop " + e);
            }

        }
//set stopRequest
    public static void setStopRequest(boolean stop){
        stopRequest=stop;
        if(stop==true)
            CustomFtpServer.stop();
        }
//set FTP server home directory
        public static void setHomeDir(String homeDir) {
            CustomFtpServer.homeDir = homeDir;
        }
//get FTP server home directory
        public static String getHomeDir() {
            return homeDir;
        }
// set integer FTP server port
        public static void setFtpPort(int ftpPort) {
            CustomFtpServer.ftpPort = ftpPort;
        }
// get integer FTP server port
        public static int getFtpPort() {
            return ftpPort;
        }
    // get FTP server IP
    public static String getFTPServerIp() {
        return FTPServerIp;
    }
}
