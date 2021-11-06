package gpsUtil;

import gpsUtil.location.Attraction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import user.VisitedLocation;


import java.util.List;
import java.util.UUID;

@RestController
public class GpsUtilController {

    @Autowired
    GpsUtilService gpsUtilService;

    @GetMapping("/getUserLocation")
    public VisitedLocation getUserLocation(@RequestParam UUID userId) {
        return gpsUtilService.getUserLocation(userId);
    }

    @GetMapping("/getAttractions")
    static final public List<Attraction> getAttractions() {
        return GpsUtilService.getAttractions();
    }

}
