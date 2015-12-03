package com.epam.rft.atsy.persistence.entities.states;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serializable;

/**
 * Created by tothd on 2015. 12. 02..
 */
@Entity
@DiscriminatorValue(value = "newstate")
public class NewStateEntity extends StateEntity implements Serializable{
}
