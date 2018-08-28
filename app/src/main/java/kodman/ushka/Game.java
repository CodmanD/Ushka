package kodman.ushka;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by android on 05.10.2017.
 */
@TargetApi(21)
public class Game extends SurfaceView implements SurfaceHolder.Callback ,
                                                  View.OnTouchListener,
                                                     SensorEventListener
{

    Context context;

    //Для восстановления позиций
    private static ArrayList<RectF> listSaveEnemy;//= new ArrayList<RectF>() ;
    private static ArrayList<Integer> listSaveEnemyLevel;//= new ArrayList<RectF>() ;
    private static ArrayList<RectF> listSaveFire;//= new ArrayList<RectF>() ;
    private static RectF rectFU2;//позиция игрока


    private static int score=0;
    private static int level=1;

    //ДЛя Sensors
    private SensorManager SM;
    private Sensor accel;

    public GameThread GT;//поток для отрисовки игры
    private U2  u2;//игрок
  private int LIFE=5;

    Bitmap bmpU2;
    Bitmap bmpU2_light;
    Bitmap bmpShield;
    Bitmap bmpFlash;
    Bitmap bmpExplosion;
    Bitmap bmpEnemy_level_1;
    Bitmap bmpEnemy_level_2;
    Bitmap bmpEnemy_level_3;
    Bitmap bmpEnemy_level_4;
    Bitmap bmpEnemy_level_5;
    Bitmap bmpEnemy_level_6;
    Bitmap bmpFire;
    Bitmap bmpSky;
    static Bitmap bmpEnd;



    String message="";
    private static int cnt= 0;//счетчик отрисовки surfaceView

    public static boolean isEnd=false; //конец игры

    public static String str="";



    private SoundPool sp;
    int soundBux;
    int soundGame;
    int soundVystrel;
    private int streamBux;
    private int streamSoundGame=-1;
    private int streamVystrel;

    public Game(Context context)
    {
        super(context);
        this.context=context;
        this.getHolder().addCallback(this);

        // this.setOnClickListener(this);
        this.setOnTouchListener(this);

        this.SM= (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        this.accel=this.SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //this.SM.registerListener(this, this.accel, SensorManager.SENSOR_DELAY_GAME);

        Resources res= getResources();



        //Картинки самолета
        BitmapDrawable drawable=(BitmapDrawable)res.getDrawable(R.drawable.u2_1,context.getTheme());
        bmpU2=drawable.getBitmap();
        drawable=(BitmapDrawable)res.getDrawable(R.drawable.u2_light,context.getTheme());
        bmpU2_light=drawable.getBitmap();

        //Картинки неба
        drawable=(BitmapDrawable)res.getDrawable(R.drawable.sky1,context.getTheme());
        bmpSky=drawable.getBitmap();

        //Картинки выстрелов самолета
        drawable=(BitmapDrawable)res.getDrawable(R.drawable.fire,context.getTheme());
        bmpFire=drawable.getBitmap();


        //Картинки врагов
        //Var_1
//        drawable=(BitmapDrawable)res.getDrawable(R.drawable.ufo_level_1,context.getTheme());
//        bmpEnemy_level_1=drawable.getBitmap();
//        drawable=(BitmapDrawable)res.getDrawable(R.drawable.ufo_level_2,context.getTheme());
//        bmpEnemy_level_2=drawable.getBitmap();
//        drawable=(BitmapDrawable)res.getDrawable(R.drawable.ufo_level_3,context.getTheme());
//        bmpEnemy_level_3=drawable.getBitmap();
//        drawable=(BitmapDrawable)res.getDrawable(R.drawable.ufo_level_4,context.getTheme());
//        bmpEnemy_level_4=drawable.getBitmap();
//        drawable=(BitmapDrawable)res.getDrawable(R.drawable.ufo_level_5,context.getTheme());
//        bmpEnemy_level_5=drawable.getBitmap();

        //Var_2
        drawable=(BitmapDrawable)res.getDrawable(R.drawable.ufo_orange_alien,context.getTheme());
        bmpEnemy_level_1=drawable.getBitmap();
        drawable=(BitmapDrawable)res.getDrawable(R.drawable.ufo_fiolet_alien,context.getTheme());
        bmpEnemy_level_2=drawable.getBitmap();
        drawable=(BitmapDrawable)res.getDrawable(R.drawable.ufo_red_alien,context.getTheme());
        bmpEnemy_level_3=drawable.getBitmap();
       // drawable=(BitmapDrawable)res.getDrawable(R.drawable.ufo_metal_with_blue_fire,context.getTheme());
        drawable=(BitmapDrawable)res.getDrawable(R.drawable.ufo_boss,context.getTheme());
        bmpEnemy_level_4=drawable.getBitmap();
        drawable=(BitmapDrawable)res.getDrawable(R.drawable.ufo_green,context.getTheme());
        bmpEnemy_level_5=drawable.getBitmap();
        drawable=(BitmapDrawable)res.getDrawable(R.drawable.ufo_metal,context.getTheme());
        bmpEnemy_level_6=drawable.getBitmap();

        drawable=(BitmapDrawable)res.getDrawable(R.drawable.explosion,context.getTheme());
        bmpExplosion=drawable.getBitmap();
        drawable=(BitmapDrawable)res.getDrawable(R.drawable.flash,context.getTheme());
        bmpFlash=drawable.getBitmap();
        drawable=(BitmapDrawable)res.getDrawable(R.drawable.camuflage,context.getTheme());
        bmpShield=drawable.getBitmap();


        Log.d("---------------- 1 ---", "constructor");

        SoundPool.Builder builder = new SoundPool.Builder();
        builder.setMaxStreams(5);
        builder.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build());
        this.sp = builder.build();
        this.soundBux = this.sp.load(context, R.raw.bux, 1);
        this.soundGame = this.sp.load(context, R.raw.sound_game_2, 1);
        streamSoundGame=-1;
        this.soundVystrel = this.sp.load(context, R.raw.vystrel1, 1);
        Log.d("------------SOUND--", "soundGame =" + this.soundGame+"  sp = "+sp);


    }


    //методы для акселерометра
    @Override
    public void onAccuracyChanged(Sensor sensor ,int accuracy)//SensorEvent event)
    {

    }

    //Обработка данных от сенсора
    @Override
    public void onSensorChanged(SensorEvent event)
    {
        synchronized (this.GT)
        {
            if (this.u2 == null) {
                Log.d("---------SENSOIR------", "" + event.sensor.isWakeUpSensor());
                return;
            }

            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            {
                float[] values = event.values;
                message = "Sensor Work" + cnt;
                RectF r = this.u2.getRf();
                if ((values[0] < -1) && (r.right + 10) < this.getWidth()) {
                    //  Log.d("---------","Fly to Right");
                    r.left += 10;
                    r.right += 10;
                } else if ((values[0] > 1) && (r.left - 10) > 0) {
                    //  Log.d("---------","Fly to LEFT");
                    r.left -= 10;
                    r.right -= 10;
                }
            }
        }
    }


    @Override
    public boolean onTouch(View v,MotionEvent event )
    {
        // Game.this.streamSoundGame=Game.this.sp.play(Game.this.soundGame,1,1,0,0,1);
        int x=(int)event.getX();
        int y=(int)event.getY();
        synchronized (this.GT)
        {
            if(isEnd)
            {
                isEnd=false;
                newGame();

            }
            if(event.getAction()==MotionEvent.ACTION_DOWN)
            {
                RectF r = u2.getRf();
                if((y>r.top&&y<r.bottom)&&(x>r.left&&x<r.right))
                {
                    if( Fire.status==1)
                        Fire.status=2;
                    else
                    if(Fire.status==2)
                        Fire.status=1;
                }
                else
                if ((x >= r.right) && (r.right + 10 < this.getWidth()))
                {
                    u2.moveLeft=false;
                    u2.moveRight=true;
                    //r.left += 10;
                    //r.right += 10;
                } else if (x <= r.left && (r.left - 10) > 0)
                {
                    u2.moveLeft=true;
                    //r.left -= 10;
                    //r.right -= 10;
                }

            }
            if(event.getAction()==MotionEvent.ACTION_UP)
            {
                u2.moveLeft=false;
                u2.moveRight=false;
            }

            // u2.setX(u2.getX() - 10);
            Log.d("----TOUCH -----","x="+this.getWidth()/2+"  xTouch="+x);
        }
        return true;
    }



    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        //u2=new U_2(this.getWidth()/2,this.getHeight()-300);
        this.GT= new GameThread();



        // Log.d("---------------- 1 -----","Surface created size list"+this.listSaveFire.size());
        if(this.listSaveFire!=null&&
                this.listSaveFire.size()>0&&
                !isEnd)
        {
            loadState();
        }

        else
        {

            float left=this.getWidth() / 2 - U2.WIDTH/2;
            float top= this.getHeight() -(2*U2.HEIGHT);
            float right=left+U2.WIDTH;
            float bottom=top+u2.HEIGHT;
            u2=new U2(new RectF(left,top,right,bottom));

            int quantity=GT.w/Enemy.WIDTH_Level_1;
            for (int i = 0; i < quantity; i++)
            {
                //враги
                RectF r = new RectF(0 + i * Enemy.WIDTH_Level_1, 0,
                        (0 + i * Enemy.WIDTH_Level_1) + Enemy.WIDTH_Level_1, Enemy.HEIGHT_Level_1);
                Enemy enemy = new Enemy(r,1,GT.curTime);

                // enemy.start();
                GT.arrEnemys.add(enemy);
            }


        }
        Game.this.SM.registerListener(Game.this, Game.this.accel, SensorManager.SENSOR_DELAY_GAME);

     this.GT.start();


    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        this.SM.unregisterListener(this);

        Game.this.sp.stop(Game.this.streamSoundGame);
        //Сохраняем текущее положение игры
        if(!isEnd) {
            this.saveState();
        }
        else
        {
            //bmpEnd=holder.lockCanvas()
        }
        try
        {
            this.GT.stopRun();
            this.GT.join();
            Log.d("----------------Stop Trhead-----","Surface destroyed");

        }
        catch(InterruptedException ie)
        {
            Log.d("----------------Exception Trhead-----","Surface destroyed");
        }
        //Log.d("---------------- 2 -----","Surface destroyed");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder,int format,int width,int height)
    {
        // Log.d("---------------- 1 -----","Surface changed");

    }


    private void newGame()
    {
        AlertDialog.Builder builder= new AlertDialog.Builder(context,android.R.style.Theme_Holo_Light_Dialog);
        builder.setMessage(R.string.continueGame);
        builder.setCancelable(true);
        builder.setNeutralButton(R.string.newGame, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearGame();
                System.out.println("========START NEW GAME========");
                Game.level = 1;
                Game.score = 0;
                Game.this.GT = new GameThread();//
                Game.this.GT.start();

                dialog.dismiss();
                return;
            }
        });
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            Game.this.LIFE=5;
                Game.this.u2.shield=100;
                if(Game.level==4||Game.level==5)
                Game.level=3;
                Game.this.GT=new GameThread();
               // Enemy.setLevel(Game.this.level);
                GT.start();

            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //    game.play();//.notify();
                //return;
                ((MainActivity)context).finish();
                System.exit(0);
            }
        });


        builder.show();
    }

    private void loadState()
    {
        synchronized (this)
        {
            // Log.d("--------------LoadCreated-------LEFT =",""+"  top=");

            u2  = new U2(rectFU2);

            for(RectF r:listSaveFire)
            {
                Fire fire = new Fire(r,bmpFire);
                GT.arrFire.add(fire);
            }

            //for(RectF r:listSaveEnemy)
            for(int i=0;i<listSaveEnemy.size();i++)
            {

                Enemy enemy = new Enemy(listSaveEnemy.get(i),listSaveEnemyLevel.get(i),GT.curTime);
                GT.arrEnemys.add(enemy);
            }

            listSaveEnemy.clear();
            listSaveEnemyLevel.clear();
            listSaveFire.clear();
        }
    }

    private void saveState()
    {
        synchronized (this)
        {
            this.rectFU2=u2.getRf();

            this.listSaveEnemy=new ArrayList<>();
            this.listSaveEnemyLevel=new ArrayList<>();
            this.listSaveFire=new ArrayList<>();
            //
            for(Enemy enemy:GT.arrEnemys)
            {
                this.listSaveEnemy.add(enemy.getRectf());
                this.listSaveEnemyLevel.add(enemy.level);
            }

            for(Fire fire:GT.arrFire)
            {
                this.listSaveFire.add(fire.getRectF());
            }
            this.streamSoundGame=-1;
            Log.d("--------------saveState-------EnemySize ="+GT.arrEnemys.size(),"  FIre SIze"+GT.arrFire);
        }
    }

    class GameThread extends Thread
    {

        final static int DELAY=50;

        private Paint P = new Paint();
        private boolean isRun = true;


        long timeForEnemy;//время появления новых врагов
        //private ArrayList<RectF> arrEnemys=new ArrayList<>();
        private ArrayList<Enemy> arrEnemys = new ArrayList<>();
        private ArrayList<Fire> arrFire = new ArrayList<>();
        ArrayList<Fire> arrFireForDel=new ArrayList<Fire>();//список огней попавших в цель



        private int bossX=0;
        private int bossY=0;

        private Canvas canvas;
        private int w = Game.this.getWidth();
        private int h = Game.this.getHeight();


        Random rand = new Random();
        long curTime;
        long lastDrawEnemyTime;
        long lastAddEnemyTime;
        long lastDrawTime;
        long lastDrawFireTime;

        private boolean isPause=false;

        public synchronized void pauseOn()//включаем паузу
        {
            isPause=true;
        }
        public synchronized void pauseOff()//выключаем паузу
        {
            isPause=false;
            this.notify();
        }

        public void GameThread()
        {

        }



        public void stopRun()
        {
            this.isRun = false;
        }




        private void drawFire()
        {

        }

        private void drawEnd()
        {
            Game.this.streamBux=Game.this.sp.play(Game.this.soundBux,1,1,0,0,1);
            RectF r=Game.this.u2.getRf();
            canvas.drawBitmap(bmpExplosion, null, new RectF(r.left - 100, r.top - 100, r.right + 100, r.bottom + 100), P);
            // canvas.drawRect(Game.this.u2.getRf(), P);
            P.setTextSize(100);
            P.setColor(Color.WHITE);
            canvas.drawText("GAME OVER", 100, 300, P);
            // for(Enemy enemy:arrEnemys)
            // enemy.interrupt();
            isRun=false;
            Game.isEnd=true;//конец игры
           // Log.d("***********", "Game Over");
          //  bmpEnd=Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        }

        @Override
        public void run()
        {

            //Game.this.SM.
           // Game.this.streamSoundGame=Game.this.sp.play(Game.this.soundGame,1,1,5,0,1);



            SurfaceHolder holder = Game.this.getHolder();

            RectF rU2=u2.getRf();
            int fireX =(int)rU2.left+((int)((rU2.right-rU2.left)/4));
            int fireY = (int)rU2.top-Fire.HEIGHT;
            this.curTime=System.currentTimeMillis();
            this.lastDrawEnemyTime=this.curTime;
            this.lastAddEnemyTime=this.curTime;
            this.lastDrawTime=this.curTime;
            this.lastDrawFireTime=this.curTime;

            Enemy.addLevel1=curTime;
            Enemy.addLevel2=curTime;
            Enemy.addLevel3=curTime;
            Enemy.addLevel4=curTime;
            Enemy.addLevel5=curTime;
            Enemy.addLevel6=curTime;

            Enemy.setLevel(Game.level);

            try
            {
                Thread.sleep(1500);


            while (isRun)
            {
                Thread.sleep(5);
                 synchronized (this)
                {

                        while(isPause)
                        {
                           this.wait();
                        }


                    curTime=System.currentTimeMillis();

                    //получили канвас
                    canvas = holder.lockCanvas();

                    if (canvas == null) continue;

                   if(Game.this.streamSoundGame==-1)
                    Game.this.streamSoundGame=Game.this.sp.play(Game.this.soundGame,1,1,1,-1,1);
                    //отрисовуем небо
                    canvas.drawBitmap(bmpSky, null, new RectF(0, 0, w, h), P);//
                    //canvas.drawColor(Color.YELLOW);

                    //добавляем врагов
                    addEnemy();

                    //отрисовем врагов
                    drawEnemy();


                    P.setColor(Color.BLUE);


                     //добавляем выстрелы
                    rU2=u2.getRf();
                    fireX =((int)rU2.left+((int)((rU2.right-rU2.left)/2)))-20;

                   // int line=(cnt%2==0)?1:2;

                    if(curTime-lastDrawFireTime>Fire.delay)
                    {
                        arrFire.add(new Fire(new RectF(fireX, fireY, fireX + Fire.WIDTH, fireY + Fire.HEIGHT),
                                bmpFire));
                        lastDrawFireTime=curTime;

                    }

                    for (Fire f : arrFire)
                    {
                        RectF r=f.R;
                        r.left+=f.kX;
                        r.right+=f.kX;

                        r.top+=f.kY;
                        r.bottom+=f.kY;


                        //  Game.this.streamVystrel=Game.this.sp.play(Game.this.soundVystrel,1,1,0,0,1);
                        canvas.drawBitmap(f.bmpFire, null, r, P);

                        // cnt++;


                        //Проверка на попадание в цель
                        Enemy hit = isHit(r);

                        if (hit != null)
                        {

                           // P.setColor(Color.MAGENTA);
                            canvas.drawBitmap(bmpFire, null, new RectF(r.left - 50, r.top - 50, r.right + 50, r.bottom + 50), P);
                            //  canvas.drawOval(r.left - 50, r.top - 50, r.right + 50, r.bottom + 50, P);
                            Game.this.streamVystrel=Game.this.sp.play(Game.this.soundVystrel,0.5f,0.5f,0,0,1);

                            P.setColor(Color.BLUE);
                            if(hit.armor==0)
                            {
                                Game.this.streamBux=Game.this.sp.play(Game.this.soundBux,1,1,0,0,1);
                                canvas.drawBitmap(bmpExplosion, null, new RectF(r.left - 100, r.top - 100, r.right + 100, r.bottom + 100), P);
                                arrEnemys.remove(hit);
                                Game.this.sp.pause(Game.this.streamSoundGame);
                                switch(hit.level)
                                {
                                    case 1:score+=50;break;
                                    case 2:score+=200;break;
                                    case 3:
                                        score+=500;
                                        break;
                                }
                            }
                            else
                                hit.armor--;
                            arrFireForDel.add(f);
                            // arrEnemys.remove(r);
                        }
                    }
                    //удаляем из списка огни попавшие в цели
                    for(Fire f:arrFireForDel)
                    {
                        arrFire.remove(f);
                    }
                    arrFireForDel.clear();

                    //Двигаем и Рисуем самолетик
                    RectF R=Game.this.u2.getRf();
                    if(u2.moveLeft && R.left>0)
                    {
                        R.left-=5;
                        R.right-=5;
                    }
                    else
                    if(u2.moveRight&& R.right<w)
                    {
                        R.left+=5;
                        R.right+=5;
                    }
                    if(u2.status==1)
                        canvas.drawBitmap(bmpU2,null,R, P);
                    else
                        canvas.drawBitmap(bmpU2_light,null,R, P);
                    //        canvas.drawRect(Game.this.u2.getRf(), P);



                    //проверка на конец игры
                    if (gameOver())
                    {
                        Game.this.LIFE--;
                        canvas.drawBitmap(bmpExplosion,null,u2.getRf(),P);
                        if(Game.this.LIFE<0)
                        {
                            Game.isEnd=true;
                        }
                        if(Game.isEnd)
                        {
                            drawEnd();
                        }
                    }


                    //враги долетели до края
                    //enemyToEnd();

                    //Устанавливаем уровни
                    changeLevel();

                    //Вывод информации об игре
                    P.setTextSize(40);
                  //  canvas.drawText("SCORE : " + score, 20, h - 80, P);
                    canvas.drawText("Level: " +Game.this.level + " SCORE = " + score, 20, h - 80, P);


                  float kX=(float)w/100;
                    float rigth=kX*U2.shield;
                    P.setColor(Color.RED);
                 //   canvas.drawText("SHIELD : Kx="+kX+" R="+(u2.shield),w-400,h-80,P);
                   // canvas.drawRect(0,h-50,rigth,h,P);
                    canvas.drawBitmap(bmpShield, null, new RectF(0, h - 50, rigth, h), P);
                    for(int i=0;i<Game.this.LIFE;i++)
                    {
                        canvas.drawBitmap(bmpU2_light,null,new RectF(20+i*50,h-50,(20+i*50)+50,h-10),P);
                    }


                    cnt ++;
                    lastDrawTime=curTime;

                    //отпускаем канвас
                    holder.unlockCanvasAndPost(canvas);

                    //  Log.d("-------------Work",""+Thread.currentThread());
                }
            }
            }
            catch (InterruptedException ex) {
            }
        }

        private void changeLevel()
        {
           // if(score>2000&&Game.level==1)
            if(score>8000&&Game.level==1)
            {
                Game.level=2;
                Enemy.setLevel(Game.level);
                Fire.delay-=10;

            }
            else
           // if(score>5000&&Game.level==2)
            if(score>15000&&Game.level==2)
            {
                Game.level=3;
                Enemy.setLevel(Game.level);

                Fire.delay-=10;
            }

            else
            if(Game.level==3&&((arrEnemys.size()==0&&score>40000)|| score>60000))
          //  if(Game.level==3&&((arrEnemys.size()==0&&score>30000)|| score>50000))
           //     if(Game.level==3&&((arrEnemys.size()==0&&score>15000)|| score>20000))
            {
                Game.level=4;
                Enemy.setLevel(Game.level);
              //  Enemy.bossTime=true;
            }

           else
            if(Enemy.bossTime&&Game.level==4)
            {
                Enemy.bossTime=false;
                Game.level=5;
                Enemy.setLevel(Game.level);
                Fire.delay-=10;
                Enemy.bossIn=false;
            }

            else
            if(Game.level==5&&arrEnemys.size()==0)
            {
                Game.level=6;
                Enemy.setLevel(Game.level);
            }


        }

        private void moveEnemy(Enemy enemy)
        {
            RectF r = enemy.getRectf();
            switch(enemy.level)
            {
                case 1:
                    if(r.bottom>=this.h||r.top<0)
                    {
                        enemy.kY*=-1;
                    }
                    r.top+=enemy.kY;
                    r.bottom+=enemy.kY;
                    r.left+=enemy.kX;
                    r.right+=enemy.kX;
                    break;
                case 2:
                    if(r.bottom>=this.h||r.top<0)
                    {
                        enemy.kY*=-1;
                    }
                    if(r.right+enemy.kX>=w||r.left+enemy.kX<=0)
                    {
                        enemy.kX*=-1;

                    }
                    r.top+=enemy.kY;
                    r.bottom+=enemy.kY;
                    r.left+=enemy.kX;
                    r.right+=enemy.kX;
                    break;
                case 3:
                    if(r.right+enemy.kX>=w||r.left+enemy.kX<=0)
                    {
                        enemy.kX*=-1;

                    }
                    if(r.top>=w/2 || r.top<=-1)
                    {
                        enemy.kY*=-1;
                    }
                    r.top+=enemy.kY;
                    r.bottom+=enemy.kY;
                    r.left+=enemy.kX;
                    r.right+=enemy.kX;
                    break;
                case 4:
                    if(r.right+enemy.kX>=w||r.left+enemy.kX<=0)
                    {

                        enemy.kX*=-1;
                    }

                    if(r.top>=w/3 || r.top<=-1)
                    {
                        enemy.kY*=-1;
                    }

                    r.top+=enemy.kY;
                    r.bottom+=enemy.kY;
                    r.left+=enemy.kX;
                    r.right+=enemy.kX;
                    bossX=(int)(r.left+(r.right-r.left)/2);
                    bossY=(int)r.bottom;
                    break;
                case 5:

                    Log.d("--------Move Level 5","");
                    if(r.right+enemy.kX>=w||r.left+enemy.kX<=0)
                    {

                        enemy.kX*=-1;
                    }
                    if(r.bottom>=this.h||r.top<0)
                    {
                        enemy.kY*=-1;
                    }

                    r.top+=enemy.kY;
                    r.bottom+=enemy.kY;
                    r.left+=enemy.kX;
                    r.right+=enemy.kX;
                    break;
                case 6:

                    Log.d("--------Move Level 6","");
                    if(r.right+enemy.kX>=w||r.left+enemy.kX<=0)
                    {

                        enemy.kX*=-1;
                    }
                    if(r.bottom>=this.h||r.top<0)
                    {
                        enemy.kY*=-1;
                    }

                    r.top+=enemy.kY;
                    r.bottom+=enemy.kY;
                    r.left+=enemy.kX;
                    r.right+=enemy.kX;
                    break;
            }

        }
        private void drawEnemy()
        {
            for(Enemy enemy:arrEnemys)
            {
                RectF r = enemy.getRectf();
                // if(curTime-enemy.lastDraw>enemy.delay)
                {
                    moveEnemy(enemy);
                    // enemy.lastDraw=curTime;
                }

                switch(enemy.level)
                {
                    case 1:
                        canvas.drawBitmap(bmpEnemy_level_1, null, r, P);
                        break;
                    case 2:
                        canvas.drawBitmap(bmpEnemy_level_2, null, r, P);
                        break;
                    case 3:

                        //Log.d("----------drawEnemy","timeIN="+arrEnemys.get(i).timeIn+"  curTime="+curTime);
                      //Выстрел из тарелки
                        if(curTime-enemy.timeIn>Enemy.DELAY_FIRE&&curTime-enemy.timeIn<Enemy.DELAY_FIRE+2000)
                        {
                            RectF rFire=new RectF(r.right-((r.right-r.left)/2)-10,r.bottom-5,
                                    r.right-((r.right-r.left)/2)+10,h);
                            P.setColor(Color.CYAN);
                         //  canvas.drawRect(rFire, P);
                            canvas.drawBitmap(bmpFlash,null,rFire, P);


                            //if(contactRectangles(rFire,u2.getRf()))
                            if(rFire.left>=u2.getRf().left&&rFire.left<=u2.getRf().right)
                            {
                                U2.shield--;
                                Log.d("--------Laser IN U2","radiation = "+u2.shield);
                                if( U2.shield<=0&& U2.status==2)
                                {

                                    Game.this.LIFE--;
                                    U2.shield=100;
                                    u2.status=1;
                                    if(Game.this.LIFE<0)
                                    {
                                        Game.isEnd=true;
                                    }
                                    if(Game.isEnd)
                                    {
                                       drawEnd();
                                    }
                                }
                                else
                                if(u2.status==1&&U2.shield<50)
                                    u2.status=2;
                            }
                        }
                        canvas.drawBitmap(bmpEnemy_level_3, null, r, P);
                        break;
                    case 4:
                        canvas.drawBitmap(bmpEnemy_level_4, null, r, P);
                        break;
                    case 5:
                     //   Log.d("##########", "DrawChildren x=" + r.left + " y=" + r.top+" x2="+r.right+" y2="+r.bottom);
                        //canvas.drawRect(r, P);
                        canvas.drawBitmap(bmpEnemy_level_5, null, r, P);
                        break;
                    case 6:
                        //   Log.d("##########", "DrawChildren x=" + r.left + " y=" + r.top+" x2="+r.right+" y2="+r.bottom);
                        //canvas.drawRect(r, P);
                        canvas.drawBitmap(bmpEnemy_level_6, null, r, P);
                        break;
                }
            }
        }

        private void addEnemy()
        {
            if(curTime- Enemy.addLevel1>=Enemy.DELAY_LEVEL_1&&(Game.level==1||Game.level==2||Game.level==6))
            {
                Enemy.addLevel1 = curTime;
                int left=rand.nextInt(w-Enemy.WIDTH_Level_1);
                int right=left+Enemy.WIDTH_Level_1;
                Enemy enemy=new Enemy(new RectF(left, 0, right, Enemy.HEIGHT_Level_1),1,curTime);
                arrEnemys.add(enemy);
                return ;
            }
            else
            if(curTime- Enemy.addLevel2>=Enemy.DELAY_LEVEL_2&&
                     (Game.level==1||Game.level==2||Game.level==3||Game.level==6))
                {
                    Enemy.addLevel2 = curTime;
                    int left=rand.nextInt(w-Enemy.WIDTH_Level_2);
                    int right=left+Enemy.WIDTH_Level_2;
                    Enemy enemy=new Enemy(new RectF(left, 0, right, Enemy.HEIGHT_Level_2),2,curTime);
                    arrEnemys.add(enemy);
                    return ;
                }
            else
            if(curTime- Enemy.addLevel3>=Enemy.DELAY_LEVEL_3&&(Game.level==3||Game.level==2||Game.level==1||Game.level==6))
            {

                Enemy.addLevel3 = curTime;
                int    left = rand.nextInt(w - Enemy.WIDTH_Level_3);
                int   right = left + Enemy.WIDTH_Level_3;
                Enemy   enemy = new Enemy(new RectF(left, 0, right, Enemy.HEIGHT_Level_3), 3, curTime);

                arrEnemys.add(enemy);
                return ;
            }
            else
            //if(curTime- Enemy.addLevel4>=Enemy.DELAY_LEVEL_4&&Game.level==4&&Enemy.bossTime)
                if(Game.level==4&&Enemy.bossTime)
            {
            Enemy.addLevel4 = curTime;
            int left=rand.nextInt(w-Enemy.WIDTH_Level_4);
            int right=left+Enemy.WIDTH_Level_4;
            RectF r=new RectF(left, 0, right, Enemy.HEIGHT_Level_4);
            Enemy enemy=new Enemy(r,4,curTime);
            arrEnemys.add(enemy);
            Enemy.bossIn=true;
            this.bossX=(int)(left);
            this.bossY=(int)r.bottom;
            return ;
            }
            else
            if(curTime- Enemy.addLevel5>=Enemy.DELAY_LEVEL_5&&
                    Game.level==5
                    &&(Enemy.bossChildren--)>=0)
            {
                Enemy.addLevel5 = curTime;
                int left= this.bossX-Enemy.WIDTH_Level_5/2;

                int top=this.bossY;
                RectF r=new RectF(left,
                        top, left+Enemy.WIDTH_Level_5,top+ Enemy.HEIGHT_Level_5);
                Enemy enemy=new Enemy(r,5,curTime);
                arrEnemys.add(enemy);
                return ;
            }

            if(curTime- Enemy.addLevel6>=Enemy.DELAY_LEVEL_6&&
                    Game.level==6)
            {
                Log.d("=========ADD LEVEL 6","");
                Enemy.addLevel6 = curTime;
                int left=rand.nextInt(w-Enemy.WIDTH_Level_6);
                int right=left+Enemy.WIDTH_Level_6;
                RectF r=new RectF(left,
                       0, left+Enemy.WIDTH_Level_6,Enemy.HEIGHT_Level_6);
                Enemy enemy=new Enemy(r,6,curTime);
                arrEnemys.add(enemy);
                return ;
            }
          }

        private Enemy isHit(RectF fire)
        {
            for (Enemy hit : arrEnemys)
            {
                RectF r = hit.getRectf();

                //условие для попадания в цель
                if (((fire.top <= r.bottom && fire.top >= r.top)||
                        (fire.bottom <= r.bottom && fire.bottom >= r.top))&&
                        (((fire.left > r.left && fire.left < r.right)||
                                (fire.right > r.left && fire.right < r.right))))//||
                {
                    //Log.d("!!!!!!!!!!!", "return true");
                    return hit;
                }
            }
            return null;
        }


      private boolean contactRectangles(RectF R1,RectF R2)
        {
            if (((R1.bottom>=R2.top && R1.bottom<=R2.bottom)||
                    (R1.top >= R2.top && R1.top <= R2.bottom)) &&
                    ((R1.left <= R2.left && R1.left <=R2.right)||
                            (R1.right >= R2.left && R1.right <= R2.right)))
            return   true;
            else
            {
                Log.d("------------LaserIn U2 return false","u2 Left="+R2.left+"  r.left"+R1.left);
                return false;
            }
        }





        private boolean gameOver()
        {

            RectF rU2 = u2.getRf();
            Enemy enemy=null;
            boolean Res=false;
            for (Enemy hit : arrEnemys)
            {

                RectF r = hit.getRectf();

                //условие попадания врага в игрока
                if (((r.bottom>=rU2.top && r.bottom<=rU2.bottom)||
                        (r.top >= rU2.top && r.top <= rU2.bottom)) &&
                        ((rU2.left < r.left && rU2.right > r.left)||
                                (rU2.left < r.right && rU2.right > r.right)))//||
                // (fire.right<hit.left&&fire.right<hit.right)))
                {
                    Log.d("!!!!!!!!!!!", "return true");

                    enemy=hit;
                    System.out.println("---------------------DELETE HIT Enemy #=" + arrEnemys.indexOf(hit));
                    arrEnemys.remove(hit);

                    Res= true;
                    break;
                }
            }
            return Res;
        }

/*

        private void enemyToEnd()//(Enemy enemy)
        {
            ArrayList<Enemy> listEnemiesToEnd=new ArrayList<Enemy>();

            for(Enemy enemy:arrEnemys)
            {
                if(enemy.getRectf().bottom>=this.h||enemy.getRectf().top<=0)
                {
                    enemy.kY*=-1;
                   // listEnemiesToEnd.add(enemy);
                    //enemy.interrupt();
                }
            }
        }

      */
    }//end class GameThread


    public void pause()
    {
        synchronized (GT){
        try
        {
            GT.wait();
            GT.join();
        }
        catch(InterruptedException ie)
        {

        }
        }
    }

    public void play()
    {
        GT.notify();
    }

    public void clearGame()
    {
        cnt=0;
        this.level=1;
        this.u2.shield=100;
        this.u2.status=1;
        Enemy.setLevel(1);
        this.LIFE=5;
        try
        {
            listSaveEnemy.clear();
            listSaveFire.clear();
            listSaveEnemyLevel.clear();
        }
        catch(Exception ex)
        {

        }
    }



}

