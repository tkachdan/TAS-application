package src.persistence.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Krasotin on 11.12.14.
 */
@Entity
@Table(name = "trip", schema = "public", catalog = "tas")
public class Trip {

    private int id;
    private String name;
    private Integer cost;
    private Set<Poi> pois = new HashSet<Poi>();
    private TripStatus tripStatus;

    public Trip() {
    }

    public Trip(String name, Integer cost) {
        this.name = name;
        this.cost = cost;
        this.tripStatus = TripStatus.PENDING;
    }

    public Trip(String name, Integer cost, Set<Poi> pois) {
        this.name = name;
        this.cost = cost;
        this.pois = pois;
        this.tripStatus = TripStatus.PENDING;
    }

    public Trip(String name, Integer cost, Set<Poi> pois, TripStatus tripStatus) {
        this.name = name;
        this.cost = cost;
        this.pois = pois;
        this.tripStatus = tripStatus;
    }

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    public Set<Poi> getPois() {
        return pois;
    }

    public void setPois(Set<Poi> pois) {
        this.pois = pois;
    }

    public void addPoi(Poi poi) {
        this.pois.add(poi);
    }

    @Basic
    @Column(name = "tripstatus", nullable = true, insertable = true, updatable = true, length = 255)
    public TripStatus getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(TripStatus tripStatus) {
        this.tripStatus = tripStatus;
    }

    @Basic
    @Column(name = "name", nullable = true, insertable = true, updatable = true, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "cost", nullable = true, insertable = true, updatable = true)
    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Trip)) {
            return false;
        }

        Trip trip = (Trip) o;

        if (id != trip.id) {
            return false;
        }
        if (cost != null ? !cost.equals(trip.cost) : trip.cost != null) {
            return false;
        }
        if (name != null ? !name.equals(trip.name) : trip.name != null) {
            return false;
        }
        if (pois != null ? !pois.equals(trip.pois) : trip.pois != null) {
            return false;
        }
        if (tripStatus != trip.tripStatus) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (cost != null ? cost.hashCode() : 0);
        result = 31 * result + (pois != null ? pois.hashCode() : 0);
        result = 31 * result + (tripStatus != null ? tripStatus.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Trip{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", cost=" + cost
                + ", pois=" + pois
                + ", tripStatus=" + tripStatus
                + '}';
    }
}
