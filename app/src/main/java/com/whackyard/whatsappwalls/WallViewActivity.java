package com.whackyard.whatsappwalls;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class WallViewActivity extends Activity {

    private ImageView mFullScrn;
    private String imgLink;
    CircularProgressView progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall_view);

        progressView = (CircularProgressView) findViewById(R.id.progress_view);

        Bundle extras = getIntent().getExtras();
        imgLink = extras.getString("imgOriginal");

        mFullScrn = (ImageView) findViewById(R.id.ivFullScreen);
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressView.startAnimation();
        /*Glide
                .with(getApplicationContext())
                .load(imgLink).asBitmap().format(DecodeFormat.PREFER_ARGB_8888)
                .placeholder(R.color.placeholder)
                .centerCrop()
                .into(mFullScrn);*/
        Picasso
                .with(getApplicationContext())
                .load(imgLink)
                .placeholder(R.color.placeholder)
                .fit()
                .centerCrop()
                .into(mFullScrn, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressView.stopAnimation();
                        progressView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });
    }
}
