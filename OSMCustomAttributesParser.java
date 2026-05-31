package com.graphhopper.routing.util.parsers;

import com.graphhopper.reader.ReaderWay;
import com.graphhopper.routing.ev.BooleanEncodedValue;
import com.graphhopper.routing.ev.DecimalEncodedValue;
import com.graphhopper.routing.ev.EdgeIntAccess;
import com.graphhopper.routing.ev.EncodedValueLookup;
import com.graphhopper.storage.IntsRef;

public class OSMCustomAttributesParser implements TagParser {
    public static final String KEY = "custom_attrs";

    private final DecimalEncodedValue widthEnc;
    private final DecimalEncodedValue poiCountEnc;
    private final DecimalEncodedValue slopeFwdEnc;
    private final DecimalEncodedValue forestOverlapEnc;
    private final DecimalEncodedValue lengthEnc;

    private final BooleanEncodedValue streetlightsEnc;
    private final BooleanEncodedValue trafficlightsEnc;
    private final BooleanEncodedValue speedingEnc;

    public OSMCustomAttributesParser(EncodedValueLookup lookup) {
        widthEnc = lookup.getDecimalEncodedValue("width");
        poiCountEnc = lookup.getDecimalEncodedValue("poi_count");
        slopeFwdEnc = lookup.getDecimalEncodedValue("slope_forward_percent");
        forestOverlapEnc = lookup.getDecimalEncodedValue("forest_overlap_ratio");
        lengthEnc = lookup.getDecimalEncodedValue("length");

        streetlightsEnc = lookup.getBooleanEncodedValue("streetlights");
        trafficlightsEnc = lookup.getBooleanEncodedValue("trafficlights");
        speedingEnc = lookup.getBooleanEncodedValue("speeding");
    }

    @Override
    public void handleWayTags(int edgeId, EdgeIntAccess eia, ReaderWay way, IntsRef relFlags) {
        setDecimalBoth(way, edgeId, eia, widthEnc, "width");
        setDecimalBoth(way, edgeId, eia, poiCountEnc, "poi_count");
        setSlopeAbsBoth(way, edgeId, eia, slopeFwdEnc, "slope_forward_percent");
        setDecimalBoth(way, edgeId, eia, forestOverlapEnc, "forest_overlap_ratio");
        setDecimalBoth(way, edgeId, eia, lengthEnc, "length");

        setBoolBoth(way, edgeId, eia, streetlightsEnc, "streetlights");
        setBoolBoth(way, edgeId, eia, trafficlightsEnc, "trafficlights");
        setBoolBoth(way, edgeId, eia, speedingEnc, "speeding");
    }

    private static void setDecimalBoth(ReaderWay way, int edgeId, EdgeIntAccess eia,
                                       DecimalEncodedValue ev, String key) {
        String v = way.getTag(key);
        if (v == null || v.isEmpty()) return;
        try {
            double d = Double.parseDouble(v);
            if (d < 0) d = 0;
            ev.setDecimal(false, edgeId, eia, d);
            ev.setDecimal(true, edgeId, eia, d);
        } catch (NumberFormatException ignore) {
        }
    }
    private static void setBoolBoth(ReaderWay way, int edgeId, EdgeIntAccess eia,
                                    BooleanEncodedValue ev, String key) {
        String v = way.getTag(key);
        if (v == null || v.isEmpty()) return;
    
        v = v.trim();
    
        boolean b;
        try {
            b = Double.parseDouble(v) > 0.0;
        } catch (NumberFormatException e) {
            b = "true".equalsIgnoreCase(v)
                    || "yes".equalsIgnoreCase(v)
                    || "y".equalsIgnoreCase(v)
                    || "1".equals(v);
        }
    
        ev.setBool(false, edgeId, eia, b);
        ev.setBool(true, edgeId, eia, b);
    }
    private static void setSlopeAbsBoth(ReaderWay way, int edgeId, EdgeIntAccess eia,
                                        DecimalEncodedValue ev, String key) {
        String v = way.getTag(key);
        if (v == null || v.isEmpty()) return;
        try {
            double d = Math.abs(Double.parseDouble(v));
            ev.setDecimal(false, edgeId, eia, d);
            ev.setDecimal(true, edgeId, eia, d);
        } catch (NumberFormatException ignore) {
        }
    }

}