# javacompletables
The CompletableFutures introduced in Java 8 makes it easier to visualize concurrency when its applied to a series of steps (or stages). It allows us to setup each step asynchronously if desired, depending on needs of the  application. 
In the following examples, I tried to 
- Setup a simple Completable Future that runs using custom executors. And completes various steps asynchronously. 
- Setup a "pipeline" which can be invoked by sending data to 1st step, and processing data from last one. 
- Create a completableFuture with exceptionally blocks that try to provide an "alternate" result for an exception and bring the pipeline back to normal execution plan. Each exceptionally only acts on the block preceding it. 

The important thing is that during the execution **each step generates a new CompletionStage object** and passes on to the next . 
Another commonality of this framework is with JavaScript Promises, where a promise can resolve or be rejected and can be handled accordingly. 
---

##Lifecycle

- The CompletableFuture can continue on its path until an exception occurs, at which point it will go to "exceptionally" block and if it returns a value, the pipeline will continue from next step from where it left (that generated an exception). Otherwise the exception handler can invoke its own exception to get out of the pipeline. 

---

- There are also methods to combine more than one completablefutures' results and perform function on that (shown in PipeLineTest.java) and similarly to accept any one result from multiple operations and so on (