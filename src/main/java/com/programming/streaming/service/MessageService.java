package com.programming.streaming.service;

import com.programming.streaming.model.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import com.programming.streaming.repository.Client.MessageRepository;
@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    public List<Message> getMessages(String room) {
        return messageRepository.findAllByRoom(room);
    }

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }
    
}