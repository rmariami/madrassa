package fr.rmariami.madrassa.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A PersonInCharge.
 */
@Entity
@Table(name = "person_in_charge")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "personincharge")
public class PersonInCharge implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "adress", nullable = false)
    private String adress;

    @NotNull
    @Column(name = "work", nullable = false)
    private String work;

    @Column(name = "phone_number")
    private String phoneNumber;

    @NotNull
    @Column(name = "mobile_phone_number", nullable = false)
    private String mobilePhoneNumber;

    @Column(name = "email")
    private String email;

    @ManyToMany(mappedBy = "personInCharges")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Scholar> scholars = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Scholar> getScholars() {
        return scholars;
    }

    public void setScholars(Set<Scholar> scholars) {
        this.scholars = scholars;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PersonInCharge personInCharge = (PersonInCharge) o;
        if(personInCharge.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, personInCharge.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PersonInCharge{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", firstName='" + firstName + "'" +
            ", adress='" + adress + "'" +
            ", work='" + work + "'" +
            ", phoneNumber='" + phoneNumber + "'" +
            ", mobilePhoneNumber='" + mobilePhoneNumber + "'" +
            ", email='" + email + "'" +
            '}';
    }
}
