package com.akshat.customlist.utils;


import com.akshat.customlist.models.RoutingData;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortUtil implements Comparator<RoutingData> {
    private static int SORT_FACTOR = -1;
    private static boolean sort_asc = true;

    public static void sortByDestPrefix(List<RoutingData> data) {
        if (SORT_FACTOR == 0 && sort_asc == false) {
            Collections.sort(data, Collections.reverseOrder(new SortUtil()));
            sort_asc = true;
        } else {
            SORT_FACTOR = 0;
            Collections.sort(data, new SortUtil());
            sort_asc = false;
        }
    }

    public static void sortByProtocol(List<RoutingData> data) {
        if (SORT_FACTOR == 1 && sort_asc == false) {
            Collections.sort(data, Collections.reverseOrder(new SortUtil()));
            sort_asc = true;
        } else {
            SORT_FACTOR = 1;
            Collections.sort(data, new SortUtil());
            sort_asc = false;
        }
    }

    public static void sortByAge(List<RoutingData> data) {
        if (SORT_FACTOR == 2 && sort_asc == false) {
            Collections.sort(data, Collections.reverseOrder(new SortUtil()));
            sort_asc = true;
        } else {
            SORT_FACTOR = 2;
            Collections.sort(data, new SortUtil());
            sort_asc = false;
        }
    }

    public static void sortByNextHop(List<RoutingData> data) {
        if (SORT_FACTOR == 3 && sort_asc == false) {
            Collections.sort(data, Collections.reverseOrder(new SortUtil()));
            sort_asc = true;
        } else {
            SORT_FACTOR = 3;
            Collections.sort(data, new SortUtil());
            sort_asc = false;
        }
    }

    @Override
    public int compare(RoutingData a1, RoutingData a2) {
        switch (SORT_FACTOR) {
            case 0:
            default:
                return a1.getDestPrefix().compareToIgnoreCase(a2.getDestPrefix());
            case 1:
                return a1.getProtocol().compareToIgnoreCase(a2.getProtocol());
            case 2:
                return String.valueOf(a1.getAge()).compareToIgnoreCase(String.valueOf(a2.getAge()));
            case 3:
                return a1.getNext_hop().compareToIgnoreCase(a2.getNext_hop());
        }
    }
}
