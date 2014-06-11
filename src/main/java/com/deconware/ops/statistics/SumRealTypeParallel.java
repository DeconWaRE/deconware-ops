package com.deconware.ops.statistics;

import net.imagej.ops.Op;
import net.imagej.ops.statistics.Sum;
import net.imagej.ops.AbstractFunction;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;
import net.imagej.ops.Contingent;

import com.deconware.algorithms.parallel.ReductionChunker;
import com.deconware.algorithms.parallel.math.ParallelSum;

/**
* Multi-threaded version of sum 
*
* @author Brian Northan
*/
@Plugin(type = Op.class, name = Sum.NAME, priority = Priority.HIGH_PRIORITY + 10)
public class SumRealTypeParallel<T extends RealType<T>, V extends RealType<V>> extends
	AbstractFunction<IterableInterval<T>, V> implements Sum<IterableInterval<T>, V>, Contingent 
{

	@Override
	public V compute(IterableInterval<T> image, V value) 
	{
		ParallelSum<T, V> sum=new ParallelSum<T, V>(image);
		
		ReductionChunker<T, V> chunker=
				new ReductionChunker<T, V>(image.size(), sum, value);
		
		V output=chunker.run();
		
		value.set(output);
		
		return value;
	}
	
	@Override
	public boolean conforms() 
	{	
		// if there is no output type can't perform this version of the op
		if (this.getOutput()==null)
		{
			return false;
		}
		return true;
	}
}
