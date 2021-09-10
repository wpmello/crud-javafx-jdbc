package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentServices;

public class DepartmentListController implements Initializable, DataChangeListener {

	private DepartmentServices service;
	
	@FXML
	private TableView<Department> tableViewDepartment;
	
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	
	@FXML 
	private TableColumn<Department, String> tableColumnName;
	
	@FXML
	private Button btNew;
	
	private ObservableList<Department> obsList;
	
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Department obj = new Department(); 
		createDialogForm(obj, "/gui/DepartmentForm.fxml", parentStage);
	}
	
	public void setDepartmentService(DepartmentServices service) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView() {
		
		if(service == null) { // programação defensiva
			throw new IllegalStateException("Sevice was null");
		}
		
		// adc elementos na lista
		List<Department> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewDepartment.setItems(obsList);
	}
	
	private void createDialogForm(Department obj, String absoluteName, Stage parentStage) {
		
		// método carregando um novo Stage (formulário para preencher os dados de um novo departmento)
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			// aqui estamos chamando o controller da classe 'DepartmentFormController', injetando o departamento e carregando seus dados no formulário
			DepartmentFormController controller = loader.getController();
			controller.setDepartmentService(new DepartmentServices());
			controller.setDepartment(obj);
			controller.subscribeDataChengeListener(this); // aqui é a tranformação da classe em um listener
			controller.updateFormDate();
			
			Stage dialogForm = new Stage();
			dialogForm.setTitle("Enter Department data");
			dialogForm.setScene(new Scene(pane));
			dialogForm.setResizable(false); // função para tela n redimensionar
			dialogForm.initOwner(parentStage); // indica o Stage em q a nova tela vai abrir
			dialogForm.initModality(Modality.WINDOW_MODAL); // ao essa tela ser aberta só será possível acessar outra fechando-a no 'x'
			dialogForm.showAndWait(); // enfim carrega a tela
		}
		catch(IOException e) {
		Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		// aqui é oq irá acontecer quando a classe ouvir o "som"
		updateTableView();
	}
}
