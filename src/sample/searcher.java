package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.*;
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
    private int filesSearched, filesFound;
    private List<File> filesInFolder;
    private static searcher instance;


    public searcher(dataContainer container, String search, File dire)
    {
        this.listOfFiles = container.getListOfFiles();
        this.FilesSearchedText = container.getFilesSearchedText();
        this.ResultsFoundText = container.getResultsFoundText();
        this.searchTerm = search;
        this.SearchTermField = container.getSearchTermField();
        this.selectedDirectory = dire;
        this.searchType = container.getSearchType();
        this.SearchBar = container.getSearchBar();
    }

    public static searcher create(dataContainer conten, String search, File dire)
    {
        searcher ret = new searcher(conten, search, dire);
//        if(instance != null){
//            instance.cancel();
//            try{
//                instance.join();
//            }
//            catch(InterruptedException e){
//                e.printStackTrace();
//            }
//        }
        instance = ret;
        return ret;

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
        filesSearched = 0;
        filesFound = 0;
        searchTerm = SearchTermField.getText();

        filesInFolder = null;
        try {
            filesInFolder = Files.walk(Paths.get(selectedDirectory.getAbsolutePath())).filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ObservableList<File> list = FXCollections.observableList(filesInFolder);

        System.out.println("filesInFolder = " + filesInFolder);
        for(File temp:list)
        {

            Platform.runLater(() -> {
                System.out.println("ok1");
                FilesSearchedText.setText(filesSearched++ + "");
                SearchBar.setProgress(filesSearched/filesInFolder.size());
            });
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

            Platform.runLater(() -> {
                System.out.println("ok2");
                ResultsFoundText.setText(filesFound + "");
            });
        }
    }


}
