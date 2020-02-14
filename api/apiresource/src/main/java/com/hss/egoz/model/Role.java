package com.hss.egoz.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

@Entity
public class Role {

	@Id
	@SequenceGenerator(name="role_seq", allocationSize = 1, initialValue = 1, sequenceName = "ROLE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq")
	private Integer roleId;
	
	private String roleName;

	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name = "roleId")
	private List<Access> access;
	
	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public List<Access> getAccess() {
		return access;
	}

	public void setAccess(List<Access> access) {
		this.access = access;
	}
	
}
