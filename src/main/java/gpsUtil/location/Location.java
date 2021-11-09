//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package gpsUtil.location;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

public class Location {
    public final double longitude;
    public final double latitude;


    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Location(@JsonProperty("latitude") double latitude,@JsonProperty("longitude") double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
