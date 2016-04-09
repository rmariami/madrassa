package fr.rmariami.madrassa.web.rest.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import fr.rmariami.madrassa.domain.enumeration.InscriptionStatusEnum;

/**
 * A DTO for the Inscription entity.
 */
public class InscriptionDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime date;


    @NotNull
    private BigDecimal price;


    @NotNull
    private InscriptionStatusEnum status;


    private Long classRoomId;

    private String classRoomCode;

    private Long scholarId;

    private String scholarName;

    private Long authorId;

    private String authorFirstName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public InscriptionStatusEnum getStatus() {
        return status;
    }

    public void setStatus(InscriptionStatusEnum status) {
        this.status = status;
    }

    public Long getClassRoomId() {
        return classRoomId;
    }

    public void setClassRoomId(Long classRoomId) {
        this.classRoomId = classRoomId;
    }

    public String getClassRoomCode() {
        return classRoomCode;
    }

    public void setClassRoomCode(String classRoomCode) {
        this.classRoomCode = classRoomCode;
    }

    public Long getScholarId() {
        return scholarId;
    }

    public void setScholarId(Long scholarId) {
        this.scholarId = scholarId;
    }

    public String getScholarName() {
        return scholarName;
    }

    public void setScholarName(String scholarName) {
        this.scholarName = scholarName;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long userId) {
        this.authorId = userId;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public void setAuthorFirstName(String userFirstName) {
        this.authorFirstName = userFirstName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InscriptionDTO inscriptionDTO = (InscriptionDTO) o;

        if ( ! Objects.equals(id, inscriptionDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "InscriptionDTO{" +
            "id=" + id +
            ", date='" + date + "'" +
            ", price='" + price + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
