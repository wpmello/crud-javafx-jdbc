package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemSeller;
	
	@FXML
	private MenuItem menuItemDepartment;
	
	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	public void onMenuItemSellerAction() {
		System.out.println("onMenuItemSellerAction");
	}
	
	@FXML
	public void onMenuItemDepartmentAction() {
		loadView("/gui/DepartmentList.fxml");	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml"); 
	}
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}

	private synchronized void loadView(String absoluteName) {
		
		// method used to load a new view
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();			
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent(); // catching and guarding the control 'VBox' from main Scene
			
			Node mainMenu = mainVBox.getChildren().get(0); 							// catching and guarding 'MenuBar'
			mainVBox.getChildren().clear();    										// its cleaning the 'MenuBar'
			mainVBox.getChildren().add(mainMenu);									// adding the 'MenuBar' by mainMenu
			mainVBox.getChildren().addAll(newVBox);									// adding the new 'MenuBar' by newVBox
		
			// "synchronized" helps method with multi threads
		}
		catch(IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
}
