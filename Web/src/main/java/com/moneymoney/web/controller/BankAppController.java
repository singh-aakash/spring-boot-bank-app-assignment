package com.moneymoney.web.controller;

import java.util.ArrayList;
import java.util.List;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.moneymoney.web.entity.Transaction;

@Controller
public class BankAppController {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping("/")
	public String depositForm() {
		return "DepositForm";
	}
	@RequestMapping("/deposit")
	public String deposit(@ModelAttribute Transaction transaction,
			Model model) {
		restTemplate.postForEntity("http://localhost:7070/transactions", transaction, null);
	
		model.addAttribute("message","Successfully deposites!");
		return "DepositForm";
	}
	

	@RequestMapping("/WithdrawForm")
	public String withdrawForm() {
		return "WithdrawForm";
	}
	
	@RequestMapping("/withdraw")
	public String withdraw(@ModelAttribute Transaction transaction,
			Model model) {
		restTemplate.postForEntity("http://localhost:7070/transactions/withdraw", transaction, null);
	
		model.addAttribute("message","Successfully withdraw!");
		return "DepositForm";
	}
	
	@RequestMapping("/FundTransfer")
	public String fundTransferForm() {
		return "FundTransfer";
	}
	
	@RequestMapping("/fundTransfer")
	public String fundTransfer(@RequestParam("senderAccountNumber") int senderAccountNumber,
			@RequestParam("receiverAccountNumber") int ReceiverAccountNumber,
			@RequestParam("amount") double amount, @ModelAttribute Transaction transaction,
			Model model) {
		transaction.setAccountNumber(senderAccountNumber);
		restTemplate.postForEntity("http://localhost:7070/transactions/withdraw", transaction, null);
		transaction.setAccountNumber(ReceiverAccountNumber);
		restTemplate.postForEntity("http://localhost:7070/transactions", transaction, null);
		model.addAttribute("message","Successfully Transfered!");
		return "FundTransfer";
	}
	
	@RequestMapping("/statementFundTransfer")
	public ModelAndView getStatementFundTransfer(@RequestParam("offset") int offset, @RequestParam("size") int size) {
		CurrentDataSet currentDataSet = restTemplate.getForObject("http://localhost:8989/transactions/statement", CurrentDataSet.class);
		int currentSize=size==0?5:size;
		int currentOffset=offset==0?1:offset;
		Link next=linkTo(methodOn(BankAppController.class).getStatementFundTransfer(currentOffset+currentSize,currentSize)).withRel("next");
		Link previous=linkTo(methodOn(BankAppController.class).getStatementFundTransfer(currentOffset-currentOffset,currentSize)).withRel("previous");
		List<Transaction> currentDataSetList = new ArrayList<Transaction>();
		for (int i = currentOffset - 1; i < currentSize + currentOffset - 1; i++) { 
			List<Transaction> transactions = currentDataSet.getTransactions();
			Transaction transaction = transactions.get(i);
			currentDataSetList.add(transaction);
			}
		CurrentDataSet dataSet = new CurrentDataSet(currentDataSetList, next, previous);
		/*
		 * currentDataSet.setNextLink(next); currentDataSet.setPreviousLink(previous);
		 */
		return new ModelAndView("DepositForm","currentDataSet",dataSet);
	}
}
