package model;

import javax.persistence.Entity;

@Entity
public class RotationRecommendation extends Recommendation
{
    private int otherContentId;

    //region Getters and Setters
    public int getOtherContentId()
    {
        return otherContentId;
    }

    public void setOtherContentId(int otherContentId)
    {
        this.otherContentId = otherContentId;
    }
    //endregion

}
