package app.ebr.domains.models;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import app.ebr.domains.enums.BillStatus;

@Entity
@Table(name = "bill")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private BillStatus status;

    @Column(name = "total")
    private float total;

    @Column(name = "paid_at")
    private Date paidAt;

    @Column(name = "time_started")
    private Date timeStarted;

    @Column(name = "time_ended")
    private Date timeEnded;

    @OneToOne
    @JoinColumn(name = "bicycle_id")
    private Bicycle bicycle;

    public Bill() {

    }

    public Bill(User user, Bicycle bicycle, float total, Date timeStarted, Date timeEnded) {
        this.user = user;
        this.bicycle = bicycle;
        this.status = BillStatus.PENDING;
        this.total = total;
        this.timeStarted = timeStarted;
        this.timeEnded = timeEnded;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setStatus(BillStatus status) {
        this.status = status;
    }

    public BillStatus getStatus() {
        return status;
    }

    public void setTimeStarted(Date timeStarted) {
        this.timeStarted = timeStarted;
    }

    public Date getTimeStarted() {
        return timeStarted;
    }

    public void setTimeEnded(Date timeEnded) {
        this.timeEnded = timeEnded;
    }

    public Date getTimeEnded() {
        return timeEnded;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getTotal() {
        return total;
    }

    public void setPaidAt(Date paidAt) {
        this.paidAt = paidAt;
    }

    public Date getPaidAt() {
        return paidAt;
    }

    public void setBicycle(Bicycle bicycle) {
        this.bicycle = bicycle;
    }

    public Bicycle getBicycle() {
        return bicycle;
    }

}
