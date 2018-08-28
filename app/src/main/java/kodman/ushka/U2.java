package kodman.ushka;

import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by android on 05.10.2017.
 */
public class U2 extends  Thread
{
    private  int x;
    private  int y;
    private boolean isRun=true;

    public boolean moveLeft=false;
    public boolean moveRight=false;

    private RectF R;

    public final static int WIDTH=130;
    public final static int HEIGHT=100;

    public static int status=1;
    public static int shield=100;

    public U2(int x,int y)
    {
        this.x=x;
        this.y=y;
    }

    public U2(RectF r)
    {
        this.R=r;
    }



    public synchronized RectF getRf(){return  R;}
    public synchronized Rect getR()
    {
        Rect r= new Rect((int)R.left,(int)R.top,(int)R.right,(int)R.bottom);
        return r;
    }
    public synchronized void  setRf(RectF r){this.R=r;}

    public synchronized int getX(){return this.x;}
    public  synchronized int getY(){return this.y;}

    public synchronized void setX(int x){this.x=x;}
    public  synchronized void  setY(int y){this.y=y;}

    @Override
    public void run()
    {
        while(isRun)
        {

        }
    }
}
