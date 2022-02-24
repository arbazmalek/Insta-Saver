package com.arbaz.instagramsaver;

import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.instagramsaver.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.lang3.StringUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link reelfragment} factory method to
 * create an instance of this fragment.
 */
public class reelfragment extends Fragment {

    String URL = "NULL";
    VideoView videoView;
    EditText getreel;
    Button getcontent;
    Button downloadreel,sharereel;
    ImageView delete;
    private MediaController mediaController;
    String reelurl="1";
    private Uri uri2;
    private AdView mAdView;


    ProgressBar progressBar;
    RelativeLayout progress_barr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_reelfragment, container, false);

        getreel = v.findViewById(R.id.getreellink);
        downloadreel = v.findViewById(R.id.download);
        delete = v.findViewById(R.id.delete);
        getcontent = v.findViewById(R.id.getreel);
        videoView = v.findViewById(R.id.particularreel);
        mediaController = new MediaController(getContext());
        mediaController.setAnchorView(videoView);
        progressBar=v.findViewById(R.id.progressBar3);
        mAdView = v.findViewById(R.id.adView);
        progress_barr = v.findViewById(R.id.progress_barr);
        //ads

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                super.onAdLoaded();

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                // Code to be executed when an ad request fails.
                super.onAdFailedToLoad(adError);
                mAdView.loadAd(adRequest);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                super.onAdOpened();
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
        //ads end

        try {
            ClipboardManager clipboardManager1 = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData1 = clipboardManager1.getPrimaryClip();
            ClipData.Item item1 = clipData1.getItemAt(0);
            getreel.setText(item1.getText().toString());

            URL = getreel.getText().toString().trim();
            if (TextUtils.isEmpty(getreel.getText().toString())) {
            } else {
                progressBar.setVisibility(View.VISIBLE);
                progress_barr.setVisibility(View.VISIBLE);
                String result2 = StringUtils.substringBefore(URL, "/?");
                URL = result2 + "/?__a=1";

                processdata();
            }
        }
        catch (Exception e) {
            Toast.makeText(getContext(), "Please copy link first ", Toast.LENGTH_SHORT).show();
        }

        getcontent.setOnClickListener(v1 -> {

            ClipboardManager clipboardManager = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = clipboardManager.getPrimaryClip();
            ClipData.Item item = clipData.getItemAt(0);
            getreel.setText(item.getText().toString());

            URL = getreel.getText().toString().trim();
            if (TextUtils.isEmpty(getreel.getText().toString())) {
                Toast.makeText(getContext(), "First Enter URL", Toast.LENGTH_SHORT).show();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                progress_barr.setVisibility(View.VISIBLE);
                String result2 = StringUtils.substringBefore(URL, "/?");
                URL = result2 + "/?__a=1";

                    processdata();
                }





        });

        //  deleteeeeeeeeeeeeeeeeeeee section

        delete.setOnClickListener(v13 -> getreel.setText(null));



     try {

         downloadreel.setOnClickListener(v12 -> {

             if (!reelurl.equals("1")) {

                 DownloadManager.Request request = new DownloadManager.Request(uri2);
                 request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
                 request.setTitle("Download");
                 request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                 request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "" + System.currentTimeMillis() + ".mp4");
                 DownloadManager manager = (DownloadManager) requireActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                 manager.enqueue(request);
                 Toast.makeText(getContext(), "Downloaded ", Toast.LENGTH_SHORT).show();
             } else {
                 Toast.makeText(getContext(), "No video to download ", Toast.LENGTH_SHORT).show();
             }
         });
     }
     catch (Exception e){
         Toast.makeText(getContext(), "No download", Toast.LENGTH_SHORT).show();
     }


        // sharereel code start


        // share reel code end

        return v;
    }













    //share reel method
    private void reel() {

        Toast.makeText(getContext(), "Under development", Toast.LENGTH_SHORT).show();
    }
    //share method end

















    private void processdata() {

        StringRequest request = new StringRequest(URL, response -> {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();

            try {
                Mainurl mainurl = gson.fromJson(response, Mainurl.class);
                reelurl = mainurl.getGraphql().getShortcode_media().getVideo_url();
                uri2 = Uri.parse(reelurl);
                videoView.setMediaController(mediaController);
                videoView.setVideoURI(uri2);
                videoView.requestFocus();
                videoView.start();
                progressBar.setVisibility(View.GONE);
                progress_barr.setVisibility(View.GONE);
            }
            catch (Exception e){
               // Toast.makeText(getContext(), "For photo download go to PHOTO", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                progress_barr.setVisibility(View.GONE);
                Toast.makeText(getContext(), reelurl, Toast.LENGTH_SHORT).show();
            }

        }, error -> Toast.makeText(getContext(), "Not able to fetch", Toast.LENGTH_SHORT).show());
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);


    }
}
