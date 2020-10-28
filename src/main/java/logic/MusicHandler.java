package logic;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import gui.components.MusicNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.sound.sampled.AudioInputStream;
import java.io.*;
import java.util.Scanner;

/**
 * Handles List of MusicNode's
 * by creating and updating the list
 * from the text file
 *
 * @author John Harris
 */

public class MusicHandler {

    private File mp3File = new File("mp3list.txt");
    AudioInputStream audioInputStream;

    public ObservableList<MusicNode> getMusicList() throws IOException, InvalidDataException, UnsupportedTagException {
        if (!mp3File.exists()) {
            mp3File.createNewFile();
            return null;
        }
        if(isEmptyFile()) {
            return null;
        }
        return generateSongsList(mp3File);
    }

    public ObservableList<MusicNode> generateSongsList(File mp3) throws IOException, InvalidDataException, UnsupportedTagException {
        ObservableList<MusicNode> items = FXCollections.observableArrayList();
        try {
            Scanner reader = new Scanner(mp3File);
            String path = reader.nextLine();
            while(reader.hasNextLine()) {
                // unsure if I should do this
                System.out.println(path);
                path = reader.nextLine();
                // print out
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        return items;
    }

    // https://docs.oracle.com/javase/8/docs/api/java/nio/file/Files.html
    public void updateSongsList(File file) throws IOException {
        FileWriter writer = new FileWriter(mp3File, true);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        System.out.println("Added" + file.getAbsolutePath());
        if (isEmptyFile()) {
            bufferedWriter.write(file.getAbsolutePath());
        } else {
            bufferedWriter.write("\n" + file.getAbsolutePath());
        }
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    private boolean isEmptyFile() {
        return mp3File.length() == 0;
    }
}