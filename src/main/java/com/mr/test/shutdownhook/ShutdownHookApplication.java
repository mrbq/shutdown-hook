package com.mr.test.shutdownhook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;
import java.util.Scanner;

@SpringBootApplication
public class ShutdownHookApplication {

	private static final Object lock = new Object();

	private final StopWatch stopWatch = new StopWatch();

	public ShutdownHookApplication() {

		stopWatch.start();
	}

	@PostConstruct
	public void setUp()	{

		System.out.println("Setting up ShudownApp....");

		Runtime.getRuntime().addShutdownHook(
				new Thread(() -> {

					System.out.println("Running Shutdown Hook");

					System.out.println("Waiting for the lock to be released");

					synchronized (lock)	{

						stopWatch.stop();

						System.out.println("Lock released!!");
					}

					System.out.println("Shutdown Hook Finished");

					System.out.println("Elapsed time: " + stopWatch.getTotalTimeMillis() + " ms");

				})
		);

	}

	public static void main(String[] args) throws InterruptedException {

		SpringApplication.run(ShutdownHookApplication.class, args);

		System.out.println("ShudownApp Initialized!!");

		synchronized (lock) {

			Scanner scanner = new Scanner(System.in);

			System.out.println("Waiting for input: ");

			String input = scanner.nextLine();

			System.out.println("Input received: " + input);

			System.out.println("Graceful Shutdown, wait two seconds to release lock");

			Thread.sleep(2000L);

			System.out.println("Releasing lock...");
		}

	}

}
