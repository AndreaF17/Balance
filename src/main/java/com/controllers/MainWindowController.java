package com.controllers;

import java.io.File;
import java.io.IOException;
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

    private List<Expense> filteredList;

    private static MainWindowController instance;

    public MainWindowController() {
        instance = this;
    }

    public static MainWindowController getInstance() {
        return instance;
    }

    private List<Image> imageList;

    @FXML
    private MenuItem thisMonthTransactions;

    @FXML
    private MenuItem lastMonthTransactions;

    @FXML
    private MenuItem allTransactions;
    
    @FXML
    private Label lb_filter;

    @FXML
    private ImageView imgView_connection;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TableView<Expense> tableView;

    @FXML
    private MenuItem addExpense;

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
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/customRangeDialog.fxml"));
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
    void onClick_totalExpensesLastMonth(ActionEvent event) {
        loadTotalExpence(LocalDate.now().minusMonths(1));
    }

    @FXML
    void onClick_totalExpensesCustomRange(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/customRangeDialog.fxml"));
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

    @FXML
    void onClick_thisMonthTransactions(ActionEvent event) {
        thisMonthTransactions.setDisable(true);
        lastMonthTransactions.setDisable(false);
        allTransactions.setDisable(false);

        filteredList = new ArrayList<Expense>();
        for (Expense expense : FileManager.getExpenseList()) {
            if (expense.getDate().getMonth().equals(LocalDate.now().getMonth()) && expense.getDate().getYear() == LocalDate.now().getYear()) {
                filteredList.add(expense);
            }
        }

        tableView.getItems().clear();
        tableView.getItems().addAll(filteredList);

        lb_filter.setText("Filtro: Transazioni di questo mese");

    }

    @FXML
    void onClick_lastMonthTransactions(ActionEvent event) {
        lastMonthTransactions.setDisable(true);
        thisMonthTransactions.setDisable(false);
        allTransactions.setDisable(false);

        filteredList = new ArrayList<Expense>();
        for (Expense expense : FileManager.getExpenseList()) {
            if (expense.getDate().getMonth().equals(LocalDate.now().getMonth().minus(1)) && expense.getDate().getYear() == LocalDate.now().getYear()) {
                filteredList.add(expense);
            }
        }

        tableView.getItems().clear();
        tableView.getItems().addAll(filteredList);

        lb_filter.setText("Filtro: Transazioni del mese scorso");
    }

    @FXML
    void onClick_allTransactions(ActionEvent event) {
        allTransactions.setDisable(true);
        lastMonthTransactions.setDisable(false);
        thisMonthTransactions.setDisable(false);

        tableView.getItems().clear();
        tableView.getItems().addAll(FileManager.getExpenseList());

        lb_filter.setText("Filtro: Tutte le transazioni");
        
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {    
        File jsonSettings = new File(FileManager.getAppdataDirPath() + FileManager.getCfgFileName());
        Alert alert = new Alert(AlertType.INFORMATION, "Seleziona dove salvare il file delle spese", ButtonType.OK);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        if (!jsonSettings.exists() || jsonSettings.length() == 0) {
           stage.getIcons().add(
                new Image(this.getClass().getResource("/img/expenses.png").toString()));
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                try {
                    jsonSettings.createNewFile();
                } catch (IOException e) {
                    System.out.println("File not found.");
                }
                FileManager.writeCfgFile(browseDirectory().toString());
            } else {
                System.exit(0);
            }
        } else {
            if (!(new File(FileManager.readCfgFile()).exists())) {
                stage.getIcons().add(
                        new Image(this.getClass().getResource("/img/expenses.png").toString()));
                    alert.showAndWait();
                    if (alert.getResult() == ButtonType.OK) {
                        FileManager.writeCfgFile(browseDirectory().toString());
                    } else {
                        System.exit(0);
                    }
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
        filteredList = new ArrayList<Expense>();


        for (Expense expense : FileManager.getExpenseList()) {
            if (expense.getDate().getMonth().equals(LocalDate.now().getMonth()) && expense.getDate().getYear() == LocalDate.now().getYear()) {
                filteredList.add(expense);
            }
        }

        tableView.getItems().addAll(filteredList);
        dataColumn.setSortType(TableColumn.SortType.DESCENDING);
        tableView.getSortOrder().add(dataColumn);
        tableView.sort();

        lb_filter.setText("Filtro: Transazioni di questo mese");
        thisMonthTransactions.setDisable(true);
         
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
