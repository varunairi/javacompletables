package com.varun.experiment.completable;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureExceptionsTest {

	public CompletableFutureExceptionsTest() {
		// TODO Auto-generated constructor stub
	}

	
	public static void letsCompleteExceptionally() throws Exception
	{
		boolean b = false; //true
		String s = CompletableFuture.supplyAsync(()->{
if(!b)			throw new RuntimeException("My Program Failed");
			return "Success!!";
		}).exceptionally((th)-> {
			System.out.println("Found Exception : " + th);
			return "Failed";
		})
		.thenApplyAsync((success)-> {
			System.out.println("In ApplyAsynch: " + success);
			if(!b ) throw new RuntimeException("My ApplyAsyncFailed");
			return success.toLowerCase();
		}).exceptionally((th)-> {
			System.out.println("Found Exception2 : " + th);
			return "Failed Again";
		}).join();
		System.out.println("Output is : "  + s);
	}
	
	
	public static void main(String[] args) throws Exception {
		letsCompleteExceptionally();
		
		Thread.sleep(1000);
	}
}
