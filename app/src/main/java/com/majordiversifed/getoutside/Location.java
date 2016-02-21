package com.majordiversifed.getoutside;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a graphic marker on the map. Represented by a unique id, latitude, and longitude.
 * TODO Fully implement Parcelable.
 * TODO Override Equals
 * Created by Andrew on 2/20/16.
 */
public class Location implements Parcelable {

    private int id;
    private double lat;
    private double lon;
    private double rating;
    private int numRatings;
    private List<Review> reviews;

    public Location(int id, double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
        this.id = id;
        reviews = new ArrayList<>(50);
    }

    /**
     * Adds review given just the review.
     * @param r review to be added
     */
    public void addReview(Review r) {
        reviews.add(r);
        this.rating = (r.getRating() + this.rating) / numRatings++;
    }

    /**
     * Getter method for the latitude of the location.
     * @return latitude
     */
    public double getLat() {
        return lat;
    }

    /**
     * Getter method for the longitude of the location.
     * @return longitude
     */
    public double getLon() {
        return lon;
    }

    /**
     * Getter method for the user rating of the location.
     * @return user rating of location
     */
    public double getRating() {
        return rating;
    }

    /**
     * Getter method for the number of ratings (aka number of visitors).
     * @return the number of visitors
     */
    public double getNumRatings() {
        return numRatings;
    }

    /**
     * Getter method for the unique location id.
     * @return the location id
     */
    public int getId() {
        return id;
    }

    /**
     * Constructor to read parcel.
     * @param in parcel to reconstruct object.
     */
    private Location(Parcel in) {
        id = in.readInt();
        lat = in.readDouble();
        lon = in.readDouble();
        rating = in.readDouble();
        numRatings = in.readInt();
        reviews = in.readArrayList(Review.class.getClassLoader());
    }

    /**
     * Implementation of Parcelable. Ensures that the read/write are given in FIFO.
     * @param dest destination parcel
     * @param flags flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeDouble(lat);
        dest.writeDouble(lon);
        dest.writeDouble(rating);
        dest.writeInt(numRatings);
        dest.writeList(reviews);
    }

    /**
     * Basic implementation of describeContents(). For our purposes, we have no need to customize
     * the implementation.
     * @return 0
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Constant for implementation of Parcelable.
     */
    public static final Parcelable.Creator<Location> CREATOR =
            new Parcelable.Creator<Location>() {

                @Override
                public Location createFromParcel(Parcel source) {
                    return new Location(source);
                }

                @Override
                public Location[] newArray(int size) {
                    return new Location[size];
                }
    };
}
