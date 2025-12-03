package com.teddybite;

import com.teddybite.repository.OrderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TeddybiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeddybiteApplication.class, args);
	}

    @Bean
    CommandLineRunner testDbConnection(OrderRepository repository) {
        return args -> {
            try {
                long count = repository.count();
                System.out.println("------------------------------------------------");
                System.out.println("✅ MONGODB CONNECTED SUCCESSFULLY!");
                System.out.println("✅ Current Order Count: " + count);
                System.out.println("------------------------------------------------");
            } catch (Exception e) {
                System.err.println("❌ MONGODB CONNECTION FAILED: " + e.getMessage());
            }
        };
    }
}
