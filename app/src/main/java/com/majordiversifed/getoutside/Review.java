package com.majordiversifed.getoutside;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents a review of a location.
 * TODO Override Equals
 * Created by Andrew on 2/20/16.
 */
public class Review implements Parcelable {
    private static int counter;
    private User user;
    private Location location;
    private int reviewId;
    private double rating;
    private String review;

    public Review(Location location, User user, double rating, String review) {
        reviewId = counter++;
        this.location = location;
        this.user = user;
        this.rating = rating;
        this.review = review;
        user.addLocationReview(this);
        location.addReview(this);

    }

    public Review(Location location, User user, double rating) {
        this(location, user, rating, "");
    }

    /**
     * Constructor to read parcel.
     * @param in parcel to reconstruct object.
     */
    private Review(Parcel in) {
        this.reviewId = in.readInt();
        this.location = in.readParcelable(Location.class.getClassLoader());
        this.user = in.readParcelable(User.class.getClassLoader());
        this.rating = in.readDouble();
        this.review = in.readString();
    }

    /**
     * Getter method for the reviewId.
     * @return review id
     */
    public int getReviewId() {
        return reviewId;
    }

    /**
     * Getter method for the location of the review.
     * @return location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Getter method for the user of the review.
     * @return user
     */
    public User getUser() {
        return user;
    }

    /**
     * Getter method for the rating of the review.
     * @return rating of the review
     */
    public double getRating() {
        return rating;
    }

    /**
     * Getter method for the review of the review.
     * @return review text
     */
    public String getReview() {
        return review;
    }

    /**
     * Setter method for the review.
     * @param review the text of the review
     */
    public void setReview(String review) {
        this.review = review;
        this.location.addReview(this);
        this.user.addLocationReview(this);
    }

    /**
     * Implementation of Parcelable. Ensures that the read/write are given in FIFO.
     * @param dest destination parcel
     * @param flags flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(reviewId);
        dest.writeParcelable(location, 0);
        dest.writeParcelable(user, 0);
        dest.writeDouble(rating);
        dest.writeString(review);
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
    public static final Parcelable.Creator<Review> CREATOR =
            new Parcelable.Creator<Review>() {

                @Override
                public Review createFromParcel(Parcel source) {
                    return new Review(source);
                }

                @Override
                public Review[] newArray(int size) {
                    return new Review[size];
                }
            };
}
