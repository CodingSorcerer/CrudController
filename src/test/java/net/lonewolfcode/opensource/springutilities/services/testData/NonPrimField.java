package net.lonewolfcode.opensource.springutilities.services.testData;

import java.util.Objects;

public class NonPrimField {
    private String id;

    public NonPrimField(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (this.id.equals(o)) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NonPrimField that = (NonPrimField) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
