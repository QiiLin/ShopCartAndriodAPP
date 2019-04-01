package com.example.andrew.cscb07.code.inventory;

/**
 * Created by LTJ on 2017/11/28.
 */

import java.math.BigDecimal;

public interface Item {

    public int getId();

    public void setId(int id);

    public String getName();

    public void setName(String name);

    public BigDecimal getPrice();

    public void setPrice(BigDecimal price);

    @Override
    public boolean equals(Object item);

}
