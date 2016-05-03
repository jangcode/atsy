package com.epam.rft.atsy.persistence.entities;

import javax.persistence.*;

@Entity
@Table(name = "Positions", schema = "atsy", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class PositionEntity extends SuperEntity implements java.io.Serializable {
    @Column(name = "name", nullable = false, length = 255, unique = true)
    private String name;

    /**
     * Empty constructor.
     */
    public PositionEntity() {
    }

    public PositionEntity(Long id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        PositionEntity that = (PositionEntity) o;

        return !(name != null ? !name.equals(that.name) : that.name != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PositionEntity{" +
                "name='" + name + '\'' +
                '}';
    }
}
