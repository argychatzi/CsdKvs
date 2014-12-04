package com.kth.csd.node.core;

import java.util.concurrent.TimeUnit;

import com.codahale.metrics.EWMA;
import com.codahale.metrics.ExponentiallyDecayingReservoir;

public class ExponentialMovingAverage {
	
	static TimeUnit intervalUnit=TimeUnit.SECONDS;
	static final double alpha = 0.15;
	static final long interval = 1; // for our case, fixed one.  
	
	public static EWMA ewma = new EWMA(alpha, interval, intervalUnit); 
	
	public static double exponentialMovingAverage(long writeperSecond){
		ewma.update(writeperSecond);
		ewma.tick();
		double value = ewma.getRate(intervalUnit);
		return value;
	}
	

}
