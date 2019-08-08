package evgenyt.spring;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Hibernate queries demo
 * Aug 2019 EvgenyT
 */

public class App {

    public static void main( String[] args ) {
        // Get application context from file
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                "applicationContext.xml");
        // Get factory bean and create session
        SessionFactory sessionFactory = context.getBean("sessionFactory",
                SessionFactory.class);
        Session session = sessionFactory.openSession();
        // Select query
        String hql = "SELECT name FROM PersonEntity WHERE id < 1000 ORDER BY id";
        Query query = session.createQuery(hql);
        List results = query.list();
        for (Object obj : results)
            System.out.println(obj);
        // Parametrised query
        Query paramQuery = session.createQuery("FROM PersonEntity WHERE id = :id");
        paramQuery.setParameter("id", 10);
        List listPersons = paramQuery.list();
        for (Object personEntity : listPersons)
            System.out.println(personEntity);
        // Update query
        session.beginTransaction();
        Query updateQuery = session.createQuery("UPDATE PersonEntity SET name = :name WHERE id =:id ");
        updateQuery.setParameter("name", "Julia Roberts");
        updateQuery.setParameter("id", 10);
        int updateCount = updateQuery.executeUpdate();
        System.out.println("Records updates " + updateCount);
        session.getTransaction().commit();
        // Delete query
        Query deleteQuery = session.createQuery("DELETE FROM PersonEntity WHERE name = :name");
        deleteQuery.setParameter("name", "Anonimous");
        session.beginTransaction();
        int deleteCount = deleteQuery.executeUpdate();
        session.getTransaction().commit();
        System.out.println("Deleted " + deleteCount + " records.");
        // Insert query only from other objects not params
        Query insertQuery = session.createQuery("INSERT INTO PersonEntity(name) " +
                "SELECT name FROM PersonEntity WHERE id = 10");
        session.beginTransaction();
        insertQuery.executeUpdate();
        session.getTransaction().commit();
        // Aggregates: avg, count, max, min, sum
        Query agrQuery = session.createQuery("SELECT count(*) FROM PersonEntity");
        Object o = agrQuery.getSingleResult();
        System.out.println("Row count = " + o.toString());
        // Group by, get two fields in list of arrays
        Query grpQuery = session.createQuery("SELECT name, count(name) FROM PersonEntity GROUP BY name");
        List<Object[]> grpResults = grpQuery.list();
        for (Object[] grpResult : grpResults)
            System.out.println("Count by name = " + grpResult[0] + " " + grpResult[1]);
        // Pagination: show 5 rows from number 1
        Query pageQuery = session.createQuery("FROM PersonEntity");
        pageQuery.setFirstResult(1);
        pageQuery.setMaxResults(5);
        List pageList = pageQuery.list();
        for (Object pageResult : pageList)
            System.out.println(pageResult);
        // Close session
        session.close();
    }
}
