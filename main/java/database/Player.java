
package database;
        import java.io.Serializable;
        import javax.persistence.*;

@Entity
public class Player implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Every Player is assigned a unique Identity
    private Long id;

    private int score;
    private String username;
    private String secondUsername;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSecondUsername(String secondUsername) {
        this.secondUsername = secondUsername;}

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public Player(){}

    public Player(String user, int score){
        this.username=user;
        this.score=score;
    }

    public String toString(){
        return (this.username + " " + this.score);
    }


}