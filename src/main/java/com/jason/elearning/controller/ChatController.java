package com.jason.elearning.controller;

import com.jason.elearning.entity.constants.ReadStatus;
import com.jason.elearning.entity.constants.UnseenMessageCount;
import com.jason.elearning.entity.request.Message;
import com.jason.elearning.entity.response.BaseResponse;
import com.jason.elearning.repository.chat.MessageRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        message.setReadStatus(ReadStatus.UNSEEN);
        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(),"/private",message);



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
    };
    @PostMapping("api/v1/mark-messages-as-read")
    public ResponseEntity<?> markMessagesAsRead(@RequestBody MessageReadRequest request) {
        try {
            List<Message> messages = messageRepository.getConversation(request.getSenderName(), request.getReceiverName());
            for (Message message : messages) {
                // Chỉ cập nhật trạng thái những tin nhắn mà đối phương gửi tới mình
                if (message.getSenderName().equals(request.getSenderName()) && message.getReadStatus() == ReadStatus.UNSEEN) {
                    message.setReadStatus(ReadStatus.SEEN);
                }
            }
            messageRepository.saveAll(messages);
            return ResponseEntity.ok(new BaseResponse("Messages marked as read", null));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @GetMapping("api/v1/unseen-messages-count")
    public ResponseEntity<?> getUnseenMessagesCount(@RequestParam String receiverName) {
        try {
            List<UnseenMessageCount> unseenMessagesCounts = messageRepository.findUnseenMessagesCountByReceiver(receiverName);
            return ResponseEntity.ok(unseenMessagesCounts);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }



};
@Getter
@Setter
class MessageReadRequest {
    private String senderName;
    private String receiverName;

    // Getters and Setters

}
