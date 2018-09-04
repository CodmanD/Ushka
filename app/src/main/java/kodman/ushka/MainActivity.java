package kodman.ushka;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;


@TargetApi(17)
public class MainActivity extends AppCompatActivity {


    private static boolean isGo=false;
    Game game;
    InterstitialAd  mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

//---------Для отображения рекламы
       MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.ad_id_app));//"ca-app-pub-3917579650161866/9949160063");
       AdView adView=(AdView)this.findViewById(R.id.adView);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);



      //  MobileAds.initialize(this, getResources().getString(R.string.ad_id_app));
         mInterstitialAd = new InterstitialAd(this);
     //   Log.d("---", "idq = ");
         String id=getResources().getString(R.string.ad_interstitial_start);
        Log.d("---", "id = "+id);
        mInterstitialAd.setAdUnitId(id);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                 Log.d("---", "Loaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Toast.makeText(getActivity().getBaseContext(),"  Failed AD:"+errorCode,Toast.LENGTH_SHORT).show();
                // Code to be executed when an ad request fails.
                  Log.d("---", "FailedToLoad :" + errorCode);
            }

            @Override
            public void onAdOpened() {
                //Toast.makeText(getActivity().getBaseContext(),"  Opened AD",Toast.LENGTH_SHORT).show();
                // Code to be executed when the ad is displayed.
                 Log.d("---", "Opened");
                //flag = true;
            }

            @Override
            public void onAdLeftApplication() {
                //  Toast.makeText(getActivity().getBaseContext(),"  LeftApp AD",Toast.LENGTH_SHORT).show();
                // Code to be executed when the user has left the app.
                  Log.d("---", "AdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                // flag = true;
                // Log.d("---", "Closed");

                // Code to be executed when when the interstitial ad is closed.

                startGame();

            }

            @Override
            public void onAdClicked() {
                 Log.d("---", "AdClickded");
                //   flag = true;
                super.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                 Log.d("---", "Impression");
                super.onAdImpression();
            }
        });

        //Для полноэкранного изображения
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Без заголовка
        //   requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(isGo)
        {
            game = new Game(this);
            setContentView(game);
        }
    }

    public void btnClick(View view)
    {
        showAd();
    }

    private void showAd() {
        //  Log.d("---", "Show AD");
        if (mInterstitialAd.isLoaded()) {

            mInterstitialAd.show();
        } else {
            Log.d("---","Ad Not loaded ");
            startGame();
            //    Log.d("---", "The interstitial wasn't loaded yet.");
        }
    }

    private void startGame(){
        game = new Game(MainActivity.this);
        isGo=true;
        game.isEnd=false;
        setContentView(game);
    }
//
//    @Override
//    public void onResume()
//    {
//        super.onResume();
//
//        if(game!=null&&game.GT!=null)
//        {
//           //   game.GT.start();
//            //game.GT.pauseOff();
//        }
//    }
/*
    @Override
    public void onPause()
    {
        super.onPause();
        if(game!=null&&game.GT!=null)
        {
            game.GT.pauseOn();
        }

    }
    */

    @Override
    public void onBackPressed()
    {
      //  Log.d("-----STOP THREAD 1", "");
      //  System.out.println("BackPress");
        if(game!=null&&game.GT!=null)
        {
           // System.out.println("BackPress +stop Thread");
          //  Log.d("-----STOP THREAD Stop", "");
            game.GT.pauseOn();
        }

        AlertDialog.Builder builder= new AlertDialog.Builder(this,android.R.style.Theme_Holo_Light_Dialog);
        builder.setMessage(R.string.exit);
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

               // game.isEnd = true;
                finish();
                System.exit(0);
                return;
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            //    game.play();//.notify();
                //return;
                 if(game!=null&&game.GT!=null)
        {
            game.GT.pauseOff();
        }
            }
        });


        builder.show();

    }

    @Override
    public void onDestroy()
    {
         super.onDestroy();
        isGo=false;
        if(game==null)return;
        game.clearGame();
    }
  }

