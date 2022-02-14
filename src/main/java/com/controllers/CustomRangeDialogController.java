package com.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CustomRangeDialogController implements Initializable {
    
    @FXML
    private AnchorPane btn_next;

    @FXML
    private DatePicker datePickerFrom;

    @FXML
    private DatePicker datePickerTo;

    @FXML
    void onClick_btnNext(ActionEvent event) {
        if (checkFields()) {
            if (datePickerFrom.getValue().isBefore(datePickerTo.getValue())) {
                loadTotalExpence(datePickerFrom.getValue(), datePickerTo.getValue());
            }
         
        }
    }

    @FXML
    void onClick_selectedDate(ActionEvent event) {
        try {
            datePickerFrom.getValue().toString();
            datePickerTo.setDisable(false);
            datePickerTo.setDayCellFactory(picker -> new DateCell(){
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    setDisable(empty || date.isBefore(datePickerFrom.getValue().plusDays(1)));
                }
            });
        } catch (NullPointerException e) { }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        datePickerFrom.setEditable(false);
        datePickerTo.setEditable(false);

        datePickerTo.setDisable(true);

        datePickerFrom.setDayCellFactory(picker -> new DateCell(){
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isAfter(LocalDate.now()));
            }
        });

        
        
    }

    boolean checkFields() {
        try {
            datePickerFrom.getValue().toString();
            datePickerTo.getValue().toString();
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }
    
    private void loadTotalExpence(LocalDate dateFrom, LocalDate dateTo) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/totalExpense.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            TotalExpenseController controller = fxmlLoader.getController();
            controller.initData(dateFrom, dateTo);
            Stage stage = new Stage();
            stage.setTitle("Estratto conto");
            stage.setResizable(false);
            stage.getIcons().add(new Image(App.class.getResourceAsStream("/img/expenses.png")));
            stage.setScene(new Scene(root));  
            stage.show();
            
        ((Stage) btn_next.getScene().getWindow()).close();;
        } catch(Exception e) {
            e.printStackTrace();
        }
      }
}
