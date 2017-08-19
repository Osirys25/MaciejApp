package org.maciejowka.maciejapp.events;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import org.maciejowka.maciejapp.R;

/**
 * Created by maciej on 19.08.17.
 */
public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {
    private EventModel[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView cardView;
        public TextView titleView;
        public TextView dateView;
        public TextView dateContView;
        public TextView durationView;
        public TextView durationContView;
        public TextView summaryContView;
        public ImageView posterView;

        public ViewHolder(View v) {
            super(v);
            cardView = (CardView) v.findViewById(R.id.card_view);
            titleView = (TextView) cardView.findViewById(R.id.title);
            dateView = (TextView) cardView.findViewById(R.id.date);
            dateContView = (TextView) cardView.findViewById(R.id.dateCont);
            durationView = (TextView) cardView.findViewById(R.id.duration);
            durationContView = (TextView) cardView.findViewById(R.id.durationCont);
            summaryContView = (TextView) cardView.findViewById(R.id.summaryCont);
            posterView = (ImageView) cardView.findViewById(R.id.poster);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public EventsAdapter(EventModel[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EventsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_event, parent, false);
        // set the view's size, margins, paddings and layout parameters
        //...
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        ///holder.mTextView.setText(mDataset[position]);
        holder.titleView.setText(mDataset[position].getTitle());
        holder.dateContView.setText(mDataset[position].getDate());
        holder.durationContView.setText(mDataset[position].getDuration());
        holder.summaryContView.setText(mDataset[position].getSummary());
        if (mDataset[position].getPoster() != null) {
            holder.posterView.setImageDrawable(mDataset[position].getPoster());
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}