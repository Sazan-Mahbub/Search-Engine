
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;

public class MyParser {
	
	public LinkedList<String> linksToVisit = new LinkedList<String>();
	public LinkedList<String> linksToVisit_temp = new LinkedList<String>();
	
	String htmlInString;
	StringBuilder stringBuilderForHTML = new StringBuilder();
	URL url = null;
	BufferedReader s;
	Boolean PageIsRead = false;
	
	public MyParser(BufferedReader s, LinkedList<String> lList, URL url) {
//		System.out.println("In MyParser's constructor");
		this.linksToVisit = lList;
		this.url = url;
		this.s = s;
//		System.out.println(s);
//		htmlInString = new String();
		String perLine = null;
		
		try {
			while(s!=null && (perLine = s.readLine()) != null){
//				htmlInString+= perLine+"\n";
				stringBuilderForHTML.append(perLine + "\n");
			}
		} catch (IOException e) {
			System.out.println(">>BufferReading error!");
		}
		htmlInString = stringBuilderForHTML.toString();
		PageIsRead = true;
		
	}

	public void findLinksInPage(){
		
		int init = 0, end = 0;
		
		
		init = htmlInString.indexOf("<a href=\"", init);
		end = htmlInString.indexOf("\"", init+(8+1)); //"<a href=\"" strlen = 8, check from 8+1 pos;
		
		while(init!=-1 && end!=-1){
			String x = htmlInString.substring(init+9, end);

			String authority = url.getAuthority();
			String protocol = url.getProtocol();
			if (!(x.contains(authority))) {
				if (!x.contains("//"))
					x = protocol+"://" + authority + (x.startsWith("/") ? (x) : ("/"+x) );
//					x = protocol+"://" + authority +"/"+ x; //NewParser and MyParser
				
				else if (!x.contains("http:") && !x.contains("https:")) {
					x = protocol+":" + x;
				}

			} else if (!x.contains("http:") && !x.contains("https:") && x.contains("//")) {
				x = protocol+":" + x;
			}

			linksToVisit_temp.add(x);
			
			init+=end-init+2;
			init = htmlInString.indexOf("<a href=\"", init);
			end = htmlInString.indexOf("\"", init+(8+1)); //"<a href=\"" strlen = 8, check from 8+1 pos;
		}
		linksToVisit.addAll(linksToVisit_temp);
		PageIsRead = true;
	}

	
	public String findParagraphsInPage(){
		if(!PageIsRead){
		}
		
		int init = 0, end = 0;
		
		
		init = htmlInString.indexOf("<p", init);

		end = htmlInString.indexOf("/p>", init+1)+3;	
		int count=0;
		
		String temp = "";
		while(init!=-1 && end!=-1+3){
			temp = temp.concat(htmlInString.substring(init, end)+"\n");
			init+=end-init;
			init = htmlInString.indexOf("<p", init);
			end = htmlInString.indexOf("/p>", init+1)+3;	
			
		}

		temp = temp.replaceAll("\\<.*?>"," "); // replace everything between <> 
		// bellow part: english chara onno kichu hole prob hocche
//		temp = temp.replaceAll("[\\W]", " "); // replace everthing other than alpha numberic, 
		temp = temp.replaceAll("\\p{Punct}|।", " "); // অন্য ভাষায় কাজ করে...
		
		PageIsRead = false;
		return temp;
		
	}

	
	// bellow: better way, less data loss
	public String findTextInPage(){

		if(!PageIsRead) return null;
		
		int init = 0, end = 0;

//		/*
		init = htmlInString.indexOf("<script", init);
		end = htmlInString.indexOf("/script>", init+1);		
		
//		int count=0;
		
		while(init!=-1 && end!=-1){

			String subStr = htmlInString.substring(init, end+"/script>".length());
			htmlInString = htmlInString.replace(subStr, " ");
			
			init = htmlInString.indexOf("<script", init);
			end = htmlInString.indexOf("/script>", init+1);
		}
//		*/

		
//		/*
		init = htmlInString.indexOf("<code", init);
		end = htmlInString.indexOf("/code>", init+1);		
		
//		int count=0;
		
		while(init!=-1 && end!=-1){

			String subStr = htmlInString.substring(init, end+"/code>".length());
			htmlInString = htmlInString.replace(subStr, " ");
			
			init = htmlInString.indexOf("<code", init);
			end = htmlInString.indexOf("/code>", init+1);
		}
//		*/
		
		
		init = htmlInString.indexOf("<", init);
		end = htmlInString.indexOf(">", init+1);
		
//		int count=0;
		while(init!=-1 && end!=-1){

			String subStr = htmlInString.substring(init, end+1);
			htmlInString = htmlInString.replace(subStr, " ");
			
			init = htmlInString.indexOf("<", init);
			end = htmlInString.indexOf(">", init+1);
		}
		
//		htmlInString = htmlInString.replaceAll("[\\W]", " ");
		htmlInString = htmlInString.replaceAll("\\p{Punct}|।", " ");
		System.out.println("htmlInString length: "+htmlInString.length());

		PageIsRead = false;
			
//		System.out.println(htmlInString);
		return htmlInString;
		
	}
	
}
