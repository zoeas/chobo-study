package com.example.puzztest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
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
	static final int PUZZ_HEAD01=2;
	static final int PUZZ_HEAD02=3;

	Bitmap[] puzz;
	Point[] point;
	SurfaceHolder mHolder;
	PuzzParts puzzParts;
	Context mcontext;
	boolean openMouse=false;

	public PuzzView(Context context) {
		super(context);
		mHolder = getHolder();
		mHolder.addCallback(this);
		mcontext=context;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		puzzParts = new PuzzParts(mcontext);
		puzz = puzzParts.getPuzz();
		point=puzzParts.getPoint();
		
		draw();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

	private void draw() {
		Canvas canvas = mHolder.lockCanvas();
		canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);

		for (int i = 0; i < PUZZ_INI_LENGTH; i++) {
			canvas.drawBitmap(puzz[i], point[i].x, point[i].y, null);
		}

		Paint paint=new Paint();
		paint.setColor(Color.WHITE);
		canvas.drawCircle(194, 192, 4, paint);
		mHolder.unlockCanvasAndPost(canvas);
	}
	

	private void movePuzz(){
		ani01();
		ani02();
		ani03();
		ani04();
	}
	
	//얼굴 01->02로 교체해서 rotate
	private void ani01(){
		Bitmap head=puzz[PUZZ_HEAD02];
		Point headPosition=point[PUZZ_HEAD02];
		
		// 머리 1번을 원소스의 머리 2번자리에 저장,보존
		puzzParts.setPuzz(puzz[PUZZ_HEAD01], PUZZ_HEAD02);
		
		Matrix m=new Matrix();
		
		
		for(int i=0;i<10;i++){
			m.postRotate(1.0f);
			Bitmap rotateHead=Bitmap.createBitmap(head, 0, 0, head.getWidth(), head.getHeight(), m, false);
			puzzParts.setPuzz(rotateHead, PUZZ_HEAD01);	// 움직인 머리를 머리 1번자리에 저장
			//제대로 돌아가게 보이기위해서 headpostion을 계산
			// 원소스의 넓이 높이를 이용해서 계산ㅇ
			
			draw();
		}
		for(int i=0;i<10;i++){
			m.postRotate(-1.0f);
			Bitmap rotateHead=Bitmap.createBitmap(head, 0, 0, head.getWidth(), head.getHeight(), m, false);
			puzzParts.setPuzz(rotateHead, PUZZ_HEAD01);
			draw();
		}
		
		// 머리자리들을 원래대로 복구
		puzzParts.setPuzz(puzz[PUZZ_HEAD02], PUZZ_HEAD01);
		puzzParts.setPuzz(head, PUZZ_HEAD02);
		draw();
		
	}
	
	//얼굴,몸통,팔 -> 세로로 scale
	private void ani02(){
		
	}
	
	//몸통,팔 -> 가로로 scale
	private void ani03(){
		
	}
	
	//얼굴, 몸통, 팔 -> 화면 중앙기준으로 확대
	private void ani04(){
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_UP:
			movePuzz();
			break;
		}
		return true;
	}
}
