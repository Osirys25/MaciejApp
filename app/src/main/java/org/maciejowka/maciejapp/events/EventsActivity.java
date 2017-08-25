package org.maciejowka.maciejapp.events;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import org.maciejowka.maciejapp.R;
import org.maciejowka.maciejapp.events.data.EventsProvider;
import org.maciejowka.maciejapp.events.list.EventsAdapter;
import org.maciejowka.maciejapp.events.list.NextEventMarkerUpdater;
import org.maciejowka.maciejapp.events.model.EventModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maciej on 19.08.17.
 */
public class EventsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EventsAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private NextEventMarkerUpdater nextEventMarkerUpdater;
    private IntentFilter nextEventMarkerIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        adapter = new EventsAdapter(new ArrayList<EventModel>(), getApplicationContext());
        recyclerView.setAdapter(adapter);
        new EventsProvider(this).execute();
//        adapter = new EventsAdapter(getSortedEvents(), getApplicationContext());
//        recyclerView.setAdapter(adapter);

        registerNextEventMarkerUpdater();

    }

    private void registerNextEventMarkerUpdater() {
        setNextEventMarkerIntentFilter();
        setNextEventMarkerReceiver();
        registerReceiver(nextEventMarkerUpdater, nextEventMarkerIntentFilter);
    }

    private void setNextEventMarkerReceiver() {
        nextEventMarkerUpdater = new NextEventMarkerUpdater(adapter);
    }

    private void setNextEventMarkerIntentFilter() {
        nextEventMarkerIntentFilter = new IntentFilter();
        nextEventMarkerIntentFilter.addAction(Intent.ACTION_TIME_TICK);
    }

    public void update(List<EventModel> events) {
            adapter.setEvents(events);
            adapter.notifyDataSetChanged();
    }

    public void onDestroy() {
        super.onDestroy();
        unregisterNextEventMarkerUpdater();
    }

    private void unregisterNextEventMarkerUpdater() {
        unregisterReceiver(nextEventMarkerUpdater);
    }
}
