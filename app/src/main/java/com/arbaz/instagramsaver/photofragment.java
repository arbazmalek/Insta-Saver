package com.arbaz.instagramsaver;

import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.instagramsaver.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link photofragment} factory method to
 * create an instance of this fragment.
 */
public class photofragment extends Fragment {


    String URL = "NULL";
    ImageView photoView;
    EditText getphotolink;
    Button getcontent;
    Button downloadphoto,sharephoto;
    ImageView delete;
    ProgressBar progressBar;
    RelativeLayout progress_barr;

    String photourl="1";
    private Uri uri2;

    private AdView mAdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_photofragment, container, false);

        getcontent = v.findViewById(R.id.getphoto);
        getphotolink = v.findViewById(R.id.getphotolink);
        downloadphoto = v.findViewById(R.id.downloadphoto);
        sharephoto = v.findViewById(R.id.sharephoto);
        photoView = v.findViewById(R.id.particularphoto);
        delete = v.findViewById(R.id.delete);
        progressBar = v.findViewById(R.id.progressBar);
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

        delete.setOnClickListener(v1 -> getphotolink.setText(null));

        try {
            ClipboardManager clipboardManager1 = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData1 = clipboardManager1.getPrimaryClip();
            ClipData.Item item1 = clipData1.getItemAt(0);
            getphotolink.setText(item1.getText().toString());

            URL = getphotolink.getText().toString().trim();


            if (TextUtils.isEmpty(getphotolink.getText().toString())) {
            } else {
                progressBar.setVisibility(View.VISIBLE);
                progress_barr.setVisibility(View.VISIBLE);
                String result2 = StringUtils.substringBefore(URL, "/?");
                URL = result2 + "/?__a=1";
                processdata();

            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Please copy link first ", Toast.LENGTH_SHORT).show();
        }


        getcontent.setOnClickListener(v12 -> {

            ClipboardManager clipboardManager = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = clipboardManager.getPrimaryClip();
            ClipData.Item item = clipData.getItemAt(0);
            getphotolink.setText(item.getText().toString());

            URL = getphotolink.getText().toString().trim();
            if (TextUtils.isEmpty(getphotolink.getText().toString())) {
                Toast.makeText(getContext(), "First Enter URL", Toast.LENGTH_SHORT).show();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                progress_barr.setVisibility(View.VISIBLE);
                String result2 = StringUtils.substringBefore(URL, "/?");
                URL = result2 + "/?__a=1";
                processdata();

            }


        });


        downloadphoto.setOnClickListener(v13 -> {

            if (!photourl.equals("1")) {
                DownloadManager.Request request = new DownloadManager.Request(uri2);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
                request.setTitle("Download");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, File.separator + ".jpeg");
                DownloadManager manager = (DownloadManager) requireActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
                Toast.makeText(getContext(), "Downloaded " , Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "No photo to download " , Toast.LENGTH_SHORT).show();
            }
        });


        //share photo code start

        sharephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               image();

            }
        });



        return v;
    }


    // share image code
    private void image(){
try {
           Drawable mDrawable = photoView.getDrawable();
                Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();

                String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), mBitmap, "description", null);
                Uri uri = Uri.parse(path);

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(intent, "share via"));
        }catch (Exception e){
    Toast.makeText(getContext(), "No image to share", Toast.LENGTH_SHORT).show();
        }

    }

// share image code end







    private void processdata() {

        StringRequest request = new StringRequest(URL, response -> {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();

            try {
                Mainurl mainurl = gson.fromJson(response, Mainurl.class);
                photourl = mainurl.getGraphql().getShortcode_media().getDisplay_url();
                uri2 = Uri.parse(photourl);

                Glide.with(getContext()).load(uri2).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        progress_barr.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        progress_barr.setVisibility(View.GONE);
                        return false;
                    }
                }).into(photoView);
            }
            catch (Exception e){
            //    Toast.makeText(getContext(), "Paste Photo links only "+("\ud83d\ude07"), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                progress_barr.setVisibility(View.GONE);

                Toast.makeText(getContext(), photourl, Toast.LENGTH_SHORT).show();
            }





        }, error -> Toast.makeText(getContext(), "Not able to fetch", Toast.LENGTH_SHORT).show());
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);


    }

}