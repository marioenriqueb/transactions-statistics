package com.n26.dao;

import com.n26.domain.Statitic;
import com.n26.domain.Transaction;

public interface TransactionDao {

    public void create(Transaction transaction);
    public void deleteAll();
    public Statitic getStatitics();

}