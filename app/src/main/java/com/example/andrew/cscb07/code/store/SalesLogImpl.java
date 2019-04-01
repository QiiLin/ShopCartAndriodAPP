package com.example.andrew.cscb07.code.store;

import com.example.andrew.cscb07.code.inventory.Item;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LTJ on 2017/11/28.
 */

public class SalesLogImpl implements SalesLog {
    private HashMap<Sale, BigDecimal> salesPriceMap;
    private int numberOfSales;
    private HashMap<Sale, HashMap<Item, Integer>> salesItemMap;
    private BigDecimal totalSale;
    private List<Sale> salesList;

    @Override
    public HashMap<Sale, BigDecimal> getSalePriceMap() {
        return this.salesPriceMap;
    }

    @Override
    public void setSalePriceMap(HashMap<Sale, BigDecimal> saleMap) {
        this.salesPriceMap = saleMap;

    }

    @Override
    public void updateSalePriceMap(Sale sale, BigDecimal price) {
        this.salesPriceMap.put(sale, price);
    }

    @Override
    public BigDecimal getTotalSales() {
        return this.totalSale;
    }

    @Override
    public void setTotalSales(BigDecimal total) {
        this.totalSale = total;
    }

    @Override
    public HashMap<Sale, HashMap<Item, Integer>> getSaleItemMap() {
        return this.salesItemMap;
    }

    @Override
    public void setSaleItemMap(HashMap<Sale, HashMap<Item, Integer>> saleItemsMap) {
        this.salesItemMap = saleItemsMap;
    }

    @Override
    public void updateSaleItemMap(Sale sale, HashMap<Item, Integer> saleItem) {
        salesItemMap.put(sale, saleItem);
    }

    @Override
    public void setTotalNumberOfSale(int totalNumber) {
        this.numberOfSales = totalNumber;
    }

    @Override
    public int getTotalNumberOfSale() {
        return this.numberOfSales;
    }

    @Override
    public List<Sale> getSaleList() {
        return this.salesList;
    }

    @Override
    public void setSaleList(List<Sale> saleList) {
        this.salesList = saleList;
    }
}
