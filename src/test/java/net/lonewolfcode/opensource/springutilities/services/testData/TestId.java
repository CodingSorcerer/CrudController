package net.lonewolfcode.opensource.springutilities.services.testData;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class TestId  {
    private String key1;
    private int key2;
    private boolean key3;

    public TestId(){
    }

    public TestId(String key1, int key2, boolean key3){
        this.key1 = key1;
        this.key2 = key2;
        this.key3 = key3;
    }

    public String getKey1() {
        return key1;
    }

    public int getKey2() {
        return key2;
    }

    public boolean isKey3() {
        return key3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestId testId = (TestId) o;
        return key2 == testId.key2 &&
                key3 == testId.key3 &&
                Objects.equals(key1, testId.key1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key1, key2, key3);
    }
}
