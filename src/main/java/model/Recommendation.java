package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class Recommendation
{
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    public String id;

    public String reason;

    public int value;

    @ManyToOne
    @JsonIgnore
    public ConcreteCrop impactedCrop;

    @ManyToOne
    @JsonIgnore
    public ConcreteCrop impacterCrop;

    public Recommendation()
    {
    }
}
