package clientforsearchengine;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @author Sazan
 */
public class ClientThread extends Thread{
    
    ClientData cDataController = null;
    String searchTextString;
    ArrayList<String> finalResultList;
    TextArea ShowULRs;
    ArrayList<Label> urlLabels = new ArrayList<>();
    public HBox hbox;
    @FXML
    Label l;
    
    public ClientThread(HBox hbox, ArrayList<Label> urlLabels, TextArea ShowULRs, String searchTextString, ArrayList<String> finalResultList, ClientData cDataController){
        this.hbox = hbox;
        this.urlLabels = urlLabels;
        this.cDataController = cDataController;
        this.finalResultList = finalResultList;
//        this.searchTextString = searchTextString.replaceAll("[\\W]", " ");
        this.searchTextString = searchTextString.replaceAll("\\p{Punct}|।", " ");
        this.ShowULRs = ShowULRs;
        this.start();
    }
    
    public void run(){
        StringTokenizer st = new StringTokenizer(searchTextString);
        StringBuilder sb = new StringBuilder();
        if(st.countTokens()==0){
            ShowULRs.setText("Nothing to search.");
            return;
        }
        int wordLimit = 15;
        if(st.countTokens()>wordLimit){
            for(int i=0;i<wordLimit;i++){
                sb.append(st.nextToken()+" ");
            }
            System.out.println(sb.toString());
            cDataController.write(sb.toString());
        }else{
            cDataController.write(searchTextString);
        }
        finalResultList = (ArrayList<String>)cDataController.read();
        
        if(finalResultList.isEmpty()){
            System.out.println("Sorry, no results found!");
            ShowULRs.setText("Sorry, no results found!");
            return;
        }
        makeHTMLFile();
        openURL("OutPut.html");
        
        ShowULRs.setText("Done.");
    }
    
    private void makeHTMLFile(){
        StringBuilder strForHtml = new StringBuilder();
        strForHtml.append( "<html>"
                + "<body style=\"background-color:#f0f0f0;\">"
                + "<style>"
                + "table { height: 60px; width: 1400px; background-color:#fefefe; border:5px solid purple}"
                + "</style>"
                + "<table align=\"left\">\n"
                + "<tr>\n"
                + "<th width=\"20%\"><h3 style=\"color:blue;\">Search Result For: </h3></th>\n"
                + "<th width=\"\"><p align=\"left\" style=\"color:purple; font-family:italic; font-size:150%;\">"
                + searchTextString
                + "<p></th>\n"
                + "</tr>\n"
                + "</table>\n"
                + "<br><br>\n"
                + "<br><br>\n"
                + "<br><br>\n"
                + "<p>\n");
        for(String str: finalResultList){
            strForHtml.append("<a href=\""+str.trim()+"\">"+str.trim()+"</a><br><br>\n");
            l = new Label(str.trim());
            l.setOnMouseClicked( e ->{
                openURL(l.getText());
                l.setVisible(true);
            }
            );
            urlLabels.add(l);
//            hbox.getChildren().add(l);
        }
//        hbox.getChildren().addAll(urlLabels);
        strForHtml.append("</p></html>"
                + "</body>");
//        System.out.println(strForHtml);
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("OutPut.html"), "utf-8"))) {
            writer.write(strForHtml.toString());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    @FXML
    private void openURL(String urlAsString){
        Application a = new Application() {
            @Override
            public void start(Stage primaryStage) throws Exception {
                //do nothing
            }
        };
        a.getHostServices().showDocument(urlAsString);
    }
    
    
    
    
}
