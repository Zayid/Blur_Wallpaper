package com.whackyard.whatsappwalls;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

public class NavigatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigator);
    }

    public void loadThumb(View v){
        switch(v.getId()){
            case R.id.frmBlur:
                Intent blurIntnt = new Intent(this,MainActivity.class);
                blurIntnt.putExtra("thumb","Blur");
                startActivity(blurIntnt);
                break;
            case R.id.frmMinimal:
                Intent miniIntnt = new Intent(this,MainActivity.class);
                miniIntnt.putExtra("thumb","Minimal");
                startActivity(miniIntnt);
                break;

        }
    }
}
