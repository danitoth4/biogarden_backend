package model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Recommendation
{
    @Id
    public String id;

    public String reason;

    public int value;
}
