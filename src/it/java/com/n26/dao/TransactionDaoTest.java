package com.n26.dao;

import java.math.BigDecimal;
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
import com.n26.dao.TransactionDao;
import com.n26.domain.Statitic;
import com.n26.domain.Transaction;
import com.n26.exception.TransactionException;
import com.n26.service.StatiticsService;
import com.n26.web.request.TransactionRequest;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TransactionDaoTest {

	@Autowired
	private TransactionDao dao;

	@Before
	public void setup() {
		this.dao.deleteAll();
	}
	
	@Test
	public void createTest() {
		// Before
		LocalDateTime localDate = LocalDateTime.now(Clock.systemUTC()).minusSeconds(5);
		BigDecimal amount = BigDecimal.valueOf(25.25);

		Transaction trans = new Transaction();
		trans.setAmount(amount);
		trans.setTimestamp(localDate);

		// Test
		dao.create(trans);
		
		// After
		Statitic statitics = dao.getStatitics();
		Assert.assertEquals(Long.valueOf(1), statitics.getCount());
		Assert.assertEquals(amount, statitics.getAvg());
		Assert.assertEquals(amount, statitics.getMax());
		Assert.assertEquals(amount, statitics.getMin());
		Assert.assertEquals(amount, statitics.getSum());
	}
	
	@Test
	public void statiticsTest() {
		// Before
		LocalDateTime localDate = LocalDateTime.now(Clock.systemUTC()).minusSeconds(5);
		BigDecimal amount1 = BigDecimal.valueOf(10.01);
		BigDecimal amount2 = BigDecimal.valueOf(10.02);
		BigDecimal amount3 = BigDecimal.valueOf(10.03);

		Transaction trans1 = new Transaction();
		trans1.setAmount(amount1);
		trans1.setTimestamp(localDate);
		
		Transaction trans2 = new Transaction();
		trans2.setAmount(amount2);
		trans2.setTimestamp(localDate);
		
		Transaction trans3 = new Transaction();
		trans3.setAmount(amount3);
		trans3.setTimestamp(localDate);
		
		dao.create(trans1);
		dao.create(trans2);
		dao.create(trans3);

		// Test
		Statitic statitics = dao.getStatitics();
		
		// After
		Assert.assertEquals(Long.valueOf(3), statitics.getCount());
		Assert.assertEquals(BigDecimal.valueOf(10.02), statitics.getAvg());
		Assert.assertEquals(BigDecimal.valueOf(10.03), statitics.getMax());
		Assert.assertEquals(BigDecimal.valueOf(10.01), statitics.getMin());
		Assert.assertEquals(BigDecimal.valueOf(30.06), statitics.getSum());
	}
	
	@Test
	public void deleteAllTest() {
		// Before
		LocalDateTime localDate = LocalDateTime.now(Clock.systemUTC()).minusSeconds(5);
		BigDecimal amount1 = BigDecimal.valueOf(10.01);
		BigDecimal amount2 = BigDecimal.valueOf(10.02);
		BigDecimal amount3 = BigDecimal.valueOf(10.03);
		BigDecimal amount4 = BigDecimal.valueOf(10.04);

		Transaction trans1 = new Transaction();
		trans1.setAmount(amount1);
		trans1.setTimestamp(localDate);
		
		Transaction trans2 = new Transaction();
		trans2.setAmount(amount2);
		trans2.setTimestamp(localDate);
		
		Transaction trans3 = new Transaction();
		trans3.setAmount(amount3);
		trans3.setTimestamp(localDate);
		
		Transaction trans4 = new Transaction();
		trans4.setAmount(amount4);
		trans4.setTimestamp(localDate);

		dao.create(trans1);
		dao.create(trans2);
		
		// Test
		dao.deleteAll();
		dao.create(trans3);
		dao.create(trans4);
		
		// After
		Statitic statitics = dao.getStatitics();
		Assert.assertEquals(Long.valueOf(2), statitics.getCount());
		Assert.assertEquals(BigDecimal.valueOf(10.04), statitics.getAvg());
		Assert.assertEquals(BigDecimal.valueOf(10.04), statitics.getMax());
		Assert.assertEquals(BigDecimal.valueOf(10.03), statitics.getMin());
		Assert.assertEquals(BigDecimal.valueOf(20.07), statitics.getSum());
	}
}
