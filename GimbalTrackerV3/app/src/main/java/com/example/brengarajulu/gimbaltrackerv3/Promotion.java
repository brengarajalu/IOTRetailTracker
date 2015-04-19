package com.example.brengarajulu.gimbaltrackerv3;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by brengarajulu on 4/18/2015.
 */
public class Promotion {

   // private String "1"";
   @JsonProperty("beacon_id")
    private String beacon_id;

    @JsonProperty("rss")
    private String rss;


    @JsonProperty("offer")
    private List<String> offer;

   public String getBeacon_id() {
        return this.beacon_id;
    }

    public String getRss() {
        return this.rss;
    }

    public List<String> getOffers() {
        return this.offer;
    }

}
