package com.musalasoft.com.web.rest;

import com.musalasoft.com.GatewaysApp;

import com.musalasoft.com.domain.Gateway;
import com.musalasoft.com.repository.GatewayRepository;
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
import java.util.List;


import static com.musalasoft.com.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the GatewayResource REST controller.
 *
 * @see GatewayResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GatewaysApp.class)
public class GatewayResourceIntTest {

    private static final String DEFAULT_SERIAL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SERIAL_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_HUMAN_RADABLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_HUMAN_RADABLE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_I_PV_FOUR = "4F112X097?545";
    private static final String UPDATED_I_PV_FOUR = "136C51/765:3";

    @Autowired
    private GatewayRepository gatewayRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restGatewayMockMvc;

    private Gateway gateway;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GatewayResource gatewayResource = new GatewayResource(gatewayRepository);
        this.restGatewayMockMvc = MockMvcBuilders.standaloneSetup(gatewayResource)
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
    public static Gateway createEntity(EntityManager em) {
        Gateway gateway = new Gateway()
            .serialNumber(DEFAULT_SERIAL_NUMBER)
            .humanRadableName(DEFAULT_HUMAN_RADABLE_NAME)
            .iPVFour(DEFAULT_I_PV_FOUR);
        return gateway;
    }

    @Before
    public void initTest() {
        gateway = createEntity(em);
    }

    @Test
    @Transactional
    public void createGateway() throws Exception {
        int databaseSizeBeforeCreate = gatewayRepository.findAll().size();

        // Create the Gateway
        restGatewayMockMvc.perform(post("/api/gateways")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gateway)))
            .andExpect(status().isCreated());

        // Validate the Gateway in the database
        List<Gateway> gatewayList = gatewayRepository.findAll();
        assertThat(gatewayList).hasSize(databaseSizeBeforeCreate + 1);
        Gateway testGateway = gatewayList.get(gatewayList.size() - 1);
        assertThat(testGateway.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);
        assertThat(testGateway.getHumanRadableName()).isEqualTo(DEFAULT_HUMAN_RADABLE_NAME);
        assertThat(testGateway.getiPVFour()).isEqualTo(DEFAULT_I_PV_FOUR);
    }

    @Test
    @Transactional
    public void createGatewayWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = gatewayRepository.findAll().size();

        // Create the Gateway with an existing ID
        gateway.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGatewayMockMvc.perform(post("/api/gateways")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gateway)))
            .andExpect(status().isBadRequest());

        // Validate the Gateway in the database
        List<Gateway> gatewayList = gatewayRepository.findAll();
        assertThat(gatewayList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllGateways() throws Exception {
        // Initialize the database
        gatewayRepository.saveAndFlush(gateway);

        // Get all the gatewayList
        restGatewayMockMvc.perform(get("/api/gateways?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gateway.getId().intValue())))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].humanRadableName").value(hasItem(DEFAULT_HUMAN_RADABLE_NAME.toString())))
            .andExpect(jsonPath("$.[*].iPVFour").value(hasItem(DEFAULT_I_PV_FOUR.toString())));
    }
    
    @Test
    @Transactional
    public void getGateway() throws Exception {
        // Initialize the database
        gatewayRepository.saveAndFlush(gateway);

        // Get the gateway
        restGatewayMockMvc.perform(get("/api/gateways/{id}", gateway.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(gateway.getId().intValue()))
            .andExpect(jsonPath("$.serialNumber").value(DEFAULT_SERIAL_NUMBER.toString()))
            .andExpect(jsonPath("$.humanRadableName").value(DEFAULT_HUMAN_RADABLE_NAME.toString()))
            .andExpect(jsonPath("$.iPVFour").value(DEFAULT_I_PV_FOUR.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGateway() throws Exception {
        // Get the gateway
        restGatewayMockMvc.perform(get("/api/gateways/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGateway() throws Exception {
        // Initialize the database
        gatewayRepository.saveAndFlush(gateway);

        int databaseSizeBeforeUpdate = gatewayRepository.findAll().size();

        // Update the gateway
        Gateway updatedGateway = gatewayRepository.findById(gateway.getId()).get();
        // Disconnect from session so that the updates on updatedGateway are not directly saved in db
        em.detach(updatedGateway);
        updatedGateway
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .humanRadableName(UPDATED_HUMAN_RADABLE_NAME)
            .iPVFour(UPDATED_I_PV_FOUR);

        restGatewayMockMvc.perform(put("/api/gateways")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGateway)))
            .andExpect(status().isOk());

        // Validate the Gateway in the database
        List<Gateway> gatewayList = gatewayRepository.findAll();
        assertThat(gatewayList).hasSize(databaseSizeBeforeUpdate);
        Gateway testGateway = gatewayList.get(gatewayList.size() - 1);
        assertThat(testGateway.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testGateway.getHumanRadableName()).isEqualTo(UPDATED_HUMAN_RADABLE_NAME);
        assertThat(testGateway.getiPVFour()).isEqualTo(UPDATED_I_PV_FOUR);
    }

    @Test
    @Transactional
    public void updateNonExistingGateway() throws Exception {
        int databaseSizeBeforeUpdate = gatewayRepository.findAll().size();

        // Create the Gateway

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGatewayMockMvc.perform(put("/api/gateways")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gateway)))
            .andExpect(status().isBadRequest());

        // Validate the Gateway in the database
        List<Gateway> gatewayList = gatewayRepository.findAll();
        assertThat(gatewayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGateway() throws Exception {
        // Initialize the database
        gatewayRepository.saveAndFlush(gateway);

        int databaseSizeBeforeDelete = gatewayRepository.findAll().size();

        // Get the gateway
        restGatewayMockMvc.perform(delete("/api/gateways/{id}", gateway.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Gateway> gatewayList = gatewayRepository.findAll();
        assertThat(gatewayList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Gateway.class);
        Gateway gateway1 = new Gateway();
        gateway1.setId(1L);
        Gateway gateway2 = new Gateway();
        gateway2.setId(gateway1.getId());
        assertThat(gateway1).isEqualTo(gateway2);
        gateway2.setId(2L);
        assertThat(gateway1).isNotEqualTo(gateway2);
        gateway1.setId(null);
        assertThat(gateway1).isNotEqualTo(gateway2);
    }
}
