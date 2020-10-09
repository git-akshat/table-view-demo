package com.akshat.customlist;

import android.app.Activity;
import android.widget.TextView;

import com.akshat.customlist.models.RoutingData;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

@Layout(R.layout.item_route_list)
public class RouteViewItem {

    @View(R.id.tv_dest_prefix)
    public TextView destPrefix;

    @View(R.id.tv_protocol)
    public TextView protocol;

    @View(R.id.tv_age)
    public TextView age;

    @View(R.id.tv_next_hop)
    public TextView nextHop;

    public RoutingData routingData;

    private Activity activity;

    public RouteViewItem(RoutingData routingData, Activity activity) {
        this.routingData = routingData;
        this.activity = activity;
    }

    @Resolve
    void renderUI() {
        destPrefix.setText(routingData.getDestPrefix().trim());
        protocol.setText(routingData.getProtocol().trim());
        age.setText(String.valueOf(routingData.getAge()));
        nextHop.setText(routingData.getNext_hop());
    }

}
