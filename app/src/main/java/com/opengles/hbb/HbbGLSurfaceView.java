
/*
Opengl2Pager
Copyright (c) 2017 Yellow_Beard <5358951@qq.com>
http://hbbtool.com/
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
    https://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.opengles.hbb;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class HbbGLSurfaceView extends GLSurfaceView {

	private final HbbRenderer hbbRenderer;

	public HbbGLSurfaceView(Context context) {
		super(context);
 
		setEGLContextClientVersion(2);	 
		hbbRenderer = new HbbRenderer(context);
		setRenderer(hbbRenderer);
 
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}
	
	public  HbbGLSurfaceView(Context context,int type) {
		super(context);
		setEGLContextClientVersion(2);

		hbbRenderer = new HbbRenderer(context);
		setRenderer(hbbRenderer);
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		
	}
	

	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
	private float previousX;
	private float previousY;

	@Override
	public boolean onTouchEvent(MotionEvent e) {

		float x = e.getX();
		float y = e.getY();

		switch (e.getAction()) {
		case MotionEvent.ACTION_MOVE:

			float dx = x - previousX;
			float dy = y - previousY;

			if (y > getHeight() / 2) {
				dx = dx * -1;
			}

			if (x < getWidth() / 2) {
				dy = dy * -1;
			}

			if(hbbRenderer.getF()>280){
				hbbRenderer.setF(0);
			}
			
			hbbRenderer.setF(hbbRenderer.getF() + ((dx + dy) * TOUCH_SCALE_FACTOR)); 
			requestRender();
		}

		previousX = x;
		previousY = y;
		return true;
	}

}
