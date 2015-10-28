package uk.co.v2systems.framework.utils;

/**
 * Created by I&T Lab User on 22/06/2015.
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;


//import cucumber.api.DataTable;
//import cucumber.runtime.table.TableConverter;
//import jodd.datetime.JDateTime;

/**
 * Created by I&T Lab User on 26/02/2015.
 */
public abstract class Methods {

    public static void printConditional(String printString, boolean print){
         if(print)
            System.out.print(printString);
    }
    public static void printConditional(String printString){
        System.out.print(printString);
    }
    public static String getLocalIP(){
        try {
            InetAddress ipAddress = InetAddress.getLocalHost();
            return ipAddress.getHostAddress();
        }catch(Exception e){
            printConditional("Exception in Methods.getLocalIP :" + e);
            return null;
        }
    }
    public static String getApplicationPath(){
        try {
            return URLDecoder.decode(new File(".").getCanonicalPath(), "UTF-8");
        }catch(Exception e){
            System.out.println("Exception in Methods.getApplicationPath: " + e);
            return null;
        }

    }

    public static List<List<String>> resultResetToListOfList(ResultSet resultSet){
        try {
            int rowNumber = 0; boolean setHeader=false;
            ResultSetMetaData resultSetMetadata=resultSet.getMetaData();
            List<List<String>> table = new ArrayList<List<String>>();
            List<String> row = new ArrayList<String>();

            //Set Header
            if(setHeader==false)
            {
                for(int i=1; i<= resultSetMetadata.getColumnCount();i++)
                    row.add(0,resultSetMetadata.getColumnName(i));
                setHeader=true;
                table.add(rowNumber,row);
                rowNumber++;
            }

            while(resultSet.next()) {
                row=new ArrayList<String>();
                for(int i=1; i<= resultSetMetadata.getColumnCount();i++)
                {
                    row.add(i-1,resultSet.getString(i));
                }
                table.add(rowNumber,row);
                rowNumber++;
            }
            return table;

        }catch(Exception e){
            printConditional("Exception in Methods.resultResetToListOfList :" + e);
            return null;
        }

    }

    /*
    public static List<List<String>> dataTableToListOfList(DataTable dataTable){
        try {
            return dataTable.raw();
        }catch(Exception e){
            printConditional("Exception in Methods.dataTableToListOfList :" + e);
            return null;
        }

    }
*/
  //Convert String array to String List
    public static List<List<String>> arrayToListOfList(String[] stringArray){
        try {
            List<String> row;
            List<List<String>> table = new ArrayList<List<String>>();
            for(int i=0; i< stringArray.length; i++) {
                row=new ArrayList<String>();
                row.add(0,stringArray[i].toString());
                table.add(i,row);
            }
            return table;
        }catch(Exception e){
            printConditional("Exception in Methods.arrayToListOfList :" + e);
            return null;
        }
    }

    public static List<List<String>> arrayToListOfList(String[] stringArray, int clashGroup[], int clashCount[]){
        try {
            List<String> row;
            List<List<String>> table = new ArrayList<List<String>>();

            for(int i=0; i< stringArray.length; i++)
            {
                row=new ArrayList<String>();
                row.add(0,stringArray[i].toString());
                int k=1;
                for(int j=0; j < clashCount.length && clashCount.length==clashGroup.length;j++) {
                    row.add(k++, Integer.toString(clashGroup[j]));
                    row.add(k++, Integer.toString(clashCount[j]));
                }
                table.add(i,row);
            }
            return table;
        }catch(Exception e){
            printConditional("Exception in Methods.arrayToListOfList :" + e);
            return null;
        }

    }

    public static String[] generateNumericArrayList(String Header, int startingId, int requestedNumber) {
        String [] NumericArrayList = new String[requestedNumber+1];
        NumericArrayList[0]=Header;
        for(int i=1; i<=requestedNumber; i++) {
            NumericArrayList[i]=Integer.toString(startingId+i-1);
        }
        return NumericArrayList;
    }
// int return int[]
    public static int[] intTointArray(int value) {
        int [] integer = {value};
        return integer;
    }
// random int value return int[]
    public static int[]  intArrayGenerator(int length, int maxValue){
        int [] integerArray = new int[length];
        for(int i=0; i<length;i++) {
            integerArray[i] = (int) (Math.random() * maxValue + 1);
        }
        return integerArray;
    }




    public static void writeToFile(String content, String fileName){
        try {
            File file = new File(fileName);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();


        }catch(Exception e){
            System.out.println("Exception in Methos.writeToFile()");
        }

    }
    public static void deleteFile(String fileName){
        File file = new File(fileName);
        // if file doesnt exists, then create it
        if (file.exists()) {
            file.delete();
        }

    }
}
