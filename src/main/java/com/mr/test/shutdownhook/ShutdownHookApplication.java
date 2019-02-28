package com.mr.test.shutdownhook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.annotation.PostConstruct;
import java.util.Scanner;

@SpringBootApplication
public class ShutdownHookApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShutdownHookApplication.class);
	
	private static final Object lock = new Object();

	private final Long startTime;
	
	public ShutdownHookApplication() {

		startTime = System.currentTimeMillis();
		
	}

	@PostConstruct
	public void setUp()	{
		
		LOGGER.info("Setting up ShudownApp....");

		Runtime.getRuntime().addShutdownHook(
				new Thread(() -> {
					
					LOGGER.info("Running Shutdown Hook");
					
					LOGGER.info("Waiting for the lock to be released");

					Long elapsedTime;
					
					synchronized (lock)	{
						
						LOGGER.info("Lock released!!");

						elapsedTime = System.currentTimeMillis() - this.startTime;
					}
					
					LOGGER.info("Elapsed time: {}ms", elapsedTime);
					
					LOGGER.info("Shutdown Hook Finished");
				})
		);

	}

	public static void main(String[] args) throws InterruptedException {

		SpringApplication.run(ShutdownHookApplication.class, args);
		
		LOGGER.info("ShudownApp Initialized!!");

		synchronized (lock) {

			Scanner scanner = new Scanner(System.in);
			
			LOGGER.info("Waiting for input: ");

			String input = scanner.nextLine();
			
			LOGGER.info("Input received: {}",input);
			
			LOGGER.info("Graceful Shutdown, wait two seconds to release lock");
			
			Thread.sleep(2000L);
			
			LOGGER.info("Releasing lock...");
		}

	}

}
