package com.arbaz.instagramsaver;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.instagramsaver.R;

import org.w3c.dom.Text;

import java.util.Objects;

public class splashscreen extends AppCompatActivity {

    Animation topanim, bottomanim;
    ImageView image2;
    TextView textt2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        image2 = findViewById(R.id.imageView2);
        textt2 = findViewById(R.id.textt2);

        Objects.requireNonNull(getSupportActionBar()).hide();

        topanim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomanim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);


        image2.setAnimation(topanim);
       textt2.setAnimation(bottomanim);


        new Handler().postDelayed(() -> {
            Intent intent=new Intent(splashscreen.this, MainActivity.class);
            startActivity(intent);
            finish();
        },2000);
    }
}