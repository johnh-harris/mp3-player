package com.github.harrisj09.mp3.client.application.components;

import com.github.harrisj09.mp3.client.application.controller.AudioController;
import com.github.harrisj09.mp3.client.application.controller.MusicController;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class PlayButtonsComponent {

    public MusicController musicController;
    private AudioController audioController;
    private Button playStatus;
    private Image playStatusImage;
    private String playStatusToggleText = "Play";

    // todo swap musiccontroller with musicmodel for grabbing list
    public PlayButtonsComponent(MusicController musicController, AudioController audioController) {
        this.musicController = musicController;
        this.audioController = audioController;
    }

    public Button playStatusToggle() {
        playStatus = new Button(playStatusToggleText);
        playStatus.setPrefSize(40, 40);
        EventHandler<MouseEvent> toggleStatus = e -> {
            if (audioController.getCurrentlyPlaying() != null) {
                if (playStatusToggleText.equals("Play")) {
                    playStatusToggleText = "Pause";
                    audioController.resumeSong();
                } else {
                    playStatusToggleText = "Play";
                    audioController.pauseSong();
                }
                changeToggleText(playStatusToggleText);
            } else {
                if(musicController.getMusicQueue().getBack() != null) {
                    audioController.playQueue(musicController.getMusicQueue().getBack());
                }
            }
        };
        playStatus.addEventFilter(MouseEvent.MOUSE_CLICKED, toggleStatus);
        return playStatus;
    }

    // TODO Fix this to handle non downloaded files
    public Button previousButton() {
        // Image
        Image img = new Image("prevarrow.png");
        ImageView view = createImageView(img);
        // Button
        Button previous = new Button();
        previous.setPadding(Insets.EMPTY);
        previous.setGraphic(view);
        EventHandler<MouseEvent> previousEvent = e -> {
            if(canGoBack()) {
                int nodeId = audioController.getCurrentlyPlaying().getSong().getId();
                if (!musicController.songIsInLibrary(nodeId - 1)) {
                    musicController.downloadSong(nodeId - 1);
                }
                musicController.playSong(nodeId - 1, audioController);
            }
        };
        previous.addEventFilter(MouseEvent.MOUSE_CLICKED, previousEvent);
        return previous;
    }

    public Button skipButton() {
        // Image
        Image img = new Image("skiparrow.png");
        ImageView view = createImageView(img);
        // Button
        Button skip = new Button();
        skip.setPadding(Insets.EMPTY);
        skip.setGraphic(view);
        EventHandler<MouseEvent> skipEvent = e -> {
            if(audioController.isInQueue() && musicController.getMusicQueue().getSize() > 1) {
                musicController.getMusicQueue().dequeue();
                audioController.pauseSong();
                audioController.playQueue(musicController.getMusicQueue().getBack());
            } else {
                if(canSkip()) {
                    int nodeId = audioController.getCurrentlyPlaying().getSong().getId();
                    if (!musicController.songIsInLibrary(nodeId + 1)) {
                        musicController.downloadSong(nodeId + 1);
                    }
                    musicController.playSong(nodeId + 1, audioController);
                }
            }
        };
        skip.addEventFilter(MouseEvent.MOUSE_CLICKED, skipEvent);
        return skip;
    }

    private ImageView createImageView(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(40);
        imageView.setFitWidth(50);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    private void changeToggleText(String text) {
        playStatus.setText(text);
    }

    private boolean canGoBack() {
        return audioController.getCurrentlyPlaying() != null && audioController.getCurrentlyPlaying().getSong().getId() > 0;
    }

    private boolean canSkip() {
        return audioController.getCurrentlyPlaying() != null && audioController.getCurrentlyPlaying().getSong().getId() < musicController.getMusicList().size() - 1;
    }
}
