package com.classes;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import java.util.UUID;





public class Expense implements Serializable{

    private String id;
    private LocalDate date;
    private String name;
    private float value;
    private TypeOfPayment card;

    public Expense(String id, String date, String name, float value, String card) {
        this.id = id;
        this.date = LocalDate.parse(date);
        this.name = name;
        this.value = value;
        this.card = TypeOfPayment.valueOf(card);
    }
    public Expense(String id, LocalDate date, String name, float value, TypeOfPayment card) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.value = value;
        this.card = card;
    }
    public String getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id.toString();
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public float getValue() {
        return value;
    }
    public void setValue(float value) {
        this.value = value;
    }
    public TypeOfPayment getCard() {
        return card;
    }
    public void setCard(int card) {
        try {
            this.card = TypeOfPayment.values()[card];
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean idExist(List<Expense> list) {
        for (Expense expense : list) {
            if (expense.getId().equals(this.id)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Expense [id= "+ id +" card= " + card + ", date= " + date + ", name= " + name + ", value= " + Currency.getInstance("EUR").getSymbol() + value % 100 + "]";
    }


    
}
