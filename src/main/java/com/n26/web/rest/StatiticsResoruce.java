package com.n26.web.rest;

import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.n26.exception.TransactionException;
import com.n26.service.StatiticsService;
import com.n26.web.request.TransactionRequest;
import com.n26.web.response.StatiticsResponse;

/**
 * REST controller for managing Statitics.
 */
@RestController
public class StatiticsResoruce {

    private final Logger log = LoggerFactory.getLogger(StatiticsResoruce.class);
    
    @Autowired
    private StatiticsService service;

    /**
     * POST  /transactions Create a new transaction.
     * @throws TransactionException 
     */
    @PostMapping("/transactions")
    public ResponseEntity<Void> transactions(@RequestBody TransactionRequest request) throws URISyntaxException, TransactionException {
        log.debug("REST request to save a transaction : {}", request);
        service.createTransaction(request);
	return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    /**
     * GET  /statistics Returns the statistics based on the transactions that happened in the last 60 seconds.
     * @throws URISyntaxException 
     */
    @GetMapping("/statistics")
    public ResponseEntity<StatiticsResponse> getStatistics() throws URISyntaxException {
        log.debug("REST request to get statitics");
        StatiticsResponse result = service.getStatistics();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    
    /**
     * DELETE  /transactions All existing transactions to be deleted.
     */
    @DeleteMapping("/transactions")
    public ResponseEntity<Void> deleteAll() {
        log.debug("REST request to delete all existing transactions");
        service.delete();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
}
