package com.arbaz.instagramsaver;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.instagramsaver.R;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private final String[] titles = new String[]{"Reel/IGTV","Photo"};

    viewerAdapter viewerAdapter;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    EditText editText;

    //double tap exit
    private long backpressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //ads

        MobileAds.initialize(this, initializationStatus -> {
        });
        //ads code end

        Objects.requireNonNull(getSupportActionBar()).setTitle("Insta Saver");

        tabLayout=findViewById(R.id.tabllayout);
        viewPager2=findViewById(R.id.viewpager2);


        viewerAdapter = new viewerAdapter(this);


        viewPager2.setAdapter(viewerAdapter);

        new TabLayoutMediator(tabLayout,viewPager2,((tab, position) -> tab.setText(titles[position]))).attach();

    }

// NAVBAR CODEEE
    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId())
        {
            case R.id.share:
                Toast.makeText(MainActivity.this, "Thankyou for sharing! ❤", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Amazing Tool");
            intent.putExtra(Intent.EXTRA_TEXT, "Instagram Saver\n" +
                    "\n" +
                    "Download Photos\uD83E\uDD29, Reels ,IGTV from Instagram for free\uD83D\uDCF1.\n" +
                    " \n" +
                    "Features :\n" +
                    "➡️Fast download\n" +
                    "➡️Download in high definition (HD)\n" +
                    "➡️No login required\n" +
                    "\n" +
                    "DOWNLOAD APP NOW AND SHARE WITH YOUR FRIENDS \uD83E\uDD70\n" +
                    "\n" +
                    "Link : https://play.google.com/store/apps/details?id=com.arbaz.instagramsaver");
            startActivity(Intent.createChooser(intent, "Share Via"));
            return true;

            case R.id.insta:
                Uri uri = Uri.parse("https://instagram.com");
                Intent instagram = new Intent(Intent.ACTION_VIEW,uri);
                instagram.setPackage("com.instagram.android");
                try{
                    startActivity(instagram);
                }
                catch(ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://instagram.com")));
                }
                return true;

            case R.id.help:
                Intent gmail = new Intent(Intent.ACTION_SEND);
                String[] email_into={"a1rbaz2344@gmail.com"};
                gmail.putExtra(Intent.EXTRA_EMAIL,email_into);
                gmail.setType("text/html");
                gmail.setPackage("com.google.android.gm");
                try{
                    startActivity(gmail);
                }
                catch(ActivityNotFoundException e){
                    Toast.makeText(MainActivity.this, "Sorry for this!!", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.rate:
                Toast.makeText(MainActivity.this, "Thankyou for rating InstaSaver! "+("\ud83d\ude07"), Toast.LENGTH_SHORT).show();
                Uri uri1 = Uri.parse("https://play.google.com/store/apps/details?id=com.arbaz.instagramsaver");
                Intent playstore = new Intent(Intent.ACTION_VIEW,uri1);
                playstore.setPackage("com.android.vending");
                try{
                    startActivity(playstore);
                }
                catch(ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id=com.arbaz.instagramsaver")));
                }
                return true;

            case R.id.use:
                Intent intent2 = new Intent(MainActivity.this,howToUse.class);
                startActivity(intent2);
                return true;

            default:
                return super.onOptionsItemSelected(item);


        }
    }

//double tap to exit method
    @Override
    public void onBackPressed() {

        if(backpressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        }
        else {
            Toast.makeText(MainActivity.this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }

        backpressedTime = System.currentTimeMillis();
    }
    // exit method ends
}