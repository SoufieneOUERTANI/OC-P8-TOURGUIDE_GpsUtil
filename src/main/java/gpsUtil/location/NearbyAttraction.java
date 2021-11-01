package gpsUtil.location;

import gpsUtil.location.Location;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import user.VisitedLocation;

@AllArgsConstructor
@NoArgsConstructor
public class NearbyAttraction {

    String attractionName;
    Location attractionLocation;
    Location userVisitedLocation;
    double distance;
    int attractionRewardPoints;

}
