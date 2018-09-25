package com.n26;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

import com.n26.domain.Transaction;

public class Prueba {

	public static void main(String[] args) {
		LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
		System.out.println(now);
		
		Comparator<Transaction> comparator = new Comparator<Transaction>() {

			@Override
			public int compare(Transaction t1, Transaction t2) {
				return t1.getAmount().compareTo(t2.getAmount());
			}
			
		};
		
		TreeSet<Transaction> arbol = new TreeSet<>(comparator);
		arbol.add(createTransaction(BigDecimal.valueOf(30.5), LocalDateTime.now()));
		arbol.add(createTransaction(BigDecimal.valueOf(20.5), LocalDateTime.now()));
		arbol.add(createTransaction(BigDecimal.valueOf(50.7), LocalDateTime.now()));
		arbol.add(createTransaction(BigDecimal.valueOf(50.5), LocalDateTime.now()));
		arbol.add(createTransaction(BigDecimal.valueOf(10.5), LocalDateTime.now()));
		arbol.add(createTransaction(BigDecimal.valueOf(5.5), LocalDateTime.now()));
		
		for (Transaction transaction : arbol) {
			System.out.println("elemento: " + transaction.getAmount());
		}
		
		System.out.println("elemento primero: " + arbol.first().getAmount());
		System.out.println("elemento primero: " + arbol.last().getAmount());
		
		
	}

	private static Transaction createTransaction(BigDecimal valueOf, LocalDateTime now) {
		Transaction trans = new Transaction();
		trans.setAmount(valueOf);
		trans.setTimestamp(now);
		return trans;
	}

}
