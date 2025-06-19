package com.raven.form;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Form_1 extends javax.swing.JPanel {

    private List<String> remainingVideoIds;

    public Form_1() {
        initComponents();
    }

    private void initComponents() {
        remainingVideoIds = new ArrayList<>();
        remainingVideoIds.add("v7AYKMP6rOE");
        remainingVideoIds.add("s2NQhpFGIOg");
        remainingVideoIds.add("Z88Rl5bpnmI");
        remainingVideoIds.add("0XRKDJdG_rA");
        remainingVideoIds.add("syS4M1G-rII");
        remainingVideoIds.add("E9mM2StxkCo");
        remainingVideoIds.add("VD9p9tEP9RE");
        remainingVideoIds.add("MgT2yuUHCws");

        setLayout(new GridLayout(2, 2)); // Divide the form into four quarters

        for (int i = 0; i < 4; i++) {
            final int index = i; // Create final variable for lambda expression
            JFXPanel jfxPanel = new JFXPanel();
            add(jfxPanel); // Add the JFXPanel to the JPanel

            Platform.runLater(() -> {
                WebView webView = new WebView();
                WebEngine webEngine = webView.getEngine();
                int randomIndex = new Random().nextInt(remainingVideoIds.size());
                String videoId = remainingVideoIds.remove(randomIndex);
                String thumbnailUrl = "https://img.youtube.com/vi/" + videoId + "/maxresdefault.jpg";
                String title = getYouTubeVideoTitle(videoId); // Get YouTube video title
                String embedCode = "<html><body style='margin:0px;padding:0px;text-align:center;position:relative;'>" +
                        "<img src='" + thumbnailUrl + "' style='cursor:pointer;width:100%;height:100%;'>" +
                        "<div class='overlay' style='position:absolute;top:0;left:0;width:100%;height:100%;background-color:rgba(0,0,0,0.5);color:white;text-align:center;display:none;'>" +
                        "<p id='title" + index + "' style='margin:0;font-family:sans-serif;font-size:24px;opacity:0;'>"
                        + title + "</p>" +
                        "</div>" +
                        "</body></html>";
                webEngine.loadContent(embedCode);

                // Set the preferred size of the WebView to cover the JFXPanel
                webView.setPrefSize(1095, 715);

                // Add hover effect to show title text
                webView.setOnMouseEntered(event -> {
                    webEngine.executeScript("var title = document.getElementById('title" + index + "');" +
                            "if (title) {" +
                            "title.style.transition = 'opacity 0.4s';" +
                            "title.style.opacity = '1';" +
                            "}");
                            webEngine.executeScript(
                                "console.log('Mouse entered');" +
                                "var overlay = document.querySelector('.overlay');" +
                                "console.log(overlay);" +
                                "if (overlay) {" +
                                "    console.log('Overlay found');" +
                                "    overlay.style.display = 'block';" +
                                "} else {" +
                                "    console.error('Overlay not found');" +
                                "}"
                            );
                });

                // Remove title text on mouse exit
                webView.setOnMouseExited(event -> {
                    webEngine.executeScript("var title = document.getElementById('title" + index + "');" +
                            "if (title) {" +
                            "title.style.transition = 'opacity 0.4s';" +
                            "title.style.opacity = '0';" +
                            "}");
                });

                // Add click event to open video in browser
                webView.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 1) {
                        openVideoInBrowser(videoId);
                    }
                });

                    
                // Set the Scene for the JFXPanel
                jfxPanel.setScene(new Scene(webView));
            });
        }
    }


    private void openVideoInBrowser(String videoId) {
        try {
            Desktop.getDesktop().browse(new URI("https://www.youtube.com/watch?v=" + videoId));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }


    private String getYouTubeVideoTitle(String videoId) {
        try {
            String url = "https://www.youtube.com/watch?v=" + videoId;
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000); // Set a timeout of 5 seconds
            connection.setReadTimeout(5000); // Set a timeout of 5 seconds
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            Pattern pattern = Pattern.compile("<meta name=\"title\" content=\"(.*?)\">");
            Matcher matcher = pattern.matcher(response.toString());
            if (matcher.find()) {
                return matcher.group(1);
            } else {
                return "Unknown Title";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Unknown Title";
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1105, 725); // Set preferred size of the form
    }
}
