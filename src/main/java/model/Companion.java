package model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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


    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Companion companion = (Companion) o;
        return id == companion.id &&
                Objects.equals(impacting, companion.impacting) &&
                Objects.equals(impacted, companion.impacted) &&
                Objects.equals(positive, companion.positive);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, impacting, impacted, positive);
    }
}
