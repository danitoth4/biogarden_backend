package model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class CompanionRecommendation extends Recommendation
{
    @ManyToOne
    private Companion companion;

    //region Getters and Setters
    public Companion getCompanion()
    {
        return companion;
    }

    public void setCompanion(Companion companion)
    {
        this.companion = companion;
    }
    //endregion
}
