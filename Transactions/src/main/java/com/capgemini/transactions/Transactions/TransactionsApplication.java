package com.capgemini.transactions.Transactions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.capgemini.transactions.Transactions.Entity.Transaction;
import com.capgemini.transactions.Transactions.Entity.TransactionType;
import com.capgemini.transactions.Transactions.repository.TransactionRepository;
import com.capgemini.transactions.Transactions.service.TransactionService;


@SpringBootApplication
public class TransactionsApplication {

	@Autowired
	TransactionService service;
	
	public static void main(String[] args) {
		SpringApplication.run(TransactionsApplication.class, args);
	}

	
	  @Bean
	public CommandLineRunner loadinitialdata(TransactionRepository repository) {
		return (arg) -> {
			repository.save(new Transaction(101,5000.0, TransactionType.DEPOSIT,"ATM"));
			service.deposit(101,10000.00,2000.00,"ATM");
			
		};
	}

	@Bean
	public RestTemplate template() {
		return new RestTemplate();
	}
	
}

