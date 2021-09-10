package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

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
import model.services.SellerServices;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemSeller;
	
	@FXML
	private MenuItem menuItemDepartment;
	
	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	public void onMenuItemSellerAction() {
		loadView("/gui/SellerList.fxml", (SellerListController controller) -> {
			controller.setSellerService(new SellerServices());
			controller.updateTableView();
		});	
	}
	
	@FXML
	public void onMenuItemDepartmentAction() {
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
			controller.setDepartmentService(new DepartmentServices());
			controller.updateTableView();
		});	
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml", x -> {}); 
	}
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}

	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) { 
		
		// foi acrecentado um 'Consumer' como parâmentro para q a função possa receber um lambda como argumento
		
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
		
			T controller = loader.getController(); // pegando o controller da classe
			initializingAction.accept(controller); // ativando o controller
		
		}
		catch(IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
}
