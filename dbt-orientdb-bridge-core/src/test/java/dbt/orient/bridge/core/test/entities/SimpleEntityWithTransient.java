/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbt.orient.bridge.core.test.entities;

import com.adition.middleware.dbt.orientdb.bridge.annotation.Vertex;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 *
 * @author dnikiforov
 */
@Vertex
public class SimpleEntityWithTransient {
	
	@Id
	Integer id;
	
	String name;
	
	Date birthDay;

	@Transient
	UUID clientId;

	public UUID getClientId() {
		return clientId;
	}

	public void setClientId(UUID clientId) {
		this.clientId = clientId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 89 * hash + Objects.hashCode(this.id);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final SimpleEntityWithTransient other = (SimpleEntityWithTransient) obj;
		if (!Objects.equals(this.id, other.id)) {
			return false;
		}
		return true;
	}

	
}