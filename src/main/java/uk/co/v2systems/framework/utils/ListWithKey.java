package uk.co.v2systems.framework.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PBU10 on 09/10/2015.
 */
public class ListWithKey {
    public static List<List<String>> listWithKey;

    public ListWithKey(){
        listWithKey = new ArrayList<List<String>>();
    }

    public boolean addRow(String inputString, char seperator){
        //Need to correct this in long term, currently seperator is fixed
        String[] splitString = inputString.split("\\|");
        String key = splitString[0];
        boolean keyExists=false;
        //considering table column 0 as key
        for(int i=0; i< listWithKey.size()&& keyExists==false ;i++) {
            if (key.equalsIgnoreCase(listWithKey.get(i).get(0).toString())) {
                keyExists = true;
                return false;
            }
        }
        if(!keyExists){
            List<String> row = new ArrayList<String>();
            for(int i=0; i<splitString.length; i++)
                row.add(splitString[i]);
            listWithKey.add(row);
            return true;
        }
        return false;
    }
    public List<String> getRow(String key){
        boolean keyExists=false;
        for(int i=0; i< listWithKey.size()&& keyExists==false ;i++) {
            if (key.equalsIgnoreCase(listWithKey.get(i).get(0).toString())) {
                keyExists=true;
                return listWithKey.get(i);
            }
        }
        return null;
    }
}
