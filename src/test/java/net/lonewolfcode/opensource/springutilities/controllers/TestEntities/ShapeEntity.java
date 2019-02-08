package net.lonewolfcode.opensource.springutilities.controllers.TestEntities;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class ShapeEntity {
    @Id
    private String name;

    public ShapeEntity(){
    }

    public ShapeEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShapeEntity that = (ShapeEntity) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
