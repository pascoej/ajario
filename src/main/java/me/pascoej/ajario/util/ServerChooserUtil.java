package me.pascoej.ajario.util;

import us.monoid.web.Resty;

import java.net.URI;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static us.monoid.web.Resty.data;
import static us.monoid.web.Resty.form;

/**
 * Created by john on 6/15/15.
 */
public class ServerChooserUtil {
    private static final String[] regions = {"US-Fremont", "US-Atlanta", "BR-Brazil", "EU-London", "RU-Russia", "JP-Tokyo", "CN-China", "SG-Singapore"};
    private static final String requestURL = "http://m.agar.io/";

    public static String[] getRegions() {
        return regions;
    }

    public static URI getServer(String regionName) {
        try {
            return URI.create("ws://" + new Resty().text(requestURL, form(data("region", regionName))).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static URI bestServer(String regionName, int amount) {
        //for fun
        return IntStream.range(0,amount).parallel().mapToObj(a->getServer(regionName)).distinct().parallel().collect(Collectors.toMap(a -> a, LagScore::lagScore)).entrySet().stream().min((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
    }
}
