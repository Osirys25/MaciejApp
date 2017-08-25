package org.maciejowka.maciejapp.events.list;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import org.maciejowka.maciejapp.R;

/**
 * Created by maciej on 21.09.17.
 */
// Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
public class EventViewHolder extends RecyclerView.ViewHolder {
    // each data item is just a string in this case
    CardView cardView;
    FrameLayout posterLayout;
    FrameLayout frameInCard;
    TextView titleView;
    TextView dateView;
    TextView dateContView;
    TextView durationView;
    TextView durationContView;
    TextView summaryContView;
    ImageView posterView;
    View view;

    public EventViewHolder(View v) {
        super(v);
        view = v;
        cardView = (CardView) v.findViewById(R.id.card_view);
        frameInCard = (FrameLayout) v.findViewById(R.id.frame_in_card_view);
        posterLayout = (FrameLayout) v.findViewById(R.id.posterLayout);
        titleView = (TextView) posterLayout.findViewById(R.id.title);
        dateView = (TextView) cardView.findViewById(R.id.date);
        dateContView = (TextView) cardView.findViewById(R.id.dateCont);
        durationView = (TextView) cardView.findViewById(R.id.duration);
        durationContView = (TextView) cardView.findViewById(R.id.durationCont);
        summaryContView = (TextView) cardView.findViewById(R.id.summaryCont);
        posterView = (ImageView) posterLayout.findViewById(R.id.posterHardcoded);
    }
}
