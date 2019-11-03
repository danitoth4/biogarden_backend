package model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Companion {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @ManyToOne
    private Crop impacting;

    @ManyToOne
    private  Crop impacted;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "companion")
    private List<CompanionRecommendation> recommendations = new ArrayList<>();

    private Boolean isPositive;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
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
        return isPositive;
    }

    public void setPositive(Boolean positive) {
        isPositive = positive;
    }

}
