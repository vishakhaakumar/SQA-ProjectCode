package pl.piomin.microservices.advanced.account.api;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.client.RestTemplate;
import pl.piomin.microservices.advanced.account.model.Account;
import pl.piomin.microservices.advanced.account.model.AccountDTO;
import pl.piomin.microservices.advanced.account.repository.AccountRepository;

@RestController
public class AccountController {
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	AccountRepository repository;

	protected Logger logger = Logger.getLogger(AccountController.class.getName());

	@GetMapping("/accounts/{number}")
	public Account findByNumber(@PathVariable("number") String number) {
		if(logger.isLoggable(Level.INFO)) {
			logger.info(String.format("Account.findByNumber(%s)", number));}
		return repository.findByNumber(number);
	}

	@GetMapping("/accounts/customer/{customer}")
	public List<Account> findByCustomer(@PathVariable("customer") String customerId) {
		if(logger.isLoggable(Level.INFO)) {logger.info(String.format("Account.findByCustomer(%s)", customerId));}
		return repository.findByCustomerId(customerId);
	}

	@GetMapping("/accounts")
	public List<Account> findAll() {
		if(logger.isLoggable(Level.INFO)) {logger.info("Account.findAll()");}
		return repository.findAll();
	}

	@PostMapping("/accounts")
	public Account add(@RequestBody AccountDTO accountDto) {
		if(logger.isLoggable(Level.INFO)) {logger.info(String.format("Account.add(%s)", accountDto));}
		Account account = new Account();
		BeanUtils.copyProperties(accountDto, account);
		return repository.save(account);
	}

	@PutMapping("/accounts")
	public Account update(@RequestBody AccountDTO accountDto) {
		if(logger.isLoggable(Level.INFO)) {logger.info(String.format("Account.update(%s)", accountDto));}
		Account account = new Account();
		BeanUtils.copyProperties(accountDto, account);
		return repository.save(account);
	}

	@GetMapping("/orders")
	public ResponseEntity<String> getOrders() {
		if(logger.isLoggable(Level.INFO)) {logger.info("fetching all the order details");}
		restTemplate.getForObject("http://localhost:4444/orders/", String.class);
		return new ResponseEntity<>("User order details", HttpStatus.OK);
	}

	@GetMapping("/orders/complete")
	public ResponseEntity<String> getOrdersdets() {
		if(logger.isLoggable(Level.INFO)) {logger.info("fetching all the order details");}
		return new ResponseEntity<>("User order details complete", HttpStatus.OK);
	}

}
