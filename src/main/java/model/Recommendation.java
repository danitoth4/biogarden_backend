package model;

import javax.persistence.*;

@Entity
public class Recommendation
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public String id;

    public String reason;

    public int value;

    @ManyToOne
    public ConcreteCrop impactedCrop;

    public String impacterId;
}
