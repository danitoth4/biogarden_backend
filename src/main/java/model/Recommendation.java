package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public abstract class Recommendation
{
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String reason;

    private int value;

    @ManyToOne
    @JsonIgnore
    private ConcreteCrop impactedCrop;

    @ManyToOne
    @JsonIgnore
    private ConcreteCrop impacterCrop;

    public Recommendation()
    {
    }

    //region Getters and Setters
    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getReason()
    {
        return reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }

    public ConcreteCrop getImpactedCrop()
    {
        return impactedCrop;
    }

    public void setImpactedCrop(ConcreteCrop impactedCrop)
    {
        this.impactedCrop = impactedCrop;
    }

    public ConcreteCrop getImpacterCrop()
    {
        return impacterCrop;
    }

    public void setImpacterCrop(ConcreteCrop impacterCrop)
    {
        this.impacterCrop = impacterCrop;
    }
    //endregion
}
