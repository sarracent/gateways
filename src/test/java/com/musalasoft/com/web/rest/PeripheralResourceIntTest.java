package com.musalasoft.com.web.rest;

import com.musalasoft.com.GatewaysApp;

import com.musalasoft.com.domain.Peripheral;
import com.musalasoft.com.domain.Gateway;
import com.musalasoft.com.repository.PeripheralRepository;
import com.musalasoft.com.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


import static com.musalasoft.com.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.musalasoft.com.domain.enumeration.Status;
/**
 * Test class for the PeripheralResource REST controller.
 *
 * @see PeripheralResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GatewaysApp.class)
public class PeripheralResourceIntTest {

    private static final Float DEFAULT_U_ID = 1F;
    private static final Float UPDATED_U_ID = 2F;

    private static final String DEFAULT_VENDOR = "AAAAAAAAAA";
    private static final String UPDATED_VENDOR = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_CREATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATED = LocalDate.now(ZoneId.systemDefault());

    private static final Status DEFAULT_STATUS = Status.ONLINE;
    private static final Status UPDATED_STATUS = Status.OFFLINE;

    @Autowired
    private PeripheralRepository peripheralRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPeripheralMockMvc;

    private Peripheral peripheral;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PeripheralResource peripheralResource = new PeripheralResource(peripheralRepository);
        this.restPeripheralMockMvc = MockMvcBuilders.standaloneSetup(peripheralResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Peripheral createEntity(EntityManager em) {
        Peripheral peripheral = new Peripheral()
            .uID(DEFAULT_U_ID)
            .vendor(DEFAULT_VENDOR)
            .dateCreated(DEFAULT_DATE_CREATED)
            .status(DEFAULT_STATUS);
        // Add required entity
        Gateway gateway = GatewayResourceIntTest.createEntity(em);
        em.persist(gateway);
        em.flush();
        peripheral.setGateway(gateway);
        return peripheral;
    }

    @Before
    public void initTest() {
        peripheral = createEntity(em);
    }

    @Test
    @Transactional
    public void createPeripheral() throws Exception {
        int databaseSizeBeforeCreate = peripheralRepository.findAll().size();

        // Create the Peripheral
        restPeripheralMockMvc.perform(post("/api/peripherals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(peripheral)))
            .andExpect(status().isCreated());

        // Validate the Peripheral in the database
        List<Peripheral> peripheralList = peripheralRepository.findAll();
        assertThat(peripheralList).hasSize(databaseSizeBeforeCreate + 1);
        Peripheral testPeripheral = peripheralList.get(peripheralList.size() - 1);
        assertThat(testPeripheral.getuID()).isEqualTo(DEFAULT_U_ID);
        assertThat(testPeripheral.getVendor()).isEqualTo(DEFAULT_VENDOR);
        assertThat(testPeripheral.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testPeripheral.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createPeripheralWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = peripheralRepository.findAll().size();

        // Create the Peripheral with an existing ID
        peripheral.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPeripheralMockMvc.perform(post("/api/peripherals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(peripheral)))
            .andExpect(status().isBadRequest());

        // Validate the Peripheral in the database
        List<Peripheral> peripheralList = peripheralRepository.findAll();
        assertThat(peripheralList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPeripherals() throws Exception {
        // Initialize the database
        peripheralRepository.saveAndFlush(peripheral);

        // Get all the peripheralList
        restPeripheralMockMvc.perform(get("/api/peripherals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(peripheral.getId().intValue())))
            .andExpect(jsonPath("$.[*].uID").value(hasItem(DEFAULT_U_ID.doubleValue())))
            .andExpect(jsonPath("$.[*].vendor").value(hasItem(DEFAULT_VENDOR.toString())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getPeripheral() throws Exception {
        // Initialize the database
        peripheralRepository.saveAndFlush(peripheral);

        // Get the peripheral
        restPeripheralMockMvc.perform(get("/api/peripherals/{id}", peripheral.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(peripheral.getId().intValue()))
            .andExpect(jsonPath("$.uID").value(DEFAULT_U_ID.doubleValue()))
            .andExpect(jsonPath("$.vendor").value(DEFAULT_VENDOR.toString()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPeripheral() throws Exception {
        // Get the peripheral
        restPeripheralMockMvc.perform(get("/api/peripherals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePeripheral() throws Exception {
        // Initialize the database
        peripheralRepository.saveAndFlush(peripheral);

        int databaseSizeBeforeUpdate = peripheralRepository.findAll().size();

        // Update the peripheral
        Peripheral updatedPeripheral = peripheralRepository.findById(peripheral.getId()).get();
        // Disconnect from session so that the updates on updatedPeripheral are not directly saved in db
        em.detach(updatedPeripheral);
        updatedPeripheral
            .uID(UPDATED_U_ID)
            .vendor(UPDATED_VENDOR)
            .dateCreated(UPDATED_DATE_CREATED)
            .status(UPDATED_STATUS);

        restPeripheralMockMvc.perform(put("/api/peripherals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPeripheral)))
            .andExpect(status().isOk());

        // Validate the Peripheral in the database
        List<Peripheral> peripheralList = peripheralRepository.findAll();
        assertThat(peripheralList).hasSize(databaseSizeBeforeUpdate);
        Peripheral testPeripheral = peripheralList.get(peripheralList.size() - 1);
        assertThat(testPeripheral.getuID()).isEqualTo(UPDATED_U_ID);
        assertThat(testPeripheral.getVendor()).isEqualTo(UPDATED_VENDOR);
        assertThat(testPeripheral.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testPeripheral.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingPeripheral() throws Exception {
        int databaseSizeBeforeUpdate = peripheralRepository.findAll().size();

        // Create the Peripheral

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeripheralMockMvc.perform(put("/api/peripherals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(peripheral)))
            .andExpect(status().isBadRequest());

        // Validate the Peripheral in the database
        List<Peripheral> peripheralList = peripheralRepository.findAll();
        assertThat(peripheralList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePeripheral() throws Exception {
        // Initialize the database
        peripheralRepository.saveAndFlush(peripheral);

        int databaseSizeBeforeDelete = peripheralRepository.findAll().size();

        // Get the peripheral
        restPeripheralMockMvc.perform(delete("/api/peripherals/{id}", peripheral.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Peripheral> peripheralList = peripheralRepository.findAll();
        assertThat(peripheralList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Peripheral.class);
        Peripheral peripheral1 = new Peripheral();
        peripheral1.setId(1L);
        Peripheral peripheral2 = new Peripheral();
        peripheral2.setId(peripheral1.getId());
        assertThat(peripheral1).isEqualTo(peripheral2);
        peripheral2.setId(2L);
        assertThat(peripheral1).isNotEqualTo(peripheral2);
        peripheral1.setId(null);
        assertThat(peripheral1).isNotEqualTo(peripheral2);
    }
}
