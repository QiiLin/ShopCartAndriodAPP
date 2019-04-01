package com.example.andrew.cscb07.code.inventory;

import java.util.HashMap;

/**
 * Created by LTJ on 2017/11/28.
 */

public interface Inventory {

    public HashMap<Item, Integer> getItemMap();

    public void setItemMap(HashMap<Item, Integer> itemMap);

    public void updateMap(Item item, Integer value);

    public int getTotalItems();

    public void setTotalItems(int total);

}