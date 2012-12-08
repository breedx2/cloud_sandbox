package net.noisybox.cloud.voldemort.entity;

import java.io.Serializable;

public class BandMember implements Serializable {

    private final String name;
    private final String instrument;

    public String getName() {
        return name;
    }

    public String getInstrument() {
        return instrument;
    }

    public BandMember(String name, String instrument) {
        this.name = name;
        this.instrument = instrument;
    }

    @Override
    public String toString() {
        return "BandMember{" +
                "name='" + name + '\'' +
                ", instrument='" + instrument + '\'' +
                '}';
    }
}
