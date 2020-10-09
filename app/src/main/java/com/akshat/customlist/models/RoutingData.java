package com.akshat.customlist.models;

public class RoutingData {
    private String destPrefix;
    private String protocol;
    private int age;
    private String next_hop;

    public RoutingData() {

    }

    public RoutingData(String destPrefix, String protocol, int age, String next_hop) {
        this.destPrefix = destPrefix;
        this.protocol = protocol;
        this.age = age;
        this.next_hop = next_hop;
    }

    public String getDestPrefix() {
        return destPrefix;
    }

    public void setDestPrefix(String destPrefix) {
        this.destPrefix = destPrefix;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getNext_hop() {
        return next_hop;
    }

    public void setNext_hop(String next_hop) {
        this.next_hop = next_hop;
    }
}
