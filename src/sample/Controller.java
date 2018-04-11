package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    private File dir;
    private String searchTerm;
    private File selectedDirectory;


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchType.getItems().addAll("Exact Match", "Contains", "Best Match", "ReGeX");
    }

    public void OpenFIleExplorer(ActionEvent actionEvent) throws IOException {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setInitialDirectory(new File(".."));
        selectedDirectory = chooser.showDialog(stage);
        if(selectedDirectory.equals(null))
            System.out.println("no directory selected");
        BUY.setText(selectedDirectory.getAbsolutePath());
        dir = selectedDirectory;
    }

    public void Search(ActionEvent actionEvent) throws IOException {
        FilesSearchedText.setText("0");
        ResultsFoundText.setText("0");
        searcher dingdong = new searcher(listOfFiles, FilesSearchedText, ResultsFoundText, searchTerm, SearchTermField, selectedDirectory, searchType, SearchBar);
        dingdong.start();
    }
}