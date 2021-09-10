package gui.util;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class Alerts {

	// alertas padr�o para usar durante a cria��o do projeto
	public static void showAlert(String title, String header, String content, AlertType type) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.show();
	}
	
	// esse � um alert de confirma��o do tipo "Optional" 
	//ele vai determinar se algo realmente deve acontecer ou n�o
	
	public static Optional<ButtonType> showConfirmation(String title, String content) {
		 Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		return alert.showAndWait();
		}
}
