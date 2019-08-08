package model;

import java.util.Objects;

public class Companion {

    private Integer cropId1;

    private  Integer cropId2;

    private Boolean isPositive;

    public Integer getCropId1() {
        return cropId1;
    }

    public void setCropId1(Integer cropId1) {
        this.cropId1 = cropId1;
    }

    public Integer getCropId2() {
        return cropId2;
    }

    public void setCropId2(Integer cropId2) {
        this.cropId2 = cropId2;
    }

    public Boolean getPositive() {
        return isPositive;
    }

    public void setPositive(Boolean positive) {
        isPositive = positive;
    }

    @Override
    public boolean equals(Object o) {
        if(!o.getClass().equals(Companion.class))
        {
            return false;
        }
        Companion other = (Companion)o;

        return !((!other.cropId1.equals(this.cropId1) && !other.cropId1.equals(this.cropId2)) || (!other.cropId2.equals(this.cropId1) && !other.cropId2.equals(this.cropId2)));
    }

    @Override
    public int hashCode()
    {
        int h1 = Objects.hash(cropId1, cropId2);
        int h2 = Objects.hash(cropId2, cropId1);

        return h1 >= h2 ? h1 : h2;
    }
}
