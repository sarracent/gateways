package com.musalasoft.com.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Gateway.
 */
@Entity
@Table(name = "gateway")
public class Gateway implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @Column(name = "serial_number", unique = true)
    private String serialNumber;

    @Column(name = "human_radable_name")
    private String humanRadableName;

    @Pattern(regexp = "^(?:[0-9]{1,3}.){3}[0-9]{1,3}$")
    @Column(name = "i_pv_four")
    private String iPVFour;

    @OneToMany(mappedBy = "gateway")
    private Set<Peripheral> peripherals = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public Gateway serialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
        return this;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getHumanRadableName() {
        return humanRadableName;
    }

    public Gateway humanRadableName(String humanRadableName) {
        this.humanRadableName = humanRadableName;
        return this;
    }

    public void setHumanRadableName(String humanRadableName) {
        this.humanRadableName = humanRadableName;
    }

    public String getiPVFour() {
        return iPVFour;
    }

    public Gateway iPVFour(String iPVFour) {
        this.iPVFour = iPVFour;
        return this;
    }

    public void setiPVFour(String iPVFour) {
        this.iPVFour = iPVFour;
    }

    public Set<Peripheral> getPeripherals() {
        return peripherals;
    }

    public Gateway peripherals(Set<Peripheral> peripherals) {
        this.peripherals = peripherals;
        return this;
    }

    public Gateway addPeripheral(Peripheral peripheral) {
        this.peripherals.add(peripheral);
        peripheral.setGateway(this);
        return this;
    }

    public Gateway removePeripheral(Peripheral peripheral) {
        this.peripherals.remove(peripheral);
        peripheral.setGateway(null);
        return this;
    }

    public void setPeripherals(Set<Peripheral> peripherals) {
        this.peripherals = peripherals;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Gateway gateway = (Gateway) o;
        if (gateway.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), gateway.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Gateway{" +
            "id=" + getId() +
            ", serialNumber='" + getSerialNumber() + "'" +
            ", humanRadableName='" + getHumanRadableName() + "'" +
            ", iPVFour='" + getiPVFour() + "'" +
            "}";
    }
}
