import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.LinkedList;

public class CrawlerThread extends Thread{

	public LinkedList<String> linksToVisit = new LinkedList<String>(); // needs to be serialized
	public HashSet<String> linksVisited = new HashSet<String>(); // needs to be serialized
	private URL url = null;
	private BufferedReader reader = null;
	private StringBuilder stringBuilderForHTML = new StringBuilder();
	private URLConnection connectionObj = null;
	private String nextLinkToVisit = null;
//	Integer fileNumber = 0; // needs to be serialized
	String LinksToVisit_FileName = "F:\\Simplest Search Engine\\LinksToVisit.txt";
	String LinksVisited_FileName = "F:\\Simplest Search Engine\\LinksVisited.txt";
	String FileNumber_FileName = "F:\\Simplest Search Engine\\FileNumber.txt";
	
	public CrawlerThread(LinkedList<String> linksToVisit, HashSet<String> linksVisited){
		this.linksToVisit = linksToVisit;
		this.linksVisited = linksVisited;
//		this.nextLinkToVisit = nextLinkToVisit;
		this.start();
	}
	
	public void run(){
		
		int counter = 0;
		do{
//			synchronized (linksVisited) {
//				fileNumber = linksVisited.size()+1;
//			}
			synchronized (linksToVisit) {
//				/*
				while(linksToVisit.isEmpty()){
					synchronized (linksToVisit) {
						try {
							System.out.println("--->>>  In wait() of "+this.getName());
							linksToVisit.wait(); // wait() use try
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
//				*/
				nextLinkToVisit = nextLink();
			}
			if(nextLinkToVisit == null){
				break;
			}
			
			if(nextLinkToVisit.contains(".pdf")){ // pdf er data bujhte na para porjonto thakbe eita
				System.out.println("<"+nextLinkToVisit+"> pdf found. continue...");
				nextLinkToVisit = nextLink();
				if(nextLinkToVisit == null) break;
				continue;
			}
			
			//debug
			/*
			if(nextLinkToVisit.contains("thedailystar")){
				nextLinkToVisit = nextLink();
				if(nextLinkToVisit == null) break;
				continue;
			}
//			*/
			try{
				System.out.println("Visiting: "+nextLinkToVisit);
				boolean check = crawlThisSite(nextLinkToVisit);
				if(check){
					linksVisited.add(nextLinkToVisit);
					System.out.println(this.getName()+">");
					System.out.println("Visited: "+(CrawlerBot.fileNumber)+": "+nextLinkToVisit);
					counter++;
				}else{
//					System.out.println("Connection Lost.Exiting...");
//					return;
					System.out.println("Connection failed in: "+this.getName());
				}

			}catch(Exception e){
				// isn't a good idea, done for a time-being:
				System.out.println("In catch of do-while loop of "+ this.getName() +". Exiting...");
				e.printStackTrace();
				return;
			}
			
			int Serializing_limit = 100;
			if(counter%Serializing_limit==0){
				System.out.println(this.getName()+": "+counter);
				synchronized (linksToVisit) {
					serializeObject(linksToVisit, LinksToVisit_FileName);
				}
				synchronized (linksVisited) {
					serializeObject(linksVisited, LinksVisited_FileName);
					serializeObject(CrawlerBot.fileNumber, FileNumber_FileName);
				}
				System.out.println("Serialized in Thread: "+this.getName());
				return;
			}
			
		}while(!linksToVisit.isEmpty());
		
	}
	
	//bellow ones: the ones i must use
	
	//bellow one: very important
	public boolean crawlThisSite(String s) {

		try {
			url = new URL(s);
			connectionObj = url.openConnection();
			connectionObj.connect();
		} catch (MalformedURLException e) {
//			e.printStackTrace();
			System.out.println(">>malformed url exception! Connection not found. exiting...");
			return false;
		} catch (IOException e1) {
			System.out.println(">>IO exception! Connection not found. exiting...");
			return false;
//			e1.printStackTrace();
		}

		try {
			reader = new BufferedReader(new InputStreamReader(connectionObj.getInputStream(), "UTF-8")); // using unicode(UTF-8)
		} catch (IOException e) {
			System.out.println(">>Buffer Reading Error!");
//			e.printStackTrace();
		}


		if(reader!=null){
//			/*
			try{
				MyParser parser = new MyParser(reader, linksToVisit, url);
				synchronized (linksToVisit) {
					parser.findLinksInPage(); 
					linksToVisit.notifyAll(); // notify() use try
				}
//				boolean abc = fileUpHTML_newVersion(parser.findTextInPage());
				fileUpHTML_newVersion(parser.findParagraphsInPage());
			}catch(Exception e){
				System.out.println(">>In the catch of MyParser!");
				e.printStackTrace();
			}
//			*/
			
			/*
			try{
				NewParser parser = new NewParser(reader, linksToVisit);
				System.out.println("view: "+ parser.getText());
				fileUpHTML_newVersion(parser.getText());
			}catch(Exception e){
				e.printStackTrace();
			}
//			*/
		}
		
		return true;
		
	} // end of crawlThisSite


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

	public boolean fileUpHTML_newVersion(String s){
		synchronized (linksVisited) {
			CrawlerBot.fileNumber+=1;
		}
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("File_"+CrawlerBot.fileNumber+".txt"), "utf-8"))) {
			writer.write(nextLinkToVisit+"   \n"+s);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	public void serializeObject(Object o, String fileName){
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		
		try {
			fos = new FileOutputStream(fileName);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(o);
			oos.flush();
//				System.out.println("Object Written");
			
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
