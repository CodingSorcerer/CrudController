package net.lonewolfcode.opensource.springutilities.controllers.TestEntities;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EmbeddableId implements Serializable {
    private String key1;
    private Integer key2;

    public EmbeddableId() {
    }

    public EmbeddableId(String key1, Integer key2) {
        this.key1 = key1;
        this.key2 = key2;
    }

    public String getKey1() {
        return key1;
    }

    public Integer getKey2() {
        return key2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmbeddableId that = (EmbeddableId) o;
        return Objects.equals(getKey1(), that.getKey1()) &&
                Objects.equals(getKey2(), that.getKey2());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey1(), getKey2());
    }
}
