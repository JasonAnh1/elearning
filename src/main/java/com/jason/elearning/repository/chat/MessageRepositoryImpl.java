package com.jason.elearning.repository.chat;

import com.jason.elearning.entity.request.Message;
import com.jason.elearning.entity.request.QMessage;
import com.jason.elearning.repository.BaseRepository;
import com.querydsl.core.BooleanBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MessageRepositoryImpl  extends BaseRepository implements MessageRepositoryCustom{
    @Override
    public List<Message> getConversation(String senderName, String receiverName) {

        QMessage qMessage = QMessage.message1;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qMessage.senderName.eq(senderName).and(qMessage.receiverName.eq(receiverName)))
        .or(qMessage.senderName.eq(receiverName).and(qMessage.receiverName.eq(senderName)));

        return query().select(qMessage).from(qMessage)

                .where(builder)
                .orderBy(qMessage.createdAt.asc())
                .fetch();

    }

    @Override
    public List<String> getUserConversationed(String userName) {
        QMessage qMessage = QMessage.message1;
        Set<String> userNamesSet = new HashSet<>();
        List<String> userNames = query().selectDistinct(qMessage.senderName)
                .from(qMessage)
                .where(qMessage.senderName.ne(userName))
                .fetch();

        List<String> receiverNames = query().selectDistinct(qMessage.receiverName)
                .from(qMessage)
                .where(qMessage.receiverName.ne(userName))
                .fetch();

        userNamesSet.addAll(userNames);
        userNamesSet.addAll(receiverNames);

        return  new ArrayList<>(userNamesSet);
    }
}
