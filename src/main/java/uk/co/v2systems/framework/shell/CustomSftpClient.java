package uk.co.v2systems.framework.shell;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import uk.co.v2systems.framework.utils.Methods;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


/**
 * Created by PBU10 on 11/03/2016.
 */
public class CustomSftpClient {

    static String hostname = "host:IP";
    static int port = 22;
    static String username = "username";
    static String password = "password";
    static String remoteDirPath = "/temp";
    static Session session = null;
    static Channel channel = null;
    static ChannelSftp channelSftp = null;
    static JSch jsch = null;

    public  static int send (String fileName, String remoteDir) {
        try {
            File file = new File(fileName);
            if(file.isFile()){
                remoteDirPath=remoteDir;
                channelSftp.cd(remoteDirPath);
                channelSftp.put(new FileInputStream(file), file.getName());
                Methods.printConditional("File transfered successfully to host.");
                return 0;
            }
            else{
                Methods.printConditional("Please enter correct file path");
                return 1;
            }

        } catch (Exception ex) {
            System.out.println("Exception found while tranfer the response.");
            return 1;
        }
    }

    //Connect using Secure Shell using username password
    public static int connect (String hostname, int port, String username, String password) {
        try{
            CustomSshClient.port=port;
            CustomSshClient.hostname=hostname;
            CustomSshClient.username=username;
            CustomSshClient.password=password;
        //Establish sftp Channel
            jsch = new JSch();
            session = jsch.getSession(username, hostname, port);
            session.setPassword(password);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            System.out.println("Host connected.");
            channel = session.openChannel("sftp");
            channel.connect();
            System.out.println("sftp channel opened and connected.");
            channelSftp = (ChannelSftp) channel;
            return 0;
        }catch(Exception e){
            Methods.printConditional("\nException in CustomSftpClient.connect: " + e.toString());
            return 1; //Exception
        }
    }
    // Returns close shell connection
    public static int close() {
        try{
            channelSftp.exit();
            System.out.println("sftp Channel exited.");
            channel.disconnect();
            System.out.println("Channel disconnected.");
            session.disconnect();
            System.out.println("Host Session disconnected.");
            return 0;
        }catch(Exception  e){
            Methods.printConditional("Exception in CustomSftpClient.close: " + e.toString());
            return 1;
        }
    }
}
