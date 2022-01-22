package com.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import com.classes.Expense;

import org.json.JSONArray;
import org.json.JSONObject;




public class FileManager {
    public static final String SERIALIZED_FILE_NAME="ExpenseList.json";
    public static final String PATH = "src/main/resources/local/";

    private static boolean fileExist() {
        if (FileManager.class.getResource("/local/" + SERIALIZED_FILE_NAME) == null) {
            return false;
        }
        return true;
    }

    public static boolean createFile() {
        try {
            File file = new File(PATH + SERIALIZED_FILE_NAME);
            if(file.createNewFile()) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    }
    public static boolean saveExpense(Expense expense) {
        if (!fileExist()) {createFile();}
        List<Expense> list = FileManager.getExpenseList();
        while(expense.idExist(list)){
            System.out.println("FileManager: ID already used replacing with a new one");
            expense.setId(UUID.randomUUID());
        }
        JSONObject object = new JSONObject();
        object.put("id", expense.getId());
        object.put("name", expense.getName());
        object.put("date", expense.getDate());
        object.put("value", expense.getValue());
        object.put("typeOfPayment", expense.getCard());
        JSONArray array = getJsonArray();
        array.put(object);
        try {
            FileWriter myWriter = new FileWriter(PATH + SERIALIZED_FILE_NAME);
            myWriter.write(array.toString());
            myWriter.close();
            System.out.println("FileManager: Successfully wrote to the file.");
          } catch (IOException e) {
            System.out.println("FileManager: An error occurred.");
            return false;
          }
        return true;
    }

    public static boolean removeExpense(Expense expense) {
        List<Expense> list= getExpenseList();
        for (Expense e : list) {
            if (e.getId().equals(expense.getId())) {
                list.remove(e);
                break;
            }
        }
        if (setNewList(list)) {
            return true;
        }
        return false;
    }

    private static boolean setNewList(List<Expense> list) {
        JSONArray array = new JSONArray();
        for (Expense expense : list) {
            JSONObject object = new JSONObject();
            object.put("id", expense.getId());
            object.put("name", expense.getName());
            object.put("date", expense.getDate());
            object.put("value", expense.getValue());
            object.put("typeOfPayment", expense.getCard());
            array.put(object);
        }
        try {
            FileWriter myWriter = new FileWriter(PATH + SERIALIZED_FILE_NAME);
            myWriter.write(array.toString());
            myWriter.close();
            System.out.println("FileManager: Successfully wrote to the file.");
          } catch (IOException e) {
            System.out.println("FileManager: An error occurred.");
            return false;
          }
        return true;
        
    }

    public static List<Expense> getExpenseList() {
        List<Expense> resl = new ArrayList<Expense>();
        if (!fileExist()) {
            createFile();
            return resl;
        }
        JSONArray array = FileManager.getJsonArray();
        if (!array.isEmpty()) {
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                Expense expenseObject = new Expense(jsonObject.getString("id"), jsonObject.getString("date"), jsonObject.getString("name"), jsonObject.getFloat("value"), jsonObject.getString("typeOfPayment"));
                resl.add(expenseObject);
            }
        }
        return resl;
    }


    private static JSONArray getJsonArray() {
        try {
            InputStream inputStream = FileManager.class.getResourceAsStream("/local/" + SERIALIZED_FILE_NAME);
            Scanner scanner = new Scanner(inputStream);
            String buffer = "";
            while(scanner.hasNext()) {
                buffer += scanner.next();
            }
            scanner.close();
            return new JSONArray(buffer);
        } catch (NullPointerException e) {
            return new JSONArray();
        }
    }

}
