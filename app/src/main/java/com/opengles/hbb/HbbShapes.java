
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



import java.lang.Math;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class HbbShapes {

	public int genStandardPlane() {
		return createVertices(480, 800, 12, 10, -100.0f);
	}

	public int genPlane(float w,float h,int divX, int divY,float z){
		return createVertices(w,h,divX,divY,z);
	}
	
	protected int createVertices(float w, float h, int divX, int divY, float z) {
		
		int numIndices = divX * divY * 6; //顶点数目 需要绘制的三角形的顶点总数  包括重复的顶点
		int numVertices = (divX + 1) * (divY + 1); //所有独立的顶点，不重复
 
		
		float dx = w / divX;
		float dy = h / divY;
		float x;
		float y;

		mVertices = ByteBuffer.allocateDirect(numVertices * 3 * 4)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mNormals = ByteBuffer.allocateDirect(numVertices * 3 * 4)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mTexCoords = ByteBuffer.allocateDirect(numVertices * 2 * 4)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mIndices = ByteBuffer.allocateDirect(numIndices * 2)
				.order(ByteOrder.nativeOrder()).asShortBuffer();

		
		System.out.println( "三角形顶点 【"+ numIndices + "】个" );	
		System.out.println( "顶点 【"+ numVertices + "】个" );	
		
		
		for (int j = 0; j <= divY; j++) {
			y = h / 2 - dy * j; // 每一行的Y坐标
			for (int i = 0; i <= divX; i++) {
				x = dx * i - w / 2;

				final int  index =  (j * (divX + 1) + i);
				
				int t = index * 3;
 
				
				mVertices.put(t, x);
				mVertices.put(t + 1, y);
				mVertices.put(t + 2, z);
				
			System.out.println( "顶点 【"+ (t/3) + "】"  + "--------------------------------------");	
			System.out.println("[t = " + t + ", x = " +x +"]");
			System.out.println("[t = " + (t+1) + ", y = " + y + "]");	
			System.out.println("[t = " + (t+2) + " ,  z = " + z +"]");
			
				int texIndex = index*2 ;
			
				mTexCoords.put( texIndex, (float) i / (float) divX);//s
				
				System.out.println("[texIndex = " + (texIndex) + " ,  S = " + (float) i / (float) divX +"]");
				
				mTexCoords.put( texIndex+1,1.0f - (float)j/ (float) divY);//t
				System.out.println("[texIndex = " + (texIndex+1) + " ,  T = " + (1.0f - (float)j/ (float) divY));
		 
			}
		}
		
		int index = 0;
		
		
		
		for (int i = 0; i < divX; i++) {
			for (int j = 0; j < divY; j++) {
				System.out.println( "顶点 【绘制顺序】"  + "--------------------------------------");	
				System.out.println("index " + index  + " ==> " + (short) (i * (divY + 1) + j) );
				mIndices.put(index++, (short) (i * (divY + 1) + j));
				
				System.out.println("index " + index  + " ==> " + (short) ((i + 1) * (divY + 1) + j));
				
				mIndices.put(index++, (short) ((i + 1) * (divY + 1) + j));
				
				
				System.out.println("index " + index  + " ==> " + (short) ((i + 1) * (divY + 1) + (j + 1)));
				mIndices.put(index++, (short) ((i + 1) * (divY + 1) + (j + 1)));
				
				System.out.println("index " + index  + " ==> " + (short) (i * (divY + 1) + j));
				mIndices.put(index++, (short) (i * (divY + 1) + j));
				
				System.out.println("index " + index  + " ==> " + (short) ((i + 1) * (divY + 1) + (j + 1)));
				mIndices.put(index++, (short) ((i + 1) * (divY + 1) + (j + 1)));
				
				System.out.println("index " + index  + " ==> " +  (short) (i * (divY + 1) + (j + 1)));
				mIndices.put(index++, (short) (i * (divY + 1) + (j + 1)));
				
		
 
			}
		}
		
 
		
		
		
		mNumIndices = numIndices;
	 
		return numIndices;
	}
 
 

	public FloatBuffer getVertices() {
		return mVertices;
	}

	public FloatBuffer getNormals() {
		return mNormals;
	}

	public FloatBuffer getTexCoords() {
		return mTexCoords;
	}

	public ShortBuffer getIndices() {
		return mIndices;
	}

	public int getNumIndices() {
		return mNumIndices;
	}

	// Member variables
	private FloatBuffer mVertices;
	private FloatBuffer mNormals;
	private FloatBuffer mTexCoords;
	private ShortBuffer mIndices;
	private int mNumIndices;
}
