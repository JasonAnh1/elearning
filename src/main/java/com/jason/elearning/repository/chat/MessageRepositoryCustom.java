package com.jason.elearning.repository.chat;

import com.jason.elearning.entity.request.Message;

import java.util.List;

public interface MessageRepositoryCustom {
    List<Message> getConversation(String senderName,String receiverName);
    List<String> getUserConversationed(String userName);
}
