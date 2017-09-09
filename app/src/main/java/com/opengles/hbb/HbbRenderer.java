
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

import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import com.opengles.simplevertexshader.R;

public class HbbRenderer implements GLSurfaceView.Renderer {

	private static final String TAG = "HbbRenderer";
	private Context context;

	private HbbShapes plane = new HbbShapes();
	private Transform perspective = new Transform();
	private Transform modelview = new Transform();
 	 
	private float f;
	private Transform MVMatrix = new Transform();
	private Transform PMatrix = new Transform();

	
	private int programObject;
	private int positionLoc;

	private int MVMatrixLoc;
	private int PMatrixLoc;
	private int fLoc;

	private int texCoordLoc;
	private int baseMapLoc;
	private int baseMapTexId;
 
	private int width;
	private int height;
 
 
	public HbbRenderer(Context context) {
		this.context = context;
	}

	
	
	public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {

		String vShaderStr = "uniform mat4 u_mvMatrix;                   							\n"
				+ "uniform mat4 u_pMatrix;                  			 							\n"
				+ "const mediump float r = 80.0;                			 						\n"
				+ "uniform float f;                  			 									\n"
				+ "attribute vec4 a_position;                  										\n"
				+ "attribute vec2 a_texCoord;   													\n"
				+ "varying vec2 v_texCoord;    														\n"
				+ "void main()                                 										\n"
				+ "{                                           										\n"
				+ "                                    		   										\n"
				+ "                            		   												\n"
				+ "		mediump vec4 v =    u_mvMatrix * a_position ;  								\n"
				+ "     mediump vec2 pd = vec2(0.8660,-0.5000);              		   				\n"
				+ "     mediump vec2 vpmax = vec2(230.0,-400.0);              		   				\n"
				+ "     mediump float vpmaxf = dot(pd, vpmax);              		   				\n"
				+ "     mediump float vp = dot(pd, v.xy);                      		   				\n"
				+ "     if (vp < f  + vpmaxf) {   													\n"
				+ "     } else if (vp < f+ vpmaxf +r*3.1416) {                      		   		\n"
				+ "         mediump float a = (vp-f-vpmaxf)/(r)-1.5708;                    			\n"
				+ "         v.xyz += vec3((f+vpmaxf+r*cos(a)-vp)*pd, r+r*sin(a));      				\n"
				+ "     } else {                    		   										\n"
				+ "        v.xyz += vec3((2.0*(f+ vpmaxf -vp)+3.1416*(r))*pd, 2.0*r);               \n"
				+ "     }                    		   												\n"
				+ "                                    		   										\n"
				+ "   gl_Position = u_pMatrix  * v  ; 												\n"
				+ "   v_texCoord = a_texCoord;  													\n"
				+ "    				 																\n"
				+ "}                                           										\n";

		//֮ǰ���˹������������ ���� û��ɾ�� ��Ӱ������Ч��
		String fShaderStr = "precision mediump float;                            \n"
				+ "varying vec2 v_texCoord;                            \n"
				+ "uniform sampler2D s_baseMap;                        \n"
				+ "void main()                                         \n"
				+ "{                                                   \n"
				+ "  vec4 baseColor;                                   \n"
				+ "  vec4 lightColor;                                  \n"
				+ "                                                    \n"
				+ "  gl_FragColor = texture2D( s_baseMap, v_texCoord );   \n"
				+ "}                                                   \n";

		programObject = ShaderUtils.loadProgram(vShaderStr, fShaderStr);

		positionLoc = GLES20.glGetAttribLocation(programObject, "a_position");
		texCoordLoc = GLES20.glGetAttribLocation(programObject, "a_texCoord");

		baseMapLoc = GLES20.glGetUniformLocation(programObject, "s_baseMap");

		baseMapTexId = loadTexture(context.getResources().openRawResource(R.raw.basemap1));
				
		MVMatrixLoc = GLES20.glGetUniformLocation(programObject, "u_mvMatrix");
		PMatrixLoc = GLES20.glGetUniformLocation(programObject, "u_pMatrix");
		fLoc = GLES20.glGetUniformLocation(programObject, "f");
	 
		this.plane.genPlane(540, 860, 20, 20, -100.0f);

		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

	}

	private int loadTexture(InputStream is) {
		int[] textureId = new int[1];
		Bitmap bitmap;
		bitmap = BitmapFactory.decodeStream(is);
		byte[] buffer = new byte[bitmap.getWidth() * bitmap.getHeight() * 3];

		for (int y = 0; y < bitmap.getHeight(); y++)
			for (int x = 0; x < bitmap.getWidth(); x++) {
				int pixel = bitmap.getPixel(x, y);
				buffer[(y * bitmap.getWidth() + x) * 3 + 0] = (byte) ((pixel >> 16) & 0xFF);
				buffer[(y * bitmap.getWidth() + x) * 3 + 1] = (byte) ((pixel >> 8) & 0xFF);
				buffer[(y * bitmap.getWidth() + x) * 3 + 2] = (byte) ((pixel >> 0) & 0xFF);
			}

		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(bitmap.getWidth()
				* bitmap.getHeight() * 3);
		byteBuffer.put(buffer).position(0);

		GLES20.glGenTextures(1, textureId, 0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0]);
  
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB,
				bitmap.getWidth(), bitmap.getHeight(), 0, GLES20.GL_RGB,
				GLES20.GL_UNSIGNED_BYTE, byteBuffer);

		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
				GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
				GLES20.GL_CLAMP_TO_EDGE);

		return textureId[0];
	}

	public static int loadShader(int type, String shaderCode) {
 
		int shader = GLES20.glCreateShader(type);
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);

		return shader;
	}

	private void createMVP() {

		float aspect = (float) width / (float) height;
		perspective.matrixLoadIdentity();
		perspective.perspective(45.0f, aspect, 1.0f, 2080.0f);

		modelview.matrixLoadIdentity();
		modelview.translate(0.0f, 0.0f, -1520.0f);
		modelview.rotate(0.0f, 0.0f, 1.0f, 0.0f);

		MVMatrix = modelview;
		PMatrix = perspective;

	}

 
	public void onDrawFrame(GL10 glUnused) {
 
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

		GLES20.glUseProgram(programObject);

		GLES20.glVertexAttribPointer(positionLoc, 3, GLES20.GL_FLOAT, false,
				0, this.plane.getVertices());
		GLES20.glVertexAttribPointer(texCoordLoc, 2, GLES20.GL_FLOAT, false,
				0, this.plane.getTexCoords());
		GLES20.glEnableVertexAttribArray(positionLoc);
		GLES20.glEnableVertexAttribArray(texCoordLoc);

	 
		GLES20.glUniformMatrix4fv(MVMatrixLoc, 1, false,
				MVMatrix.getAsFloatBuffer());
		GLES20.glUniformMatrix4fv(PMatrixLoc, 1, false,
				PMatrix.getAsFloatBuffer());
		GLES20.glUniform1f(fLoc, f);

		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, baseMapTexId);
 
		GLES20.glUniform1i(baseMapLoc, 0);

		GLES20.glDrawElements(GLES20.GL_TRIANGLES, this.plane.getNumIndices(),
				GLES20.GL_UNSIGNED_SHORT, this.plane.getIndices());

		GLES20.glDisableVertexAttribArray(positionLoc);
		GLES20.glDisableVertexAttribArray(texCoordLoc); //

	}
 
	public void onSurfaceChanged(GL10 glUnused, int width, int height) {
		this.width = width;
		this.height = height;
	   
		GLES20.glViewport(0, 0, width, height);
		createMVP();
	}


	public float getF() {
		return f;
	}

	public void setF(float f) {
 
		this.f = f;
	}

	public static void checkGlError(String glOperation) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Log.e(TAG, glOperation + ": glError " + error);
			throw new RuntimeException(glOperation + ": glError " + error);
		}
	}

}
