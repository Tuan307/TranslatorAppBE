package com.translator.up.controller;

import com.translator.up.aop.SessionRequired;
import com.translator.up.model.common.ApiResponse;
import com.translator.up.model.request.SupportTicketRequest;
import com.translator.up.model.response.SupportTicketDTO;
import com.translator.up.service.support_ticket.SupportTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/support/")
public class SupportTicketController {
    @Autowired
    private SupportTicketService supportTicketService;

    @GetMapping("")
    public ApiResponse<List<SupportTicketDTO>> getAllSupportTickets() {
        return supportTicketService.getAllSupportTicket();
    }

    @SessionRequired
    @PostMapping("/create")
    public ApiResponse<SupportTicketDTO> addSupportTicket(@RequestBody SupportTicketRequest request) {
        return supportTicketService.addSupportTicket(request);
    }
}
