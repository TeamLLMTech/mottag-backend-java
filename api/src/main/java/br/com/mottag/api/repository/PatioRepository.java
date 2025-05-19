package br.com.mottag.api.repository;

import br.com.mottag.api.model.Moto;
import br.com.mottag.api.model.Patio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatioRepository extends JpaRepository<Patio, Long> {
}
