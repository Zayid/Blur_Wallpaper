package com.whackyard.whatsappwalls;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import static com.whackyard.whatsappwalls.MyTask.database;
import static com.whackyard.whatsappwalls.MyTask.myRef;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mImgList;
    private ActionBar mActionBar;
    private String fbChild;
    GridLayoutManager mRecyclerGridMan;

    private Parcelable mListState = null;
    private static Bundle mBundleRecyclerViewState;
    private final String KEY_RECYCLER_STATE = "recycler_state";


/*  private final String LIST_STATE_KEY = "recycler_state";
    private int scrollPosition = 0;
    private boolean shouldKeepScrollPosition = true;

    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";

    private  int lastFirstVisiblePosition;  */

    private static final String TAG = "SampleActivity";
    private static final boolean VERBOSE = true;

    FirebaseRecyclerAdapter<Walls, WallsViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (VERBOSE) Log.v(TAG, "+++ ON CREATE +++");
        setContentView(R.layout.activity_main);

        mImgList = (RecyclerView) findViewById(R.id.wall_list);
        mImgList.setHasFixedSize(true);
        mRecyclerGridMan = new GridLayoutManager(getApplicationContext(),3);
        mImgList.setLayoutManager(mRecyclerGridMan);

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplicationContext(), R.dimen.item_offset);
        mImgList.addItemDecoration(itemDecoration);

        Bundle extras = getIntent().getExtras();
        fbChild = extras.getString("thumb");

        mActionBar = getSupportActionBar();
        myRef = database.getReference().child(fbChild);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (VERBOSE) Log.v(TAG, "++ ON START ++");

        mActionBar.setTitle(fbChild);
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Walls, WallsViewHolder>(
                Walls.class,
                R.layout.wall_row,
                WallsViewHolder.class,
                myRef
        ) {
            @Override
            protected void populateViewHolder(WallsViewHolder viewHolder, final Walls model, final int position) {

                viewHolder.setImg(getApplicationContext(), model.getThumb());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String imgClicked="";
                        imgClicked=model.getLink();
                        Intent imgFullScrn = new Intent(getBaseContext(), WallViewActivity.class);
                        imgFullScrn.putExtra("imgOriginal",imgClicked);
                        startActivity(imgFullScrn);
                    }
                });
            }
        };

        mImgList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class WallsViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public WallsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setImg(Context ctx, String link){
            ImageView load_img = (ImageView) mView.findViewById(R.id.ivWalls);
            Glide
                    .with(ctx)
                    .load(link)
                    .thumbnail(0.1f)
                    .placeholder(R.color.placeholder)
                    .centerCrop()
                    .crossFade(300)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(load_img);
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (VERBOSE) Log.v(TAG, "- ON PAUSE -");

        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        mListState = mImgList.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, mListState);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (VERBOSE) Log.v(TAG, "+ ON RESUME +");

        // restore RecyclerView state
        if (mBundleRecyclerViewState != null) {
            if (VERBOSE) Log.v(TAG, "-- NOT NULL --");
            mListState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            mImgList.getLayoutManager().onRestoreInstanceState(mListState);
        }
        else {
            if (VERBOSE) Log.v(TAG, "-- NULL --");
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);

        // Retrieve list state and list/item positions
        if(state != null)
            mListState =   mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (VERBOSE) Log.v(TAG, "-- ON STOP --");
        mBundleRecyclerViewState = new Bundle();
        mListState = mImgList.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE,     mListState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (VERBOSE) Log.v(TAG, "- ON DESTROY -");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (VERBOSE) Log.v(TAG, "+++ ON RESTART +++");
    }

    /*    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

        // Save list state
        mListState = mRecyclerGridMan.onSaveInstanceState();
        state.putParcelable(LIST_STATE_KEY, mListState);
    }
    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);

        // Retrieve list state and list/item positions
        if(state != null)
            mListState = state.getParcelable("recycler_state");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mListState != null) {
            mRecyclerGridMan.onRestoreInstanceState(mListState);
        }
    }*/

  /* @Override
    protected void onPause() {
        super.onPause();
        lastFirstVisiblePosition = 0;
        lastFirstVisiblePosition = ((GridLayoutManager) mImgList.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((GridLayoutManager) mImgList.getLayoutManager()).scrollToPosition(lastFirstVisiblePosition);
        lastFirstVisiblePosition = 0;
    }   */

 /*   @Override
    public void onRestoreInstanceState( Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null)
        {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            mImgList.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, mImgList.getLayoutManager().onSaveInstanceState());
    }*/
}
