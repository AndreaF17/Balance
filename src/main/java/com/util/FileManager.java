package com.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;


import com.classes.Expense;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;




public class FileManager {

    private static final String CFG_FILE_NAME = "\\userSettings.cfg";
    private static final String FILE_NAME = "ExpenseList.json";

    private static final String CREDENTIALS_APP_NAME = "Balance";
    private static final String CREDENTIALS_AUTHOR = "Ferrario Andrea";
    private static final String CREDENTIALS_VERSION = "v1.0";

    public static String getCfgFileName() {
        return CFG_FILE_NAME;
    }

    public static String getAppdataDirPath() {
        File file = new File(System.getenv("LOCALAPPDATA")+"\\Ferrario Andrea\\Balance\\v1.0");
        if (!file.exists()) {
            if(file.mkdirs()){
                System.out.println("FileManager: Appdata folders created succesfully.");
            } else {
                System.out.println("FileManager: Appdata folders creation error.");
            }
        } else {
            System.out.println("FileManager: Appdata folders already created.");
        }
        AppDirs appDirs = AppDirsFactory.getInstance();
        return appDirs.getUserDataDir(CREDENTIALS_APP_NAME, CREDENTIALS_VERSION, CREDENTIALS_AUTHOR);
    }

    public static boolean writeCfgFile(String path) {
        try {
            File myObj = new File(getAppdataDirPath() + CFG_FILE_NAME);
            if (myObj.createNewFile()) {
              System.out.println("FileManager: File created: " + myObj.getName());
            } else {
              System.out.println("FileManager: File userSettings.cfg already exists.");
            }
            FileWriter myWriter = new FileWriter(myObj);
            myWriter.write(path);
            myWriter.close();
            System.out.println("FileManager: Successfully worte on userSettings.cfg");
            return true;
          } catch (IOException e) {
            System.out.println("FileManager: An error occurred.");
            e.printStackTrace();
            return false;
          }
        }
    public static String readCfgFile() {
        String data = null;
        try {
            File myObj = new File(getAppdataDirPath() + CFG_FILE_NAME);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
               data = myReader.nextLine();
            }
            myReader.close();
          } catch (FileNotFoundException e) {
            System.out.println("FileManager: An error occurred.");
            e.printStackTrace();
          }
          System.out.println("FileManager: Read - " + data);
          return data;
        }
    public static String getFilePath() {
        return readCfgFile() + "\\" + FILE_NAME; 
    }

    public static boolean createFile() {
        File file = new File(Path.of(getFilePath()).toUri());
        if (file.exists()) {
            System.out.println("FileManager: File already exist.");
        } else {
            try {
                if(file.createNewFile()) {
                    System.out.println("FileManager: "+ FILE_NAME +" crated.");
                } else {
                    System.out.println("FileManager: "+ FILE_NAME +" already created.");
                }
            } catch (IOException e) {
                System.out.println("FileManager: Error during cration of file: " + FILE_NAME);
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public static boolean writeFile(String string) {
        if(createFile()) {
            try {
                File file = new File(getFilePath());
                FileWriter writer = new FileWriter(file);
                writer.write(string);
                writer.close();
                System.out.println("FileManager: "+ FILE_NAME +" wrote successfully.");
            } catch (IOException e) {
                System.out.println("FileManager: Writing error for "+ FILE_NAME +".");
                return false;
            }
            return true;
        }
        return false;
    }
    
    public static String readFile() {
        String data = null;
        try {
            File myObj = new File(getFilePath());
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
               data = myReader.nextLine();
            }
            myReader.close();
          } catch (FileNotFoundException e) {
            createFile();
          }
          System.out.println("FileManager: Read - " + data);
          return data;
    }

    private static JSONArray getJsonArray() {
        String fileContent = readFile();
        if (fileContent != null) {
            JSONArray array = new JSONArray(fileContent);
            return array;
        } else {
            return new JSONArray();
        }
    }

    public static List<Expense> getExpenseList() {
        List<Expense> resl = new ArrayList<Expense>();
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

    public static boolean saveExpense(Expense expense) {
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
        if (writeFile(array.toString())) {
            return true;
        } else {
            return false;
        }
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
        if (writeFile(array.toString())) {
            return true;
        } else {
            return false;
        }
    }

}
