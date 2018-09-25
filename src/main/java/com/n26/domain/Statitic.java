package com.n26.domain;

import java.math.BigDecimal;

public class Statitic {

	private BigDecimal sum;
	private BigDecimal avg;
	private BigDecimal max;
	private BigDecimal min;
	private Long count;

	public Statitic() {
		this.sum = BigDecimal.valueOf(0);
		this.avg = BigDecimal.valueOf(0);
		this.count = Long.valueOf(0);
		this.min = BigDecimal.valueOf(0);
		this.max = BigDecimal.valueOf(0);
	}

	public BigDecimal getSum() {
		return sum;
	}

	public void setSum(BigDecimal sum) {
		this.sum = sum;
	}

	public BigDecimal getAvg() {
		return avg;
	}

	public void setAvg(BigDecimal avg) {
		this.avg = avg;
	}

	public BigDecimal getMax() {
		return max;
	}

	public void setMax(BigDecimal max) {
		this.max = max;
	}

	public BigDecimal getMin() {
		return min;
	}

	public void setMin(BigDecimal min) {
		this.min = min;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}
	
	public void print() {
		System.out.println("---------------------------------- ");
		System.out.println("sumatoria: " + this.sum);
		System.out.println("promedio: " + this.avg);
		System.out.println("maximo: " + this.max);
		System.out.println("minimo: " + this.min);
		System.out.println("cantidad: " + this.count);
		System.out.println("---------------------------------- ");
	}
}
