package custom;

public final class PreferenceCoefficients {

    public final double gradient;
    public final double width;
    public final double tree;
    public final double poi;
    public final double speeding;
    public final double streetlights;
    public final double trafficlights;

    private PreferenceCoefficients(double gradient,
                                   double width,
                                   double tree,
                                   double poi,
                                   double speeding,
                                   double streetlights,
                                   double trafficlights) {
        this.gradient = gradient;
        this.width = width;
        this.tree = tree;
        this.poi = poi;
        this.speeding = speeding;
        this.streetlights = streetlights;
        this.trafficlights = trafficlights;
    }

    public static PreferenceCoefficients of(PreferenceProfile profile) {
        switch (profile) {
            case NONE:
                return new PreferenceCoefficients(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
            case PATH:
                return new PreferenceCoefficients(
                        1.574,
                        0.383,
                        27.426,
                        3.426,
                        1.128,
                        -24.894,
                        45.638
                );
            case META:
                return new PreferenceCoefficients(
                        2.932,
                        17.754,
                        4.131,
                        7.230,
                        16.607,
                        13.037,
                        13.634
                );
            default:
                throw new IllegalArgumentException("Unsupported profile: " + profile);
        }
    }
}
