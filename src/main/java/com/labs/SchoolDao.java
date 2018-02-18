package com.labs;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.labs.School;

public class SchoolDao {

	private EntityManager entityManager;
	
	public SchoolDao() {
		try{
			entityManager = LocalEntityManagerFactory.getEntityManager();
		} catch( RuntimeException ex){
			ex.printStackTrace(System.err);
			throw ex;
		}
	}
	
	public void close(){
		entityManager.close();
	}

	@SuppressWarnings("unchecked")
	public List getAllSchoolDetails() {
		Query queryObj = entityManager.createQuery("SELECT s FROM School s");
		List schoolList = queryObj.getResultList();
		if (schoolList != null && schoolList.size() > 0) {			
			return schoolList;
		} else {
			return null;
		}
	}

	public String createNewSchool(String name) {
		entityManager.getTransaction().begin();
		School school = new School();
		school.setName(name);
		entityManager.persist(school);
		entityManager.getTransaction().commit();
		return "schoolsList.xhtml?faces-redirect=true";	
	}

	public String deleteSchoolDetails(int schoolId) {
		entityManager.getTransaction().begin();
		School school = new School();
		if(isSchoolIdPresent(schoolId)) {
			school.setId(schoolId);
			entityManager.remove(entityManager.merge(school));
		}		
		entityManager.getTransaction().commit();
		return "schoolsList.xhtml?faces-redirect=true";
	}

	public String updateSchoolDetails(int schoolId, String updatedSchoolName) {
		entityManager.getTransaction().begin();
		if(isSchoolIdPresent(schoolId)) {
			Query queryObj = entityManager.createQuery("UPDATE School s SET s.name=:name WHERE s.id= :id");			
			queryObj.setParameter("id", schoolId);
			queryObj.setParameter("name", updatedSchoolName);
			int updateCount = queryObj.executeUpdate();
			if(updateCount > 0) {
				System.out.println("Record For Id: " + schoolId + " Is Updated");
			}
		}
		entityManager.getTransaction().commit();
		FacesContext.getCurrentInstance().addMessage("editSchoolForm:schoolId", new FacesMessage("School Record #" + schoolId + " Is Successfully Updated In Db"));
		return "schoolEdit.xhtml";
	}

	private boolean isSchoolIdPresent(int schoolId) {
		boolean idResult = false;
		Query queryObj = entityManager.createQuery("SELECT s FROM School s WHERE s.id = :id");
		queryObj.setParameter("id", schoolId);
		School selectedSchoolId = (School) queryObj.getSingleResult();
		if(selectedSchoolId != null) {
			idResult = true;
		}
		return idResult;
	}
}