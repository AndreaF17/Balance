package com.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.classes.Expense;
import com.classes.Item;
import com.classes.TypeOfPayment;
import com.util.FileManager;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
@SuppressWarnings("unchecked")

public class TotalExpenseController implements Initializable{

    private static final LocalDate dateNow = LocalDate.now();

    @FXML
    private Label label_month;

    @FXML
    private TableView<Item> tableView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Float americanExpressTot = (float) 0;
        Float bancomatGtot = (float) 0;
        Float bancomatPtot = (float) 0;
        Float visaTot = (float) 0;
        Float cashTot = (float) 0;
        for (Expense expense : FileManager.getExpenseList()) {
            switch (expense.getCard()) {
                case AmericanExpress:
                    americanExpressTot += expense.getValue();
                    break;
                case BancomatG:
                    bancomatGtot += expense.getValue();
                    break;
                case BancomatP:
                    bancomatPtot += expense.getValue();
                    break;
                case Visa:
                    visaTot += expense.getValue();
                    break;
                case Contante:
                    cashTot += expense.getValue();
                    break;          
                default:
                    break;
            }

        label_month.setText("Mese: " + dateNow.toString());
            
            TableColumn<Item, TypeOfPayment> typeColumn = new TableColumn<Item, TypeOfPayment>("Tipo Pagamento");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Item, Float> valueColumn = new TableColumn<Item, Float>("Totale");
            valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        tableView.getColumns().removeAll();
        tableView.getColumns().addAll(typeColumn, valueColumn);

            tableView.getItems().add(new Item(TypeOfPayment.AmericanExpress, americanExpressTot));
            tableView.getItems().add(new Item(TypeOfPayment.BancomatG, bancomatGtot));
            tableView.getItems().add(new Item(TypeOfPayment.BancomatP, bancomatPtot));
            tableView.getItems().add(new Item(TypeOfPayment.Visa, visaTot));
            tableView.getItems().add(new Item(TypeOfPayment.Contante, cashTot));

            tableView.refresh();
            
            
        }
    }
}
