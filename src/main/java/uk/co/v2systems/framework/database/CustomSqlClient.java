package uk.co.v2systems.framework.database;

/**
 * Created by Pankaj Buchade on 23/06/2015.
 */
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.co.v2systems.framework.utils.Methods;
import java.sql.*;
import java.util.Properties;

//Supports both Oracle and SqLite connections
public class CustomSqlClient {
//oracle specific variables
    public static String serverName;
    public  static String portNumber = "1525";
//sqLite specific variables
    public  static String dbFileName;
//Common variable
    public static Connection conn = null;
    public static String dbms;
    public static String dbName;
    public static String userName;
    public static String password;
    public static String queryString;
    public static Statement statement;
    public static ResultSet resultSet;
    public static int rowCount=0;
    public static ResultSetMetaData resultSetMetadata;

    static Logger logger = LogManager.getLogger(CustomSqlClient.class);

//Set Connection properties for Oracle DB Connection
    public void setConnectionDetails(String dbms, String serverName, String portNumber, String dbName, String userName, String password){
        this.dbms=dbms;
        this.serverName=serverName;
        this.portNumber = portNumber;
        this.dbName=dbName;
        this.userName=userName;
        this.password=password;
    }
//Set Connection properties for SQLite Connection
    public void setConnectionDetails(String dbms, String dbFileName){
        this.dbFileName =dbFileName;
        this.dbms=dbms;
    }
    public void getConnectionDetails(){
        //Common Details
        logger.info("\nDatabase: " + dbms);
        if(dbms.equalsIgnoreCase("oracle")){
            logger.info("\nHostName: " + serverName);
            logger.info("\nDB Port: " + portNumber);
        }
        if(dbms.equalsIgnoreCase("sqlite")){
            logger.info("\nDB File Name: " + dbFileName);
        }
            logger.info("\nDB Name: " + dbName);
            logger.info("\nDB UserName: " + userName);
            logger.info("\nPassword: " + password);
    }
//Establish Connection to Database
    public Connection connect(){
        try{
            if(dbms.equalsIgnoreCase("oracle")) {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                Properties connectionProps = new Properties();
                connectionProps.put("user", this.userName);
                connectionProps.put("password", this.password);
                    conn = DriverManager.getConnection(
                            "jdbc:" + this.dbms + ":thin:@" +
                                    this.serverName +
                                    ":" + this.portNumber + ":" +
                                    this.dbName, connectionProps);
                logger.info("Connected to database: " + this.serverName + "::" + this.dbName);
            }
            if(dbms.equalsIgnoreCase("sqlite")){
                Class.forName("org.sqlite.JDBC");
                Properties connectionProps = new Properties();
                    conn = DriverManager.getConnection(
                            "jdbc:" + this.dbms + ":" +
                                    this.dbFileName, connectionProps );
                logger.info("Connected to sqlLite database File: " + this.dbFileName);
            }
            return conn;
        }catch(Exception e){
            logger.error("Error While establishing sql connection...\n" + e.toString());
            return null;
        }
    }
//Execute SQL query to connected Oracle Database
    public void executeQuery(String queryString){
        try {
            this.queryString=queryString;
            statement = conn.createStatement();
            boolean result= statement.execute(queryString);
            if(result){
               resultSet=statement.executeQuery(queryString);
               resultSetMetadata=resultSet.getMetaData();
            }
            logger.debug("Executing SQL: " + queryString);
        }catch(SQLException e){
            logger.error(e.toString() + "SQL: "+queryString);
        }
    }
//Print as well as return SQL query result
    public ResultSet getResultSet(){
        return getResultSet(true);
    }
//getResultSet can be controlled as verbose or non verbose also used by getRowCount
    public ResultSet getResultSet(boolean verbose){
        try{

            String strResultRow="";
            int numberOfRows = 0;
            rowCount=0;
            //printing column Headers
            for(int i=0; i< resultSetMetadata.getColumnCount();i++) {
                strResultRow=strResultRow+resultSetMetadata.getColumnLabel(i + 1) + "\t";
            }
            logger.debug("\nSQL: " + this.queryString + "; Number of rows returned: "+ numberOfRows);
            logger.debug(strResultRow);
            strResultRow="";
            //printing Result set rows
            while(resultSet.next()) {
                if(resultSet.toString()!=null) {
                    for (int i = 0; i < resultSetMetadata.getColumnCount(); i++) {
                        strResultRow=strResultRow+resultSet.getString(resultSetMetadata.getColumnLabel(i + 1)) + "\t";
                    }
                    logger.debug(strResultRow);
                    strResultRow="";
                    numberOfRows++;
                }
            }
            logger.debug("Total rows returned: " + numberOfRows);
            this.rowCount = numberOfRows;
            this.executeQuery(queryString);
            return this.resultSet;
        }catch(SQLException e){
            logger.error("Failed to get result using SQL: " + queryString);
            return null;
        }
    }
//Close DB connection
    public void close(){
        try{
            statement.close();
            resultSet.close();
            conn.close();
        }catch(Exception e){
            logger.error("Failed to close database...");
        }
    }

    //Get row count, please note that you should execute show result before you use this function.
    public int getRowCount() {
        if(rowCount==0){
            getResultSet(false);
        }
        //Methods.printConditional(Integer.toString(rowCount));
        return rowCount;
    }

}
