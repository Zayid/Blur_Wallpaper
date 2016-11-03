package com.whackyard.whatsappwalls;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.whackyard.whatsappwalls.NavigatorActivity.database;
import static com.whackyard.whatsappwalls.NavigatorActivity.myRef;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mImgList;
    //private FirebaseDatabase database;
    //private DatabaseReference myRef;

    FirebaseRecyclerAdapter<Walls, WallsViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImgList = (RecyclerView) findViewById(R.id.wall_list);
        mImgList.setHasFixedSize(true);
        mImgList.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplicationContext(), R.dimen.item_offset);
        mImgList.addItemDecoration(itemDecoration);

        Bundle extras = getIntent().getExtras();
        String fbChild = extras.getString("thumb");

        //database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child(fbChild);
    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Walls, WallsViewHolder>(
                Walls.class,
                R.layout.wall_row,
                WallsViewHolder.class,
                myRef
        ) {
            @Override
            protected void populateViewHolder(WallsViewHolder viewHolder, final Walls model, final int position) {
                viewHolder.setImg(getApplicationContext(), model.getLink());

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
                    //.override(250,400)
                    .crossFade(500)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(load_img);
        }
    }
}
