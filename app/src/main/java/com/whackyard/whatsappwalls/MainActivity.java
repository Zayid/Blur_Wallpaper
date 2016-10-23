package com.whackyard.whatsappwalls;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mImgList;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImgList = (RecyclerView) findViewById(R.id.wall_list);
        mImgList.setHasFixedSize(true);
        mImgList.setLayoutManager(new LinearLayoutManager(this));

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Blur");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Walls, WallsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Walls, WallsViewHolder>(
                Walls.class,
                R.layout.wall_row,
                WallsViewHolder.class,
                myRef
        ) {
            @Override
            protected void populateViewHolder(WallsViewHolder viewHolder, Walls model, int position) {
                viewHolder.setImg(getApplicationContext(), model.getLink());
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
            Picasso.with(ctx).load(link).into(load_img);
        }
    }

}
