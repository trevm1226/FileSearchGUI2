package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class searcher extends Thread {
    private ListView listOfFiles;
    private Text FilesSearchedText;
    private Text ResultsFoundText;
    private String searchTerm;
    private TextField SearchTermField;
    private File selectedDirectory;
    private ComboBox<String> searchType;
    private ProgressBar SearchBar;

    public searcher(ListView listOfFiles, Text FilesSearchedText, Text ResultsFoundText, String searchTerm, TextField SearchTermField, File selectedDirectory, ComboBox<String> searchType, ProgressBar SearchBar)
    {
        this.listOfFiles = listOfFiles;
        this.FilesSearchedText = FilesSearchedText;
        this.ResultsFoundText = ResultsFoundText;
        this.searchTerm = searchTerm;
        this.SearchTermField = SearchTermField;
        this.selectedDirectory = selectedDirectory;
        this.searchType = searchType;
        this.SearchBar = SearchBar;
    }

    public ObservableList<File> regexSearch(ObservableList<File> list)
    {
        List<File> returnList = new ArrayList<File>();

        for(File temp: list)
        {
            if(Pattern.compile(searchTerm).matcher(temp.getName()).matches()) returnList.add(temp);
        }

        return FXCollections.observableArrayList(returnList);
    }

    public ObservableList<File> fileRegexSearch(ObservableList<File> list) throws FileNotFoundException {
        List<File> returnList = new ArrayList<File>();
        Scanner scan;

        Pattern pattern = Pattern.compile(searchTerm);
        Matcher m;

        for (File temp: list) {
            String fileText = "";
            scan = new Scanner(temp);
//            m = pattern.matcher(temp.getName());
//        while (scan.hasNext()) {
//            fileText = fileText + scan.nextLine().toLowerCase();
//            }
//        if(m.find())returnList.add(temp);
        }
        return FXCollections.observableArrayList(returnList);
    }

    public ObservableList<File> fuzzySearch(ObservableList<File> list)
    {
        List<File> returnList = new ArrayList<File>();
        Map<Integer, File> fuzzyList = new TreeMap<Integer, File>();
        for(File temp: list)
        {
            fuzzyList.put(distance(searchTerm,temp.getName()),temp);
        }

        for(Map.Entry<Integer, File> temp:fuzzyList.entrySet())
        {
            if(returnList.size()< 25)
                returnList.add(temp.getValue());
        }
        return FXCollections.observableArrayList(returnList);
    }

    public int distance(String a, String b)
    {
        a = a.toLowerCase();
        b = b.toLowerCase();

        int[] costs = new int[b.length() + 1];
        for(int j = 0; j < costs.length; j++)
        {
            costs[j] = j;
        }
        for(int i = 1; i <= a.length(); i++)
        {
            costs[0] = i;
            int nw = i - 1;
            for(int j = 1; j <=b.length(); j++)
            {
                int cj = Math.min(1 + Math.min(costs[j], costs[j-1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }

    public void run()
    {
        listOfFiles.getItems().clear();
        int filesSearched = 0;
        int filesFound = 0;
        searchTerm = SearchTermField.getText();

        List<File> filesInFolder = null;
        try {
            filesInFolder = Files.walk(Paths.get(selectedDirectory.getAbsolutePath())).filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        ObservableList<File> list = FXCollections.observableList(filesInFolder);

        System.out.println("filesInFolder = " + filesInFolder);
        for(File temp:list)
        {

            if(searchType.getValue().equals("Exact Match") && (temp.getName().equals(searchTerm)))
            {
                listOfFiles.getItems().add(temp);
                filesFound++;
            }
            if(searchType.getValue().equals("Contains") && (temp.getName().contains(searchTerm)))
            {
                listOfFiles.getItems().add(temp);
                filesFound++;
            }
            if(searchType.getValue().equals("Best Match"))
            {
                listOfFiles.setItems(fuzzySearch(list));
                filesFound++;
            }
            if(searchType.getValue().equals("ReGeX"))
            {
                listOfFiles.setItems(regexSearch(list));
                filesFound++;
            }
            final int tempint = filesSearched++;
            final int otherint = filesFound;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    FilesSearchedText.setText(tempint+"");
                    ResultsFoundText.setText(otherint+"");
                    SearchBar.setProgress(tempint/listOfFiles.getItems().size());
                }
            });
        }
    }
}
