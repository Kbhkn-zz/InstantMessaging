package com.instantmessaging.rest;

import com.instantmessaging.domain.MessageOfUsers;
import com.instantmessaging.domain.User;
import com.instantmessaging.dto.UserMessageDto;
import com.instantmessaging.repository.MessageOfUsersRepository;
import com.instantmessaging.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api/service/")
public class MessageService {
    private final Logger log = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageOfUsersRepository messageOfUsersRepository;


    @PostMapping(value = "/register", produces = MediaType.TEXT_PLAIN_VALUE)
    private ResponseEntity<?> register(@RequestBody String phoneNumber) {
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
        if (user.isPresent())
            return new ResponseEntity<Object>("This phone number is exist!", HttpStatus.OK);

        long pKey;

        while (true) {
            pKey = ThreadLocalRandom.current().nextInt(100, 999 + 1);
            if (!userRepository.findByPublicKey(pKey).isPresent())
                break;
        }

        User newUser = new User(phoneNumber, pKey);

        log.info("New User --> {}", newUser);
        userRepository.save(newUser);

        return new ResponseEntity<Object>(String.valueOf(pKey), HttpStatus.OK);

    }

    @GetMapping(value = "/getPkey/{phoneNumber:^[(0)(+90)]+[0-9]{10}$}", headers = "Accept=*/*", produces = MediaType.TEXT_PLAIN_VALUE)
    private ResponseEntity<?> getUserPkeyByPhoneNumber(@PathVariable("phoneNumber") String phoneNumber) {
        Optional<User> u = userRepository.findByPhoneNumber(phoneNumber);

        if (u.isPresent()) {
            log.info("Fetching public key for --> {} phone number.", phoneNumber);
            return new ResponseEntity<Object>(String.valueOf(u.get().getPublicKey()), HttpStatus.OK);
        }

        log.error(">>> {} <<< phone number dosnt't have registered.", phoneNumber);
        return new ResponseEntity<Object>("This phone number doesn't have any registered users.", HttpStatus.OK);
    }

    @GetMapping(value = "/getMessage/{senderPhoneNo:^[(0)(+90)]+[0-9]{10}$}/{receiverPhoneNo:^[(0)(+90)]+[0-9]{10}$}", headers = "Accept=*/*", produces = MediaType.TEXT_PLAIN_VALUE)
    private ResponseEntity<?> getMessage(@PathVariable("senderPhoneNo") String sender, @PathVariable("receiverPhoneNo") String receiver) {
        Optional<MessageOfUsers> message = messageOfUsersRepository.getMessageBySenderUserPhoneAndReceiverUserPhone(sender, receiver);

        if (message.isPresent()) {
            log.info("Fetching encrypted message by Sender >>> {} <<<, Receiver >>> {} <<<", sender, receiver);
            return new ResponseEntity<Object>(message.get().getSenderMessage(), HttpStatus.OK);
        }

        return new ResponseEntity<Object>(String.format("You didn't receive any messages from the phone number of %s", sender), HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteMessage/{messageId:^[0-9]*$}/{receiverPhoneNo:^[(0)(+90)]+[0-9]{10}$}", produces = MediaType.TEXT_PLAIN_VALUE)
    private ResponseEntity<?> deleteMessage(@PathVariable("messageId") long messageId, @PathVariable("receiverPhoneNo") String receiver) {
        Optional<MessageOfUsers> message = messageOfUsersRepository.getMessageByReceiverPhoneNo(receiver);
        if (message.isPresent() && message.get().getId() == messageId) {
            messageOfUsersRepository.delete(message.get());
            log.info("Encrypted message has been deleted. Receiver phone number >>> {} <<<", receiver);
            return new ResponseEntity<Object>(HttpStatus.OK);
        }

        log.error("Incorrent Operation.. Phone >>> {} <<< , MessageId >>> {} <<<", receiver, messageId);
        return new ResponseEntity<Object>("Incorrent Operation. Do not try again!", HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/sendMessage", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    private ResponseEntity<?> sendMessage(@RequestBody UserMessageDto messageDto) {
        MessageOfUsers messageOfUsers = new MessageOfUsers();

        if (messageDto == null || messageDto.getSenderPhone() == null || messageDto.getReceiverPhone() == null || messageDto.getMessage() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String senderPhoneNo = messageDto.getSenderPhone();
        Optional<User> sender = userRepository.findByPhoneNumber(senderPhoneNo);
        if (!sender.isPresent()) {
            return new ResponseEntity<Object>("Sender phone number dosnt't have registered.", HttpStatus.BAD_REQUEST);
        }

        String receiverPhoneNo = messageDto.getReceiverPhone();
        Optional<User> receiver = userRepository.findByPhoneNumber(receiverPhoneNo);
        if (!receiver.isPresent()) {
            return new ResponseEntity<Object>("Receiver phone number dosnt't have registered.", HttpStatus.BAD_REQUEST);
        }

        messageOfUsers.setSenderUser(sender.get());
        messageOfUsers.setReceiverUser(receiver.get());
        messageOfUsers.setSenderMessage(messageDto.getMessage());

        messageOfUsersRepository.save(messageOfUsers);

        log.info("The new encrypted message was saved! Sender >>> {} <<<, Receiver >>> {} <<<", senderPhoneNo, receiverPhoneNo);
        return new ResponseEntity<Object>(String.valueOf(messageOfUsers.getId()), HttpStatus.CREATED);
    }
}
