package com.translator.up.repository.support_ticket;

import com.translator.up.entity.SupportTicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicketEntity, Long> {

}
