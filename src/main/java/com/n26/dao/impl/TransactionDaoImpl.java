package com.n26.dao.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.n26.dao.TransactionDao;
import com.n26.domain.Statitic;
import com.n26.domain.Transaction;

@Component
public class TransactionDaoImpl implements TransactionDao {

	private List<Transaction> transactions;
	private Statitic[] statitics;

	@PostConstruct
	public void init() {
		this.transactions = new ArrayList<>();
		this.statitics = new Statitic[60];
	}

	@Override
	public void create(Transaction transaction) {
		
		synchronized (transactions) {
			LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
			this.transactions.removeIf((item) -> (item.getTimestamp().compareTo(now.minusSeconds(60)) < 0));
			this.transactions.add(transaction);
			calculateStatitics(transaction.getTimestamp().getSecond());
		}
	}

	private void calculateStatitics(int second) {
		
		Supplier<Stream<BigDecimal>> validTransactions = () -> this.transactions.stream()
		.map(Transaction::getAmount);

		BigDecimal sum = validTransactions.get().reduce(BigDecimal.ZERO, BigDecimal::add);
		Optional<BigDecimal> min = validTransactions.get().min(Comparator.naturalOrder());
		Optional<BigDecimal> max = validTransactions.get().max(Comparator.naturalOrder());
		Long count = validTransactions.get().count();

		Statitic stats = new Statitic();
		
		stats.setSum(sum);
		stats.setMin(min != null && min.isPresent() ? min.get() : BigDecimal.valueOf(0.0));
		stats.setMax(max != null && max.isPresent() ? max.get() : BigDecimal.valueOf(0.0));
		stats.setCount(Long.valueOf(count != null ? count : 0));
		
		if (count != null && count > 0) {
			stats.setAvg(sum.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP));
		}
		
		this.statitics[second] = stats;
	}

	@Override
	public void deleteAll() {
		synchronized (transactions) {
			this.init();
		}
	}

	@Override
	public Statitic getStatitics() {
		LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
		LocalDateTime nowMinus60Secs = now.minusSeconds(60);
		int second = nowMinus60Secs.getSecond();

		System.out.println("TRANSACCIONES AL MOMENTO: " + this.transactions.size());
		System.out.println("PIDIENDO ESTADISTICAS PARA EL TIEMPO: " + now.toString());
		
		for (int index = second; index >= 0; index--) {
			if (this.statitics[index] != null) {
				System.out.println("RESPONDIENDO CON EL SEGUNDO: " + index);
				printStatitics();
				return this.statitics[index];
			}
		}
		
		for (int index = this.statitics.length - 1; index < second; index++) {
			if (this.statitics[index] != null) {
				System.out.println("RESPONDIENDO CON EL SEGUNDO: " + index);
				printStatitics();
				return this.statitics[index];
			}
		}
		
		return new Statitic();
	}
	
	public void printStatitics() {
		System.out.print("Statitics: ");
		for (int index = 0; index < statitics.length; index++) {
			Statitic statitic = statitics[index];
			if (statitic != null) {
				System.out.print(index + ", ");
				//statitic.print();
			}
		}
		System.out.println("");
	}

}
