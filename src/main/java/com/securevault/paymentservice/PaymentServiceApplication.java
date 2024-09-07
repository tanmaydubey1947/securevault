package com.securevault.paymentservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.securevault.paymentservice.service.PaymentService;
import com.securevault.userservice.entity.AddMoney;
import com.securevault.userservice.entity.SendMoney;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
public class PaymentServiceApplication {

	@Autowired
	PaymentService paymentService;

	public static void main(String[] args) {
		SpringApplication.run(PaymentServiceApplication.class, args);
	}


	@KafkaListener(topics = "SECURE_VAULT_ADD_MONEY", groupId = "Payment_SecureVault")
	public void consumeAddMoney(AddMoney addMoney) throws JsonProcessingException {
		log.info("add money consumed message {} ", new ObjectMapper().writeValueAsString(addMoney));
		paymentService.addMoney(addMoney);
	}

	@KafkaListener(topics = "SECURE_VAULT_SEND_MONEY", groupId = "Payment_SecureVault")
	public void consumeSendMoney(SendMoney sendMoney) throws JsonProcessingException {
		log.info("send money consumed message {} ", new ObjectMapper().writeValueAsString(sendMoney));
		paymentService.sendMoney(sendMoney);
	}

}
