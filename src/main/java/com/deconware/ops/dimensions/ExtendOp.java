package com.deconware.ops.dimensions;

import net.imagej.ops.AbstractFunction;
import net.imagej.ops.Op;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;
import net.imglib2.outofbounds.OutOfBoundsFactory;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import com.deconware.algorithms.dim.ExtendImageUtility;
import com.deconware.algorithms.dim.ExtendImageUtility.BoundaryType;
import com.deconware.algorithms.fft.SimpleFFTFactory.FFTTarget;

@Plugin(type = Op.class, name = Extend.NAME)
public class ExtendOp <T extends RealType<T>> extends
	AbstractFunction<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> implements Extend
{
	@Parameter
	int[] axisIndices;
	
	@Parameter
	int[] extension;
	
	@Parameter(required = false)
	BoundaryType boundaryType;
	
	@Parameter(required = false)
	FFTTarget fftTarget;
	
	@Override
	public RandomAccessibleInterval<T> compute(RandomAccessibleInterval<T> input,
		RandomAccessibleInterval<T> output)
	{
		if (axisIndices.length!=extension.length)
		{
			return null;
		}
		
		ExtendImageUtility utility=new ExtendImageUtility(axisIndices, extension, input, boundaryType, fftTarget);
		
		OutOfBoundsFactory< T, RandomAccessibleInterval<T> > outOfBoundsFactory= utility.getOutOfBoundsFactory(); 
				
		final RandomAccessible< T > temp = Views.extend( input, outOfBoundsFactory );
		
		final RandomAccessibleInterval<T> extendedInput = Views.offsetInterval(temp, utility.getOffset(), utility.getNewDimensions());
		
		return extendedInput;
	}
}