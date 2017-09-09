
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

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

public class HbbPagerEffectActivity extends Activity {
	

	private GLSurfaceView glSurfaceView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		glSurfaceView = new HbbGLSurfaceView(this);

		setContentView(glSurfaceView);
	}

	@Override
	protected void onResume() {

		super.onResume();
		glSurfaceView.onResume();
	}

	@Override
	protected void onPause() {

		super.onPause();
		glSurfaceView.onPause();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		return super.dispatchTouchEvent(ev);

	}

}
