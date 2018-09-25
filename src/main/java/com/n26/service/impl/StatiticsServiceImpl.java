package com.n26.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.n26.dao.TransactionDao;
import com.n26.domain.Statitic;
import com.n26.domain.Transaction;
import com.n26.exception.InvalidJsonException;
import com.n26.exception.NotParseableFieldsException;
import com.n26.exception.TransactionException;
import com.n26.exception.TransactionOlderException;
import com.n26.service.StatiticsService;
import com.n26.web.request.TransactionRequest;
import com.n26.web.response.StatiticsResponse;

@Component
public class StatiticsServiceImpl implements StatiticsService {

	@Autowired
	private TransactionDao dao;

	/**
	 * 201 – in case of success 204 – if the transaction is older than 60
	 * seconds 400 – if the JSON is invalid 422 – if any of the fields are not
	 * parsable or the transaction date is in the future
	 * 
	 * @throws TransactionException
	 */
	@Override
	public void createTransaction(TransactionRequest request) throws TransactionException {
		LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
		Transaction transaction = new Transaction();

		if (request.getAmount() == null || request.getTimestamp() == null) {
			throw new InvalidJsonException("JSON is invalid");
		}

		try {
			BigDecimal amount = new BigDecimal(request.getAmount());
			transaction.setAmount(amount);
		} catch (Exception e) {
			throw new NotParseableFieldsException("Amount field is not parsable.");
		}

		try {
			// DateTimeFormatter formatter =
			// DateTimeFormatter.ofPattern("YYYY-MM-DD'T'hh:mm:ss.SSSZ");
			LocalDateTime dateTime = LocalDateTime.parse(request.getTimestamp(), DateTimeFormatter.ISO_DATE_TIME);
			transaction.setTimestamp(dateTime);
		} catch (Exception e) {
			throw new NotParseableFieldsException("Timestamp field is not parsable.");
		}

		LocalDateTime timestamp = transaction.getTimestamp();
		if (timestamp.compareTo(now) > 0) {
			throw new NotParseableFieldsException("Transaction date is in the future.");
		}

		LocalDateTime nowMinus60Seconds = now.minusSeconds(60);
		if (timestamp.compareTo(nowMinus60Seconds) < 0) {
			throw new TransactionOlderException("The transaction is older than 60 seconds.");
		}

		dao.create(transaction);
	}

	/**
	 * All BigDecimal values always contain exactly two decimal places and use
	 * 'HALF_ROUND_UP' rounding. eg: 10.345 is returned as 10.35 10.8 is
	 * returned as 10.80
	 */
	@Override
	public StatiticsResponse getStatistics() {
		Statitic stats = dao.getStatitics();
		StatiticsResponse response = new StatiticsResponse();
		response.setAvg(getNormalizedNumber(stats.getAvg()));
		response.setSum(getNormalizedNumber(stats.getSum()));
		response.setMin(getNormalizedNumber(stats.getMin()));
		response.setMax(getNormalizedNumber(stats.getMax()));
		response.setCount(stats.getCount());
		return response;
	}

	private String getNormalizedNumber(BigDecimal value) {
		String result = null;
		if (value != null) {
			result = value.setScale(2, RoundingMode.HALF_UP).toString();
		}

		return result;

	}

	@Override
	public void delete() {
		this.dao.deleteAll();
	}

}
