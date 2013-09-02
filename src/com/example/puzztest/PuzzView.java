package com.example.puzztest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
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

	SurfaceHolder mHolder;
	PuzzParts puzzParts;
	Context mcontext;

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
		draw();

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

	private void draw() {
		Canvas canvas = mHolder.lockCanvas();
		Bitmap[] puzz = puzzParts.getPuzz();
		Point[] point=puzzParts.getPoint();

		for (int i = 0; i < puzz.length; i++) {
			canvas.drawBitmap(puzz[i], point[i].x, point[i].y, null);
		}

		mHolder.unlockCanvasAndPost(canvas);
	}

}
