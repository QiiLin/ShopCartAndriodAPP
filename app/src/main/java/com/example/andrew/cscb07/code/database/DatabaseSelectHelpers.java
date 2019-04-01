package com.example.andrew.cscb07.code.database;

import com.example.andrew.cscb07.code.exceptions.InvalidIdException;
import com.example.andrew.cscb07.code.com.users.Account;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.andrew.cscb07.code.com.users.AccountImpl;
import com.example.andrew.cscb07.code.com.users.Admin;
import com.example.andrew.cscb07.code.com.users.Customer;
import com.example.andrew.cscb07.code.com.users.Employee;
import com.example.andrew.cscb07.code.exceptions.InvalidItemNameException;
import com.example.andrew.cscb07.code.inventory.Inventory;
import com.example.andrew.cscb07.code.inventory.InventoryImpl;
import com.example.andrew.cscb07.code.inventory.ItemImpl;
import com.example.andrew.cscb07.code.com.users.Roles;
import com.example.andrew.cscb07.code.store.Sale;
import com.example.andrew.cscb07.code.store.SaleImpl;
import com.example.andrew.cscb07.code.store.SalesLog;
import com.example.andrew.cscb07.code.store.SalesLogImpl;
import com.example.andrew.cscb07.code.store.ShoppingCart;
import com.example.andrew.cscb07.code.com.users.User;
import com.example.andrew.cscb07.code.inventory.Item;
import com.example.andrew.cscb07.code.exceptions.InvalidItemException;

/**
 * Created by LTJ on 2017/11/28.
 */

public class DatabaseSelectHelpers {

    public static List<Integer> getRoleIds(Context context){
        DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
        Cursor cursor = mydb.getRoles();
        List<Integer> roleIdList = new ArrayList<>();
        while (cursor.moveToNext()){
            int currRoleId = cursor.getInt(cursor.getColumnIndex("ID"));
            roleIdList.add(currRoleId);
        }
        cursor.close();
        return roleIdList;
    }

    public static String getRoleName(int id, Context context) throws InvalidIdException {
        if (roleIdCheck(id, context)) {
            DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
            String roleName = mydb.getRole(id);
            return roleName;
        } else {
            throw new InvalidIdException();
        }
    }

    public static int getUserRoleId(int userId, Context context) throws InvalidIdException {
        if (isValidUserId(userId, context)) {
            DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
            return mydb.getUserRole(userId);
        } else {
            throw new InvalidIdException();
        }
    }

    public static List<Integer> getUserIdsByRole(int roleId, Context context) throws InvalidIdException {
        if (roleIdCheck(roleId, context)) {
            DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
            List<Integer> userIdList = new ArrayList<>();
            Cursor cursor = mydb.getUsersByRole(roleId);
            while (cursor.moveToNext()) {
                int currUserId = cursor.getInt(cursor.getColumnIndex("USERID"));
                userIdList.add(currUserId);
            }
            cursor.close();
            return userIdList;
        } else {
            throw new InvalidIdException();
        }
    }

    public static User getUserDetails(int userId, Context context) throws InvalidIdException {
        if (isValidUserId(userId, context)) {
            DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
            Cursor cursor = mydb.getUserDetails(userId);
            int userRoleId = getUserRoleId(userId, context);
            String roleName = getRoleName(userRoleId, context);
            if(cursor != null && cursor.moveToFirst()) {
                int userAge = cursor.getInt(cursor.getColumnIndex("AGE"));
                String userName = cursor.getString(cursor.getColumnIndex("NAME"));
                String userAddress = cursor.getString(cursor.getColumnIndex("ADDRESS"));
                User currentUser;
                if (roleName.equals(Roles.ADMIN.name())) {
                    currentUser = new Admin(userId, userName, userAge, userAddress);
                } else if (roleName.equals(Roles.EMPLOYEE.name())) {
                    currentUser = new Employee(userId, userName, userAge, userAddress);
                } else if (roleName.equals(Roles.CUSTOMER.name()))
                    currentUser = new Customer(userId, userName, userAge, userAddress);
                else {
                    currentUser = null;
                }
                cursor.close();

                return currentUser;
            }
        } else {
            throw new InvalidIdException();
        }
        return null;
    }

    public static List<User> getUsersDetails(Context context) throws InvalidIdException {
        DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
        List<User> usersList = new ArrayList<>();
        Cursor cursor = mydb.getUsersDetails();
        while (cursor.moveToNext()){
            int userId = cursor.getInt(cursor.getColumnIndex("ID"));
            User currentUser = getUserDetails(userId, context);
            usersList.add(currentUser);
        }
        cursor.close();
        return usersList;
    }

    public static String getPassword(int userId, Context context) throws InvalidIdException {
        if (isValidUserId(userId, context)) {
            DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
            return mydb.getPassword(userId);
        } else {
            throw new InvalidIdException();
        }
    }

    public static Item getItem(int itemId, Context context) throws InvalidIdException {
        if (itemIdCheck(itemId, context)) {
            DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
            Cursor cursor = mydb.getItem(itemId);
            Item currentItem = new ItemImpl();
            if(cursor != null && cursor.moveToFirst()){
                String itemName = cursor.getString(cursor.getColumnIndex("NAME"));
                BigDecimal itemPrice = new BigDecimal(cursor.getString(cursor.getColumnIndex("PRICE")));
                currentItem.setId(itemId);
                currentItem.setName(itemName);
                currentItem.setPrice(itemPrice);
                cursor.close();
            }
            return currentItem;
        } else {
            throw new InvalidIdException();
        }
    }

    public static List<Item> getAllItems(Context context) throws InvalidIdException {
        DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
        Cursor cursor = mydb.getAllItems();
        List<Item> itemList = new ArrayList<>();
        while (cursor.moveToNext()){
            int itemId = cursor.getInt(cursor.getColumnIndex("ID"));
            Item currItem = getItem(itemId, context);
            itemList.add(currItem);
        }
        cursor.close();
        return itemList;
    }

    public static int getInventoryQuantity(int itemId, Context context) throws InvalidIdException {
        if (itemIdCheck(itemId, context)) {
            DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
            return mydb.getInventoryQuantity(itemId);
        } else {
            throw new InvalidIdException();
        }
    }

    public static Inventory getInventory(Context context) throws InvalidIdException {
        DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
        Cursor cursor = mydb.getInventory();
        Inventory storeInventory = new InventoryImpl();
        HashMap<Item, Integer> itemMap = new HashMap<>();
        storeInventory.setItemMap(itemMap);
        int totalItems = 0;
        storeInventory.setTotalItems(totalItems);
        while (cursor.moveToNext()){
            int itemId = cursor.getInt(cursor.getColumnIndex("ITEMID"));
            int currentItemQuantity = getInventoryQuantity(itemId, context);
            Item currentItem = getItem(itemId, context);
            storeInventory.updateMap(currentItem, currentItemQuantity);
            totalItems += currentItemQuantity;
        }
        storeInventory.setTotalItems(totalItems);
        cursor.close();
        return storeInventory;
    }

    public static SalesLog getSales(Context context) throws InvalidIdException {
        SalesLog salesLog = new SalesLogImpl();
        getItemizedSales(salesLog, context);
        return salesLog;
    }

    public static void getItemizedSaleById(int saleId, Sale sale, Context context) throws InvalidIdException {
        if (salesIdCheck(saleId, context)) {
            DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
            Cursor cursor = mydb.getItemizedSaleById(saleId);
            HashMap<Item, Integer> items = new HashMap<>();
            BigDecimal totalPrice = new BigDecimal(0);
            while (cursor.moveToNext()) {
                int itemId = cursor.getInt(cursor.getColumnIndex("ITEMID"));
                Item currItem = getItem(itemId, context);
                int saleQuantity = cursor.getInt(cursor.getColumnIndex("QUANTITY"));
                items.put(currItem, saleQuantity);
                BigDecimal currentQuantity = new BigDecimal(saleQuantity);
                BigDecimal itemPrice = currItem.getPrice();
                BigDecimal currentTotalPrice = currentQuantity.multiply(itemPrice);
                totalPrice = totalPrice.add(currentTotalPrice);
            }
            sale.setId(saleId);
            sale.setItemMap(items);
            sale.setTotalPrice(totalPrice);
            cursor.close();
        } else {
            throw new InvalidIdException();
        }
    }

    public static void getItemizedSales(SalesLog salesLog, Context context) throws InvalidIdException {
        DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
        Cursor cursor = mydb.getItemizedSales();
        int numberOfsale = 0;
        BigDecimal totalSale = new BigDecimal(0);
        List<Sale> saleList = new ArrayList<>();
        while (cursor.moveToNext()) {
            int saleId = cursor.getInt(cursor.getColumnIndex("SALEID"));
            Sale currentSale = getSaleById(saleId, context);
            salesLog.updateSaleItemMap(currentSale, currentSale.getItemMap());
            salesLog.updateSalePriceMap(currentSale, currentSale.getTotalPrice());
            numberOfsale = numberOfsale + 1;
            totalSale = totalSale.add(currentSale.getTotalPrice());
            saleList.add(currentSale);
        }
        salesLog.setTotalSales(totalSale);
        salesLog.setTotalNumberOfSale(numberOfsale);
        salesLog.setSaleList(saleList);
        cursor.close();
    }
    public static List<Sale> getSalesToUser(int userId, Context context) throws InvalidIdException {
        if (isValidUserId(userId, context)) {
            DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
            Cursor cursor = mydb.getSalesToUser(userId);
            List<Sale> sales = new ArrayList<>();
            while (cursor.moveToNext()) {
                int saleId = cursor.getInt(cursor.getColumnIndex("ID"));
                Sale currSale = getSaleById(saleId, context);
                sales.add(currSale);
            }
            cursor.close();
            return sales;
        } else {
            throw new InvalidIdException();
        }
    }

    public static Sale getSaleById(int saleId, Context context) throws InvalidIdException {
        if (salesIdCheck(saleId, context)) {
            DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
            Cursor cursor = mydb.getSaleById(saleId);
            Sale sale = new SaleImpl();
            int saleUserId = cursor.getInt(cursor.getColumnIndex("USERID"));
            User saleUser = getUserDetails(saleUserId, context);
            sale.setUser(saleUser);
            getItemizedSaleById(saleId, sale, context);
            cursor.close();
            return sale;
        } else {
            throw new InvalidIdException();
        }
    }

    public static Account getAccountDetails(int accountId, Context context) throws InvalidIdException, InvalidItemException {
        if (accountIdCheck(accountId, context)) {
            DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
            Account account = new AccountImpl();
            Cursor cursor = mydb.getAccountDetails(accountId);
            account.setId(accountId);
            ShoppingCart shoppingCart = new ShoppingCart();
            if(cursor != null && cursor.moveToFirst()) {
                int itemId = cursor.getInt(cursor.getColumnIndex("ITEMID"));
                int quantity = cursor.getInt(cursor.getColumnIndex("QUANTITY"));
                Item item = getItem(itemId, context);
                shoppingCart.addItem(item, quantity);
                account.setShoppingCart(shoppingCart);
            }
            cursor.close();
            return account;
        } else {
            throw new InvalidIdException();
        }
    }

    public static List<Account> getUserAccounts(int userId, Context context) throws InvalidIdException, InvalidItemException {
        List<Account> userAccounts = new ArrayList<Account>();
        if (isValidUserId(userId, context)) {
            List<Account> userActiveAccounts = getUserActiveAccounts(userId, context);
            userActiveAccounts.addAll(getUserInactiveAccounts(userId, context));
            userAccounts = userActiveAccounts;
            return userAccounts;
        } else {
            throw new InvalidIdException();
        }
    }

    public static List<Account> getUserActiveAccounts(int userId, Context context) throws InvalidIdException, InvalidItemException {
        if (isValidUserId(userId, context)) {
            DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
            Cursor cursor = mydb.getUserActiveAccounts(userId);
            List<Account> userActiveAccounts = new ArrayList<>();
            while (cursor.moveToNext()) {
                int accountId = cursor.getInt(cursor.getColumnIndex("ID"));
                Account currUserAccount = getAccountDetails(accountId, context);
                User user = getUserDetails(userId, context);
                currUserAccount.setUser(user);
                currUserAccount.setUserId(userId);
                currUserAccount.setActive(true);
                userActiveAccounts.add(currUserAccount);
            }
            cursor.close();
            return userActiveAccounts;
        } else {
            throw new InvalidIdException();
        }
    }

    public static List<Account> getUserInactiveAccounts(int userId, Context context) throws InvalidIdException, InvalidItemException{
        if (isValidUserId(userId, context)) {
            DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
            Cursor cursor = mydb.getUserInactiveAccounts(userId);
            List<Account> userInactiveAccounts = new ArrayList<>();
            while (cursor.moveToNext()) {
                int accountId = cursor.getInt(cursor.getColumnIndex("ID"));
                Account currUserAccount = getAccountDetails(accountId, context);
                User user = getUserDetails(userId, context);
                currUserAccount.setUser(user);
                currUserAccount.setUserId(userId);
                currUserAccount.setActive(false);
                userInactiveAccounts.add(currUserAccount);
            }
            cursor.close();
            return userInactiveAccounts;
        } else {
            throw new InvalidIdException();
        }
    }

    public static Bitmap getUserImage(int userId, Context context) throws InvalidIdException {
        if (isValidUserId(userId, context)){
            DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
            byte[] image = mydb.getUserImage(userId);
            return BitmapFactory.decodeByteArray(image, 0, image.length);
        } else {
            throw new InvalidIdException();
        }
    }

    public static Bitmap getItemImage(int itemId, Context context) throws InvalidIdException {
        if (itemIdCheck(itemId, context)){
            DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
            byte[] image = mydb.getItemImage(itemId);
            return BitmapFactory.decodeByteArray(image, 0, image.length);
        } else {
            throw new InvalidIdException();
        }
    }

    private static boolean roleIdCheck(int roleId, Context context) {
        List<Integer> ids = getRoleIds(context);
        return ids.contains(roleId);
    }

    private static boolean isValidUserId(int userId, Context context) {
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

    private static boolean itemIdCheck(int itemId, Context context) {
        DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
        Cursor cursor = mydb.getAllItems();
        List<Integer> itemIds = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("ID"));
            itemIds.add(id);
        }
        cursor.close();
        for (Integer id : itemIds) {
            if (id == itemId) {
                return true;
            }
        }
        return false;
    }

    private static boolean salesIdCheck(int saleId, Context context) {
        try {
            List<User> listOfUsers = getUsersDetails(context);
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


    public static List<Account> getAllaccounts(Context context) throws  InvalidIdException, InvalidItemException {
        List<Account> accountList = new ArrayList<Account>();
        DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
        Cursor cursor = mydb.getAllAccount();
        while (cursor.moveToNext()) {
            int accountId = cursor.getInt(cursor.getColumnIndex("ID"));
            int userId = cursor.getInt(cursor.getColumnIndex("USERID"));
            int activeValue = cursor.getInt(cursor.getColumnIndex("ACTIVE"));
            int approveValue = cursor.getInt(cursor.getColumnIndex("APPROVE"));
            Account account = getAccountDetails(accountId, context);
            account.setUserId(userId);
            if (approveValue == 0) {
                account.setApprove(false);
            } else {
                account.setApprove(true);
            }
            if (activeValue == 0) {
                account.setActive(false);
            } else {
                account.setActive(true);
            }
            accountList.add(account);
        }
        cursor.close();
        return accountList;
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


    public static List<Account> getUserApproveAccounts(int userId, Context context)
            throws InvalidIdException, InvalidItemException {
        List<Account> accountList = new ArrayList<Account>();
        DatabaseDriverAndroid mydb = new DatabaseDriverAndroid(context);
        Cursor cursor = mydb.getUserApproveAccounts(userId, context);
        while (cursor.moveToNext()) {
            int accountId = cursor.getInt(cursor.getColumnIndex("ID"));
            int activeValue = cursor.getInt(cursor.getColumnIndex("ACTIVE"));
            int approveValue = cursor.getInt(cursor.getColumnIndex("APPROVE"));
            Account account = getAccountDetails(accountId, context);
            account.setUserId(userId);
            if (approveValue == 0) {
                account.setApprove(false);
            } else {
                account.setApprove(true);
            }
            if (activeValue == 0) {
                account.setActive(false);
            } else {
                account.setActive(true);
            }
            accountList.add(account);
        }
        cursor.close();
        return accountList;
    }
}
