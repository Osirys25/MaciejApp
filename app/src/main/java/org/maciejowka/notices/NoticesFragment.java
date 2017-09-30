package org.maciejowka.notices;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import org.maciejowka.R;

public class NoticesFragment extends Fragment {

    private NoticesLoader noticesLoader;
    private SwipeRefreshLayout swipeRefreshLayout;
    private WebView webView;

    public NoticesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notices, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setWebView();
        setSwipeRefreshLayout();
        startLoading(LoadingStatus.RESTORING);
    }

    @Override
    public void onStop() {
        super.onStop();
        noticesLoader.stop();
    }

    private void setWebView() {
        webView = getView().findViewById(R.id.fragment_notices_web_view);
    }

    private void setSwipeRefreshLayout() {
        swipeRefreshLayout = getView().findViewById(R.id.fragment_notices_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startLoading(LoadingStatus.DOWNLOADING);
            }
        });
    }

    void startLoading(LoadingStatus loadingStatus) {
        noticesLoader = new NoticesLoader(this);
        noticesLoader.execute(loadingStatus);
    }

    void loadData(Notices notices) {
        getActivity().setTitle(notices.getTitle());
        webView.loadDataWithBaseURL("file:///android_asset/notices/", notices.getContent(), "text/html; charset=utf-8", "UTF-8", null);
        stopRefreshing();
    }

    void stopRefreshing() {
        swipeRefreshLayout.setRefreshing(false);
    }
}
