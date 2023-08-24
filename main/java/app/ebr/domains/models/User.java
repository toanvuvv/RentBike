package app.ebr.domains.models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import at.favre.lib.crypto.bcrypt.BCrypt;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", targetEntity = Bicycle.class)
    private List<Bicycle> bicycles;

    @OneToMany(mappedBy = "user", targetEntity = Bill.class)
    private List<Bill> bills;

    @Column(name = "money")
    private float money;

    public User() {

    }

    public User(String email, String password) {
        this.email = email;
        // Hash password
        this.password = BCrypt.with(BCrypt.Version.VERSION_2X).hashToString(6, password.toCharArray()).toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = BCrypt.with(BCrypt.Version.VERSION_2X).hashToString(6, password.toCharArray()).toString();
    }

    public String encodeJwt() {
        Algorithm algorithm = Algorithm.HMAC256("secret");
        String jwtToken = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 24 * 60 * 60 * 1000))
                .withClaim("uuid", this.id)
                .sign(algorithm);
        return jwtToken;
    }

    public void setBicycles(List<Bicycle> bicycles) {
        this.bicycles = bicycles;
    }

    public List<Bicycle> getBicycles() {
        return bicycles;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public float getMoney() {
        return money;
    }
}
