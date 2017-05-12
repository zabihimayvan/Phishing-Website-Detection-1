
package phishing_website_features;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.net.Socket;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.InetAddress;
import java.net.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 *
 * @author XinZhou
 */
public class Phishing_Website_Features {
    
    public static int lengthOfUrl(int l){

        if(l < 50){
            return 1;
        }
        
        else return -1;
    }
    public static int lengthOfDomain(int l){

        if(l < 15){
            return 1;
        }
        else if(l < 25){
            return 0;
        }
        else return -1;
    }
    
    public static int portNumber(int number){
        if(number ==443){
            return 1;
        }
        else if( number == 80 || number == -1){
            return 0;
        }
        else{
            return -1;
        }
    }
    
    public static int usingIPAddress(String s){  
        boolean result = true;
       
        for(int i = 0; i < 3; i++){
            char c = s.charAt(i);
            //System.out.println(Character.getNumericValue(c));
            if(Character.getNumericValue(c) > 9){
               result = false;
            }   
        }
        if(result){
            char c = s.charAt(3);
            if(c == '.'){
               return -1;
            }   
        }
        return 1;
    }
    
    public static int hasSymbolAt(String url){
        String s = url;
        for(int i =0; i < s.length(); i++){
            if( s.charAt(i) == '@'){
                return -1;
            }
        }
        return 1;
    }
    
    public static int usingHttpAfterUrl(String url){
        
       String s = url;
        if(s.charAt(0) == 'h' && s.charAt(1) == 't' && s.charAt(2) == 't' && s.charAt(3) == 'p' ){
            return -1;
        }
        else{
            return 1;
        }
    }
    
    public static int usingDirecting(String path){
        int length = path.length() - 1;
        for(int i = 0; i <length ; i++){
            if(path.charAt(i) == '/' && path.charAt(i+1) == '/'){
               return -1;
            }   
        }
        return 1;
    }
 
    public static int numberOfLinks(Document doc){ // count # of total links
        int total = 0;
        Elements links = doc.getElementsByTag("a"); 
      
        for( Element link: links){ 
            String l = link.attr("href");
            if(l.length() > 0){
                if(l.length() < 4){
                    l = doc.baseUri() + l.substring(1);
                }else if(!l.substring(0, 4).equals("http")){     
                    l = doc.baseUri() + l.substring(1);           
                }
            }
            //System.out.println(l);
            total++;
        }
        //System.out.println(total);
        if(total < 15){
            return 1;
        }
        else if(total < 50){
            return 0;
        }
        else{
            return -1;
        }   
        
    }
    public static int numberOfAlert(Document doc){ // count # of total links
        int total = 0;
        
        Elements links = doc.getElementsByTag("script"); 
        //System.out.println(links);
        String content = links.toString();
        int length = content.length() - 5;
        //System.out.println(content);
        
        for(int i = 0; i < length; i++){
            int j = i + 6;
            String substring = content.substring(i, j);
            //System.out.println(substring);
            
            if(substring.equals("window") || substring.equals("Window")){
                total++;
            }
        }
         
        //System.out.println("total : " + total);
        //System.out.println(doc.appendText("alert"));
        if(total < 10){
            return 1;
        }
        else if(total < 80){
            return 0;
        }
        else{
            return -1;
        }
        
    }
    
    public static int pageRank(String s){
       
        AlexaSEO obj = new AlexaSEO();  
        int rank = obj.getAlexaRanking(s);
        System.out.println("Ranking : " + rank);
         
        if (rank == 0 ){
            return -1;
        }
        else if(rank < 5000000){
            return 1;
        }
        return 0;
    }
    
    
    public static boolean pingHost(String host, int port, int timeout) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), timeout);
            System.out.println("Yes awesome ");
            return true;
        } catch (IOException e) {
            System.out.println("No Found 404 ");
            return false; // Either timeout or unreachable or failed DNS lookup.
        }
    }
    
    
    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args)throws IOException, NullPointerException,UnknownHostException {
        // TODO code application logic here
        
        FileReader fileReader = new FileReader(new File("1000-phishing.txt"));

        BufferedReader br = new BufferedReader(fileReader);

        String[] urlList = new String[1000];
        String line = "";
        int count = 0;
        
        // read all URL
        while ((line = br.readLine()) != null) {
             urlList[count] = line;
             //System.out.println(urlList[count]);
             count++;            
        }
        
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {
            fw = new FileWriter("1000-phishing-output2.txt");
            bw = new BufferedWriter(fw);
            
            //bw.write("test"+","+System.lineSeparator());
            
            for(int i = 0; i < count; i++){
                String url = urlList[i];
                URL aURL = new URL(url); 
//                System.out.println("protocol = " + aURL.getProtocol());
                System.out.println("authority = " + aURL.getAuthority());
                //System.out.println("host = " + aURL.getHost());
                //System.out.println("port = " + aURL.getPort());
                //System.out.println("path = " + aURL.getPath());
                //System.out.println("filename = " + aURL.getFile());
                //System.out.println("URL length = " + aURL.getAuthority().length());
                //System.out.println("user information = " + aURL.getUserInfo());
                       
    //            //length of url
    //            System.out.println(lengthOfUrl(url.length())); 
                 bw.write(lengthOfUrl(url.length())+",");
    //           //length of domain
    //            System.out.println(lengthOfUrl(aURL.getHost().length())); 
                 bw.write(lengthOfDomain(aURL.getHost().length())+",");
    //            // port number
    //            System.out.println(portNumber(aURL.getPort()));
                 bw.write(portNumber(aURL.getPort())+",");
    //            // using IP address for url
    //            System.out.println(usingIPAddress(aURL.getAuthority()));
                 bw.write(usingIPAddress(aURL.getAuthority())+",");
    //            //having sumbal '@'
    //            System.out.println(hasSymbolAt(aURL.getAuthority()));
                 bw.write(hasSymbolAt(aURL.getAuthority())+",");
    //            //using http or https after https:/
    //            System.out.println(usingHttpAfterUrl(aURL.getAuthority()));
                 bw.write(usingHttpAfterUrl(aURL.getAuthority())+",");
    //            //redirecting using '//'
    //            System.out.println(usingDirecting(aURL.getPath()));
                 bw.write(usingDirecting(aURL.getPath())+",");
    //            //page rank 
    //            System.out.println(pageRank(aURL.getAuthority()));
                 bw.write(pageRank(aURL.getAuthority())+",");          
                try {
                    InetAddress address = InetAddress.getByName(new URL(url).getHost());
                    String ip = address.getHostAddress();
                    InetAddress inet = InetAddress.getByName(ip);
                    System.out.println(inet.isReachable(500) ? "Host is reachable" : "Host is NOT reachable");
                    if(inet.isReachable(500)){
                        try{
                            Document doc = Jsoup.connect(url).ignoreHttpErrors(true).ignoreContentType(true).get(); 
                            // Number of links
                            System.out.println(numberOfLinks(doc)); 
                            bw.write(numberOfLinks(doc)+",");
                            //using pop-window
                            System.out.println(numberOfAlert(doc)); 
                            bw.write(numberOfAlert(doc)+",");
                        }
                        catch(IOException e){
                            System.out.println("Not Found");  
                            bw.write("0"+","+"0"+",");  
                        }
                      }
                    else{
                        bw.write("0"+","+"0"+",");  
                    }
                }    
                catch (UnknownHostException e)
                    {
                        System.err.println("Couldn't find Host");
                        bw.write("0"+","+"0"+",");  
                }
                bw.write("-1"+System.lineSeparator());     // write the label for url  -1 is phishing, 1 is legitimate        
            }
            
            
        } catch (IOException e) {
                e.printStackTrace();

        } finally {
            try {
                    if (bw != null)
                            bw.close();
                    if (fw != null)
                            fw.close();
            } catch (IOException ex) {
                    ex.printStackTrace();
            }
        }    
    }  
}
