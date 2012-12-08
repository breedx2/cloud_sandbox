package net.noisybox.cloud.voldemort;

import com.google.gson.Gson;
import net.noisybox.cloud.voldemort.entity.Band;
import net.noisybox.cloud.voldemort.entity.BandMember;
import voldemort.client.ClientConfig;
import voldemort.client.SocketStoreClientFactory;
import voldemort.client.StoreClient;
import voldemort.client.StoreClientFactory;
import voldemort.versioning.Versioned;

public class VoldemortClient {

    public static final String CONNECT_STRING = "tcp://localhost:6666";

    public static void main(String[] args) {
        ClientConfig config = new ClientConfig().setBootstrapUrls(CONNECT_STRING);
        StoreClientFactory factory = new SocketStoreClientFactory(config);

        // create a client that executes operations on a single store
        StoreClient<String, String> client = factory.getStoreClient("test");
        Versioned<String> result = client.get("howdy");
        System.out.println("Value: " + result.getValue());

        // Wow, voldemort json schema is too simple or I'm dumb or both.
//        StoreClient<String, Band> bandClient = factory.getStoreClient("bands");
//        Versioned<Band> entry = bandClient.get("Cerebral Bore");
//        if (entry == null) {
//            System.out.println("Band doesn't exist, adding...");
//            Band band = buildBand();
//            bandClient.put(band.getName(), band);
//        }
//        else{
//            System.out.println("Band exists: " + entry.getValue());
//        }

        StoreClient<String, String> bandClient = factory.getStoreClient("bands");

        Versioned<String> entry = bandClient.get("Cerebral Bore");
        if (entry == null) {
            System.out.println("Band doesn't exist, adding...");
            Band band = buildBand();
            bandClient.put(band.getName(), new Gson().toJson(band));
        }
        else{
            String rawJson = entry.getValue();
            System.out.println("raw json: " + rawJson);
            Band band = new Gson().fromJson(rawJson, Band.class);
            System.out.println("Band exists: " + band);
        }
    }

    private static Band buildBand() {
        BandMember vocalist = new BandMember("Simone \"Som\" Pluijmers", "vocals");
        BandMember guitarist = new BandMember("Paul McGuire", "guitar");
        BandMember bassist = new BandMember("Kyle Rutherford", "bass");
        BandMember drummer = new BandMember("Allan \"McDibet\" MacDonald", "drums");
        return new Band("Cerebral Bore", vocalist, guitarist, bassist, drummer);
    }
}
