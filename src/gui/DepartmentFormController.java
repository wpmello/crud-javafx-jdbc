package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

public class DepartmentFormController implements Initializable {

	private Department entity;
	
	@FXML
	private TextField txtId; 
	
	@FXML
	private TextField txtxName;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	public void setDepartment(Department entity) {
		this.entity = entity;
	}
	
	@FXML
	public void onBtSaveAction() {
		System.out.println("onBtNewAction");	
	}
	
	@FXML
	public void onBtCancelAction() {
		System.out.println("onBtCancelAction");
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initialaziNodes();
	}
	
	private void initialaziNodes() {
		// método para restringir os 'TextFields'
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtxName, 30); 
	}
	
	public void updateFormDate() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		// aqui vamos carregar os dados do departamento q já estão no formulário
		txtId.setText(String.valueOf(entity.getId())); 
		txtxName.setText(entity.getName());
	}
	
}