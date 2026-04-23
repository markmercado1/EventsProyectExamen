package com.emm.msregistrations.repositorys;

import com.emm.msregistrations.enums.RegistrationStatus;
import com.emm.msregistrations.models.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    List<Registration> findByEventId(Long eventId);

    List<Registration> findByParticipantId(Long participantId);

    List<Registration> findByStatus(RegistrationStatus status);

    Optional<Registration> findByEventIdAndParticipantId(Long eventId, Long participantId);

    boolean existsByEventIdAndParticipantId(Long eventId, Long participantId);

    List<Registration> findByPaymentOrderId(Long paymentOrderId);
}