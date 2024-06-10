package com.jason.elearning.controller;

import com.jason.elearning.entity.request.Message;
import com.jason.elearning.entity.response.BaseResponse;
import com.jason.elearning.repository.chat.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@Transactional
public class ChatController extends BaseController{
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private MessageRepository messageRepository;

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public Message receiveMessage(@Payload Message message){
        return message;
    }

    @MessageMapping("/private-message")
    public Message recMessage(@Payload Message message){

        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(),"/private",message);
        System.out.println(message.toString());


        return  messageRepository.save(message);}

    @GetMapping("api/v1/get-conversation")
    public ResponseEntity<?> getConersation(@RequestParam String senderName
            ,@RequestParam String receiverName) {
        try {

            return ResponseEntity.ok( messageRepository
                    .getConversation(senderName, receiverName));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    };
    @GetMapping("api/v1/user-conversationed")
    public ResponseEntity<?> getUserConersationed(@RequestParam String userName) {
        try {

            return ResponseEntity.ok( messageRepository
                    .getUserConversationed(userName));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }

}
