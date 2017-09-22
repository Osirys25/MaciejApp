package org.maciejowka.events.list;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;

/**
 * Created by maciej on 09.09.17.
 */
public class NextEventMarkerUpdater extends BroadcastReceiver {

    private RecyclerView.Adapter adapter;

    public NextEventMarkerUpdater(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if (action.equals(Intent.ACTION_TIME_TICK)) {
            updateMarkerOfClosestEvent();
        }
    }

    private void updateMarkerOfClosestEvent() {
            adapter.notifyDataSetChanged();
    }

}
