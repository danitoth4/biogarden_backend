package model;

import javax.persistence.*;

@Entity
public class Recommendation
{
    @Id
    public String id;

    public String reason;

    public int value;

    @ManyToOne
    public ConcreteCrop impactedCrop;

    public String impacterId;

    public Recommendation()
    {
        if(id == null)
            id = java.util.UUID.randomUUID().toString();
    }
}
