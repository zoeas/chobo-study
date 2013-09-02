package com.example.puzztest;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

/*
 * 각 파츠를 로딩, 리사이징, 각 파츠마다의 초기위치를 설정해주고 반환하는 클래스
 */
public class PuzzParts {
	Bitmap[] puzz;
	Point[] point;
	int width,height;
	
	final static int PUZZ_PARTS_NUMS=4;
	
	public PuzzParts(Context context){
		DisplayMetrics outMetrics=new DisplayMetrics();
		Display display=((WindowManager)context.getSystemService(context.WINDOW_SERVICE)).getDefaultDisplay();
		display.getMetrics(outMetrics);
		
		width=outMetrics.widthPixels;
		height=outMetrics.heightPixels;
		float density=outMetrics.density;
		
		Resources res=context.getResources();
		
//		BitmapFactory.Options opts=new BitmapFactory.Options();
//		opts.inJustDecodeBounds=true;
		
		puzz=new Bitmap[PUZZ_PARTS_NUMS];
		puzz[0]=BitmapFactory.decodeResource(res, R.drawable.puzz_body);
		puzz[1]=BitmapFactory.decodeResource(res, R.drawable.puzz_hand);
		puzz[2]=BitmapFactory.decodeResource(res, R.drawable.puzz_head01);
		puzz[3]=BitmapFactory.decodeResource(res, R.drawable.puzz_head02);
		
		resizeBitmap();
		setCoordinates(density);
	}

	
	private void resizeBitmap() {
		float ratio=(height/2)/puzz[0].getHeight();
		Log.d("ratio",""+ratio+","+height+","+puzz[0].getHeight());
		ratio=0.5f;
		Matrix m=new Matrix();
		m.setScale(ratio, ratio);
		
		
		for(int i=0;i<puzz.length;i++){
			Log.d("puzz[i].getwidth,",puzz[i].getWidth()+","+puzz[i].getHeight()+","+ratio);
			puzz[i]=Bitmap.createBitmap(puzz[i], 0, 0, puzz[i].getWidth(), puzz[i].getHeight(), m, false);
		}
	}
	
	private void setCoordinates(float density) {
		point=new Point[PUZZ_PARTS_NUMS];
		
		for(int i=0;i<point.length;i++){
			point[i]=new Point();
		}
	
		//body
		point[0].x=(width-puzz[0].getWidth())/2;
		point[0].y=(height-puzz[0].getHeight())/2;
		//hand
		point[1].x=(int) (point[0].x+(10*density));
		point[1].y=(int) (point[0].y+(10*density));
		//head01
		point[2].x=(int) (point[0].x-(30*density)); 
		point[2].y=(int) (point[0].y-(30*density)); 
		//head02
		point[3].x=(int) (point[0].x+(150*density)); 
		point[3].y=(int) (point[0].y+(150*density)); 
		
	}
	
	public Bitmap[] getPuzz(){
		return puzz;
	}
	
	public Point[] getPoint(){
		return point;
	}
	
	

}
