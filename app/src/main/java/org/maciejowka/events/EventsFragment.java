package org.maciejowka.events;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.maciejowka.R;
import org.maciejowka.events.data.EventsProvider;
import org.maciejowka.events.list.EventsAdapter;
import org.maciejowka.events.list.NextEventMarkerUpdater;
import org.maciejowka.events.model.EventModel;

import java.util.ArrayList;
import java.util.List;

public class EventsFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventsAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private NextEventMarkerUpdater nextEventMarkerUpdater;
    private IntentFilter nextEventMarkerIntentFilter;
    private Activity activity;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_events, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setFields();
        executeEventsProvider();
    }

    private void setFields() {
        activity = getActivity();
        recyclerView = getView().findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new EventsAdapter(new ArrayList<EventModel>(), activity.getApplicationContext());
        recyclerView.setAdapter(adapter);
        setSwipeRefreshLayout();
    }

    private void setSwipeRefreshLayout() {
        swipeRefreshLayout = getView().findViewById(R.id.fragment_events_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                executeEventsProvider();
            }
        });
    }

    private void executeEventsProvider() {
        new EventsProvider(this).execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        registerNextEventMarkerUpdater();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterNextEventMarkerUpdater();
    }

    private void registerNextEventMarkerUpdater() {
        setNextEventMarkerIntentFilter();
        setNextEventMarkerReceiver();
        activity.registerReceiver(nextEventMarkerUpdater, nextEventMarkerIntentFilter);
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

    private void unregisterNextEventMarkerUpdater() {
        activity.unregisterReceiver(nextEventMarkerUpdater);
    }

    public Context getApplicationContext() {
        return activity.getApplicationContext();
    }

    public void stopRefreshing() {
        swipeRefreshLayout.setRefreshing(false);
    }
}
