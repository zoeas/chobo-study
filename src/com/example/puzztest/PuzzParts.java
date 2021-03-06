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
	
	final static int PUZZ_PARTS_NUMS=7;
	
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
		puzz[4]=BitmapFactory.decodeResource(res, R.drawable.out01);
		puzz[5]=BitmapFactory.decodeResource(res, R.drawable.out02);
		puzz[6]=BitmapFactory.decodeResource(res, R.drawable.out03);
		
		resizeBitmap();
		setCoordinates(density);
	}

	
	private void resizeBitmap() {
		Matrix m=new Matrix();
		m.setScale(0.6f, 0.6f);
		
		
		for(int i=0;i<puzz.length-3;i++){
			puzz[i]=Bitmap.createBitmap(puzz[i], 0, 0, puzz[i].getWidth(), puzz[i].getHeight(), m, false);
		}
	}
	
	private void setCoordinates(float density) {
		point=new Point[PUZZ_PARTS_NUMS];
		
		for(int i=0;i<point.length;i++){
			point[i]=new Point();
		}
	
		//body
		point[0].x=(width-puzz[0].getWidth())/2+(int)(15*density);
		point[0].y=(height-puzz[0].getHeight())/2;
		//hand
		point[1].x=(int) (point[0].x+(20*density));
		point[1].y=(int) (point[0].y+(50*density));
		//head01
		point[2].x=(int) (point[0].x+(15*density)); 
		point[2].y=(int) (point[0].y-(47*density)); 
		//head02
		point[3].x=(int) (point[0].x+(15*density)); 
		point[3].y=(int) (point[0].y-(47*density)); 
		//out01
		point[4].x=(int) (point[0].x+(35*density));
		point[4].y=(int) (point[0].y+(85*density));
		//out02
		point[5].x=(int) (point[0].x+(35*density));
		point[5].y=(int) (point[0].y+(85*density));
		//out03
		point[6].x=(int) (point[0].x+(35*density));
		point[6].y=(int) (point[0].y+(85*density));
		
	}
	
	public Bitmap[] getPuzz(){
		return puzz;
	}
	
	public Point[] getPoint(){
		return point;
	}
	
	public void setPuzz(Bitmap[] puzz){
		this.puzz=puzz;
	}
	public void setPuzz(Bitmap puzz,int index){
		this.puzz[index]=puzz;
	}
	
	public void setPoint(Point[] point){
		this.point=point;
	}
	
	

}
