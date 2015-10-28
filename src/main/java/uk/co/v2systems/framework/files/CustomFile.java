package uk.co.v2systems.framework.files;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by PBU10 on 28/08/2015.
 */
public class CustomFile {
//Default Character set
    Charset ENCODING = StandardCharsets.UTF_8;
    String fileName;
    String lastLine;
    long lineCounter=0;
    File file;
    static BufferedReader lineReader;

    public CustomFile(){

    }
    public String readCompleteFile(){
        try {
            String stringFileContent="";
            if (fileExist()){
                Path path = Paths.get(fileName);
                BufferedReader reader = Files.newBufferedReader(path, ENCODING);
                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringFileContent=stringFileContent+line+"\n";
                    lineCounter++;
                    lastLine=line;
                }
            }
            // In case the file doesnt exists or empty result is the same empty string ""
            return stringFileContent;
        }catch (Exception e){
            System.out.println("Exception in CustomFile.readCompleteFile(): Reading File");
            return null;
        }
    }

    public String readLine(){
        try {
            return lineReader.readLine();
        }catch (Exception e){
            System.out.println("Exception " +this.getClass()+".Reading File");
            return null;
        }
    }
    public String readLastLine(){
        if(lastLine==null){
            readCompleteFile();
        }
        return lastLine;
    }

    public long getNumberOfLines() {
        if (lineCounter==0){
            readCompleteFile();
        }
        return lineCounter;
    }

    public void appendToFile(String textToappend){
        try {
            if(!fileExist())
                createFile();
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
            out.println(textToappend);
            out.flush();
            //appended string is the new last line in the file.
            lastLine=textToappend;
        }catch(Exception e){

        }
    }


    public boolean fileExist() {
        try {
            if (file.exists() && !file.isDirectory())
                return true;
            else
                return false;
        } catch (Exception e) {
            System.out.println("Exception in CustomFile.openFileName(): Error reading File");
            return false;
        }
    }
    public boolean createFile(){
        try {
            if (!fileExist()){
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            return true;
        } catch (Exception e) {
            System.out.println("Exception in " +this.getClass()+".createFileName(): Error creating empty file - " + fileName);
            return false;
        }
    }
    public boolean deleteFile(){
        try {
            if (fileExist()){
                file.delete();
            }
            return true;
        } catch (Exception e) {
            System.out.println("Exception in CustomFile.deleteFile()");
            return false;
        }
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
        file = new File(fileName);
        try{
            if (fileExist()) {
                Path path = Paths.get(fileName);
                lineReader = Files.newBufferedReader(path, ENCODING);
            }
        }catch (Exception e){
            System.out.println("Exception " +this.getClass()+".setFileName");
        }

    }
}
