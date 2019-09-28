package model;

import javax.persistence.Entity;

@Entity
public class CompanionRecommendation extends Recommendation
{
    public int otherCropId;
}
