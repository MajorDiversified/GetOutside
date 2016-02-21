package com.majordiversifed.getoutside;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Utility class to manage app operations.
 * Created by Andrew on 2/21/16.
 */
public final class Utility {
    private static User user = new User("Victor");
    private static Set<Location> locations = new HashSet<>(50);
    private static Set<Review> reviews = new HashSet<>(50);

    /**
     * Private constructor in order to make it impossible to create an instance. The class is
     * also final.
     */
    private Utility() {
    }

    /**
     * Adds the current user with their associated username. Should only be used once, and there
     * is a default hardcoded one.
     * @param username username of the user.
     */
    public static void addUser(String username) {
        user = new User(username);
    }

    /**
     * Adds a location to the locations in the app if they don't exist already.
     * @param locId unique identifier for the marker
     * @param lat latitude of marker
     * @param lon longitude of marker
     * @return true if added, false if the location already existed
     */
    public static boolean addLocation(int locId, double lat, double lon) {
        return locations.add(new Location(locId, lat, lon));
    }

    /**
     * Adds a location to the locations in the app, if they don't exist.
     * @param loc location to be added
     * @return true if added, false if the location already exists
     */
    public static boolean addLocation(Location loc) {
        return locations.add(loc);
    }

    /**
     * Adds a review to the reviews in the app, if they don't exist already.
     * @param review review to be added
     * @return true if added, false if the review exists already
     */
    public static boolean rateLocation(Review review) {
        return reviews.add(review);
    }

    /**
     * Rates a location and adds the respective review.
     * @param locId location id to be added
     * @param rating rating to be added
     * @param review review to be added
     * @return true if the review was added, false if it already exists
     * @throws NoSuchElementException if location is not found
     */
    public static boolean rateLocation(int locId, double rating, String review) {
        Location location = null;
        for (Location loc : locations) {
            if (locId == loc.getId()) {
                location = loc;
            }
        }
        if (location == null) {
            throw new NoSuchElementException("Location not found.");
        }
        return reviews.add(new Review(location, user, rating, review));
    }

    /**
     * Rates a location without the review.
     * @param locId locatio id to be added
     * @param rating rating to be added
     * @return true if the review was added, false if it already exists
     * @throws NoSuchElementException if location is not found
     */
    public static boolean rateLocation(int locId, double rating) {
        return rateLocation(locId, rating, "");
    }

    /**
     * Allows for the user to add a review if they didn't make one before.
     * @param user user making the review
     * @param loc location to be reviewed
     * @param review review text
     * @throws NoSuchElementException if element is not found
     */
    public static void appendReview(User user, Location loc, String review) {
        for (Review r : reviews) {
            if (r.getUser().equals(user) && r.getLocation().equals(loc)) {
                r.setReview(review);
                return;
            }
        }
        throw new NoSuchElementException("This rating combination doesn't exist.");
    }

    /**
     * Getter method for the current user.
     * @return current user
     */
    public static User getCurrentUser() {
        return user;
    }

    /**
     * Getter method for a review based on user and location.
     * @param user user of the review
     * @param loc location of the review
     * @return
     */
    public static Review getReview(User user, Location loc) {
        for (Review r : reviews) {
            if (r.getUser().equals(user) && r.getLocation().equals(loc)) {
                return r;
            }
        }
        throw new NoSuchElementException("This review combination doesn't exist.");
    }

    /**
     * Getter method for all possible reviews.
     * @return set of all of the reviews
     */
    public static Set<Review> getReviews() {
        return reviews;
    }

    /**
     * Getter method for all reviews from a certain location.
     * @param loc location for the reviews
     * @return set of all of these reviews, empty set otherwise
     */
    public static Set<Review> getReviews(Location loc) {
        Set<Review> set = new HashSet<>();
        for (Review r : reviews) {
            if (r.getLocation().equals(loc)) {
                set.add(r);
            }
        }
        return set;
    }

    /**
     * Getter method for all reviews from a certain location.
     * @param user user for the reviews
     * @return set of all of these reviews, empty set otherwise
     */
    public static Set<Review> getReviews(User user) {
        Set<Review> set = new HashSet<>();
        for (Review r : reviews) {
            if (r.getUser().equals(user)) {
                set.add(r);
            }
        }
        return set;
    }

    /**
     * Getter method for all locations in the phone.
     * @return unsorted list of all locations
     */
    public static List<Location> getAllLocations() {
        return new ArrayList<Location>(locations);
    }

    /**
     * Getter method for all locations within a limit, sorted by distance.
     * @param currentLat latitude of current location
     * @param currentLon longitude of current location
     * @param distanceTotal surrounding distance requested
     * @param limitQueries max number of queries (not necessarily exactly)
     * @return sorted ascending list by distance from current location
     */
    public static List<Location> getLocationsByDistance(double currentLat, double currentLon, double distanceTotal, int limitQueries) {
        final double lat = currentLat;
        final double lon = currentLon;
        List<Location> locs = new ArrayList<>(locations);
        for (Location loc : locs) {
            if (Math.pow(currentLat - loc.getLat(), 2) + Math.pow(currentLon - loc.getLon(), 2) > distanceTotal) {
                locs.remove(loc);
            }
        }
        Collections.sort(locs, new Comparator<Location>() {
            @Override
            public int compare(Location lhs, Location rhs) {
                double a = Math.pow(lat - lhs.getLat(), 2) + Math.pow(lon - lhs.getLon(), 2);
                double b = Math.pow(lat - rhs.getLat(), 2) + Math.pow(lon - rhs.getLon(), 2);
                return Double.compare(a, b);
            }
        });
        if (limitQueries < locations.size()) {
            locs = locs.subList(0, limitQueries);
        }
        return locs;
    }

    /**
     * Getter method for all locations within a distance, sorted by distance.
     * @param currentLat latitude of current location
     * @param currentLon longitude of current location
     * @param distanceTotal surrounding distance requested
     * @return sorted ascending list by distance from current location
     */
    public static List<Location> getLocationsByDistance(double currentLat, double currentLon, double distanceTotal) {
        return getLocationsByDistance(currentLat, currentLon, distanceTotal, locations.size());
    }

    /**
     * Getter method for all locations within a limit, sorted by rating.
     * @param currentLat latitude of current location
     * @param currentLon longitude of current location
     * @param distanceTotal surrounding distance requested
     * @param limitQueries max number of queries (not necessarily exactly)
     * @return sorted descending list by rating from current location
     */
    public static List<Location> getLocationsByRating(double currentLat, double currentLon, double distanceTotal, int limitQueries) {
        List<Location> locs = new ArrayList<Location>(locations);
        for (Location loc : locs) {
            if (Math.pow(currentLat - loc.getLat(), 2) + Math.pow(currentLon - loc.getLon(), 2) > distanceTotal) {
                locs.remove(loc);
            }
        }
        Collections.sort(locs, new Comparator<Location>() {
            @Override
            public int compare(Location lhs, Location rhs) {
                return Double.compare(lhs.getRating(), rhs.getRating());
            }
        });
        if (limitQueries < locations.size()) {
            locs = locs.subList(0, limitQueries);
        }
        return locs;
    }

    /**
     * Getter method for all locations within a distance, sorted by rating.
     * @param currentLat latitude of current location
     * @param currentLon longitude of current location
     * @param distanceTotal surrounding distance requested
     * @return sorted descending list by rating from current location
     */
    public static List<Location> getLocationsByRating(double currentLat, double currentLon, double distanceTotal) {
        return getLocationsByRating(currentLat, currentLon, distanceTotal, locations.size());
    }

    /**
     * Getter method for all locations within a limit, sorted by popularity.
     * @param currentLat latitude of current location
     * @param currentLon longitude of current location
     * @param distanceTotal surrounding distance requested
     * @param limitQueries max number of queries (not necessarily exactly)
     * @return sorted descending list by popularity from current location
     */
    public static List<Location> getLocationsByPopularity(double currentLat, double currentLon, double distanceTotal, int limitQueries) {
        List<Location> locs = new ArrayList<Location>(locations);
        for (Location loc : locs) {
            if (Math.pow(currentLat - loc.getLat(), 2) + Math.pow(currentLon - loc.getLon(), 2) > distanceTotal) {
                locs.remove(loc);
            }
        }
        Collections.sort(locs, new Comparator<Location>() {
            @Override
            public int compare(Location lhs, Location rhs) {
                return Double.compare(lhs.getNumRatings(), rhs.getNumRatings());
            }
        });
        if (limitQueries < locations.size()) {
            locs = locs.subList(0, limitQueries);
        }
        return locs;
    }

    /**
     * Getter method for all locations within a distance, sorted by popularity.
     * @param currentLat latitude of current location
     * @param currentLon longitude of current location
     * @param distanceTotal surrounding distance requested
     * @return sorted descending list by popularity from current location
     */
    public static List<Location> getLocationsByPopularity(double currentLat, double currentLon, double distanceTotal) {
        return getLocationsByPopularity(currentLat, currentLon, distanceTotal, locations.size());
    }

}
