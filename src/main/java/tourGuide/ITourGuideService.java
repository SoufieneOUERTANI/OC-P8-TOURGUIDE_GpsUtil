package tourGuide;

import gpsUtil.location.Attraction;
import tripPricer.Provider;
import user.User;
import user.UserReward;
import user.VisitedLocation;

import java.util.List;

public interface ITourGuideService {
    List<UserReward> getUserRewards(User user);

    VisitedLocation getUserLocation(User user);

    User getUser(String userName);

    List<User> getAllUsers();

    void addUser(User user);

    List<Provider> getTripDeals(User user);

    VisitedLocation trackUserLocation(User user);

    List<Attraction> getNearByAttractions(VisitedLocation visitedLocation);

}
