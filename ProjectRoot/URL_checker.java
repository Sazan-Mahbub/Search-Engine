import java.net.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import javax.swing.text.html.parser.ParserDelegator;

public class URL_checker {
    public static void main(String[] args) throws Exception {
    	CrawlerBot c = new CrawlerBot();
//		String root  = "https://cse.buet.ac.bd/";
//		String root  = "https://facebook.com/";
//		String root  = "https://en.wikipedia.org/";
//		String root  = "http://stackoverflow.com/";
//		String root  = "http://web.mit.edu/";
//		String root  = "https://en.wikipedia.org/wiki/Sazan_Island";
//		String root  = "https://en.wikipedia.org/wiki/George_Mason";
//		String root  = "https://www.google.com/";
//		String root  = "https://www.youtube.com/channel/UCJbPGzawDH1njbqV-D5HqKw";
//    	String root = "http://www.thedailystar.net/";
//    	String root = "http://www.prothom-alo.com/";
//    	String root = "http://stackoverflow.com/questions/1389184/building-an-absolute-url-from-a-relative-url-in-java";
//    	String root = "http://www.buet.ac.bd/";
//    	String root = "https://cse.buet.ac.bd/moodle/enrol/index.php?id=151";
    	String root = "http://www.prothom-alo.com/todays-paper/2016-12-20";
    	System.out.println("lets begin!");
		
//    	System.out.println(c.linksToVisit.size());    	
		try{
    	 	c.crawlStarter(root);
     	}catch(Exception e){
    		e.printStackTrace();
    	}
//    	c.printNodeNumber();
//    	c.printLinksToVisit();
		System.out.println(c.linksToVisit.size());

    }
    
    
}