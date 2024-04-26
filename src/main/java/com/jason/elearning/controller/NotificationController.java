package com.jason.elearning.controller;

import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/")
@Transactional
public class NotificationController {

    // Map để lưu trữ các SseEmitters theo userId
    private final Map<Long, SseEmitter> userEmitters = new HashMap<>();

    @GetMapping(path = "/sse/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter handleSse(@PathVariable Long userId) {
        SseEmitter emitter = new SseEmitter();
        // Lưu SseEmitter cho userId
        userEmitters.put(userId, emitter);

        // Xử lý khi SseEmitter hoàn tất
        emitter.onCompletion(() -> userEmitters.remove(userId));
        emitter.onTimeout(() -> userEmitters.remove(userId));

        return emitter;
    }

    // Phương thức gửi thông báo tới người dùng
    public void sendNotification(Long userId, String notificationMessage) {
        SseEmitter emitter = userEmitters.get(userId);
        if (emitter != null) {
            try {
                emitter.send(notificationMessage);
            } catch (IOException e) {
                // Xử lý lỗi
                emitter.completeWithError(e);
            }
        }
    }

}
