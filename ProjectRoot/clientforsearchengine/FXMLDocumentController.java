/**
 *
 * @author Sazan
 */
package clientforsearchengine;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @author USER
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Label label;
    @FXML
    private Button searchButton;
    @FXML
    private Button goToButton;
    @FXML
    private TextField searchField;
    @FXML
    private TextField goToField;
    @FXML
    public TextArea ShowURLs;
    @FXML
    private ArrayList<Label> urlLabels = new ArrayList<>();
    @FXML
    public HBox hbox;
    
    
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }
    
    //.....
    ArrayList<String> finalResultList = new ArrayList<>();
    String serverName = "server"; // lage na
    String serverIP = "localhost";
    int serverPort = 1234;
    Socket socket = null;
    ClientData cdataController = null; 
    String searchTextString = "";
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            socket = new Socket(InetAddress.getByName(serverIP), serverPort);
        } catch (UnknownHostException e) {
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
        }
        
        cdataController = new ClientData(socket);
        ShowURLs.setEditable(false);
        
    }
    
    Boolean buttonClickCheck = false;
    Thread thread = null;
    @FXML
    private void searchButtonAction(ActionEvent e){
        System.out.println("click");
        if(thread!=null && thread.isAlive()){
            System.out.println("thread alive");
            return;
        }
        ShowURLs.setText("Loading search results.\nPlease wait...");
        searchTextString = searchField.getText();
        if(searchTextString.equals("")){
            System.out.println("Nothing to search.");
            ShowURLs.setText("Nothing to search.");
            return;
        }
        thread = new ClientThread(hbox, urlLabels, ShowURLs, searchTextString, finalResultList, cdataController);
        searchField.setText("");
    
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
