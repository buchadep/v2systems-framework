package uk.co.v2systems.framework.utils;

import jodd.datetime.JDateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by I&T Lab User on 24/06/2015.
 */
public class CustomDate {
//Convert long in format 'hhmmssMsMs' to long
    public static long getLongSeconds(long hhmmssMsMs){
        long longSeconds=0; long MsMs=0; long ss=0; long mm=0; long hh=0;
        for(int i=0; hhmmssMsMs>0; i++){
            long quotient = hhmmssMsMs/100;
            if(i==0)
                MsMs = (hhmmssMsMs - quotient *100);
            if(i==1)
                ss = (hhmmssMsMs - quotient *100);
            if(i==2)
                mm = (hhmmssMsMs - quotient *100);
            if(i==3)
                hh = (hhmmssMsMs - quotient *100);
            hhmmssMsMs=quotient;
        }
        longSeconds = hh * 3600000 + mm * 60000 + ss *1000;
        return longSeconds;
    }
//Get current Date
    public static Date getDateTime(){
        Date referenceDateTime = new Date();
        return referenceDateTime;
    }
//get current Date in long Format
    public static long getLongDateTime(){
        Date referenceDateTime = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(referenceDateTime);
        long longReferenceDateTime = referenceDateTime.getTime();
        return longReferenceDateTime;
    }
//Get current Date in specified DateTimeFormat
    public static String getDateTime(String strDateTimeFormat){
        DateFormat dateFormat = new SimpleDateFormat(strDateTimeFormat);
        return dateFormat.format(CustomDate.convertDateTime(CustomDate.getLongDateTime()));
    }
//Convert longDate to Date in specified DateTimeFormat
    public static String convertDateTime(long longDateTime, String strDateTimeFormat){
        DateFormat dateFormat = new SimpleDateFormat(strDateTimeFormat);
        return dateFormat.format(CustomDate.convertDateTime(longDateTime));
    }
//Convert longDate to Date
    public static Date convertDateTime(long longDateTime){
        try {
            return new Date(longDateTime);
        }catch(Exception e){
            return new Date();
        }
    }
//Convert StringDate it's DateTimeFormat to Date
    public static Date convertDateTime(String strDateTime, String strDateTimeFormat){
        try {
            DateFormat dateFormat = new SimpleDateFormat(strDateTimeFormat);
            return dateFormat.parse(strDateTime);
        }catch(Exception e){
            return new Date();
        }
    }
 //Convert Date to long Date
    public static long convertLongDateTime(Date referenceDateTime){
        Calendar cal = Calendar.getInstance();
        cal.setTime(referenceDateTime);
        return referenceDateTime.getTime();
    }
//convert long date to MJD String
    public static String getMJDDate(long longDateTime){
        JDateTime jdt = new JDateTime(longDateTime);
        //jdt.getJulianDate().getModifiedJulianDate();
        return jdt.getJulianDate().getModifiedJulianDate().toString();
    }

}



    /*
    public static String getMJDDate(long longDateTime){
    /*
        Double doubleDateTime;
        doubleDateTime= (double)  longDateTime * 25 / ( 86400000 * 25) +  2440587.5 - 2400001;
        return String.format("%.7f",doubleDateTime);
        //String.valueOf(doubleDateTime);
        */
 /*       JDateTime jdt = new JDateTime(longDateTime);
        jdt.addMillisecond(30000);
        return jdt.getJulianDate().toString();

    }
*/

