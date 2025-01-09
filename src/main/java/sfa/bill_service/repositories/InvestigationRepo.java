package sfa.bill_service.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sfa.bill_service.entities.InvestigationEntity;

import java.util.List;

@Repository
public interface InvestigationRepo extends JpaRepository<InvestigationEntity, Long> {
}
