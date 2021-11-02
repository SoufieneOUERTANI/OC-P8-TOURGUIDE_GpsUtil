//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package user;

import com.fasterxml.jackson.annotation.JsonFilter;
import gpsUtil.location.Location;
import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@JsonFilter("VisitedLocationFilter")
public class VisitedLocation {
    public final UUID userId;
    public final Location location;
    public final Date timeVisited;

}
