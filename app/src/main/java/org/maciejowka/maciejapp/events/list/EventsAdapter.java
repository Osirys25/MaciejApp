package org.maciejowka.maciejapp.events.list;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.picasso.Picasso;
import org.maciejowka.maciejapp.R;
import org.maciejowka.maciejapp.events.model.EventDate;
import org.maciejowka.maciejapp.events.model.EventModel;
import org.maciejowka.maciejapp.events.poster.PosterHardcodedFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by maciej on 19.08.17.
 */
public class EventsAdapter extends RecyclerView.Adapter<EventViewHolder> {
    private List<EventModel> events;
    private Context context;
    private EventViewHolder holder;
    private int position;
    private final static int PADDING_16 = 16;
    private final static int PADDING_8 = 8;
    private final static int PADDING_0 = 0;

    // Provide a suitable constructor (depends on the kind of dataset)
    public EventsAdapter(List<EventModel> myDataset, Context context) {
        events = myDataset;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent,
                                              int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_event, parent, false);
        // set the view's size, margins, paddings and layout parameters
        //...
        return new EventViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        ///holder.mTextView.setText(events[position]);
        this.holder = holder;
        this.position = holder.getAdapterPosition();
        holder.titleView.setText(events.get(position).getTitle());
        holder.durationContView.setText(events.get(position).getDuration());
        holder.summaryContView.setText(events.get(position).getSummary());
        setDateOutput();
        setPosterWithPicasso();
        setNextEventMarker();
    }

    private void setDateOutput() {
        String dateTimeOutput = new EventDate(events.get(position)).getDateTimeOutput();
        holder.dateContView.setText(dateTimeOutput);
    }

    private void setPosterWithPicasso() {
        String uri = events.get(position).getPosterUrl();
        String posterLabel = events.get(position).getPosterHardcoded();
        if (posterLabel != null) {
            Drawable posterHardcoded = PosterHardcodedFactory.getPoster(posterLabel, context);
            holder.posterView.setImageDrawable(posterHardcoded);
        } else {
            Picasso.with(context).load(uri).placeholder(PosterHardcodedFactory.getBlank(context)).into(holder.posterView);
        }
        holder.posterView.setAdjustViewBounds(true);
    }

    private void setNextEventMarker() {
        if (findNextEventPosition() == position) {
            markEvent();
        } else {
            unmarkEvent();
        }
    }

    private void markEvent() {
        int paddingDpInPx_8 = dpToPx(PADDING_8);
        holder.view.setPadding(paddingDpInPx_8, paddingDpInPx_8, paddingDpInPx_8, paddingDpInPx_8);
        holder.frameInCard.setPadding(paddingDpInPx_8, paddingDpInPx_8, paddingDpInPx_8, paddingDpInPx_8);
    }

    private void unmarkEvent() {
        int paddingDpInPx_16 = dpToPx(PADDING_16);
        holder.view.setPadding(paddingDpInPx_16, paddingDpInPx_16, paddingDpInPx_16, paddingDpInPx_16);
        holder.frameInCard.setPadding(PADDING_0, PADDING_0, PADDING_0, PADDING_0);
    }

    private int dpToPx(int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        int px = (int) (dp * scale + 0.5f);
        return px;
    }

    private int findNextEventPosition() {
        if (events.isEmpty()) {
            return -1;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
        Date dateNow = new Date();
        int indexOfTheClosestEvent = -1;
        Date dateOfTheClosestEvent = new Date(dateNow.getTime() + DateUtils.YEAR_IN_MILLIS);
        Date dateOfEvent = null;
        for (EventModel event : events) {
            try {
                dateOfEvent = simpleDateFormat.parse(event.getDateAccurate());
            } catch (ParseException e) {
                e.printStackTrace(); //TODO handle
            }
            if (dateOfEvent.after(dateNow) && dateOfEvent.before(dateOfTheClosestEvent)) {
                dateOfTheClosestEvent = dateOfEvent;
                indexOfTheClosestEvent = events.indexOf(event);
            }
        }

        return indexOfTheClosestEvent;
    }

    public void setEvents(List<EventModel> events) {
        Log.i("@@@@@@@@@@@@@@@", "marker");
        this.events = events;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return events.size();
    }
}