package com.musalasoft.com.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.musalasoft.com.domain.Peripheral;
import com.musalasoft.com.repository.PeripheralRepository;
import com.musalasoft.com.web.rest.errors.BadRequestAlertException;
import com.musalasoft.com.web.rest.util.HeaderUtil;
import com.musalasoft.com.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Peripheral.
 */
@RestController
@RequestMapping("/api")
public class PeripheralResource {

    private final Logger log = LoggerFactory.getLogger(PeripheralResource.class);

    private static final String ENTITY_NAME = "peripheral";

    private final PeripheralRepository peripheralRepository;

    public PeripheralResource(PeripheralRepository peripheralRepository) {
        this.peripheralRepository = peripheralRepository;
    }

    /**
     * POST  /peripherals : Create a new peripheral.
     *
     * @param peripheral the peripheral to create
     * @return the ResponseEntity with status 201 (Created) and with body the new peripheral, or with status 400 (Bad Request) if the peripheral has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/peripherals")
    @Timed
    public ResponseEntity<Peripheral> createPeripheral(@Valid @RequestBody Peripheral peripheral) throws URISyntaxException {
        log.debug("REST request to save Peripheral : {}", peripheral);
        if (peripheral.getId() != null) {
            throw new BadRequestAlertException("A new peripheral cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Peripheral result = peripheralRepository.save(peripheral);
        return ResponseEntity.created(new URI("/api/peripherals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /peripherals : Updates an existing peripheral.
     *
     * @param peripheral the peripheral to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated peripheral,
     * or with status 400 (Bad Request) if the peripheral is not valid,
     * or with status 500 (Internal Server Error) if the peripheral couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/peripherals")
    @Timed
    public ResponseEntity<Peripheral> updatePeripheral(@Valid @RequestBody Peripheral peripheral) throws URISyntaxException {
        log.debug("REST request to update Peripheral : {}", peripheral);
        if (peripheral.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Peripheral result = peripheralRepository.save(peripheral);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, peripheral.getId().toString()))
            .body(result);
    }

    /**
     * GET  /peripherals : get all the peripherals.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of peripherals in body
     */
    @GetMapping("/peripherals")
    @Timed
    public ResponseEntity<List<Peripheral>> getAllPeripherals(Pageable pageable) {
        log.debug("REST request to get a page of Peripherals");
        Page<Peripheral> page = peripheralRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/peripherals");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /peripherals/:id : get the "id" peripheral.
     *
     * @param id the id of the peripheral to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the peripheral, or with status 404 (Not Found)
     */
    @GetMapping("/peripherals/{id}")
    @Timed
    public ResponseEntity<Peripheral> getPeripheral(@PathVariable Long id) {
        log.debug("REST request to get Peripheral : {}", id);
        Optional<Peripheral> peripheral = peripheralRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(peripheral);
    }

    /**
     * DELETE  /peripherals/:id : delete the "id" peripheral.
     *
     * @param id the id of the peripheral to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/peripherals/{id}")
    @Timed
    public ResponseEntity<Void> deletePeripheral(@PathVariable Long id) {
        log.debug("REST request to delete Peripheral : {}", id);

        peripheralRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
