package sfa.bill_service.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sfa.bill_service.entities.MedicalServicesEntity;

import java.util.Optional;

@Repository
public interface ServicesRepo extends JpaRepository<MedicalServicesEntity, Long> {
    Page<MedicalServicesEntity> findByServiceCategoryId(Long serviceCategoryId, Pageable pageable);
    @Query("SELECT b FROM MedicalServicesEntity b " + "WHERE b.serviceCode = :serviceCode " + "AND b.serviceCategory.cghsLocation = :cghsLocation")
    Optional<MedicalServicesEntity> findByServiceCodeAndCghsLocation(@Param("serviceCode")String serviceCode, @Param("cghsLocation") String cghsLocation);
}
