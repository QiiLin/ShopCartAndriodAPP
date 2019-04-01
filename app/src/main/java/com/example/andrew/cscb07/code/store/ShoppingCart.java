package com.example.andrew.cscb07.code.store;

/**
 * Created by LTJ on 2017/11/28.
 */

import android.content.Context;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


import com.example.andrew.cscb07.code.com.users.Customer;
import com.example.andrew.cscb07.code.inventory.Item;
import com.example.andrew.cscb07.code.database.DatabaseInsertHelpers;
import com.example.andrew.cscb07.code.database.DatabaseSelectHelpers;
import com.example.andrew.cscb07.code.database.DatabaseUpdateHelpers;
import com.example.andrew.cscb07.code.exceptions.InvalidIdException;
import com.example.andrew.cscb07.code.exceptions.InvalidItemException;
import com.example.andrew.cscb07.code.exceptions.InvalidItemQuantityException;
import com.example.andrew.cscb07.code.exceptions.InvalidTotalPriceException;

public class ShoppingCart implements Serializable {
    private static final long serialVersionUID = -3333881851467856011L;
    private HashMap<Item, Integer> items = new HashMap<Item, Integer>();
    private transient Customer customer;
    private transient BigDecimal total = new BigDecimal(0);
    private transient final BigDecimal TAXRATE = new BigDecimal(1.13);

    /**
     * Create a instance of ShoppingCartobjec with a input customer.
     * @param customer inpputed customer
     */
    public ShoppingCart(Customer customer) {
        this.customer = customer;
    }

    public ShoppingCart() {

    }
    /**
     * Method add the quantity of item to the items list. Update the total accordingly.
     * @param item the item need to be add
     * @param quantity the quantity of item that need to be add
     * @throws InvalidItemException throw if item are invalid
     * @throws InvalidItemQuantityException throw if quantity are negative
     */
    public void addItem(Item item, int quantity)
            throws InvalidIdException, InvalidItemException {
        if (quantity > 0) {
            if (item != null) {
                if (this.items.containsKey(item)) {
                    int currentQuantity = this.items.get(item);
                    int newQuantity = currentQuantity + quantity;
                    this.items.put(item, newQuantity);
                } else {
                    this.items.put(item, quantity);
                }
                BigDecimal quantityOfItem = new BigDecimal(quantity);
                BigDecimal totalPrice = item.getPrice().multiply(quantityOfItem);
                this.total = total.add(totalPrice);
            } else {
                throw new InvalidIdException();
            }
        } else {
            throw new InvalidItemException();
        }
    }

    public int getItemQuantity(Item item) {
        return this.items.get(item);
    }

    /**
     * this Remove the quantity given of the item from items. If the number
     * becomes zero, remove itentirely from the items list. Update the totalaccordingly
     * @param item item that need to be remove
     * @param quantity quanitu that need to be add
     * @throws InvalidItemQuantityException throw if needed
     * @throws InvalidIdException throw if needed
     * @throws InvalidItemException throw if needed
     */
    public void removeItem(Item item, int quantity) throws InvalidItemException, InvalidIdException, InvalidItemQuantityException {
        if (quantity > 0) {
            if (item != null && getItems().contains(item)) {
                Item rightOne = null;
                for (Item currentItem: this.items.keySet()) {
                    if (currentItem.getId() == item.getId()) {
                        rightOne = currentItem;
                    }
                }
                int currentQuantity = this.items.get(rightOne);
                if (currentQuantity >= quantity) {
                    if (currentQuantity == quantity) {
                        this.items.remove(rightOne);
                    } else {
                        int newQuantity = currentQuantity - quantity;
                        this.items.put(rightOne, newQuantity);
                    }
                    BigDecimal quantityOfItem = new BigDecimal(quantity);
                    BigDecimal totalPrice = rightOne.getPrice().multiply(quantityOfItem);
                    this.total = total.add(totalPrice);
                } else {
                    throw new InvalidItemQuantityException();
                }
            } else {
                throw new InvalidItemException();
            }
        } else {
            throw new InvalidItemException();
        }
    }

    public List<Item> getItems() {
        List<Item> itemList = new ArrayList<Item>(this.items.keySet());
        return itemList;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    /**
     * Method to get total of cart.
     * @return int of total value of cart
     */
    public BigDecimal getTotal() {
        this.total = new BigDecimal(0);
        List<Item> allItem = this.getItems();
        for (Item item : allItem) {
            BigDecimal itemPrice = item.getPrice();
            BigDecimal itemQuantity = new BigDecimal(this.items.get(item));
            BigDecimal currentItemTotalPrice = itemPrice.multiply(itemQuantity);
            this.total = total.add(currentItemTotalPrice);
        }
        return this.total;
    }

    public BigDecimal getTaxRate() {
        return this.TAXRATE;
    }

    /**
     * this method check the Take in a shopping cart, validate it has an
     * associated customer. If it does, calculate the
     * total after tax, and submit the purchase to the
     * database. If there are enough of each requested
     * item in inventory, update the required tables,
     * and clear the shopping cart out, returning true.
     * Return false if the operation fails, or if there are
     * insufficient items available.
     * @param shoppingCart a fulled cart object
     * @return true if it checked out false, otherwise
     */
    public boolean checkOut(ShoppingCart shoppingCart, Context context) {
        boolean checkedOut = true;
        if (shoppingCart.customer != null && shoppingCart.customer instanceof Customer) {
            try {
                DatabaseInsertHelpers dataInsertHelper = new DatabaseInsertHelpers();
                List<Item> allItem = shoppingCart.getItems();
                HashMap<Item, Integer> currentCartItems = shoppingCart.getInformation();
                HashMap<Item, Integer> inventoryItems = DatabaseSelectHelpers.getInventory(context).getItemMap();
                Set<Item> itemSet = inventoryItems.keySet();
                int numberOfItems = allItem.size();
                int counter = 0;
                boolean enoughItem = true;
                while (counter < numberOfItems) {
                    Item item = allItem.get(counter);
                    Item inventoryItemKey = null;
                    for (Item inventoryItem : itemSet) {
                        if (inventoryItem.getId() == item.getId()) {
                            inventoryItemKey = inventoryItem;
                        }
                    }
                    if (inventoryItemKey != null){
                        if (currentCartItems.get(item) > inventoryItems.get(inventoryItemKey)) {
                            enoughItem = false;
                            counter = numberOfItems;
                        }
                    } else {
                        return false;
                    }
                    counter = counter + 1;
                }
                if (enoughItem) {
                    int cartCustomer = shoppingCart.getCustomer().getId();
                    BigDecimal cartPrice = shoppingCart.getTotal().multiply(this.TAXRATE);
                    int saleId = dataInsertHelper.insertSale(cartCustomer, cartPrice, context);
                    for (Item cartItem : allItem) {
                        Item inventoryItemsKey = null;
                        for (Item inventoryItem : itemSet) {
                            if (inventoryItem.getId() == cartItem.getId()) {
                                inventoryItemsKey = inventoryItem;
                            }
                        }
                        int cartItemId = cartItem.getId();
                        int cartItemQuantity = currentCartItems.get(cartItem);
                        int currentItemQuantity = inventoryItems.get(inventoryItemsKey) - cartItemQuantity;
                        dataInsertHelper.insertItemizedSale(saleId, cartItemId, cartItemQuantity, context);
                        DatabaseUpdateHelpers.updateInventoryQuantity(currentItemQuantity, cartItemId, context);
                    }
                }
        } catch (InvalidIdException
                    | InvalidItemException | InvalidTotalPriceException e) {
                checkedOut = false;
            }
        }
        return checkedOut;
    }

    public void clearCart() {
        this.items.clear();;
        this.total = new BigDecimal(0);
    }

    public HashMap<Item, Integer> getInformation(){
        return this.items;
    }
}
