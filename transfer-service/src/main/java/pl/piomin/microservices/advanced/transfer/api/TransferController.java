package pl.piomin.microservices.advanced.transfer.api;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.client.RestTemplate;
import pl.piomin.microservices.advanced.transfer.contract.AccountClient;
import pl.piomin.microservices.advanced.transfer.model.Transfer;
import pl.piomin.microservices.advanced.transfer.model.TransferDTO;
import pl.piomin.microservices.advanced.transfer.repository.TransferRepository;

@RestController
public class TransferController {

	@Autowired
	private AccountClient accountClient;
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	TransferRepository repository;
	
	protected Logger logger = Logger.getLogger(TransferController.class.getName());
	
	@GetMapping("/transfers/sender/{sender}")
	public List<Transfer> findBySender(@PathVariable("sender") String sender) {
		if(logger.isLoggable(Level.INFO)) {logger.info(String.format("Transfer.findBySender(%s)", sender));}
		return repository.findBySender(sender);
	}
	
	@GetMapping("/transfers/recipient/{recipient}")
	public List<Transfer> findByRecipient(@PathVariable("recipient") String recipient) {
		if(logger.isLoggable(Level.INFO)) {logger.info(String.format("Transfer.findByRecipient(%s)", recipient));}
		return repository.findByRecipient(recipient);
	}
	
	@GetMapping("/transfers")
	public List<Transfer> findAll() {
		if(logger.isLoggable(Level.INFO)) {logger.info("Transfer.findAll()");}
		return repository.findAll();
	}
	
	@GetMapping("/transfers/{id}")
	public Transfer findById(@PathVariable("id") String id) {
		if(logger.isLoggable(Level.INFO)) {logger.info(String.format("Transfer.findById(%s)", id));}
		return repository.findById(id);
	}
	
	@PostMapping("/transfers")
	public Transfer add(@RequestBody TransferDTO transferDto) {
		if(logger.isLoggable(Level.INFO)) {logger.info(String.format("Transfer.add(%s)", transferDto));}
		Transfer transfer = new Transfer();
		BeanUtils.copyProperties(transferDto, transfer);
		return repository.save(transfer);
	}

	@GetMapping("/orders")
	public ResponseEntity<String> transferOrders() {
		if(logger.isLoggable(Level.INFO)) {logger.info("Finding order details from Transfer service");}
		restTemplate.getForObject("http://localhost:2222/orders/complete", String.class);
		return new ResponseEntity<>(" Order details returned from the Transfer service", HttpStatus.OK);
	}
	
}
