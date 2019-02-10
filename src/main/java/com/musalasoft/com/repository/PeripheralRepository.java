package com.musalasoft.com.repository;

import com.musalasoft.com.domain.Peripheral;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Peripheral entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PeripheralRepository extends JpaRepository<Peripheral, Long> {

}
