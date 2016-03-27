package fr.rmariami.madrassa.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import fr.rmariami.madrassa.domain.enumeration.SexEnum;

/**
 * A Scholar.
 */
@Entity
@Table(name = "scholar")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "scholar")
public class Scholar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "scholar_number", nullable = false)
    private Integer scholarNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "sex")
    private SexEnum sex;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "birth_date", nullable = false)
    private ZonedDateTime birthDate;

    @NotNull
    @Column(name = "birth_place", nullable = false)
    private String birthPlace;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "photo_content_type")    
    private String photoContentType;

    @NotNull
    @Column(name = "nb_years_xp", nullable = false)
    private Integer nbYearsXP;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "scholar_person_in_charge",
               joinColumns = @JoinColumn(name="scholars_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="person_in_charges_id", referencedColumnName="ID"))
    private Set<PersonInCharge> personInCharges = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getScholarNumber() {
        return scholarNumber;
    }

    public void setScholarNumber(Integer scholarNumber) {
        this.scholarNumber = scholarNumber;
    }

    public SexEnum getSex() {
        return sex;
    }

    public void setSex(SexEnum sex) {
        this.sex = sex;
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

    public ZonedDateTime getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(ZonedDateTime birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return photoContentType;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public Integer getNbYearsXP() {
        return nbYearsXP;
    }

    public void setNbYearsXP(Integer nbYearsXP) {
        this.nbYearsXP = nbYearsXP;
    }

    public Set<PersonInCharge> getPersonInCharges() {
        return personInCharges;
    }

    public void setPersonInCharges(Set<PersonInCharge> personInCharges) {
        this.personInCharges = personInCharges;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Scholar scholar = (Scholar) o;
        if(scholar.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, scholar.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Scholar{" +
            "id=" + id +
            ", scholarNumber='" + scholarNumber + "'" +
            ", sex='" + sex + "'" +
            ", name='" + name + "'" +
            ", firstName='" + firstName + "'" +
            ", birthDate='" + birthDate + "'" +
            ", birthPlace='" + birthPlace + "'" +
            ", photo='" + photo + "'" +
            ", photoContentType='" + photoContentType + "'" +
            ", nbYearsXP='" + nbYearsXP + "'" +
            '}';
    }
}
