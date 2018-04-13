package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
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
    @FXML private Text FilesSearchedIdentifier;
    @FXML private Text ResultsFoundIdentifier;
    @FXML private ProgressBar SearchBar;
    @FXML private Text SearchTermIdentifier;
    @FXML private TextField SearchTermField;
    @FXML private Button SearchButton;
    @FXML private Text FilesSearchedText;
    @FXML private Text ResultsFoundText;
    @FXML private ListView listOfFiles;
    @FXML private Label BUY;
    @FXML private Stage stage;
    @FXML private ComboBox<String> searchType;

    private List<File> fileList;
    private String searchTerm;
    private File selectedDirectory;

    private dataContainer contents;


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchType.getItems().addAll("Exact Match", "Contains", "Best Match", "ReGeX");
        contents = new dataContainer(SearchBar, SearchTermField, FilesSearchedText, ResultsFoundText, listOfFiles, BUY, stage, searchType);
    }

    public void OpenFIleExplorer(ActionEvent actionEvent) throws IOException {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setInitialDirectory(new File(".."));
        selectedDirectory = chooser.showDialog(stage);
        if(selectedDirectory.equals(null))
            System.out.println("no directory selected");
        BUY.setText(selectedDirectory.getAbsolutePath());
    }

    public void Search(ActionEvent actionEvent) throws IOException {
        FilesSearchedText.setText("0");
        ResultsFoundText.setText("0");
        new Thread(new searcher(contents, searchTerm, selectedDirectory)).start();
    }
}