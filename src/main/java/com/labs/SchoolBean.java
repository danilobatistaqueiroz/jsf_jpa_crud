package com.labs;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.annotation.PostConstruct;

import com.labs.SchoolDao;

@ManagedBean
public class SchoolBean {

	private int id;
	private String name;	
	private String editSchoolId;
	private SchoolDao schoolDao;
	
	@PostConstruct
	private void init() {
		schoolDao = new SchoolDao();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEditSchoolId() {
		return editSchoolId;
	}

	public void setEditSchoolId(String editSchoolId) {
		this.editSchoolId = editSchoolId;
	}

	public List schoolListFromDb() {
		return schoolDao.getAllSchoolDetails();		
	}

	public String addNewSchool(SchoolBean schoolBean) {
		return schoolDao.createNewSchool(schoolBean.getName());		
	}

	public String deleteSchoolById(int schoolId) {		
		return schoolDao.deleteSchoolDetails(schoolId);		
	}

	public String editSchoolDetailsById() {
		editSchoolId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("selectedSchoolId");		
		return "schoolEdit.xhtml";
	}

	public String updateSchoolDetails(SchoolBean schoolBean) {
		return schoolDao.updateSchoolDetails(Integer.parseInt(schoolBean.getEditSchoolId()), schoolBean.getName());		
	}
}