//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package gpsUtil.location;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Getter
public class Attraction extends Location {
    public String attractionName;

    public String city;

    public String state;

    public UUID attractionId;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Attraction(@JsonProperty("attractionName") String attractionName, @JsonProperty("city") String city,@JsonProperty("state") String state, @JsonProperty("latitude") double latitude, @JsonProperty("longitude") double longitude) {
        super(latitude, longitude);
        this.attractionName = attractionName;
        this.city = city;
        this.state = state;
        this.attractionId = UUID.randomUUID();
    }

    public Attraction(double latitude, double longitude) {
        super(latitude, longitude);
    }

}
