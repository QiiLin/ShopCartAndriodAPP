package com.example.andrew.cscb07.code.store;

import com.example.andrew.cscb07.code.inventory.Item;
import com.example.andrew.cscb07.code.com.users.User;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Created by LTJ on 2017/11/28.
 */

public interface Sale {
    public int getId();

    public void setId(int id);

    public User getUser();

    public void setUser(User user);

    public BigDecimal getTotalPrice();

    public void setTotalPrice(BigDecimal price);

    public HashMap<Item, Integer> getItemMap();

    public void setItemMap(HashMap<Item, Integer> itemMap);
}
