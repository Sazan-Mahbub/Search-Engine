import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.Buffer;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

//import javax.swing.text.html.parser.ParserDelegator;

public class CrawlerBot {

	public LinkedList<String> linksToVisit = new LinkedList<String>(); // needs to be serialized
	public HashSet<String> linksVisited = new HashSet<String>(); // needs to be serialized
	private URL url = null;
	private BufferedReader reader = null;
	private StringBuilder stringBuilderForHTML = new StringBuilder();
	private URLConnection connectionObj = null;
	private String nextLinkToVisit = "";
	public static Integer fileNumber = 0; // needs to be serialized
//	int fileNumber = 0;
	String LinksToVisit_FileName = "F:\\Simplest Search Engine\\LinksToVisit.txt";
	String LinksVisited_FileName = "F:\\Simplest Search Engine\\LinksVisited.txt";
	String FileNumber_FileName = "F:\\Simplest Search Engine\\FileNumber.txt";
	
	
	String wordToSearch;

	//crawling process will be initiated by calling "the crawlStarter()" :
	
	public void crawlStarter(String root) {
		
		deSerializeLinkedList(LinksToVisit_FileName);
		deSerializeSet(LinksVisited_FileName);
		deSerializeFileNumber(FileNumber_FileName);
		
		// following portion: for debug
		/*
		System.out.println("fn:"+fileNumber+"linksvisited:"+linksVisited.size());
		
		for (Iterator<String> i = linksVisited.iterator(); i.hasNext();) {
		    String element = i.next();
		    if (element.contains("facebook")) {
		        i.remove();
		        fileNumber--;
		    }
		}
		
		for(String s: linksVisited){
			if(s.contains("facebook")){
				System.out.println("Still found!");
			}
		}
		System.out.println("fn:"+fileNumber+"linksvisited:"+linksVisited.size());
		serializeObject(linksVisited, LinksVisited_FileName);
		serializeObject(fileNumber, FileNumber_FileName);
		
		if(true)
			return;
//		*/ //end of debug portion
		
		
		System.out.println("Are there more links to visit? -> "+(!linksToVisit.isEmpty()?"Yes":"No"));
		
		if(linksVisited.contains(root) && linksToVisit.isEmpty()){
			System.out.println("No more links to visit. Given Root is already visited. Exiting...");
			return;
		}
		
		linksToVisit.add(root);
		
		int ThreadNumber = 10;
		CrawlerThread[] t = new CrawlerThread[ThreadNumber];
		for(int i=0; i<ThreadNumber; i++){
			t[i] = new CrawlerThread(linksToVisit, linksVisited);
		}
		
		for(int i=0; i<ThreadNumber; i++){
			try {
				t[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Crawling Ended. Exiting...");
		
		
	} //end of crawlStarter(String root)
	
	public void printLinksToVisit() {
		for (String x : linksToVisit) {
			System.out.println(x);
		}
	}

	public void printLinksVisited() {
		for (String x : linksVisited) {
			System.out.println(x);
		}
	}

	public void printNodeNumber() {
		System.out.println(linksVisited.size());
	}

	public HashSet<String> getLinksvisited() {
		return linksVisited;
	}

	public LinkedList<String> getLinksToVisit() {
		return linksToVisit;
	}

	//bellow ones: the ones i must use
	public String nextLink() {
		String s = linksToVisit.removeFirst();
		
		while (linksVisited.contains(s) && !linksToVisit.isEmpty()) {
			s = linksToVisit.removeFirst();
		}
		if(linksVisited.contains(s)){
			return null;
		}
		return s;
	}

	public void deSerializeLinkedList(String fileName){
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		
		try{
			fis = new FileInputStream(fileName);
			ois = new ObjectInputStream(fis);
			if(fis==null || ois==null) return;
			linksToVisit = (LinkedList<String>)ois.readObject();
			System.out.println("LinksToVisit_List Read"); 
		} catch (FileNotFoundException e) {
//			e.printStackTrace();
			System.out.println("File not found for linksToVisit");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally{
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		
			if(ois!=null){
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	
	}
	
	public void deSerializeSet(String fileName){
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		
		try{
			fis = new FileInputStream(fileName);
			ois = new ObjectInputStream(fis);
			linksVisited = (HashSet<String>)ois.readObject();
			System.out.println("LinksVisited_Set Read"); 
		} catch (FileNotFoundException e) {
//			e.printStackTrace();
			System.out.println("File not found for linksVisited");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally{
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		
			if(ois!=null){
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	
	}
	
	public void deSerializeFileNumber(String fileName){
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		
		try{
			fis = new FileInputStream(fileName);
			ois = new ObjectInputStream(fis);
			fileNumber = (Integer)ois.readObject();
			System.out.println("FileNumber Read"); 
		} catch (FileNotFoundException e) {
//			e.printStackTrace();
			System.out.println("File not found for fileNumber");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally{
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		
			if(ois!=null){
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	
	}
	
	public void serializeObject(Object o, String fileName){
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		
		try {
			fos = new FileOutputStream(fileName);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(o);
//			System.out.println("Object Written");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		
			if(oos!=null){
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	
	
}
