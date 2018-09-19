
package jpacontrol;

import entity.Address;
import entity.Person;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class Populater
{
    public static void main(String[] args)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        
        em.getTransaction().begin();
        
        Address a1 = new Address("Road 1", "Berlin");
        Address a2 = new Address("Road 2", "New York");
        
        Person p1 = new Person("Mike", "Samson", 4321);
        p1.addAddress(a1);
        p1.addAddress(a2);
        em.persist(p1);
        
        Person p2 = new Person("Lars", "Samson", 43212);
        p2.addAddress(a1);
        p2.addAddress(a2);
        em.persist(p2);
        
        em.getTransaction().commit();
        em.close();
    }
}
