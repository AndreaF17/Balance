package com.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.UUID;

import com.classes.Expense;
import com.classes.TypeOfPayment;
import com.util.FileManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * AddExpenseController
 */
public class AddExpenseController implements Initializable {

    @FXML
    private Button btn_add;

    @FXML
    private ChoiceBox<TypeOfPayment> cbx_type;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField tf_name;

    @FXML
    private TextField tf_value;

    @FXML
    void onClick(ActionEvent event) {
        if(!areAllFieldsEmpty()) {
            if(datePicker.getValue().isBefore(LocalDate.now().plusDays(1)))
            {
                if (isNumber(tf_value.getText())) {
                    Expense expense = new Expense(UUID.randomUUID().toString(), datePicker.getValue(), tf_name.getText(), Float.parseFloat(tf_value.getText()), cbx_type.getSelectionModel().getSelectedItem());
                    System.out.println(expense);
                    if(MainWindowController.getInstance().addExpense(expense) && FileManager.saveExpense(expense)) {
                        Node  source = (Node)  event.getSource(); 
                        Stage stage  = (Stage) source.getScene().getWindow();
                        stage.close();
                    } else {
                        System.err.println("Something went wrong!");
                    }
                } else {
                    System.out.println("Number value incorrect.");
                }
            }
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        cbx_type.getItems().addAll(
            TypeOfPayment.Visa,
            TypeOfPayment.AmericanExpress,
            TypeOfPayment.BancomatG,
            TypeOfPayment.BancomatP,
            TypeOfPayment.Contante
        );
        cbx_type.getSelectionModel().select(0);
        datePicker.setEditable(false);
        datePicker.setDayCellFactory(picker -> new DateCell(){
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isAfter(LocalDate.now()));
            }
        });
    }

    private boolean isNumber(String text){
        try {
            Float.parseFloat(text);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    

    private boolean areAllFieldsEmpty() {
        if( tf_name.getText().isEmpty() || tf_value.getText().isEmpty()) {
            return true;
        }
        try {
            datePicker.getValue().toString();
        } catch (NullPointerException e) {
            return true;
        }
        return false;
    }
}