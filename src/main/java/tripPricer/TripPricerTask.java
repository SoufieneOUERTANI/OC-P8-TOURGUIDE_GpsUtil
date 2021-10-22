//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package tripPricer;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

@AllArgsConstructor
public class TripPricerTask implements Callable<List<Provider>> {
    private final String apiKey;
    private final UUID attractionId;
    private final int adults;
    private final int children;
    private final int nightsStay;

    public List<Provider> call() throws Exception {
        return (new TripPricer()).getPrice(this.apiKey, this.attractionId, this.adults, this.children, this.nightsStay, 5);
    }
}
