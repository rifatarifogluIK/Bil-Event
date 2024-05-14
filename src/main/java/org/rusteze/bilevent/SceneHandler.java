package org.rusteze.bilevent;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public interface SceneHandler {

    public void homeBtn(ActionEvent event);
    public void calendarBtn(ActionEvent event);
    public void discoverBtn(ActionEvent event);
    public void searchBtn(ActionEvent event);
    public void createCommunityButtons();
    public void createBtn(ActionEvent event);
    public void createEventBtn(ActionEvent event);
    public void logOutBtn(ActionEvent event);
    public void profileBtn(MouseEvent event);
    public void displayFriends();
}
