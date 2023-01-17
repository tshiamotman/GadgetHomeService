package za.co.wethinkcode.gadgethomeserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.wethinkcode.gadgethomeserver.models.domain.ChatDto;
import za.co.wethinkcode.gadgethomeserver.models.domain.DeviceTokenDto;
import za.co.wethinkcode.gadgethomeserver.models.domain.UserDto;
import za.co.wethinkcode.gadgethomeserver.service.ChatService;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/sendMessage")
    public ResponseEntity<?> sendMessageToDevice(@RequestBody ChatDto chat) {
        try {
            return ResponseEntity.ok(chatService.sendMessage(chat));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/updateDeviceId")
    public ResponseEntity<?> updateDeviceId(@RequestBody DeviceTokenDto deviceTokenDto) {
        try {
            return ResponseEntity.ok(chatService.updateDeviceId(deviceTokenDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/messageRead/{messageId}")
    public ResponseEntity<?> messageRead(@PathVariable("messageId") String messageId) {
        try {
            return ResponseEntity.ok(chatService.setMessageToRead(messageId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getConversations")
    public ResponseEntity<?> getConversations() {
        try {
            return ResponseEntity.ok(chatService.getAllConversations());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
