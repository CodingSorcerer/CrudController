package net.lonewolfcode.opensource.springutilities.controllers.TestEntities;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.util.Objects;

@Entity
public class EmbededEntity {
    @EmbeddedId
    private EmbeddableId id;
    private String aField;

    public EmbededEntity(){
    }

    public EmbededEntity(EmbeddableId id, String aField) {
        this.id = id;
        this.aField = aField;
    }

    public EmbeddableId getId() {
        return id;
    }

    public String getaField() {
        return aField;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmbededEntity that = (EmbededEntity) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getaField(), that.getaField());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getaField());
    }
}
