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
import model.services.DepartmentServices;

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
		loadView2("/gui/DepartmentList.fxml");	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml"); 
	}
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}

	private synchronized void loadView(String absoluteName) {
		
		// método usado para carregar nova tela sobre a principal
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();			
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent(); // pegando e guardando o control 'VBox' da Scene principal' 
			Node mainMenu = mainVBox.getChildren().get(0); 	// pegando e guardando 'MenuBar'
			mainVBox.getChildren().clear();  // limpando 'MenuBar'
			mainVBox.getChildren().add(mainMenu);	// adc o 'MenuBar' pela mainMenu
			mainVBox.getChildren().addAll(newVBox);	// adc o novo 'MenuBar' pela newVBox
		
			// "synchronized" ajuda o método com multi threads, fazendo com q ele rode independente das threads q estão rolando
		}
		catch(IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
	
	private synchronized void loadView2(String absoluteName) {
	
		// foi feito uma cópia apenas a critério da aula. será arrumado no proximo commit
		
		// método usado para carregar nova tela sobre a principal
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();			
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent(); // pegando e guardando o control 'VBox' da Scene principal' 
			Node mainMenu = mainVBox.getChildren().get(0); 	// pegando e guardando 'MenuBar'
			mainVBox.getChildren().clear();  // limpando 'MenuBar'
			mainVBox.getChildren().add(mainMenu);	// adc o 'MenuBar' pela mainMenu
			mainVBox.getChildren().addAll(newVBox);	// adc o novo 'MenuBar' pela newVBox
		
			// "synchronized" ajuda o método com multi threads, fazendo com q ele rode independente das threads q estão rolando
		
			DepartmentListController controller = loader.getController(); // acessa controller da classe
			controller.setDepartmentService(new DepartmentServices());
			controller.updateTableView();
		}
		catch(IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
}
