package com.controllers;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.App;
import com.classes.Expense;
import com.classes.TypeOfPayment;
import com.util.FileManager;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
@SuppressWarnings("unchecked")

public class MainWindowController implements Initializable {

    private static MainWindowController instance;

    public MainWindowController() {
        instance = this;
    }

    public static MainWindowController getInstance() {
        return instance;
    }

    private List<Image> imageList;
    
    @FXML
    private Label lb_connection;

    @FXML
    private ImageView imgView_connection;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TableView<Expense> tableView;

    @FXML
    private MenuItem addExpense;

    @FXML
    private MenuItem searchByDay;

    @FXML
    private MenuItem searchByName;

    @FXML
    private MenuItem totalExpensesCustomMontn;

    @FXML
    private MenuItem totalExpensesLastMontn;

    @FXML
    private MenuItem totalExpensesThisMonth;

    @FXML
    void onClick_addExpense(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/addExpense.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Nuova spesa");
            stage.getIcons().add(new Image(App.class.getResourceAsStream("/img/expenses.png")));
            stage.setScene(new Scene(root));  
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onClick_deleteExpense(ActionEvent event) {
    if(!(tableView.getSelectionModel().getSelectedItem() == null)) {
        if (FileManager.removeExpense(tableView.getSelectionModel().getSelectedItem())) {      
            tableView.getItems().remove(tableView.getSelectionModel().getSelectedItem());
            tableView.refresh();
            }

        }
    }

    @FXML
    void onClick_totalExpensesCustomMonth(ActionEvent event) {

    }

    @FXML
    void onClick_totalExpensesLastMonth(ActionEvent event) {
        loadTotalExpence(LocalDate.now().minusMonths(1));
    }

    @FXML
    void onClick_searchByDay(ActionEvent event) {

    }

    @FXML
    void onClick_searchByName(ActionEvent event) {

    }

    @FXML
    void onClick_totalExpensesCustomRange(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/totalExpense.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Nuova spesa");
            stage.getIcons().add(new Image(App.class.getResourceAsStream("/img/expenses.png")));
            stage.setScene(new Scene(root));  
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onClick_totalExpensesThisMonth(ActionEvent event) {
        loadTotalExpence(LocalDate.now());
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
            File jsonSettings = new File(FileManager.getAppdataDirPath() + FileManager.getCfgFileName());
        if (!jsonSettings.exists() || jsonSettings.length() == 0) {
            Alert alert = new Alert(AlertType.INFORMATION, "Seleziona dove salvare il file delle spese", ButtonType.OK);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(
                new Image(this.getClass().getResource("/img/expenses.png").toString()));
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                try {
                jsonSettings.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                FileManager.writeCfgFile(browseDirectory().toString());
            } else {
                System.exit(0);
            }
        }
        
        TableColumn<Expense, LocalDate> dataColumn = new TableColumn<Expense, LocalDate>("Data acquisto");
        dataColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Expense, String> nameColumn = new TableColumn<Expense, String>("Descrizione");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Expense, Float> valueColumn = new TableColumn<Expense, Float>("Importo");
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        TableColumn<Expense, TypeOfPayment> typeOfPaymentColumn = new TableColumn<Expense, TypeOfPayment>("Tipo di pagamento");
        typeOfPaymentColumn.setCellValueFactory(new PropertyValueFactory<>("card"));

        
        tableView.getColumns().addAll(dataColumn, nameColumn, valueColumn, typeOfPaymentColumn);

        for (Expense expense : FileManager.getExpenseList()) {
            addExpense(expense);
        }
        
        imageList = new ArrayList<Image>();
        imageList.add(new Image(App.class.getResourceAsStream("/img/green_tick.png")));
        imageList.add(new Image(App.class.getResourceAsStream("/img/red_tick.png")));
        imgView_connection.setFitHeight(15);
        imgView_connection.setFitWidth(15);
        lb_connection.setVisible(false);
        imgView_connection.setVisible(false);

        searchByDay.setDisable(true);
        searchByName.setDisable(true);
        totalExpensesCustomMontn.setDisable(true);
        // connectionThread = new CheckInternet();
        // connectionThread.setDaemon(true);
        // connectionThread.start();
         
    }


    public void setConnectionLabel(Boolean in){
        if (in) {
            imgView_connection.setImage(imageList.get(0));
            
        } else {
            imgView_connection.setImage(imageList.get(1));
        }
    }
    public boolean addExpense(Expense expense) {
        try {
            tableView.getItems().add(expense);
            tableView.refresh();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Path browseDirectory() {
        Path resl = null;
        try {
            Stage stage = new Stage();
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(stage);
            resl = selectedDirectory.toPath();
            return resl;
        } catch (NullPointerException e) {
            System.exit(0);
        }
        return resl;
      }
      
      private void loadTotalExpence(LocalDate date) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/totalExpense.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            TotalExpenseController controller = fxmlLoader.getController();
            controller.initData(date);
            Stage stage = new Stage();
            stage.setTitle("Estratto conto");
            stage.setResizable(false);
            stage.getIcons().add(new Image(App.class.getResourceAsStream("/img/expenses.png")));
            stage.setScene(new Scene(root));  
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
      }
}
