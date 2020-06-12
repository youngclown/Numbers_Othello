package com.chat.number.controller;

import com.chat.number.domain.ChatRoomForm;
import com.chat.number.domain.GameRoom;
import com.chat.number.repository.GameRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {
    private final GameRoomRepository gameRoomRepository;

    @GetMapping("/start")
    public String rooms(Model model){
        model.addAttribute("rooms",gameRoomRepository.findAllRoom());
        return "rooms";
    }

    @GetMapping("/rooms/{id}")
    public String room(@PathVariable String id, Model model){
        GameRoom room = gameRoomRepository.findRoomById(id);
        model.addAttribute("room",room);
        return "room";
    }

    @GetMapping("/new")
    public String make(Model model){
        ChatRoomForm form = new ChatRoomForm();
        model.addAttribute("form",form);
        return "newRoom";
    }

    @PostMapping("/room/new")
    public String makeRoom(ChatRoomForm form){
        gameRoomRepository.createChatRoom(form.getName());
        return "redirect:/";
    }

}
