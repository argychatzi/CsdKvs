package com.kth.csd.node.core;

import java.util.concurrent.TimeUnit;

import com.codahale.metrics.EWMA;
import com.codahale.metrics.ExponentiallyDecayingReservoir;

public class ExponentialMovingAverage {
	
	TimeUnit intervalUnit=TimeUnit.SECONDS;
	final double alpha = 0.15;
	final long interval = 1; // for our case, fixed one.  
	
	EWMA ewma = new EWMA(alpha, interval, intervalUnit); 
	
	public double exponentialMovingAverage(long operations){
		ewma.update(operations);
		ewma.tick();
		double value = ewma.getRate(intervalUnit);
		return value;
	}
	

}
