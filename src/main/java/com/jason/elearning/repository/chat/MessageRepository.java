package com.jason.elearning.repository.chat;

import com.jason.elearning.entity.constants.UnseenMessageCount;
import com.jason.elearning.entity.request.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long>, MessageRepositoryCustom {
    List<Message> findAllBySenderNameAndReceiverNameOrderByCreatedAtAsc(String senderName,String receiverName);
    @Query("SELECT new com.jason.elearning.entity.constants.UnseenMessageCount(m.senderName, COUNT(m)) " +
            "FROM Message m WHERE m.receiverName = :receiverName AND m.readStatus = 1 " +
            "GROUP BY m.senderName")
    List<UnseenMessageCount> findUnseenMessagesCountByReceiver(@Param("receiverName") String receiverName);
}
