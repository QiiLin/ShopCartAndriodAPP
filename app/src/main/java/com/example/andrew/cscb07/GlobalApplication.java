package com.example.andrew.cscb07;

import android.app.Application;

import com.example.andrew.cscb07.code.store.ShoppingCart;

/**
 * Created by Andrew on 12/2/2017.
 */

public class GlobalApplication extends Application{
    static int userid;
    static int roleid;
    static ShoppingCart cart = new ShoppingCart();
    static boolean fromEmployee = false;
    static int accountId = 0;
}
