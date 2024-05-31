package com.programming.streaming.controller.Client;

import com.programming.streaming.model.Message;
import com.programming.streaming.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor

public class MessageController {
    private final MessageService messageService;

    @CrossOrigin
    @GetMapping("/{room}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable String room) {
        return ResponseEntity.ok(messageService.getMessages(room));
    }
}
