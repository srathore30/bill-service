package sfa.bill_service.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sfa.bill_service.entities.ServicesEntity;

import java.util.Optional;

@Repository
public interface ServicesRepo extends JpaRepository<ServicesEntity, Long> {
    Page<ServicesEntity> findByServiceCategoryId(Long serviceCategoryId, Pageable pageable);
    Optional<ServicesEntity> findByServiceCodeAndCghsLocation(String serviceCode, String cghsLocation);
}
