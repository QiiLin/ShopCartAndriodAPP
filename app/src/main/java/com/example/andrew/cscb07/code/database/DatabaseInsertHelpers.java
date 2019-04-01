package com.example.andrew.cscb07.code.database;

import android.content.Context;
import android.database.Cursor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


import com.example.andrew.cscb07.code.exceptions.InvalidAddressExcetpion;
import com.example.andrew.cscb07.code.exceptions.InvalidAgeException;
import com.example.andrew.cscb07.code.exceptions.InvalidIdException;
import com.example.andrew.cscb07.code.exceptions.InvalidItemException;
import com.example.andrew.cscb07.code.exceptions.InvalidItemNameException;
import com.example.andrew.cscb07.code.exceptions.InvalidItemQuantityException;
import com.example.andrew.cscb07.code.exceptions.InvalidTotalPriceException;
import com.example.andrew.cscb07.code.exceptions.InvalidUserNameException;
import com.example.andrew.cscb07.code.com.users.Account;
import com.example.andrew.cscb07.code.com.users.Roles;
import com.example.andrew.cscb07.code.inventory.Item;
import com.example.andrew.cscb07.code.store.Sale;
import com.example.andrew.cscb07.code.com.users.User;

/**
 * Created by LTJ on 2017/11/28.
 */

public class DatabaseInsertHelpers {

    public static int insertRole(String name, Context context) throws InvalidIdException {
        if (isInRoles(name)) {
            DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
            return (int) mydb.insertRole(name);
        } else{
            throw new InvalidIdException();
        }
    }

    public static int insertNewUser(String name, int age, String address, String password, Context context) throws InvalidUserNameException, InvalidAddressExcetpion, InvalidAgeException {
        if (ageCheck(age)) {
              if(addressLimitCheck(address)) {
                  if(userNameFormatCheck(name)) {
                      DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
                      return (int) mydb.insertNewUser(name, age, address, password);
                  } else{
                      throw new InvalidUserNameException();
                  }
              } else {
                  throw new InvalidAddressExcetpion();
              }
        } else{
            throw new InvalidAgeException();
        }
    }

    public static int insertUserRole(int userId, int roleId, Context context) throws InvalidIdException {
        if (roleIdCheck(roleId, context)) {
            if (isValidUserId(userId, context)) {
                DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
                return (int) mydb.insertUserRole(userId, roleId);
            } else {
                throw new InvalidIdException();
            }
        } else throw new InvalidIdException();
    }

    public static int insertItem(String name, BigDecimal price, Context context) throws InvalidItemException, InvalidItemNameException {
        if (validItemPriceCheck(price)) {
            if (checkValidItemName(name, context)) {
                DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
                return (int) mydb.insertItem(name, price);
            } else {
                throw new InvalidItemNameException();
            }
        } else {
            throw new InvalidItemException();
        }
    }

    public static int insertInventory(int itemId, int quantity, Context context) throws InvalidIdException, InvalidItemException {
        if (itemQuantityCheck(quantity)) {
            if (itemIdCheck(itemId, context)) {
                DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
                return (int) mydb.insertInventory(itemId, quantity);
            } else {
                throw new InvalidIdException();
            }
        } else {
            throw new InvalidItemException();
        }
    }

    public static int insertSale(int userId, BigDecimal totalPrice, Context context) throws InvalidIdException, InvalidTotalPriceException {
        if (totalPriceCheck(totalPrice)) {
            if (isValidUserId(userId, context)) {
                DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
                return (int) mydb.insertSale(userId, totalPrice);
            } else {
                throw new InvalidIdException();
            }
        } else {
            throw new InvalidTotalPriceException();
        }
    }

    public static int insertItemizedSale(int saleId, int itemId, int quantity, Context context) throws InvalidIdException, InvalidItemException {
        if (itemSaleQuantityCheck(quantity)) {
            if (itemIdCheck(itemId, context)){
                if (salesIdCheck(saleId, context)) {
                    DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
                    return (int) mydb.insertItemizedSale(saleId, itemId, quantity);
                } else {
                    throw new InvalidIdException();
                }
            } else {
                throw new InvalidIdException();
            }
        } else {
            throw new InvalidItemException();
        }
    }

    public static int insertAccount(int userId, boolean active,boolean approve, Context context) throws InvalidIdException {
        if (isValidUserId(userId, context)) {
            DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
            return (int) mydb.insertAccount(userId, active, approve);
        } else {
            throw new InvalidIdException();
        }
    }

    public static int insertAccountLine(int accountId, int itemId, int quantity, Context context) throws InvalidItemQuantityException, InvalidIdException, InvalidItemException {
        boolean accountIdJudge = accountIdCheck(accountId, context);
        boolean itemIdJudge = itemIdCheck(itemId, context);
        boolean quantityJudge = false;
        if (itemIdJudge) {
            int itemQuantity = DatabaseSelectHelpers.getInventoryQuantity(itemId, context);
            if (itemQuantity >= quantity) {
                quantityJudge = true;
            }
        }
        if (accountIdJudge && itemIdJudge && quantityJudge) {
            DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
            return (int) mydb.insertAccountLine(accountId, itemId, quantity);
        } else if (!accountIdJudge || !itemIdJudge) {
            throw new InvalidIdException();
        } else {
            throw new InvalidItemQuantityException();
        }
    }

    public static int insertUserImage(int userId, byte[] image, Context context)
            throws InvalidIdException {
        if (isValidUserId(userId, context)){
            DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
            return (int) mydb.insertUserPics(userId, image);
        } else {
            throw new InvalidIdException();
        }
    }

    public static int insertItemImage(int itemId, byte[] image, Context context)
            throws InvalidIdException {
        if (itemIdCheck(itemId, context)){
            DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
            return (int) mydb.insertItemPics(itemId, image);
        } else {
            throw new InvalidIdException();
        }
    }

    private static boolean checkValidItemName(String name, Context context){
        try {
            List<Item> itemList = DatabaseSelectHelpers.getAllItems(context);
            List<String> itemNames = new ArrayList<>();
            for (Item item : itemList){
                itemNames.add(item.getName());
            }
            return !itemNames.contains(name);
        } catch (InvalidIdException e) {
            return false;
        }

    }

    private static boolean isInRoles(String name) {
        // if name is not null
        if (name != null) {
            // loop through a Roles values once
            for (Roles currentRole : Roles.values()) {
                // if the name equals to the currentRole.
                if (currentRole.name().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean addressLimitCheck(String address) {
        boolean withInLimit = false;
        // if address is not null
        if (address != null) {
            // if address is less than 101
            if (address.length() <= 100) {
                withInLimit = true;
            }
        }
        return withInLimit;
    }

    private static boolean totalPriceCheck(BigDecimal totalPrice) {
        BigDecimal zero = new BigDecimal(0);
        return totalPrice.compareTo(zero) != -1;
    }

    private static boolean userNameFormatCheck(String userName) {
        if (userName == null) {
            return false;
        }
        try {
            // splite userName into Array of String that contains all the word in it
            String[] splitedName = userName.split("\\s+");
            // By add up the first index and second index with a space between it, we form a good formatName
            String goodFormatNameCopy = splitedName[0] + " " + splitedName[1];
            // if there are only 2 word in userName and goodCopy version is the same as userName
            if (splitedName.length == 2 && goodFormatNameCopy.equals(userName)) {
                // loop through each char in each word of userName
                for (String currentWord : splitedName) {
                    char firstCharacter = currentWord.charAt(0);
                    // if the firstCharacter is upper case and is alphabetic
                    if (Character.isUpperCase(firstCharacter) && Character.isAlphabetic(firstCharacter)) {
                        // we get sub string of it from index 1
                        char[] charList = currentWord.substring(1).toCharArray();
                        // loop through all elelemnt in the charlIst
                        for (char currentChar : charList) {
                            // if it is not lowcase or it is not alphabetic
                            if (Character.isUpperCase(currentChar) || !(Character.isAlphabetic(currentChar))) {
                                return false;
                            }
                        }
                    } else {
                        return false;
                    }
                }
                return true;
            }
            return false;
        } catch ( java.lang.ArrayIndexOutOfBoundsException e){
            return false;
        }
    }

    private static boolean ageCheck(int age) {
        boolean valid = false;
        if (age > 0) {
            valid = true;
        }
        return valid;

    }

    private static boolean roleIdCheck(int roleId, Context context){
        List<Integer> roleIds = DatabaseSelectHelpers.getRoleIds(context);
        return roleIds.contains(roleId);
    }

    private static boolean isValidUserId(int userId, Context context){
        DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
        Cursor cursor = mydb.getUsersDetails();
        List<Integer> userIds = new ArrayList<>();
        while (cursor.moveToNext()) {
            int currUserId = cursor.getInt(cursor.getColumnIndex("ID"));
            userIds.add(currUserId);
        }
        cursor.close();
        for (int id : userIds) {
            if (id == userId) {
                return true;
            }
        }
        return false;
    }

    private static boolean validItemPriceCheck(BigDecimal price) {
        boolean itemNameCheck = false;
        if (price != null) {
            BigDecimal bottomValue = new BigDecimal(0);
            if (price.compareTo(bottomValue) == 1) {
                itemNameCheck = true;
            }
        }
        return itemNameCheck;
    }

    private static boolean itemQuantityCheck(int quantity) {
        boolean valid = false;
        if (quantity >= 0) {
            valid = true;
        }
        return valid;
    }

    private static boolean itemIdCheck(int itemId, Context context) {
        DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
        Cursor cursor = mydb.getAllItems();
        List<Integer> items = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("ID"));
            items.add(id);
        }
        cursor.close();
        for (Integer item : items) {
            if (item == itemId) {
                return true;
            }
        }
        return false;
    }

    private static boolean itemSaleQuantityCheck(int quantity) {
        boolean valid = false;
        if (quantity > 0) {
            valid = true;
        }
        return valid;
    }

    private static boolean salesIdCheck(int saleId, Context context) {
        try {
            List<User> listOfUsers = DatabaseSelectHelpers.getUsersDetails(context);
            List<Integer> sales = new ArrayList<>();
            for (User user : listOfUsers) {
                sales.addAll(getSalesListToUser(user.getId(), context));
            }
            if (sales.contains(saleId)){
                return true;
            }
        } catch (InvalidIdException e) {
            return false;
        }
        return false;
    }


    private static List<Integer> getSalesListToUser(int userId, Context context) throws InvalidIdException {
        if (isValidUserId(userId, context)) {
            DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
            Cursor cursor = mydb.getSalesToUser(userId);
            List<Integer> sales = new ArrayList<>();
            while (cursor.moveToNext()) {
                int saleId = cursor.getInt(cursor.getColumnIndex("ID"));
                sales.add(saleId);
            }
            cursor.close();
            return sales;
        } else {
            throw new InvalidIdException();
        }
    }

    public static List<Integer> getAllaccountId(Context context) throws  InvalidIdException, InvalidItemException {
        List<Integer> accountList = new ArrayList<Integer>();
        DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
        Cursor cursor = mydb.getAllAccount();
        while (cursor.moveToNext()) {
            int accountId = cursor.getInt(cursor.getColumnIndex("ID"));
            accountList.add(accountId);
        }
        cursor.close();
        return accountList;
    }
    private static boolean accountIdCheck(int accountId, Context context) throws InvalidItemException {
        try {
            List<Integer> currAccounts;
            currAccounts = DatabaseSelectHelpers.getAllaccountId(context);
            if (currAccounts.contains(accountId)){
                return true;
            }
        } catch(InvalidIdException e){
            return false;
        }
        return false;
    }
}
