package custom;

import com.graphhopper.routing.ev.DecimalEncodedValueImpl;
import com.graphhopper.routing.ev.DefaultImportRegistry;
import com.graphhopper.routing.ev.ImportUnit;
import com.graphhopper.routing.ev.SimpleBooleanEncodedValue;
import com.graphhopper.routing.util.parsers.OSMCustomAttributesParser;

public final class MyImportRegistry extends DefaultImportRegistry {

    @Override
    public ImportUnit createImportUnit(String name) {
        switch (name) {
            case "length":
                return ImportUnit.create(
                        "length",
                        props -> new DecimalEncodedValueImpl("length", 24, 0.1, true),
                        (lookup, p) -> new OSMCustomAttributesParser(lookup)
                );
            case "width":
                return ImportUnit.create(
                        "width",
                        props -> new DecimalEncodedValueImpl("width", 16, 0.1, true),
                        (lookup, p) -> new OSMCustomAttributesParser(lookup)
                );
            case "poi_count":
                return ImportUnit.create(
                        "poi_count",
                        props -> new DecimalEncodedValueImpl("poi_count", 16, 1, true),
                        (lookup, p) -> new OSMCustomAttributesParser(lookup)
                );
            case "slope_forward_percent":
                return ImportUnit.create(
                        "slope_forward_percent",
                        props -> new DecimalEncodedValueImpl("slope_forward_percent", 20, 0.001, true),
                        (lookup, p) -> new OSMCustomAttributesParser(lookup)
                );
            case "forest_overlap_ratio":
                return ImportUnit.create(
                        "forest_overlap_ratio",
                        props -> new DecimalEncodedValueImpl("forest_overlap_ratio", 16, 0.001, true),
                        (lookup, p) -> new OSMCustomAttributesParser(lookup)
                );
            case "streetlights":
                return ImportUnit.create(
                        "streetlights",
                        props -> new SimpleBooleanEncodedValue("streetlights", true),
                        (lookup, p) -> new OSMCustomAttributesParser(lookup)
                );
            case "trafficlights":
                return ImportUnit.create(
                        "trafficlights",
                        props -> new SimpleBooleanEncodedValue("trafficlights", true),
                        (lookup, p) -> new OSMCustomAttributesParser(lookup)
                );
            case "speeding":
                return ImportUnit.create(
                        "speeding",
                        props -> new SimpleBooleanEncodedValue("speeding", true),
                        (lookup, p) -> new OSMCustomAttributesParser(lookup)
                );
            default:
                return super.createImportUnit(name);
        }
    }
}
