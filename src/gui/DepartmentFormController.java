package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
		// aqui � onde vamos guardar nossos "sons"
		// esse m�todo chamado em outra classe como fun��o transforma a classe em um listener ou seja
		// a classe agora consegue ouvir o "som" q outra(s) emite/emitem
		
		dataChangeListeners.add(listeners); 
	}
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity was null"); // programa��o defensiva
		}
		if (service == null) {
			throw new IllegalStateException("Service was null"); // programa��o defensiva
		}
		try {
			entity = getFormDate();	
			service.saveOrUpdate(entity);
			notifyDataChengeListeners(); // m�todo q emite o "som"
			Utils.currentStage(event).close();
		}
		catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
		
	}
	
	private void notifyDataChengeListeners() {
		
		// dentro do for est� sendo executado o m�todo da interface e � aqui 
		// onde o "som" ser� emitido
		
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	private Department getFormDate() {
		Department obj = new Department();
		
		obj.setId((Utils.tryParseToInt(txtId.getText())));
		obj.setName(txtName.getText());
		
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
		// m�todo para restringir os 'TextFields'
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30); 
	}
	
	public void updateFormDate() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		// aqui vamos carregar os dados do departamento q j� est�o no formul�rio
		txtId.setText(String.valueOf(entity.getId())); 
		txtName.setText(entity.getName());
	}
	
}