package pl.piomin.microservices.advanced.product.contract;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("account-service")
public interface AccountClient {

	@GetMapping("/accounts/{accountId}")
	Account getAccount(@PathVariable("accountId") String accountId);

}
