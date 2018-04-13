package sample;

import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by trevm on 4/12/2018.
 * Holds info about everything
 */
public class dataContainer {


    public Text getResultsFoundText() {
        return resultsFoundText;
    }

    public void setResultsFoundText(Text resultsFoundText) {
        this.resultsFoundText = resultsFoundText;
    }

    public ProgressBar getSearchBar() {
        return searchBar;
    }

    public void setSearchBar(ProgressBar searchBar) {
        this.searchBar = searchBar;
    }

    public TextField getSearchTermField() {
        return searchTermField;
    }

    public void setSearchTermField(TextField searchTermField) {
        this.searchTermField = searchTermField;
    }

    public Text getFilesSearchedText() {
        return filesSearchedText;
    }

    public void setFilesSearchedText(Text filesSearchedText) {
        this.filesSearchedText = filesSearchedText;
    }

    public ListView getListOfFiles() {
        return listOfFiles;
    }

    public void setListOfFiles(ListView listOfFiles) {
        this.listOfFiles = listOfFiles;
    }

    public Label getBuy() {
        return buy;
    }

    public void setBuy(Label buy) {
        this.buy = buy;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public ComboBox getSearchType() {
        return searchType;
    }

    public void setSearchType(ComboBox searchType) {
        this.searchType = searchType;
    }

    private Text resultsFoundText;
    private ProgressBar searchBar;
    private TextField searchTermField;
    private Text filesSearchedText;
    private ListView listOfFiles;
    private Label buy;
    private Stage stage;
    private ComboBox searchType;


    public dataContainer(ProgressBar searchBar, TextField searchTermField, Text filesSearchedText, Text resultsFoundText, ListView listOfFiles, Label buy, Stage stage, ComboBox<String> searchType) {
        this.searchBar = searchBar;
        this.searchTermField = searchTermField;
        this.filesSearchedText = filesSearchedText;
        this.resultsFoundText = resultsFoundText;
        this.listOfFiles = listOfFiles;
        this.buy = buy;
        this.stage = stage;
        this.searchType = searchType;
    }
}
