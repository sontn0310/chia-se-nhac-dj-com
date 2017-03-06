/*
 * Copyright (C) 2015 Naman Dwivedi
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package com.bteam.sharedj.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;


import com.bteam.sharedj.R;
import com.bteam.sharedj.adapters.SongsListAdapter;
import com.bteam.sharedj.listeners.INetworkCallback;
import com.bteam.sharedj.listeners.MusicStateListener;
import com.bteam.sharedj.models.Data;
import com.bteam.sharedj.models.Song;
import com.bteam.sharedj.networks.GetRequest;
import com.bteam.sharedj.widgets.DividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SongsFragment extends Fragment implements INetworkCallback, SwipeRefreshLayout.OnRefreshListener, MusicStateListener {
    List<Song> songList = new ArrayList<>();

    private SongsListAdapter mAdapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean userScrolled = true;
    private boolean isRefreshData = false;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    int page = 1;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_home, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        final ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.home);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        init();
        return rootView;
    }

    private void init() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        mAdapter = new SongsListAdapter((AppCompatActivity) getActivity());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    userScrolled = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                visibleItemCount = linearLayoutManager.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();
                if (userScrolled
                        && (visibleItemCount + pastVisiblesItems) == totalItemCount) {
                    userScrolled = false;
                    page++;
                    getdata(false);
                }
            }
        });
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        getdata(true);
    }

    public void restartLoader() {

    }

    public void onPlaylistChanged() {

    }

    @Override
    public void onMetaChanged() {

    }

    private void getdata(boolean isRefreshData) {
        this.isRefreshData = isRefreshData;
        mSwipeRefreshLayout.setRefreshing(isRefreshData);
        String uri = "/get/newest/" + page;
        GetRequest request = new GetRequest(getActivity());
        request.setTyper(Data.Typer.GETDATA);
        request.setOnNetWorkCallBack(this);
        request.execute(uri);
    }

    private void parseData(JSONArray data) throws JSONException {
        songList.clear();
        if (data != null) {
            for (int i = 0; i < data.length(); i++) {
                JSONObject obData = data.getJSONObject(i);
                Song song = new Song();
                song.setId(obData.optLong("id"));
                song.setTitle(obData.optString("title"));
                song.setLow_quality_link(obData.optString("low_quality_link"));
                song.setHigh_quality_link(obData.optString("high_quality_link"));
                song.setCreated_at(obData.optString("created_at"));
                songList.add(song);
                song = null;
                obData = null;
            }

            mAdapter.updateItems(isRefreshData, songList);
        }

    }

    @Override
    public void onLoadStart() {

    }

    @Override
    public void onResultData(Data result) {
        if (result != null) {
            if (result.mTyper == Data.Typer.GETDATA) {
                try {
                    boolean error = result.mJson.optBoolean("error");
                    JSONArray data = result.mJson.optJSONArray("data");
                    if (!error) {
                        parseData(data);
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.error), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mSwipeRefreshLayout.setRefreshing(false);

            }

        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.error), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onErrorData() {
        mSwipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onRefresh() {
        page = 1;
        getdata(true);
    }
}
