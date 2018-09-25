package com.n26.service;

import com.n26.exception.TransactionException;
import com.n26.web.request.TransactionRequest;
import com.n26.web.response.StatiticsResponse;

public interface StatiticsService {

    public void createTransaction(TransactionRequest request) throws TransactionException;

    public StatiticsResponse getStatistics();

    public void delete();

}
