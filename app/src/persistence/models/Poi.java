package src.persistence.models;

import javax.persistence.*;
import java.sql.Time;

/**
 * Created by tkachdan on 10-Dec-14.
 */
@Entity
public class Poi {
    private int id;
    private Boolean accesibility;
    private Integer minimalAge;
    private String name;
    private Double rating;
    private Time requiredTime;
    private PoiType type;
    private Integer cost;
    private Double latitude;
    private Double longtitude;

    public Poi() {
    }

    public Poi(Boolean accesibility, Integer minimalAge, String name, Double rating,
               Time requiredTime, PoiType type, Integer cost,
               Double latitude, Double longtitude) {
        this.accesibility = accesibility;
        this.minimalAge = minimalAge;
        this.name = name;
        this.rating = rating;
        this.requiredTime = requiredTime;
        this.type = type;
        this.cost = cost;
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    @Id
    @GeneratedValue
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "accesibility")
    public Boolean getAccesibility() {
        return accesibility;
    }

    public void setAccesibility(Boolean accesibility) {
        this.accesibility = accesibility;
    }

    @Basic
    @Column(name = "minimal_age")
    public Integer getMinimalAge() {
        return minimalAge;
    }

    public void setMinimalAge(Integer minimalAge) {
        this.minimalAge = minimalAge;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "rating")
    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    @Basic
    @Column(name = "required_time")
    public Time getRequiredTime() {
        return requiredTime;
    }

    public void setRequiredTime(Time requiredTime) {
        this.requiredTime = requiredTime;
    }

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    public PoiType getType() {
        return type;
    }

    public void setType(PoiType type) {
        this.type = type;
    }

    @Basic
    @Column(name = "cost")
    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    @Basic
    @Column(name = "latitude")
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Basic
    @Column(name = "longtitude")
    public Double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Double longtitude) {
        this.longtitude = longtitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Poi)) return false;

        Poi poi = (Poi) o;

        if (id != poi.id) return false;
        if (accesibility != null ? !accesibility.equals(poi.accesibility) : poi.accesibility != null) return false;
        if (cost != null ? !cost.equals(poi.cost) : poi.cost != null) return false;
        if (latitude != null ? !latitude.equals(poi.latitude) : poi.latitude != null) return false;
        if (longtitude != null ? !longtitude.equals(poi.longtitude) : poi.longtitude != null) return false;
        if (minimalAge != null ? !minimalAge.equals(poi.minimalAge) : poi.minimalAge != null) return false;
        if (name != null ? !name.equals(poi.name) : poi.name != null) return false;
        if (rating != null ? !rating.equals(poi.rating) : poi.rating != null) return false;
        if (requiredTime != null ? !requiredTime.equals(poi.requiredTime) : poi.requiredTime != null) return false;
        if (type != poi.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (accesibility != null ? accesibility.hashCode() : 0);
        result = 31 * result + (minimalAge != null ? minimalAge.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (rating != null ? rating.hashCode() : 0);
        result = 31 * result + (requiredTime != null ? requiredTime.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (cost != null ? cost.hashCode() : 0);
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        result = 31 * result + (longtitude != null ? longtitude.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Poi{" +
                "id=" + id +
                ", accesibility=" + accesibility +
                ", minimalAge=" + minimalAge +
                ", name='" + name + '\'' +
                ", rating=" + rating +
                ", requiredTime=" + requiredTime +
                ", type=" + type +
                ", cost=" + cost +
                ", latitude=" + latitude +
                ", longtitude=" + longtitude +
                '}';
    }
}
