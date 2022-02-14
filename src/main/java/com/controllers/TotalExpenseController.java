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
public class TotalExpenseController implements Initializable {
    @FXML
    private Label label_month;

    @FXML
    private TableView<Item> tableView;

    public void initData(LocalDate date) {
        Float americanExpressTot = (float) 0;
        Float bancomatGtot = (float) 0;
        Float bancomatPtot = (float) 0;
        Float visaTot = (float) 0;
        Float cashTot = (float) 0;
        for (Expense expense : FileManager.getExpenseList()) {
            if (expense.getDate().getMonth() == date.getMonth()) {
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
            }
        }
            
        label_month.setText("Mese: " + date.toString());
            
            TableColumn<Item, TypeOfPayment> typeColumn = new TableColumn<Item, TypeOfPayment>("Tipo Pagamento");
        typeColumn.setCellValueFactory(new PropertyValueFactory<Item,TypeOfPayment>("type"));

        TableColumn<Item, Float> valueColumn = new TableColumn<Item, Float>("Totale");
            valueColumn.setCellValueFactory(new PropertyValueFactory<Item, Float>("value"));


        tableView.getColumns().removeAll();
        tableView.getColumns().addAll(typeColumn, valueColumn);

            tableView.getItems().add(new Item(TypeOfPayment.AmericanExpress, americanExpressTot));
            tableView.getItems().add(new Item(TypeOfPayment.BancomatG, bancomatGtot));
            tableView.getItems().add(new Item(TypeOfPayment.BancomatP, bancomatPtot));
            tableView.getItems().add(new Item(TypeOfPayment.Visa, visaTot));
            tableView.getItems().add(new Item(TypeOfPayment.Contante, cashTot));

            tableView.refresh();
      }

      public void initData(LocalDate dateFrom, LocalDate dateTo) {
        Float americanExpressTot = (float) 0;
        Float bancomatGtot = (float) 0;
        Float bancomatPtot = (float) 0;
        Float visaTot = (float) 0;
        Float cashTot = (float) 0;
        for (Expense expense : FileManager.getExpenseList()) {
            
            if (expense.getDate().isAfter(dateFrom.minusDays(1)) && expense.getDate().isBefore(dateTo.plusDays(1))) {

                System.out.println("Expense " + expense.getName()  + " date: " + expense.getDate());
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
            }
        }
            
        label_month.setText("Mese: da " + dateFrom.toString() + " a " + dateTo.toString());
            
            TableColumn<Item, TypeOfPayment> typeColumn = new TableColumn<Item, TypeOfPayment>("Tipo Pagamento");
        typeColumn.setCellValueFactory(new PropertyValueFactory<Item,TypeOfPayment>("type"));

        TableColumn<Item, Float> valueColumn = new TableColumn<Item, Float>("Totale");
            valueColumn.setCellValueFactory(new PropertyValueFactory<Item, Float>("value"));


        tableView.getColumns().removeAll();
        tableView.getColumns().addAll(typeColumn, valueColumn);

            tableView.getItems().add(new Item(TypeOfPayment.AmericanExpress, americanExpressTot));
            tableView.getItems().add(new Item(TypeOfPayment.BancomatG, bancomatGtot));
            tableView.getItems().add(new Item(TypeOfPayment.BancomatP, bancomatPtot));
            tableView.getItems().add(new Item(TypeOfPayment.Visa, visaTot));
            tableView.getItems().add(new Item(TypeOfPayment.Contante, cashTot));

            tableView.refresh();
      }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
       
            
    }
}
