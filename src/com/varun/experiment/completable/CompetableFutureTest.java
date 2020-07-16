package com.varun.experiment.completable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Simple Completable Future Test. Multiple calls in a loop. 
 * The main thread hands off to Completable Future which closes its pipeline
 * For some reason on my system, it uses "main" for first 3 executions
 * before taking the executor pool . This causes the main to "wait" until one execution
 * is done before moving on to next and does not really work asynchronously.
 * 
 *
 */
public class CompetableFutureTest {
	CompletableFuture<Double> fut= new CompletableFuture<Double>();

	public CompetableFutureTest() {
	}
	
	ExecutorService executors = Executors.newFixedThreadPool(2);
	public void print( double data) {
		System.out.println(new java.util.Date() + " " + data + " from " + Thread.currentThread().getName());
	}
	
	public CompletableFuture<Void> basicTestWithSuppliersAsync(Supplier<Double> seed) throws Exception, Exception
	{
		
		CompletableFuture<Void> cf=
				CompletableFuture.supplyAsync(()->{
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return seed.get()*10d;}, executors).thenApply(data->data*data)
				
		.thenAccept(data -> print(data)).thenRun(()->{
			
			try {
				System.out.println("Hello from " + Thread.currentThread().getName());
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			print(1.0d);
		})
		;
		System.out.println(cf.isDone());
		return cf;

	}
	
	public CompletableFuture basicTestWithAllAsync(double seed) throws Exception, Exception
	{
		
		System.out.println("Seed value is " + seed);
		
		CompletableFuture<Void> cf=
				fut.supplyAsync(()->seed*10d, executors).thenApplyAsync(data->data*data, executors)
				
		.thenAcceptAsync(data -> print(data),executors).thenRunAsync(()->{
			
			try {
				System.out.println("Hello from " + Thread.currentThread().getName());
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			print(1.0d);
		}, executors)
		;
		System.out.println(cf.isDone());
		return cf;

	}

	
	public static void main(String[] args) throws Exception {
		CompetableFutureTest test = new CompetableFutureTest();
		List<CompletableFuture<Void>> cfList = new ArrayList<>();		
		for (int i = 0; i < 5; i ++) 
		{
			System.out.println("i="+ i);
			//test.basicTest(Math.random());
			cfList.add(test.basicTestWithSuppliersAsync(Math::random));
			cfList.add(test.basicTestWithAllAsync(Math.random()));
			
		}
		
		cfList.forEach(data->data.join());
		System.out.println("All Joined");
		test.executors.shutdown();
		test.executors.awaitTermination(30, TimeUnit.SECONDS);
		
		System.out.println("Ending");
	//	test.fut.cancel(true);
	}
}
