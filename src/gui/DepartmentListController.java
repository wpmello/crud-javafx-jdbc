package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
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
	private TableColumn<Department, Department> tableColumnEDIT;

	@FXML
	private TableColumn<Department, Department> tableColumnREMOVE;

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

		if (service == null) { // programa??o defensiva
			throw new IllegalStateException("Sevice was null");
		}

		// adc elementos na lista
		List<Department> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewDepartment.setItems(obsList);
		initEditButtons(); 
		initRemoveButtons(); 
	}

	private void createDialogForm(Department obj, String absoluteName, Stage parentStage) {

		// m?todo carregando um novo Stage (formul?rio para preencher os dados de um
		// novo departmento)

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			// aqui estamos chamando o controller da classe 'DepartmentFormController',
			// injetando o departamento e carregando seus dados no formul?rio
			DepartmentFormController controller = loader.getController();
			controller.setDepartmentService(new DepartmentServices());
			controller.setDepartment(obj);
			controller.subscribeDataChangeListener(this); // aqui ? a tranforma??o da classe em um listener
			controller.updateFormDate();

			Stage dialogForm = new Stage();
			dialogForm.setTitle("Enter Department data");
			dialogForm.setScene(new Scene(pane));
			dialogForm.setResizable(false); // fun??o para tela n redimensionar
			dialogForm.initOwner(parentStage); // indica o Stage em q a nova tela vai abrir
			dialogForm.initModality(Modality.WINDOW_MODAL); // ao essa tela ser aberta s? ser? poss?vel acessar outra
															// fechando-a no 'x'
			dialogForm.showAndWait(); // enfim carrega a tela
		} catch (IOException e) {
			e.printStackTrace(); 
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		// aqui ? oq ir? acontecer quando a classe ouvir o "som"
		updateTableView();
	}

	private void initEditButtons() {
		// m?todo especifico onde cria um bot?o nomeado com "edit" ao lado de cada linha
		// no formul?rio que for o m?todo chamado

		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/DepartmentForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		// m?todo especifico onde cria um bot?o nomeado com "remove" ao lado de cada linha
		// no formul?rio que for o m?todo chamado
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Department obj) {
		
		// m?todo da execu??o do bot?o de remover um departamento
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");
	
		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				service.remove(obj);
				updateTableView();
			}
			catch (DbIntegrityException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}	
		}
	}
}
