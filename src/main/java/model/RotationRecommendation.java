package model;

import javax.persistence.Entity;

@Entity
public class RotationRecommendation extends Recommendation
{
    public int otherContentId;
}
