package org.maciejowka.maciejapp.events;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import org.maciejowka.maciejapp.R;

/**
 * Created by maciej on 19.08.17.
 */
public class EventsActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new EventsAdapter(getEvents());
        mRecyclerView.setAdapter(mAdapter);
    }

    private EventModel[] getEvents(){
        EventModel[] events = new EventModel[3];
        events[0]=new EventModel("Obiad", "dzisiaj 14:15", "2 godziny",
                " O 13:00 zaczynamy gotować. Przyjdź pomóc lub po prostu zjeść razem z nami!", null, null);
        events[1]=new EventModel("Sprzątanie", "dzisiaj 15:30", "do końca",
                " Przyjdź i pomóż wspolnie posprzątać duszpasterstwo!", null, null);
        events[2]=new EventModel("Planszówki", "dzisiaj 19:00", "do końca",
                " Przyjdź sam lub ze znajomymi, z grą lub bez! Spędźmy razem wieczór.", null, null);

        return events;
    }
}
