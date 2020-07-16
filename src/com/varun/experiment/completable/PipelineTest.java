package com.varun.experiment.completable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class PipelineTest {

	public PipelineTest() {
		// TODO Auto-generated constructor stub
	}
	
	private String greetings[] = {"Hello ", "Ki haal hai ", "Ciao "};
	
	public CompletableFuture<Void> getPipeLineBuilt(Supplier<String> supplier){
		return CompletableFuture.supplyAsync(()->supplier.get()).thenCombine(CompletableFuture.completedFuture("World"),
				(data1, data2) -> {return data1+data2;}
				).thenApply(data->data.toUpperCase()).thenAccept(System.out::println);
	}
	
	
	
	public CompletableFuture<String> recieivePipeLine (){
		String supply = null;
		return CompletableFuture.supplyAsync(()->supply.toUpperCase()).thenCombine(CompletableFuture.completedFuture("World"),
				(data1, data2) -> {return data1+data2;}
				).thenApply(data->data.toUpperCase());
	}
	
	public void setPipeLine(CompletableFuture<String> cf) {
		cf.thenApply(data->data.toUpperCase()).thenApply(data-> (data + "World!!")
				).thenAccept(System.out::println);
	}
	
	public CompletableFuture<String> setAnotherPipeLine(CompletableFuture<String> cf) {
		return cf.thenApply(data->data.toUpperCase()).thenApply(data-> (data + "World!!")
				);
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		
		List<CompletableFuture<Void>> cfList  = new ArrayList<>();
		PipelineTest pipelineTest = new PipelineTest();
		for(int i = 0; i < pipelineTest.greetings.length; i ++ ) {
			String s = pipelineTest.greetings[i];
			cfList.add(pipelineTest.getPipeLineBuilt(()->s));
		}
			
		cfList.forEach(cf->cf.join());
		System.out.println("Ending");
		
				
		CompletableFuture<String> cff1 = new CompletableFuture<String>();
		
		pipelineTest.setPipeLine(cff1);
		
		cff1.complete("Hola ");
		//This is wrong , the Completable Future has moved to a new Stage, and we should be invoking the operation on that stage
		System.out.println(cff1.get()); 
		cff1.thenAccept(System.out::println).join();
		
		System.out.println("------------------");
		
CompletableFuture<String> cff2 = new CompletableFuture<String>();
		//Retrun the new STAGE 
		CompletableFuture cff3 = pipelineTest.setAnotherPipeLine(cff2);
		//start the pipeline
		cff2.complete("Hola ");
		//If I invoke the GET on new Completion Stage it will give me result of that stage
		//EVER?Y STEP Is a NEW STAGE.
		System.out.println(cff3.get());
		cff3.thenAccept(System.out::println).join(); //Expecting HOLA World!
	}
	


}
