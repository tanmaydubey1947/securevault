//package com.securevault.paymentservice.consumer;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.securevault.paymentservice.dto.AddMoney;
//import com.securevault.paymentservice.dto.SendMoney;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.annotation.KafkaListener;
//
//@Slf4j
//public class PaymentConsumer {
//
//    @KafkaListener(topics = "SECURE_VAULT_ADD_MONEY", groupId = "Payment_SecureVault")
//    public void consumeAddMoney(AddMoney addMoney) throws JsonProcessingException {
//        log.info("add money consumed message {} ", new ObjectMapper().writeValueAsString(addMoney));
//    }
//
//    @KafkaListener(topics = "SECURE_VAULT_SEND_MONEY", groupId = "Payment_SecureVault")
//    public void consumeSendMoney(SendMoney sendMoney) throws JsonProcessingException {
//        log.info("add money consumed message {} ", new ObjectMapper().writeValueAsString(sendMoney));
//    }
//}
