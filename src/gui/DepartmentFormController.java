package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.exception.ValidationException;
import model.services.DepartmentServices;

public class DepartmentFormController implements Initializable {

	private Department entity;
	
	private DepartmentServices service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId; 
	
	@FXML
	private TextField txtName;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	public void setDepartment(Department entity) {
		this.entity = entity;
	}
	
	public void setDepartmentService(DepartmentServices service) {
		this.service = service;
	}
	
	public void subscribeDataChengeListener(DataChangeListener listeners) {
		// aqui é onde vamos guardar nossos "sons"
		// esse método chamado em outra classe como função transforma a classe em um listener ou seja
		// a classe agora consegue ouvir o "som" q outra(s) emite/emitem
		
		dataChangeListeners.add(listeners); 
	}
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity was null"); // programação defensiva
		}
		if (service == null) {
			throw new IllegalStateException("Service was null"); // programação defensiva
		}
		try {
			entity = getFormDate();	
			service.saveOrUpdate(entity);
			notifyDataChengeListeners(); // método q emite o "som"
			Utils.currentStage(event).close();
		}
		catch (ValidationException e) {
			setErrorMessages(e.getError());
		}
		catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
		
	}
	
	private void notifyDataChengeListeners() {
		
		// dentro do for está sendo executado o método da interface e é aqui 
		// onde o "som" será emitido
		
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	private Department getFormDate() {
		Department obj = new Department();
		
		ValidationException exception = new ValidationException("Validation error");
		
		obj.setId((Utils.tryParseToInt(txtId.getText())));
		
		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "Field can't be empty");
		}
		obj.setName(txtName.getText());
		
		if (exception.getError().size() > 0) {
			throw exception;
		}
		
		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		service.saveOrUpdate(entity);
		Utils.currentStage(event).close();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initialaziNodes();
	}
	
	private void initialaziNodes() {
		// método para restringir os 'TextFields'
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30); 
	}
	
	public void updateFormDate() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		// aqui vamos carregar os dados do departamento q já estão no formulário
		txtId.setText(String.valueOf(entity.getId())); 
		txtName.setText(entity.getName());
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if (fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}
	}
}