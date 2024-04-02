package com.jason.elearning.repository.chat;

import com.jason.elearning.entity.request.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long>, MessageRepositoryCustom {
    List<Message> findAllBySenderNameAndReceiverNameOrderByCreatedAtAsc(String senderName,String receiverName);
}
