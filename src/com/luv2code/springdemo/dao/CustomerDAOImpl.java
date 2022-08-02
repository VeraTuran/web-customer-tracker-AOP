package com.luv2code.springdemo.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.luv2code.springdemo.entity.Customer;
import com.luv2code.springdemo.util.SortUtils;

@Repository
// l'annotation @Repository est un composant spécialisé qui sait communiquer avec la BDD
public class CustomerDAOImpl implements CustomerDAO {
	@Autowired
	private SessionFactory sessionFactory;

//	// l'annotation @Transactional gère toute la transaction avec la base de
//	// données (ouvrir transaction, commit transaction, fermer transaction, ...)
//	@Transactional
	@Override
	public List<Customer> getCustomers() {
		// récupérer la session hibernate
		Session currentSession = sessionFactory.getCurrentSession();
		// créer une requête
		Query<Customer> theQuery = currentSession.createQuery("from Customer order by lastName", Customer.class);
		// exécuter la requête et récupérer la liste des clients
		List<Customer> customers = theQuery.getResultList();
		// return les résultats
		return customers;
	}

	@Override
	public void saveCustomer(Customer theCustomer) {
		// get current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		// save/update the customer
		currentSession.saveOrUpdate(theCustomer);
	}

	@Override
	public Customer getCustomer(int theId) {
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		// retrieve/read the customer from database using the primary key
		Customer theCustomer = currentSession.get(Customer.class, theId);
		return theCustomer;
	}

	@Override
	public void deleteCustomer(int theId) {
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		// delete object using primary key
		Query theQuery = currentSession.createQuery("delete from Customer where id=:customerId");
		theQuery.setParameter("customerId", theId);
		theQuery.executeUpdate();
	}

	@Override
	public List<Customer> searchCustomers(String theSearchName) {
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		Query theQuery = null;

		// only search by name if theSearchName is not empty
		if (theSearchName != null && theSearchName.trim().length() > 0) {
			// search for firstName or lastName ... case insensitive
			theQuery = currentSession.createQuery(
					"from Customer where lower(firstName) like :theName or lower(lastName) like :theName",
					Customer.class);
			theQuery.setParameter("theName", "%" + theSearchName.toLowerCase() + "%");
		} else {
			// if theSearchName is empty - get all the customers!
			theQuery = currentSession.createQuery("from Customer", Customer.class);
		}
		
		List<Customer> customers = theQuery.getResultList();
		
		return customers;
	}

	@Override
	public List<Customer> getCustomers(int theSortField) {
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		// determine the sort field
		String theFieldName = null;
		switch (theSortField) {case SortUtils.FIRST_NAME: 
			theFieldName = "firstName";
			break;
		case SortUtils.LAST_NAME:
			theFieldName = "lastName";
			break;
		case SortUtils.EMAIL:
			theFieldName = "email";
			break;
		default:
			// if nothing matches sort by lastName by default
			theFieldName = "lastName";
		}
		// create a query
		String queryString = "from Customer order by " + theFieldName;
		Query<Customer> theQuery = currentSession.createQuery(queryString, Customer.class);
		// execute query and get result list
		List<Customer> customers = theQuery.getResultList();
		// return the results
		return customers;
	}

}
