package com.whackyard.whatsappwalls;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class WallViewActivity extends Activity {

    //private ImageView mFullScrn;
    private String imgLink;
    CircularProgressView progressView;
    FloatingActionsMenu menuMultipleActions;
    private FloatingActionButton mDownload;
    private FloatingActionButton mShare;
    private FloatingActionButton mSetWall;
    private FloatingActionButton mSetWaWall;
    private Target target;
    private PhotoViewAttacher mAttacher;
    private PhotoView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall_view);

        progressView = (CircularProgressView) findViewById(R.id.progress_view);

        Bundle extras = getIntent().getExtras();
        imgLink = extras.getString("imgOriginal");

        //mFullScrn = (ImageView) findViewById(R.id.ivFullScreen);
        photoView = (PhotoView) findViewById(R.id.ivFullScreen);

        mDownload = (FloatingActionButton) findViewById(R.id.action_download);
        mShare =(FloatingActionButton) findViewById(R.id.action_share);
        mSetWall =(FloatingActionButton) findViewById(R.id.action_set_wall);
        mSetWaWall =(FloatingActionButton) findViewById(R.id.action_set_wa_wall);

        mAttacher = new PhotoViewAttacher(photoView);

        menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
    }



    @Override
    protected void onStart() {
        super.onStart();
        progressView.startAnimation();

        //mDownload.setIcon(R.drawable.ic_fab_download);
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
                //.fit()
                //.centerCrop()
                .into(photoView, new Callback() {
                    @Override
                    public void onSuccess() {
                        mAttacher.update();
                        progressView.stopAnimation();
                        progressView.setVisibility(View.GONE);
                        menuMultipleActions.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {

                    }
                });
        mDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Download Started...", Toast.LENGTH_SHORT).show();
                boolean success = true;
                File wallpaperDirectory = new File(Environment.getExternalStorageDirectory().getPath()+File.separator+"Walls");
                if(!wallpaperDirectory.exists()){
                    success = wallpaperDirectory.mkdir();
                }
                if (success) {
                    downloadImg();
                    mDownload.setEnabled(!mDownload.isEnabled());
                }
                else{
                    Toast.makeText(getBaseContext(), "The app was not allowed to write to your storage. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }
        });

        mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShareItem();
            }
        });

        mSetWall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWall();
                mSetWall.setEnabled(!mSetWall.isEnabled());
            }
        });

        mSetWaWall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWatsWall();
                mSetWaWall.setEnabled(!mSetWaWall.isEnabled());
                //Intent tst = new Intent(getBaseContext(), FirebaseStrgTest.class);
                //startActivity(tst);
            }
        });
    }

    private void downloadImg() {
        target = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String filename = "img"+ new Date().getTime() + ".png";

                        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/Walls"+File.separator, filename);
                            try {
                                file.createNewFile();
                                FileOutputStream ostream = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.PNG,100,ostream);
                                ostream.close();
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                    }
                }).start();

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {}

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {}
        };

        Picasso
                .with(getApplicationContext())
                .load(imgLink)
                .into(target);

    }

    public void onShareItem() {

        photoView.buildDrawingCache();
        Bitmap bitmap = photoView.getDrawingCache();
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/LatestShare.png";
        OutputStream out = null;
        File file=new File(path);
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        path=file.getPath();
        Uri bmpUri = Uri.parse("file://"+path);
        Intent shareIntent = new Intent();
        shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.setType("image/jpg");
        startActivity(Intent.createChooser(shareIntent,"Share with"));
    }

    public void setWall(){
        photoView.buildDrawingCache();
        Bitmap bitmap = photoView.getDrawingCache();
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/LatestShare.png";
        OutputStream out = null;
        File file=new File(path);
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Uri contentUri = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(contentUri, "image/*");
        intent.putExtra("mimeType", "image/*");
        startActivity(intent);
    }

    public void setWatsWall(){
        target = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //String filename = "img"+ new Date().getTime() + ".png";

                        File file = new File("/data/data/com.gbwhatsapp3/files/wallpaper.jpg");
                        try {
                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.PNG,100,ostream);
                            ostream.close();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {}

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {}
        };

        Picasso
                .with(getApplicationContext())
                .load(imgLink)
                .into(target);

    }
}
