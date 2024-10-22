package com.translator.up.service.support_ticket;

import com.translator.up.entity.SupportTicketEntity;
import com.translator.up.entity.UserEntity;
import com.translator.up.model.common.ApiResponse;
import com.translator.up.model.request.SupportTicketRequest;
import com.translator.up.model.response.SupportTicketDTO;
import com.translator.up.repository.support_ticket.SupportTicketRepository;
import com.translator.up.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SupportTicketService {
    @Autowired
    private SupportTicketRepository supportTicketRepository;
    @Autowired
    private UserRepository userRepository;

    public ApiResponse<List<SupportTicketDTO>> getAllSupportTicket() {
        return new ApiResponse<>("success", "Successful", supportTicketRepository.findAll().stream().map(SupportTicketEntity::mapToDTO).collect(Collectors.toList()), "");
    }

    public ApiResponse<SupportTicketDTO> addSupportTicket(SupportTicketRequest request) {
        Optional<UserEntity> user = userRepository.findById(request.getUserId());
        if (user.isEmpty()) {
            return new ApiResponse<>("error", "Error", null, "400");
        }
        SupportTicketEntity supportTicketEntity = new SupportTicketEntity();
        supportTicketEntity.setDescription(request.getDescription());
        supportTicketEntity.setTitle(request.getTitle());
        supportTicketEntity.setUserSupport(user.get());
        supportTicketRepository.save(supportTicketEntity);
        return new ApiResponse<>("success", "Successful", supportTicketEntity.mapToDTO(), "");
    }
}
