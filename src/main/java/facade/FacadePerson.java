package facade;

import entity.Person;
import entity.PersonDTO;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

public class FacadePerson
{
    EntityManagerFactory emf;
    
    public FacadePerson(EntityManagerFactory emf)
    {
        this.emf = emf;
    }
    
    public Person getPerson(Long id)
    {
        EntityManager em = emf.createEntityManager();

        Person p = null;
        
        try
        {
            em.getTransaction().begin();
            p = em.find(Person.class, id);
            em.getTransaction().commit();
            return p;
        }
        finally
        {
            em.close();
        }    
    }
    
    public List<PersonDTO> getPersons()
    {
        EntityManager em = emf.createEntityManager();

        List<PersonDTO> persons = null;
        
        try
        {
            em.getTransaction().begin();
//            persons = em.createQuery("Select p from Person p").getResultList();
            //TypedQuery<PersonDTO> qt = em.createQuery("SELECT NEW entity.PersonDTO(p.firstName, p.lastName, p.phoneNumber) from Person p", PersonDTO.class);
            //persons = qt.getResultList();
            persons = em.createQuery("SELECT NEW entity.PersonDTO(p.firstName, p.lastName, p.phoneNumber) from Person p", PersonDTO.class).getResultList();
                    
            em.getTransaction().commit();
            return persons;
        }
        finally
        {
            em.close();
        }
    }

    public Person addPerson(Person p)
    {
        EntityManager em = emf.createEntityManager();
       
        try
        {
            em.getTransaction().begin();
            em.persist(p);
            em.getTransaction().commit();
            return p;
        }
        finally
        {
            em.close();
        }
    }
    
    public Person deletePerson(Long id)
    {
        EntityManager em = emf.createEntityManager();

        try
        {
            em.getTransaction().begin();
            Person p = em.find(Person.class, id);
            em.remove(p);
            em.getTransaction().commit();
            return p;
        }
        finally
        {
            em.close();
        }
    }
    
    public Person editPerson(Person pers)
    {
        EntityManager em = emf.createEntityManager();

        try
        {
            em.getTransaction().begin();
            Person p = em.find(Person.class, pers.getId());
            if(p != null)
            {
                p = pers;
                em.merge(p);
                em.getTransaction().commit();
                return p;
            }
        }
        finally
        {
            em.close();
        }  
        
        return null;
    }
}