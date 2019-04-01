package com.example.andrew.cscb07.code.store;

import com.example.andrew.cscb07.code.inventory.Item;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LTJ on 2017/11/28.
 */

public interface SalesLog {

    public HashMap<Sale, BigDecimal> getSalePriceMap();

    public void setSalePriceMap(HashMap<Sale, BigDecimal> saleMap);

    public void updateSalePriceMap(Sale sale, BigDecimal price);

    public HashMap<Sale, HashMap<Item, Integer>> getSaleItemMap();

    public void setSaleItemMap(HashMap<Sale, HashMap<Item, Integer>> saleItemsMap);

    public void updateSaleItemMap(Sale sale, HashMap<Item, Integer> saleItem);

    public BigDecimal getTotalSales();

    public void setTotalSales(BigDecimal total);

    public void setTotalNumberOfSale(int totalNumber);

    public int getTotalNumberOfSale();

    public List<Sale> getSaleList();

    public void setSaleList(List<Sale> saleList);
}
