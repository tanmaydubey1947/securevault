package com.securevault.userservice.producer;


import com.securevault.userservice.entity.AddMoney;
import com.securevault.userservice.entity.SendMoney;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProducerService {


    @Value("${producer.send.money.topic.name}")
    private String sendMoneyTopic;

    @Value("${producer.add.money.topic.name}")
    private String addMoneyTopic;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;


    public String sendMoney(SendMoney sendMoney) {
        kafkaTemplate.send(sendMoneyTopic, sendMoney);
        log.info("Pushed message to PaymentService to sendMoney...");
        return "Payment is in progress...";
    }


    public String addMoney(AddMoney addMoney) {
        kafkaTemplate.send(addMoneyTopic, addMoney);
        log.info("Pushed message to PaymentService to addMoney...");
        return "Payment is in progress...";
    }
}
