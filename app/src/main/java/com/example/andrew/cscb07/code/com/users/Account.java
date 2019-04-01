package com.example.andrew.cscb07.code.com.users;

import com.example.andrew.cscb07.code.store.ShoppingCart;

/**
 * Created by LTJ on 2017/11/28.
 */

public interface Account {

    public int getId();

    public void setId(int id);

    public int getUserId();

    public void setUserId(int id);

    public User getUser();

    public void setUser(User user);

    public ShoppingCart getShoppingCart();

    public void setShoppingCart(ShoppingCart shoppingCart);

    public void setActive(boolean active);

    public boolean getActive();

    public void setApprove(boolean approve);

    public boolean getApprove();
}
