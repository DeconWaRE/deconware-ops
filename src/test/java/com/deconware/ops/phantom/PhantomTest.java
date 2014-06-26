package com.deconware.ops.phantom;

import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.meta.AxisType;
import net.imglib2.meta.Axes;
import net.imglib2.view.Views;

import org.junit.Test;
import org.junit.Assert;

import java.lang.Math;

import net.imagej.ops.OpService;

import com.deconware.ops.AbstractOpsTest;

public class PhantomTest extends AbstractOpsTest
{
	public static Img<FloatType> makeMultiChannelPhantom(OpService ops, int xSize, int ySize, int numSlices, int numChannels, int radius)
	{
		int[] size=new int[4];
		size[0]=xSize;
		size[1]=ySize;
		size[2]=numSlices;
		size[3]=numChannels;

		AxisType[] ax=new AxisType[]{Axes.X, Axes.Y, Axes.Z, Axes.CHANNEL};
     
		Img<FloatType> testImage=(Img<FloatType>)ops.run("create", size, new FloatType());

		int[] location=new int[3];
		location[0]=40;
		location[1]=size[1]/2;
		location[2]=size[2]/2;
		
		for (int c=0;c<numChannels;c++)
		{
			RandomAccessibleInterval<FloatType> hyperSlice= 
					Views.hyperSlice(testImage, 3, c);
			
			ops.run("addsphere",  hyperSlice, location, 1.0, radius);
			location[0]+=10;
		}
		
		return testImage;
	}
	
	@Test
	public void MultiChannelPhantomTest()
	{
		int xSize=40;
		int ySize=50;
		int numSlices=25;
		int numChannels=3;
		int radius =10;

		Img<FloatType> testImage=makeMultiChannelPhantom(ops, xSize, ySize, numSlices, numChannels, radius);

		float sum1=0.0f;
		for (int c=0;c<numChannels;c++)
		{
			RandomAccessibleInterval<FloatType> hyperSlice= 
					Views.hyperSlice(testImage, 3, c);
			
			Object sum=ops.run("sum", Views.iterable(hyperSlice));
			sum1+=(Float)sum;
			
			System.out.println("sum channel "+c+":"+sum);
		}
		
		Float sum2=(Float)ops.run("sum", testImage);
		System.out.println("total sum "+sum2);
		
		// the accumalitive sum of each channel (sum1) should be equal to the total sum
		Assert.assertEquals(sum1, sum2, 0.00001);
		
		// calculate the geometric volume of the sphere(s)
		float volume_apr=4f/3f*((float)Math.PI)*radius*radius*radius*3;
		
		System.out.println("volume/aprvolume: "+sum1+":"+volume_apr);
		
		// make the "pixelated" volume is within 2% of the approximate volume
		Assert.assertEquals(1.0, volume_apr/sum1, .02);
	}
	
	@Test
	public void Phantom3DTest()
	{
		int[] size=new int[]{100,100,100};
		
		Img<FloatType> testImage=(Img<FloatType>)ops.run("create", size, new FloatType());

		int[] location=new int[]{50, 50, 50};
	
		ops.run("addsphere",  testImage, location, 1.0, 10);
		
		FloatType max=new FloatType();
		
		max.setReal(Float.MIN_VALUE);
		
		ops.run("max", max, testImage);
		
		System.out.println("global max: "+max);
		
		int accumulator=0;
		
		System.out.println("acc: "+accumulator);

		
	}
}
