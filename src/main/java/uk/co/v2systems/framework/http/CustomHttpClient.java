package uk.co.v2systems.framework.http;

import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import uk.co.v2systems.framework.utils.KeyValuePair;
import uk.co.v2systems.framework.utils.Methods;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;


/**
 * Created by Pankaj Buchade on 22/06/2015.
 */
public class CustomHttpClient {

        String url;
        HttpClient httpClient= null;
        HttpPost httpPost=null;
        HttpGet httpGet=null;
        HttpPut httpPut=null;
        MultipartEntityBuilder builder=null;
        List<KeyValuePair> httpHeader=null;
        InputStreamEntity reqInputStreamEntity=null;
        File sendFile = null, outfile = null;
        String lastHttpRequestStatus;

        //Constructor
        public CustomHttpClient(String url){
            this.setUrl(url);
            httpClient = new DefaultHttpClient();
            //default File
            //this.sendFile = new File("c:\\post.xml");
            //this.outfile = new File ("c:\\response.xml");
        }

        public void setUrl(String url){
            this.url = new String(url);
        }

        public int setSendFile(String filename){
            try {
                this.sendFile = new File(filename);
                //set header
                FileEntity entity = new FileEntity(sendFile, "text/plain; charset=\"UTF-8\"");
                reqInputStreamEntity = new InputStreamEntity(new FileInputStream(sendFile), -1);
                reqInputStreamEntity.setContentType("application/xml");
                reqInputStreamEntity.setChunked(true);
                return 0;
            }catch(Exception e){
                Methods.printConditional("Exception in CustomHttpClient.setFile");
                return 1; //Exception
            }
        }
        public int setHeader(String key, String value){
            if(key!=""){
                if(httpHeader==null)
                    httpHeader= new LinkedList<KeyValuePair>();
                KeyValuePair kv = new KeyValuePair(key,value);
                httpHeader.add(kv);
                return 0;
            }
            else {
                Methods.printConditional("Error in CustomHttpClient.setHeader \nPlease ensure header is correct!");
                return 1; //Exception
            }
        }
        public void getHeader(){
            if(httpHeader!=null){
                for(int i=0; i<httpHeader.size();i++)
                Methods.printConditional(httpHeader.get(i).getKey()+"::"+httpHeader.get(i).getValue());
            }
        }
        public void clearHeader(){
            if(httpHeader!=null)
                httpHeader.removeAll(httpHeader);
        }

        public void setResponseFile(String filename){
            this.outfile = new File(filename);
        }
        public String getUrl(){
            return this.url;
        }

        public int post(){
            return post(false);
        }
        public int post(boolean verbose){
            try {
                    httpPost = new HttpPost(this.url);
                    httpPost.setEntity(reqInputStreamEntity);
                    if(builder!=null) {
                        HttpEntity multipart = builder.build();
                        httpPost.setEntity(multipart);
                    }
                    this.assignHeader(httpPost);
                    //Print details of POST Request
                    Methods.printConditional("\n" + httpPost.getRequestLine(), verbose);
                    if(sendFile !=null) {
                        Methods.printConditional("\nSending File: " + this.sendFile + " Req Header:" + reqInputStreamEntity.getContentType(), verbose);
                    }
                    else{
                        Methods.printConditional("\nWarning: No File selected! you can select File using method setFile() before post()", verbose);
                    }
                    //list all set header
                    Header headers[] = httpPost.getAllHeaders();
                    for(Header h:headers)
                        Methods.printConditional("\nRequest Header: " + h.getName()+": "+h.getValue(),verbose);
                    //Response
                    HttpResponse response = httpClient.execute(httpPost);
                    printHttpResponse(response, verbose);
                return 0; //success
            }catch(Exception e){
                Methods.printConditional("\nException in [CustomHttpClient.post] : "+ e.toString());
                return 1; //Exception
            }
        }
        public int get(){
        return get(false);
    }
        public int get(boolean verbose){
            try{

                httpGet = new HttpGet(this.url);
                this.assignHeader(httpGet);
                //Print details of GET Request
                Methods.printConditional("\n" + httpGet.getRequestLine(),verbose);
                //list all set header
                Header headers[] = httpGet.getAllHeaders();
                for(Header h:headers)
                Methods.printConditional("\nRequest Header: " + h.getName()+": "+h.getValue(),verbose);
                //Response
                HttpResponse response = httpClient.execute(httpGet);
                printHttpResponse(response, verbose);
                return 0; //success
            }catch(Exception e){
                System.out.println("Exception in CustomHttpClient.get");
                return 1; //Exception
            }
        }
        public int put(){
            return put(false);
        }
        public int put(boolean verbose){
            try{
                    httpPut = new HttpPut(this.url);
                    httpPut.setEntity(reqInputStreamEntity);
                    this.assignHeader(httpPut);
                    //Print details of PUT Request
                    Methods.printConditional("\n" + httpPut.getRequestLine(), verbose);
                    if(sendFile !=null) {
                        Methods.printConditional("Sending File: " + this.sendFile + " Req Header:" + reqInputStreamEntity.getContentType(), verbose);
                    }
                    else{
                        Methods.printConditional("Warning: No File selected! you csn select File using method setFile() before put()", verbose);
                    }
                    //listing request headers
                    Header headers[] = httpGet.getAllHeaders();
                    for(Header h:headers)
                        Methods.printConditional("\nRequest Header: " + h.getName()+": "+h.getValue(),verbose);
                    //Response
                    HttpResponse response = httpClient.execute(httpPut);
                    printHttpResponse(response, verbose);
                    return 0; //success
            }catch(Exception e){
                System.out.println("Exception in CustomHttpClient.put");
                return 1; //Exception
            }
        }

        public void assignHeader(HttpRequestBase httpRequestBase){
            //setting headers
            if(httpHeader!=null) {
                for (int i = 0; i < httpHeader.size(); i++) {
                    httpRequestBase.setHeader(httpHeader.get(i).getKey(), httpHeader.get(i).getValue());
                }
            }
        }

        private int printHttpResponse( HttpResponse response, boolean verbose) {
            try {
                HttpEntity resEntity = response.getEntity();
                //Single Line Response Status
                lastHttpRequestStatus = response.getStatusLine().toString();
                Methods.printConditional("\nRESPONSE: " + lastHttpRequestStatus, verbose);
                //List all Response Headers
                Header[] resHeaders = response.getAllHeaders();
                for (int i = 0; i < resHeaders.length; i++) {
                    Methods.printConditional("Header: " + resHeaders[i].toString(), verbose);
                }
                //reading Response
                if(resEntity!=null) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(resEntity.getContent()));
                    String line = null;
                    this.writeToFile(r);
                }
                return 0; //success
            }catch(Exception e){
                System.out.println("Exception in [CustomHttpClient.printHttpResponse]:" + e.toString());
                return 1; //exception
            }
        }
        private void writeToFile( BufferedReader r){
            Charset charset = Charset.forName("US-ASCII");
            String line = null;
            try {
                if(outfile!=null) {
                    BufferedWriter w = Files.newBufferedWriter(outfile.toPath(), charset);
                    while ((line = r.readLine()) != null) {
                        if (outfile.getName().toUpperCase().contains(".XML")) {
                            w.write(line.replaceAll("><", ">\n<"));
                            //System.out.println(line.replaceAll("><", ">\n<"));
                        } else {
                            w.write(line);
                            //System.out.println(line);
                        }
                    }
                    //close the sendFile
                    w.close();
                }
            }catch (Exception e){
                System.out.println("Exception in [CustomHttpClient.writeToFile]:" + e.toString());
            }
        }
        public String getLastHttpRequestStatus(){
            return lastHttpRequestStatus;
        }

        public void addTextParametr(String param, String paramValue){
            if(builder==null)
            builder = MultipartEntityBuilder.create();
            builder.addTextBody(param, paramValue, ContentType.TEXT_PLAIN);
        }
}
