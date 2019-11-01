package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@Entity
public class Recommendation
{
    @Id
    public String id;

    public String reason;

    public int value;

    @ManyToOne
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    public ConcreteCrop impactedCrop;

    @ManyToOne
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    public ConcreteCrop impacterCrop;

    public Recommendation()
    {
        if(id == null)
            id = java.util.UUID.randomUUID().toString();
    }
}
