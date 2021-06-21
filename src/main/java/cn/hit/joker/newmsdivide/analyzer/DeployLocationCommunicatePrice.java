package cn.hit.joker.newmsdivide.analyzer;

import cn.hit.joker.newmsdivide.importer.classImporter.Deploy;

import java.util.Set;

/**
 * @author joker
 * @version 1.0
 * @date 2021/6/16 15:51
 * @description
 */
public class DeployLocationCommunicatePrice {
    public static final double EndToEdge = 1d;
    public static final double EndToEnd = 1.25d;
    public static final double EdgeToCloud = 1.25d;
    public static final double EdgeToEdge = 1.25d;
    public static final double CloudToCloud = 1.25d;
    public static final double EndToCloud = 1.5d;

    static class LocationPair {
        Deploy.Location from;
        Deploy.Location to;

        public LocationPair(Deploy.Location from, Deploy.Location to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof LocationPair) {
                LocationPair locationPair = (LocationPair) obj;
                if (locationPair.from == from && locationPair.to == to) {
                    return true;
                } else return locationPair.from == to && locationPair.to == from;
            }
            return false;
        }
    }

    // compute location pair price
    private static double locationPairPrice(LocationPair pair) {
        LocationPair endToEnd = new LocationPair(Deploy.Location.End, Deploy.Location.End);
        LocationPair endToEdge = new LocationPair(Deploy.Location.End, Deploy.Location.Edge);
        LocationPair endToCloud = new LocationPair(Deploy.Location.End, Deploy.Location.Cloud);
        LocationPair edgeToEdge = new LocationPair(Deploy.Location.Edge, Deploy.Location.Edge);
        LocationPair edgeToCloud = new LocationPair(Deploy.Location.Edge, Deploy.Location.Cloud);
        LocationPair cloudToCloud = new LocationPair(Deploy.Location.Cloud, Deploy.Location.Cloud);
        if (pair.equals(endToEnd)) {
            return EndToEnd;
        } else if (pair.equals(endToEdge)) {
            return EndToEdge;
        } else if (pair.equals(endToCloud)) {
            return EndToCloud;
        } else if (pair.equals(edgeToEdge)) {
            return EdgeToEdge;
        } else if (pair.equals(edgeToCloud)) {
            return EdgeToCloud;
        } else if (pair.equals(cloudToCloud)) {
            return CloudToCloud;
        } else {
            throw new IllegalArgumentException("locationPair 参数错误！");
        }
    }

    /**
     * get different deploy location communicate price
     *
     * @param from deploy location set
     * @param to deploy location set
     * @return communicate price
     */
    public static double getLocationPrice(Set<Deploy.Location> from, Set<Deploy.Location> to) {
        double price = 0d;
        for (Object o : from.toArray()) {
            Deploy.Location fromLocation = (Deploy.Location) o;
            for (Object m : to.toArray()) {
                Deploy.Location toLocation = (Deploy.Location) m;
                // generate location pair
                LocationPair locationPair = new LocationPair(fromLocation, toLocation);
                price += locationPairPrice(locationPair);
            }
        }
        return price / (from.size() * to.size());
    }
}
