package com.example.puzztest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.WindowManager;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/*
 * puzz 구성품들을 다 읽어들임
 * 읽어들일때 그 그림들의 비율을 계산 저장
 * 그 구성품들을 차례대로 그림
 * 그릴때 화면크기에 따라서 그림크기(저장한 비율을 사용, 가로세로크기를 일정비로 설정), 그림위치를 설정
 * 
 * =============다시 수정===========================
 * 
 * puzz 구성품을 다 읽어들임
 * 화면크기에 따라서 그림크기(matrix로 조절), 그림위치 설정
 * 화면크기의 세로의 절반크기가 되도록 설정
 * 
 * ==============방법2=============================
 * matrix 안씀. 
 * 비율을 구해서 bitmap scale로 생성
 */

public class PuzzView extends SurfaceView implements Callback {
	
	static final int PUZZ_INI_LENGTH=3;
	static final int PUZZ_BODY=0;
	static final int PUZZ_HAND=1;
	static final int PUZZ_HEAD01=2;
	static final int PUZZ_HEAD02=3;
	static final int LIMIT=10;
	
	Context mcontext;
	SurfaceHolder mHolder;
	int width,height;
	boolean loop=true;
	
	// 디버그용 변수
	int a,b,c,d;
	//---------------

	PuzzParts puzzParts;
	Bitmap[] puzz;
	Point[] point;
	
	int phase,count=1,bound=1;
	private boolean ing=false;
	

	public PuzzView(Context context) {
		super(context);
		mHolder = getHolder();
		mHolder.addCallback(this);
		mcontext=context;
		
		DisplayMetrics outMetrics=new DisplayMetrics();
//		((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(outMetrics);
//		width=outMetrics.widthPixels;
//		height=outMetrics.heightPixels;
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		puzzParts = new PuzzParts(mcontext);
		puzz =puzzParts.getPuzz();
		point=puzzParts.getPoint();
		
		Rect rect=mHolder.getSurfaceFrame();
		width=rect.width();
		height=rect.height();
		
		draw();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}

	// 비트맵이 변형된 상태일때 그려주는 방식으로 비트맵 변형은 다른곳에서 연산하고 여기선 그냥 있는거 그대로 뿌려줌
	private void draw() {
		Canvas canvas = mHolder.lockCanvas();
		canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);

		for (int i = 0; i < PUZZ_INI_LENGTH; i++) {
			canvas.drawBitmap(puzz[i], point[i].x, point[i].y, null);
		}
		
		

		Paint paint=new Paint();
		
		//디버그용 점----------------------
		paint.setColor(Color.WHITE);
		canvas.drawCircle(a,b, 5, paint);
		canvas.drawText(point[PUZZ_HEAD01].x+","+point[PUZZ_HEAD01].y, 50, 50, paint);
		//-------------------------------
		
		mHolder.unlockCanvasAndPost(canvas);
	}
	
	private void draw2(){
		
		while(loop){
			Canvas canvas = mHolder.lockCanvas();
						
			switch(phase){
			case 2:
				ani02(canvas);
				break;
			case 3:
				ani03(canvas);
				break;
			case 4:
				ani04(canvas);
				break;
			case 5:
				ani05(canvas);
				break;
			default:
				break;
			}
			
			mHolder.unlockCanvasAndPost(canvas);
		}	
	}
	

	private synchronized void movePuzz(){
		//ani01();
		ing=true;
		loop=true;
		phase=4;
		draw2();
		ing=false;
//		ani02();
//		ani03();
//		ani04();
		
	}
	

	
	/*
	 * 얼굴 01->02로 교체해서 rotate
	 * 세팅가능값 
	 * 		ROTATE_X,ROTATE_Y (회전원점 설정)
	 * 		DEGREE			(1회 회전 각도 설정)
	 * 		FREQUENCY       (회전 빈도 설정)
	 */
	
	private void ani01(){
		// 나중에 다시 되돌릴 얼굴 백업
		Bitmap head=puzz[PUZZ_HEAD02];
		// 움직이기전의 원래 비트맵 크기와 초기 위치
		// 회전원점을 비트맵의 가로 4/5지점, 세로 1/5지점
		int puzzWidth=head.getWidth();           
		int puzzHeight=head.getHeight();
		
		// ======회전을 시켜줄 기준점을 설정======= 설정가능값
	    final int ROTATE_X=puzzWidth*3/5;
	    final int ROTATE_Y=puzzHeight*2/5;
	    int rotateYY=puzzHeight-ROTATE_Y; // 안드로이드는 y좌표가 거꾸로이기에 이게 실제 원점
	    
	    int puzzX=point[PUZZ_HEAD02].x;
		int puzzY=point[PUZZ_HEAD02].y;
		
		// 안드로이드 상에 표시될 원점좌표
		int x=puzzX+ROTATE_X;
		int y=puzzY+rotateYY;
		
		//원점,puzzX,y 확인용 - 사용후 지움-----
	    a=x;
	    b=y;
	    c=ROTATE_X;
	    d=ROTATE_Y;
	    //---------------------------
		
		// 머리 1번을 원소스의 머리 2번자리에 저장,보존
		puzzParts.setPuzz(puzz[PUZZ_HEAD01], PUZZ_HEAD02);
		
		// ===========회전 각도,빈도 설정============ 설정가능값
		Matrix m=new Matrix();
		final float DEGREE=3.0f;
		final int FREQUENCY=11;
		float radian=(float) (DEGREE*Math.PI/180);
		float endRadian=0;
		//반지름
		double radiusX=Math.hypot(ROTATE_X,puzzHeight-rotateYY);
		double radiusY=Math.hypot(ROTATE_X,rotateYY);
		
		Log.d("radius",radiusX+","+radiusY);
		
		//각도
		double xdeg=Math.acos(ROTATE_X/radiusX);
		double ydeg=Math.acos(ROTATE_X/radiusY);
		
		Log.d("deg",xdeg+","+ydeg);
		
		for(int i=1;i<FREQUENCY;i++){
			m.postRotate(DEGREE);
			Bitmap rotateHead=Bitmap.createBitmap(head, 0, 0, head.getWidth(), head.getHeight(), m, false);
			puzzParts.setPuzz(rotateHead, PUZZ_HEAD01);	// 움직인 머리를 머리 1번자리에 저장
			//제대로 돌아가게 보이기위해서 point을 계산
			// 원소스의 넓이 높이를 이용해서 계산
			
			// 원의 좌표 구하는 공식 (반지름*cos, 반지름*sin)으로 좌표를 구한후 (x,y)원점 만큼 이동
			point[PUZZ_HEAD01].x=(int)Math.round(x-radiusX*Math.cos(i*radian-xdeg));
			point[PUZZ_HEAD01].y=(int)Math.round(y-radiusY*Math.sin(i*radian+ydeg));
			
			draw();
			endRadian=i*radian;
		}
		for(int i=1;i<FREQUENCY;i++){
			m.postRotate(-DEGREE);
			Bitmap rotateHead=Bitmap.createBitmap(head, 0, 0, head.getWidth(), head.getHeight(), m, false);
			puzzParts.setPuzz(rotateHead, PUZZ_HEAD01);
			
			point[PUZZ_HEAD01].x=(int)Math.round(x-radiusX*Math.cos(endRadian-i*radian-xdeg));
			point[PUZZ_HEAD01].y=(int)Math.round(y-radiusY*Math.sin(endRadian-i*radian+ydeg));
			draw();
		}
		
		// 바꿔치기한 머리를 원래대로 복구
		puzzParts.setPuzz(puzz[PUZZ_HEAD02], PUZZ_HEAD01);
		puzzParts.setPuzz(head, PUZZ_HEAD02);
		draw();
		
	}
	
	//얼굴,몸통,팔 -> 세로로 scale 얼굴 크게(턱에서부터) 팔,몸통 위로 스케일
	private void ani02(Canvas canvas){
		canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
		
		float scaleY=0.005f;
		float scaleY2=0.01f;
		float scaleY3=0.05f;
		
		// scaleDraw(캔바스, 가로변화, 세로변화, 넓이중에서의 기준점 위치, 높이중에서의 기준점 위치 ex:3/2->3/2지점,그릴 파츠) 
		scaleDraw(canvas,1,1+scaleY*count, 1/2, 3/2, PUZZ_BODY);
		scaleDraw(canvas,1,1+scaleY2*count, 1/2, 3/2, PUZZ_HAND);
		scaleDraw(canvas,1,1+scaleY3*count, 1/2, 1, PUZZ_HEAD01);
				
		counter();
	}
	
	//몸통,팔 -> 가로로 scale
	private void ani03(Canvas canvas){
		canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
		
		float scaleX=0.02f;
		float scaleX2=0.01f;
		float scaleX3=0.1f;
		
		scaleDraw(canvas,1+scaleX*count,1,0.5f,1,PUZZ_BODY);	
		scaleDraw(canvas,1+scaleX2*count,1,0.5f,1,PUZZ_HAND);	
		
		canvas.drawBitmap(puzz[PUZZ_HEAD01], point[PUZZ_HEAD01].x, point[PUZZ_HEAD01].y+count, null);	
			
		counter();
	}
	
	//얼굴, 몸통, 팔 -> 화면 중앙기준으로 확대
	private void ani04(Canvas canvas){
		canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
		
		float scale=0.1f;
		
		scaleDraw2(canvas,1+scale*count,1+scale*count, point[PUZZ_BODY].x, point[PUZZ_BODY].y+puzz[PUZZ_BODY].getHeight()/2, PUZZ_BODY);
		scaleDraw2(canvas,1+scale*count,1+scale*count, point[PUZZ_BODY].x, point[PUZZ_BODY].y+puzz[PUZZ_BODY].getHeight()/2, PUZZ_HAND);
		scaleDraw2(canvas,1+scale*count,1+scale*count, point[PUZZ_BODY].x, point[PUZZ_BODY].y+puzz[PUZZ_BODY].getHeight()/2, PUZZ_HEAD01);
		
		count++;
		
		if(count>11)
		{
			count=1;
			phase++;
		}
	}	
	
	private void ani05(Canvas canvas){
		eggOn();
		canvas.drawBitmap(puzz[5], point[5].x, point[5].y, null);
		loop=false;
	}
	
	private void eggOn(){
		
	}

	// scaleDraw(캔바스, 가로변화, 세로변화, 넓이중에서의 기준점 위치, 높이중에서의 기준점 위치 ex:3/2->3/2지점,그릴 파츠) 
	private void scaleDraw(Canvas canvas,float sx,float sy,float bx,float by,int index){
		canvas.save();
		canvas.scale(sx, sy, point[index].x+puzz[index].getWidth()*bx, point[index].y+puzz[index].getHeight()*by);
		canvas.drawBitmap(puzz[index], point[index].x, point[index].y, null);
		canvas.restore();
	}
	
	// scaleDraw(캔바스, 가로변화, 세로변화, 가로 기준점, 세로 기준점,그릴 파츠) 
	private void scaleDraw2(Canvas canvas,float sx,float sy,float bx,float by,int index){
		canvas.save();
		canvas.scale(sx, sy, bx, by);
		canvas.drawBitmap(puzz[index], point[index].x, point[index].y, null);
		canvas.restore();
	}
	
	private void counter(){
				
		if(count>=LIMIT){
			bound=-1;
		}
		
		count+=bound;
		
		if(count<0){
			phase++;
			bound=count=1;
		}
		
	}
	
	
	class PuzzThread extends Thread{
		public void run(){
			movePuzz();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_UP:
			if(!ing){
				new PuzzThread().start();
			}
			break;
		}
		return true;
	}
}
