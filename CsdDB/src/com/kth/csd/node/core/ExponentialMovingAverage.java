package com.kth.csd.node.core;

import java.util.concurrent.TimeUnit;

import com.codahale.metrics.EWMA;
import com.codahale.metrics.ExponentiallyDecayingReservoir;

public class ExponentialMovingAverage {
	
	private TimeUnit intervalUnit=TimeUnit.SECONDS;
	private static final double alpha = 0.15;
	private static final long interval = 1; // for our case, fixed one.  
	private EWMA ewma;
	
	public ExponentialMovingAverage(){
		ewma = new EWMA(alpha, interval, intervalUnit); 
	}
	
	public double exponentialMovingAverage(long writeperSecond){
		ewma.update(writeperSecond);
		ewma.tick();
		double value = ewma.getRate(intervalUnit);
		return value;
	}
	

}
