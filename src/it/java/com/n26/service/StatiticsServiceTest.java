package com.n26.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import com.n26.Application;
import com.n26.exception.TransactionException;
import com.n26.web.request.TransactionRequest;
import com.n26.web.response.StatiticsResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class StatiticsServiceTest {

	@Autowired
	private StatiticsService service;

	@Before
	public void setup() {
		this.service.delete();
	}

	@Test
	public void createTransactionTest_ok() {
		// Before
		LocalDateTime localDate = LocalDateTime.now(Clock.systemUTC()).minusSeconds(5);
		String amount = "25.25";

		TransactionRequest request = new TransactionRequest();
		request.setAmount(amount);
		request.setTimestamp(localDate.toString());

		// Test
		try {
			service.createTransaction(request);
		} catch (TransactionException e) {
			Assert.fail();
		}
	}

	@Test
	public void createTransactionTest_invalidAmount() {
		// Before
		LocalDateTime localDate = LocalDateTime.now(Clock.systemUTC()).minusMinutes(20);
		String amount = "123456d";

		TransactionRequest request = new TransactionRequest();
		request.setAmount(amount);
		request.setTimestamp(localDate.toString());

		// Test
		try {
			service.createTransaction(request);
		} catch (TransactionException expected) {
			Assert.assertEquals(422, HttpStatus.UNPROCESSABLE_ENTITY.value());
			Assert.assertEquals("Amount field is not parsable.", expected.getMessage());
		}
	}

	@Test
	public void createTransactionTest_invalidTimestamp() {
		// Before
		String amount = "25.25";

		TransactionRequest request = new TransactionRequest();
		request.setAmount(amount);
		request.setTimestamp("pepe");

		// Test
		try {
			service.createTransaction(request);
		} catch (TransactionException expected) {
			Assert.assertEquals(422, HttpStatus.UNPROCESSABLE_ENTITY.value());
			Assert.assertEquals("Timestamp field is not parsable.", expected.getMessage());
		}
	}

	@Test
	public void createTransactionTest_less60minutesTimestamp() {
		// Before
		LocalDateTime localDate = LocalDateTime.now(Clock.systemUTC()).minusMinutes(80);
		String amount = "25.25";

		TransactionRequest request = new TransactionRequest();
		request.setAmount(amount);
		request.setTimestamp(localDate.toString());

		// Test
		try {
			service.createTransaction(request);
		} catch (TransactionException expected) {
			Assert.assertEquals(204, HttpStatus.NO_CONTENT.value());
			Assert.assertEquals("The transaction is older than 60 seconds.", expected.getMessage());
		}
	}

	@Test
	public void createTransactionTest_less60minutesTimestamp2() {
		// Before
		LocalDateTime localDate = LocalDateTime.now(Clock.systemUTC());
		localDate.minusYears(1);
		String amount = "25.25";

		TransactionRequest request = new TransactionRequest();
		request.setAmount(amount);
		request.setTimestamp(localDate.toString());

		// Test
		try {
			service.createTransaction(request);
		} catch (TransactionException expected) {
			Assert.assertEquals(204, HttpStatus.NO_CONTENT.value());
			Assert.assertEquals("The transaction is older than 60 seconds.", expected.getMessage());
		}
	}

	@Test
	public void createTransactionTest_futureTimestamp() {
		// Before
		LocalDateTime localDate = LocalDateTime.now(Clock.systemUTC()).plusMinutes(20);
		String amount = "25.25";

		TransactionRequest request = new TransactionRequest();
		request.setAmount(amount);
		request.setTimestamp(localDate.toString());

		// Test
		try {
			service.createTransaction(request);
		} catch (TransactionException expected) {
			Assert.assertEquals(422, HttpStatus.UNPROCESSABLE_ENTITY.value());
			Assert.assertEquals("Transaction date is in the future.", expected.getMessage());
		}
	}

	@Test
	public void getStatisticsTest_ok1() throws TransactionException {
		// Before
		service.createTransaction(createTransactionRequest("10.10"));
		service.createTransaction(createTransactionRequest("10.20"));
		service.createTransaction(createTransactionRequest("10.30"));
		service.createTransaction(createTransactionRequest("10.40"));
		service.createTransaction(createTransactionRequest("10.50"));

		// Test
		StatiticsResponse actual = service.getStatistics();

		// After
		Assert.assertEquals("10.30", actual.getAvg());
		Assert.assertEquals("51.50", actual.getSum());
		Assert.assertEquals("10.10", actual.getMin());
		Assert.assertEquals("10.50", actual.getMax());
		Assert.assertEquals(Long.valueOf(5), actual.getCount());
	}

	@Test
	public void getStatisticsTest_ok2() throws TransactionException {
		// Before
		BigDecimal min = new BigDecimal("15.2698");
		BigDecimal max = new BigDecimal("1254.54567");

		BigDecimal[] values = { new BigDecimal("250.524"), new BigDecimal("32.5682"), new BigDecimal("524.26"), max,
				min };

		for (BigDecimal value : values) {
			service.createTransaction(createTransactionRequest(value.toString()));
		}

		// Test
		StatiticsResponse actual = service.getStatistics();

		// After
		BigDecimal sumatory = BigDecimal.valueOf(0);
		for (BigDecimal value : values) {
			sumatory = sumatory.add(value);
		}

		BigDecimal average = sumatory.divide(BigDecimal.valueOf(5));

		Assert.assertEquals(average.setScale(2, RoundingMode.HALF_UP).toString(), actual.getAvg());
		Assert.assertEquals(sumatory.setScale(2, RoundingMode.HALF_UP).toString(), actual.getSum());
		Assert.assertEquals(min.setScale(2, RoundingMode.HALF_UP).toString(), actual.getMin());
		Assert.assertEquals(max.setScale(2, RoundingMode.HALF_UP).toString(), actual.getMax());
		Assert.assertEquals(Long.valueOf(values.length), actual.getCount());
	}

	@Test
	public void getStatisticsTest_okWithDiferentTimes() throws TransactionException {
		// Before
		BigDecimal min = new BigDecimal("15.2698");
		BigDecimal max = new BigDecimal("1254.54567");

		BigDecimal[] values = { new BigDecimal("250.524"), new BigDecimal("32.5682"), new BigDecimal("524.26"), max,
				min };

		for (BigDecimal value : values) {
			service.createTransaction(createTransactionRequest(value.toString()));
		}

		// Test
		StatiticsResponse actual = service.getStatistics();

		// After
		BigDecimal sumatory = BigDecimal.valueOf(0);
		for (BigDecimal value : values) {
			sumatory = sumatory.add(value);
		}

		BigDecimal average = sumatory.divide(BigDecimal.valueOf(5));

		Assert.assertEquals(average.setScale(2, RoundingMode.HALF_UP).toString(), actual.getAvg());
		Assert.assertEquals(sumatory.setScale(2, RoundingMode.HALF_UP).toString(), actual.getSum());
		Assert.assertEquals(min.setScale(2, RoundingMode.HALF_UP).toString(), actual.getMin());
		Assert.assertEquals(max.setScale(2, RoundingMode.HALF_UP).toString(), actual.getMax());
		Assert.assertEquals(Long.valueOf(values.length), actual.getCount());
	}

	private TransactionRequest createTransactionRequest(String amount) {
		LocalDateTime localDate = LocalDateTime.now(Clock.systemUTC()).minusSeconds(30);
		TransactionRequest request = new TransactionRequest();
		request.setAmount(amount);
		request.setTimestamp(localDate.toString());
		return request;
	}

}
