package com.majordiversifed.getoutside;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that represents a user. There is only one user per installation.
 * TODO Override Equals
 * Created by Andrew on 2/20/16.
 */
public class User implements Parcelable {
    private static int counter = 0;
    private int userid;
    private String username;
    private double rating;
    private int numRatings;
    private Map<Location, Review> reviews;

    public User(String username) {
        this.username = username;
        userid = counter++;
        reviews = new HashMap<>();
    }

    /**
     * Getter method for userid.
     * @return userid
     */
    public int getUserid() {
        return userid;
    }

    /**
     * Getter method for username.
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter method for user rating.
     * @return user rating
     */
    public double getRating() {
        return rating;
    }

    /**
     * Getter method for all reviews.
     * @return reviews from the user
     */
    public Map<Location, Review> getReviews() {
        return reviews;
    }

    /**
     * Method to add locations and reviews to the user.
     * @param loc location visited
     * @param rating user rating of the location
     * @param review user review of the location
     */
    public void addLocationReview(Location loc, double rating, String review) {
        reviews.put(loc, new Review(loc, this, rating, review));
    }

    /**
     * Calculates the new user rating based on other users.
     * @param rating rating of other user
     */
    public void calculateRating(double rating) {
        this.rating = (rating + this.rating) / numRatings++;
    }

    /**
     * Method to add locations and ratings without a review.
     * @param loc location visited
     * @param rating user rating of the location
     */
    public void addLocationReview(Location loc, double rating) {
        addLocationReview(loc, rating, "");
    }

    /**
     * Method to add locations and reviews to the user.
     * @param review review of location
     */
    public void addLocationReview(Review review) {
        reviews.put(review.getLocation(), review);
    }

    /**
     * Constructor to read parcel.
     * @param in parcel to reconstruct object.
     */
    private User(Parcel in) {
        this.userid = in.readInt();
        this.username = in.readString();
        this.rating = in.readDouble();
        this.numRatings = in.readInt();
        in.readMap(reviews, Review.class.getClassLoader());
    }

    /**
     * Implementation of Parcelable. Ensures that the read/write are given in FIFO.
     * @param dest destination parcel
     * @param flags flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userid);
        dest.writeString(username);
        dest.writeDouble(rating);
        dest.writeInt(numRatings);
        dest.writeMap(reviews);
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
     * Parcelable CREATOR constant.
     */
    public static final Parcelable.Creator<User> CREATOR =
            new Parcelable.Creator<User>() {

                @Override
                public User createFromParcel(Parcel source) {
                    return new User(source);
                }

                @Override
                public User[] newArray(int size) {
                    return new User[size];
                }
            };


}
