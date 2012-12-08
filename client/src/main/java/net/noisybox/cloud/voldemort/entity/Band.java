package net.noisybox.cloud.voldemort.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Band implements Serializable {

    private final String name;
    private final List<BandMember> members;

    public Band(String name, BandMember... members) {
        this(name, Arrays.asList(members));
    }

    public Band(String name, List<BandMember> members) {
        this.members = members;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<BandMember> getMembers() {
        return members;
    }

    @Override
    public String toString() {
        return "Band{" +
                "name='" + name + '\'' +
                ", members=" + members +
                '}';
    }
}
