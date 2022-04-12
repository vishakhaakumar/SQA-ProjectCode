package pl.piomin.microservices.advanced.product.api;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.client.RestTemplate;
import pl.piomin.microservices.advanced.product.contract.Account;
import pl.piomin.microservices.advanced.product.contract.AccountClient;
import pl.piomin.microservices.advanced.product.model.Product;
import pl.piomin.microservices.advanced.product.model.ProductDTO;
import pl.piomin.microservices.advanced.product.repository.ProductRepository;

@RestController
public class ProductController {
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private AccountClient accountClient;
	
	@Autowired
	ProductRepository repository;
	
	protected Logger logger = Logger.getLogger(ProductController.class.getName());
	
	@RequestMapping("/products/account/{accountId}")
	public Product findByPesel(@PathVariable("accountId") String accountId) {
		if(logger.isLoggable(Level.INFO)) {logger.info((String.format("Product.findByAccountId(%s)", accountId)));}
		return repository.findByAccountId(accountId);
	}
	
	@RequestMapping("/products")
	public List<Product> findAll() {
		logger.info("Product.findAll()");
		return repository.findAll();
	}
	
	@RequestMapping("/products/{id}")
	public Product findById(@PathVariable("id") String id) {
		if(logger.isLoggable(Level.INFO)) {logger.info((String.format("Product.findById(%s)", id)));}
		Product product = repository.findById(id);
		Account account =  accountClient.getAccount(id);
		product.setCustomerId(account.getCustomerId());
		return product;
	}
	
	@PostMapping("/products")
	public Product add(@RequestBody ProductDTO productDto) {
		if(logger.isLoggable(Level.INFO)) {logger.info(String.format("Product.add(%s)", productDto));}
		Product product = new Product();
		BeanUtils.copyProperties(productDto, product);
		return repository.save(product);
	}

	@GetMapping("/orders")
	public ResponseEntity<String> findOrders() {
		if(logger.isLoggable(Level.INFO)) {logger.info(("Finding order details from Product service"));}
		restTemplate.getForObject("http://localhost:5555/orders/", String.class);
		return new ResponseEntity<>(" Order details returned from the product service", HttpStatus.OK);
	}
	
}
