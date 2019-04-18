package model;

public class Companion {

    private Short cropId1;

    private  Short cropId2;

    private Boolean isPositive;

    public Short getCropId1() {
        return cropId1;
    }

    public void setCropId1(Short cropId1) {
        this.cropId1 = cropId1;
    }

    public Short getCropId2() {
        return cropId2;
    }

    public void setCropId2(Short cropId2) {
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
}
