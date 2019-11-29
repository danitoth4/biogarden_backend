package model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Companion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private Crop impacting;

    @ManyToOne
    private  Crop impacted;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "companion")
    private List<CompanionRecommendation> recommendations = new ArrayList<>();

    private Boolean positive;

    //region Getters and Setters
    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public Crop getImpacting()
    {
        return impacting;
    }

    public void setImpacting(Crop impacting)
    {
        this.impacting = impacting;
    }

    public Crop getImpacted()
    {
        return impacted;
    }

    public void setImpacted(Crop impacted)
    {
        this.impacted = impacted;
    }

    public Boolean getPositive() {
        return positive;
    }

    public void setPositive(Boolean positive) {
        this.positive = positive;
    }
    //endregion

}
