package com.ozguryazilim.zoro.core.db.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

/**
 * Base entity used in the generic controller
 *
 * @author aaslannn
 *
 */
@MappedSuperclass
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = -6869036595119293902L;
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "ID")
    private String id;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    public String getName()
    {
    	
    	return "";
    }
    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
}
