
package database;


import javax.persistence.*;
import java.util.List;

public class AsteroidsDAO {
    private EntityManagerFactory emf  ;
    private EntityManager em  ;
    private String filename ;

    public void getAllPlayers() {
        Query q1 = em.createQuery("SELECT p FROM Player p order by p.score desc ");
        System.out.println();
 //   q1.getr
        List<database.Player> result = q1.getResultList();
        for(Player p : result){
            System.out.println(p);
        }
    }

    public void openDataSource() {
        System.out.println("open from localhost");
        emf = Persistence.createEntityManagerFactory(filename + ".odb");
        em = emf.createEntityManager();

    }

    public void closeDataSource() {
        // Close the database connection:
        em.close();
        emf.close();
    }

    public void addPlayer(Player s){
            em.getTransaction().begin();
            em.persist(s);
            em.getTransaction().commit();
            System.out.println("Stored: " + s);
    }

    public AsteroidsDAO(String filename){
        this.filename=filename;
    }

   /* public Collection<Player> getPlayerWithName(String name) {

		em.getTransaction().begin();
		TypedQuery <Player> query =
			em.createQuery("SELECT p FROM Player p WHERE p.username='"+name+"'", Player.class);
    	em.getTransaction().commit();
    	 List<Player> results = query.getResultList();
		return  results ;*/
	//}

    public void removeAll () {
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Player ").executeUpdate() ;
            em.getTransaction().commit();
            System.out.println ("Removed all") ;
        } catch (PersistenceException e) {
            em.getTransaction().commit();
            System.out.println("exception occured") ;
        }
    }



}
