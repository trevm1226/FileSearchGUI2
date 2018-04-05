package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import javax.xml.soap.Text;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * TODO: 4/4/2018
 * root directory
 * search term
 * output list of files that match/contain search terms
 * save output to file
 * status bar
 * search text files
 * using regular expressions
 * fuzzy match
 */


public class Controller implements Initializable{
    @FXML private Button PickButton;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML private Stage stage;
    @FXML private Label BUY;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void OpenFIleExplorer(ActionEvent actionEvent) throws IOException {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setInitialDirectory(new File(".."));
        File selectedDirectory = chooser.showDialog(stage);
        if(selectedDirectory.equals(null))
            System.out.println("no directory selected");
        BUY.setText(selectedDirectory.getAbsolutePath());
    }

    public void Search(ActionEvent actionEvent)
    {

    }
}