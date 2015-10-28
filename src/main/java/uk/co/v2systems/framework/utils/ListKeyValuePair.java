package uk.co.v2systems.framework.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PBU10 on 06/10/2015.
 */
public class ListKeyValuePair {
    List<KeyValuePair> keyValuePairList;
    char keyValuePairSeperator=':';

    public void add(String inputString, char seperator){
        String tempString = inputString;
        String keyValuePair;
        String[] splitString = tempString.split(Character.toString(seperator));
        String[] arKeyValuePair;

        for(int i=0; i< splitString.length;i++){
            keyValuePair=splitString[i].trim();
            arKeyValuePair=keyValuePair.split(Character.toString(keyValuePairSeperator));
            if(keyExists(arKeyValuePair[0])==-1) {
                keyValuePairList.add(new KeyValuePair(keyValuePair, keyValuePairSeperator));
            }else{
                //if the key exists the value will be updated
                keyValuePairList.set(keyExists(arKeyValuePair[0]), new KeyValuePair(keyValuePair, keyValuePairSeperator));
            }

        }
    }


    //if return value is -1 then the key doesnt exists else location is returned in the list
    public int keyExists(String key){
        int keyPresent=-1;
        if(keyValuePairList!=null){
            for(int i=0;i< keyValuePairList.size();i++){
                if(keyValuePairList.get(i).getKey().contentEquals(key)){
                    keyPresent=i;
                    return keyPresent;
                }
            }
            return keyPresent;
        }else
        return keyPresent;
    }

    public KeyValuePair getKeyValuePairByKey(String key){
        int index = keyExists(key);
        if(index!=-1){
            return keyValuePairList.get(index);
        }
        return null;
    }
    public ListKeyValuePair(){
        keyValuePairList= new ArrayList<KeyValuePair>();
    }

    public KeyValuePair getKeyValuePairByKey(int index){
            return keyValuePairList.get(index);
    }
    public void setKeyValuePairSeparator(char separator){
        keyValuePairSeperator=separator;
    }

    public int size(){
        return keyValuePairList.size();
    }
}
