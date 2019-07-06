package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/Servlet")
public class MyServlet extends HttpServlet {

    //.....
    ArrayList<String> finalResultList = new ArrayList<>();
    String serverName = "server"; // lage na, yet
    String serverIP = "localhost";
    int serverPort = 1234;
    Socket socket = null;
    ClientData cDataController = null; 
    String searchTextString = "";
    
	@Override
	public void init() throws ServletException {
		super.init();
		try {
            socket = new Socket(InetAddress.getByName(serverIP), serverPort);
        } catch (UnknownHostException e) {
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
        }
		cDataController = new ClientData(socket);
		System.out.println("In servlet: init");

	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		finalResultList = new ArrayList<>(); // to reset e list everytime
		System.out.println("In servlet: doPost");
		// read form fields
		searchTextString = request.getParameter("searchWord");

		System.out.println("searchWord: " + searchTextString);

        PrintWriter out = response.getWriter();

        ///....
        generateResult(out);
        
	}
	
	public void generateResult(PrintWriter out){
//		searchTextString = searchTextString.replaceAll("[\\W]", " ");
		searchTextString = searchTextString.replaceAll("\\p{Punct}|।", " ");
		System.out.println(searchTextString);
		StringTokenizer st = new StringTokenizer(searchTextString);
        StringBuilder sb = new StringBuilder();
        int tokenNumber = st.countTokens();
        System.out.println(tokenNumber);
        int wordLimit = 15;
        if(tokenNumber>wordLimit){
            for(int i=0;i<wordLimit;i++){
                sb.append(st.nextToken()+" ");
            }
            System.out.println(sb.toString());
            cDataController.write(sb.toString());
            finalResultList = (ArrayList<String>)cDataController.read();
        }
        else if(tokenNumber>0){
            cDataController.write(searchTextString);
            finalResultList = (ArrayList<String>)cDataController.read();
        }

        makeHTMLFile(out);
	}
	
	private void makeHTMLFile(PrintWriter out){
        StringBuilder strForHtml = new StringBuilder();
        strForHtml.append( "<html>"
                + "<body style=\"background-color:#f0f5f0;\">\n"
                + "<style>\n"
                + "table { height: 60px; width: 1400px; background-color: #efefee; border:5px solid purple}\n"
                + "</style>\n"
                + "<form name=\"SearchPage\" method=\"post\" action=\"Servlet\">\n"
                + "<p align=\"left\" style=\"color:purple; font-size:200%\">"
                + "Search here<br/>"
                + "<input type=\"text\" name=\"searchWord\" style=\"font-size:90%\"/> <br/>\n"
                + "<input type=\"submit\" value=\"Search\" style=\"font-size:70%\"/>\n"
                + "</p>"
                + "</form>\n"
                + "<table align=\"left\">\n"
                + "<tr>\n"
                + "<th width=\"20%\"><h3 style=\"color:blue;\">Search Result For: </h3></th>\n"
                + "<th width=\"\"><p align=\"left\" style=\"color:purple; font-family:italic; font-size:150%;\">\n"
                + searchTextString+"\n"
                + "<p></th>\n"
                + "</tr>\n"
                + "</table>\n"
                + "<br><br>\n"
                + "<br><br>\n"
                + "<br><br>\n"
                + "<p style = \"font-size:100%\"><br>\n");
        if(finalResultList.isEmpty()){
            strForHtml.append("<b>Sorry, no results found!</b>");
        }else{
	        for(String str: finalResultList){
	            strForHtml.append("<a href=\""+str.trim()+"\">"+str.trim()+"</a><br><br>\n");
	        }
        }
        strForHtml.append("</p></body></html>");
//        System.out.println(strForHtml.toString());
        out.println(strForHtml.toString());
	}

}
