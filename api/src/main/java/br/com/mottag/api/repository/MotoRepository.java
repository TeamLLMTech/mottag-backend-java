package br.com.mottag.api.repository;

import br.com.mottag.api.model.Moto;
import br.com.mottag.api.model.StatusMoto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MotoRepository extends JpaRepository<Moto, Long> {
    @Query("SELECT m FROM Moto m WHERE " +
            "(:idPatio IS NULL OR m.patio.idPatio = :idPatio) AND " +
            "(:status IS NULL OR m.status = :status)")
    Page<Moto> findAllWithFilters(@Param("idPatio") Long idPatio,
                                  @Param("status") StatusMoto status,
                                  Pageable pageable);
}
