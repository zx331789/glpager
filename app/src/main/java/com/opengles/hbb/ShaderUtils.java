
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

import android.opengl.GLES20;

public class ShaderUtils {
	//
 
	public static int loadShader(int type, String shaderSrc) {
		int shader;
		int[] compiled = new int[1];
 
		shader = GLES20.glCreateShader(type);

		if (shader == 0)
			return 0;
 
		GLES20.glShaderSource(shader, shaderSrc);

 
		GLES20.glCompileShader(shader);

 
		GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);

		if (compiled[0] == 0) {
	 
			GLES20.glDeleteShader(shader);
			return 0;
		}
		return shader;
	}

 
	public static int loadProgram(String vertShaderSrc, String fragShaderSrc) {
		int vertexShader;
		int fragmentShader;
		int programObject;
		int[] linked = new int[1];

	 
		vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertShaderSrc);
		if (vertexShader == 0)
			return 0;

		fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragShaderSrc);
		if (fragmentShader == 0) {
			GLES20.glDeleteShader(vertexShader);
			return 0;
		}

	 
		programObject = GLES20.glCreateProgram();

		if (programObject == 0)
			return 0;

		GLES20.glAttachShader(programObject, vertexShader);
		GLES20.glAttachShader(programObject, fragmentShader);

 
		GLES20.glLinkProgram(programObject);

	 
		GLES20.glGetProgramiv(programObject, GLES20.GL_LINK_STATUS, linked, 0);

		if (linked[0] == 0) {
	 
			GLES20.glDeleteProgram(programObject);
			return 0;
		}

 
		GLES20.glDeleteShader(vertexShader);
		GLES20.glDeleteShader(fragmentShader);

		return programObject;
	}

}
