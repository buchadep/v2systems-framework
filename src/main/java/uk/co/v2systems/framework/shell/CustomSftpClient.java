package uk.co.v2systems.framework.shell;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.io.FileInputStream;



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
    static Logger logger = LogManager.getLogger(CustomSftpClient.class);

    public  static int send (String fileName, String remoteDir) {
        try {
            File file = new File(fileName);
            if(file.isFile()){
                remoteDirPath=remoteDir;
                channelSftp.cd(remoteDirPath);
                channelSftp.put(new FileInputStream(file), file.getName());
                logger.info("sft successfully transferred file="+fileName+" to "+hostname+":"+remoteDir);
                return 0;
            }
            else{
                logger.error("file "+fileName+" doesnt exists");
                return 1;
            }

        } catch (Exception ex) {
            logger.error("Failed to sft file.");
            return 1;
        }
    }

    //Connect using Secure Shell using username password
    public static int connect (String hostnameIn, int portIn, String usernameIn, String passwordIn) {
        try{
            port=portIn;
            hostname=hostnameIn;
            username=usernameIn;
            password=passwordIn;
        //Establish sftp Channel
            jsch = new JSch();
            session = jsch.getSession(username, hostname, port);
            session.setPassword(password);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            logger.debug("successfully connected to host " + hostname + "port: " + port + " user: " + username);
            channel = session.openChannel("sftp");
            channel.connect();
            logger.debug("sftp channel opened and connected.");
            channelSftp = (ChannelSftp) channel;
            return 0;
        }catch(Exception e){
            logger.error("Failed to connect host "+ hostname +"\n"+ e.toString());
            return 1; //Exception
        }
    }

    //Connect using Secure Shell using username password
    public static int connect (String hostnameIn, int portIn, String usernameIn, String keyfilePath, String keyfilePass) {
        try{
            port=portIn;
            hostname=hostnameIn;
            username=usernameIn;
            //Establish sftp Channel
            jsch = new JSch();
            session = jsch.getSession(username, hostname, port);
            jsch.addIdentity(keyfilePath);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            channel = session.openChannel("sftp");
            channel.connect();
            logger.debug("sftp channel opened and connected.");
            channelSftp = (ChannelSftp) channel;
            logger.info("sftp successfully connected to host: " + hostname + " port: " + port + " user: " + username);
            return 0;
        }catch(Exception e){
            logger.error("Failed to connect host: "+ hostname +"\n"+ e.toString());
            return 1; //Exception
        }
    }
    // Returns close shell connection
    public static int close() {
        try{
            channelSftp.exit();
            logger.debug("sftp Channel exited.");
            channel.disconnect();
            logger.debug("Channel disconnected.");
            session.disconnect();
            logger.debug("Host Session disconnected.");
            return 0;
        }catch(Exception  e){
            logger.error("Exception in CustomSftpClient.close: " + e.toString());
            return 1;
        }
    }
}
