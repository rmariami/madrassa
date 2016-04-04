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

import fr.rmariami.madrassa.domain.enumeration.MomentEnum;

/**
 * A ClassRoom.
 */
@Entity
@Table(name = "class_room")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "classroom")
public class ClassRoom implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "moment", nullable = false)
    private MomentEnum moment;

    @NotNull
    @Column(name = "start_hour", nullable = false)
    private String startHour;

    @NotNull
    @Column(name = "end_hour", nullable = false)
    private String endHour;

    @OneToMany(mappedBy = "classes")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Teacher> teachers = new HashSet<>();

    @ManyToOne
    private Scholar scholars;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MomentEnum getMoment() {
        return moment;
    }

    public void setMoment(MomentEnum moment) {
        this.moment = moment;
    }

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getEndHour() {
        return endHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    public Set<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(Set<Teacher> teachers) {
        this.teachers = teachers;
    }

    public Scholar getScholars() {
        return scholars;
    }

    public void setScholars(Scholar scholar) {
        this.scholars = scholar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClassRoom classRoom = (ClassRoom) o;
        if(classRoom.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, classRoom.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ClassRoom{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", name='" + name + "'" +
            ", moment='" + moment + "'" +
            ", startHour='" + startHour + "'" +
            ", endHour='" + endHour + "'" +
            '}';
    }
}
