package com.musalasoft.com.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import com.musalasoft.com.domain.enumeration.Status;

/**
 * A Peripheral.
 */
@Entity
@Table(name = "peripheral")
public class Peripheral implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "u_id")
    private Float uID;

    @Column(name = "vendor")
    private String vendor;

    @Column(name = "date_created")
    private LocalDate dateCreated;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Gateway gateway;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getuID() {
        return uID;
    }

    public Peripheral uID(Float uID) {
        this.uID = uID;
        return this;
    }

    public void setuID(Float uID) {
        this.uID = uID;
    }

    public String getVendor() {
        return vendor;
    }

    public Peripheral vendor(String vendor) {
        this.vendor = vendor;
        return this;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public Peripheral dateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Status getStatus() {
        return status;
    }

    public Peripheral status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Gateway getGateway() {
        return gateway;
    }

    public Peripheral gateway(Gateway gateway) {
        this.gateway = gateway;
        return this;
    }

    public void setGateway(Gateway gateway) {
        this.gateway = gateway;
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
        Peripheral peripheral = (Peripheral) o;
        if (peripheral.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), peripheral.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Peripheral{" +
            "id=" + getId() +
            ", uID=" + getuID() +
            ", vendor='" + getVendor() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
