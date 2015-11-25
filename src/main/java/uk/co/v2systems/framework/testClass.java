package uk.co.v2systems.framework;

import uk.co.v2systems.framework.database.CustomSqlClient;
import uk.co.v2systems.framework.files.CustomFile;
import uk.co.v2systems.framework.files.CustomXml;
import uk.co.v2systems.framework.http.CustomHttpClient;
import uk.co.v2systems.framework.server.CustomFtpServer;
import uk.co.v2systems.framework.shell.CustomSshClient;
import uk.co.v2systems.framework.shell.CustomTelnetClient;
import uk.co.v2systems.framework.utils.*;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by I&T Lab User on 22/06/2015.
 */
public class testClass {

        public static void main(String args[]) {
            try {
                //CustomSshClient.connect("172.26.128.26",22,"PBU10","Changeme_14");
                //CustomSshClient.executeCommand("ls -ltr /apps ",true);
                //CustomSshClient.executeCommand("ls -ltr /apps ",true);
                //CustomSshClient.executeCommand("ls -ltr /apps ",true);

                /*
                CustomFtpServer ftpServer = new CustomFtpServer();
                ftpServer.setFtpPort(2121);
                Thread thread = new Thread(ftpServer, "FTP Server Thread - 0");
                //Thread thread1 = new Thread(ftpServer, "FTP Server Thread - 1");
                thread.start();
                //thread1.start();

                CustomTelnetClient c = new CustomTelnetClient();
                c.connect("10.66.56.9", 23, "root", "");
                c.sendCommand("ls  /");
                c.sendCommand("ftpput -v -u anonymous 172.22.19.9  -P 2121 VERSION.gz /VERSION.gz");
                //c.sendCommand("sh");

                ftpServer.setStopRequest(true);
                */
                CustomXml xml = new CustomXml();
                //KeyValuePair keyValuePair;
                //List<KeyValuePair> attributes = new ArrayList<KeyValuePair>();
                //attributes.add(new KeyValuePair("added_pankaj=Pankaj",'='));
                xml.openXml("c:\\work\\data\\templates\\BOXSET_Show_Template.xml");
                System.out.println(xml.countElementsByXpath("//Ext"));
                xml.cloneXmlTag("//Ext",1);
                xml.cloneXmlTag("//*[name()='ext:GroupMemberRef']",1);
                System.out.println(xml.countElementsByXpath("//Ext"));
                //xml.appendXmlTag("ext:ProgramKey", attributes, "true", "core:Ext");

                /*xml.appendXmlTag("Windows", attributes, null, "Schedule");
                attributes.add(new KeyValuePair("time=12:00:00", '='));
                xml.appendXmlTag("Avail", null, null, "Windows");
                xml.appendXmlTag("Spot", attributes, null, "Avail");
                xml.appendXmlTag("ns2:substitutionOptionSlot", null, null, "Spot");
                xml.appendXmlTag("ns2:adInSpot", null, null, "ns2:substitutionOptionSlot");
                xml.appendXmlTag("ns2:campaignRef", null, null, "ns2:adInSpot");
                xml.appendXmlTag("ns2:campaignIdRef", null, "1111", "ns2:campaignRef");
                */
                //xml.printXml();
                //System.out.print(xml.validateAgainstXSD("c:\\work\\data\\ADI.xsd"));

/*
                //xml.findElements("//Movie[1]/*[contains(.,'Widescreen')]");
                //xml.updateElementValue(xml.findElementsByXpath("//Movie[1]/*[name()='content:SourceUrl']"), "Source_Pankaj");
                //Methods.printConditional(CustomDate.getMJDDate(CustomDate.getLongDateTime()));
                //all with attribute
                xml.updateElementAttributeValue(xml.findElementsByXpath("//*[@providerVersionNum]"), "providerVersionNum","44");
                xml.updateElementAttributeValue(xml.findElementsByXpath("//*[@endDateTime]"), "endDateTime", "2018-12-31T22:59:59Z");
                //replace the provider in all the attributes
                xml.replaceElementAttributeValue(xml.findElementsByXpath("//*[@uriId]"), "uriId", "est__sbo_hd", "est__sbo_hd");
                String today = CustomDate.getDateTime("ddMMyyyy");
                String uriIdCounter=today+"0000000000";
                xml.replaceElementAttributeValue(xml.findElementsByXpath("//*[@uriId]"), "uriId", "0000000000000000", uriIdCounter);

                //linked objects parentContent
                xml.replaceElementAttributeValue(xml.findElementsByXpath("//*[@type='parentContent']"), "object", "0000000000000000", uriIdCounter);
                xml.replaceElementAttributeValue(xml.findElementsByXpath("//*[@type='MultiFormat']"), "object", "0000000000000000", uriIdCounter);

                //Change the Asset Name
                xml.updateElementValue(xml.findElementsByXpath("//Title//*[name()='title:TitleBrief']"), uriIdCounter);
                xml.updateElementValue(xml.findElementsByXpath("//Title//*[name()='title:TitleMedium']"), "Automated Ingest - "+ uriIdCounter);
                xml.updateElementValue(xml.findElementsByXpath("//Title//*[name()='title:TitleLong']"), "Automated Ingest - "+ uriIdCounter+ "Long");
                /*

                //Provider
                xml.updateElementAttributeValue(xml.findElementsByXpath("//Title[@uriId]"), "uriId","est__sbo_hd/TITL2309201500000001");
                xml.updateElementAttributeValue(xml.findElementsByXpath("//Movie[@uriId]"), "uriId","est__sbo_hd/MAIN2309201500000001");
                xml.updateElementAttributeValue(xml.findElementsByXpath("//Thumbnail[@uriId]"), "uriId","est__sbo_hd/THEP2309201500000001");
                xml.updateElementAttributeValue(xml.findElementsByXpath("//ContentGroup1[@uriId]"), "uriId","est__sbo_hd/CGVT2309201500000002");
                xml.updateElementAttributeValue(xml.findElementsByXpath("//ContentGroup2[@uriId]"), "uriId","est__sbo_hd/CGPP2309201500000002");
                xml.updateElementAttributeValue(xml.findElementsByXpath("//Ext/*[name()='ext:PressPackImage']"), "uriId","est__sbo_hd/CGPP2309201500000001");
                xml.updateElementAttributeValue(xml.findElementsByXpath("//Offer[@uriId]"), "Offer","est__sbo_hd/OAVT2309201500000001");
                xml.updateElementAttributeValue(xml.findElementsByXpath("//Terms[@uriId]"), "Terms","est__sbo_hd/TAVT2309201500000001");

                */
                xml.writeXml("c:\\work\\data\\processedxml.xml");
                /*CustomFile file = new CustomFile();
                file.setFileName("c:\\work\\data\\appdata");
                file.deleteFile();
                file.appendToFile("30/09/2015");
                System.out.print(file.getNumberOfLines() + ": ");
                System.out.print(file.readLastLine());

                ListWithKey l= new ListWithKey();
                l.addRow("uriId|A| //*[@uriId]",'|');
                l.addRow("TitleMedium|T|//Title//*[name()='title:TitleMedium']",'|');
                l.getRow("TitleMedium");
                */
               /*
                CustomSqlClient customSqlClient = new CustomSqlClient();
                String sqLightDBFilePath= "c:\\work\\data\\OnDemand";
                customSqlClient.setConnectionDetails("sqlite",sqLightDBFilePath);
                customSqlClient.connect();
                customSqlClient.executeQuery("SELECT * FROM URIID WHERE DATE='"+CustomDate.getDateTime("dd-MM-yyyy")+"'");
                List<List<String>> result = Methods.resultResetToListOfList(customSqlClient.getResultSet());
                for(int i=0; i<result.size();i++) {
                    System.out.println(result.get(i).toString());
                }
                */
            }catch(Exception e){
                System.out.println("I am in main: " + e);
            }
        }
}
