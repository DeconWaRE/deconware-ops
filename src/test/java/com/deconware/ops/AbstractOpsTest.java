/** 
 * 
 * FIXME: Cut and pasted from imagej-ops for early testing.  Need to get rid of this
 * and use imagej version once the exact structure of the testing code is semi-final
 * 
 * 
 * #%L
 * ImageJ OPS: a framework for reusable algorithms.
 * %%
 * Copyright (C) 2014 Board of Regents of the University of
 * Wisconsin-Madison and University of Konstanz.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package com.deconware.ops;

import net.imagej.ops.OpService;
import net.imagej.ops.OpMatchingService;

import net.imglib2.FinalInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Intervals;

import org.junit.After;
import org.junit.Before;
import org.scijava.Context;
import org.scijava.plugin.Parameter;

/**
 * Base class for {@link Op} unit testing.
 * <p>
 * <i>All</i> {@link Op} unit tests need to have an {@link OpService} instance.
 * Following the DRY principle, we should implement it only once. Here.
 * </p>
 * 
 * @author Johannes Schindelin
 * @author Curtis Rueden
 */
public abstract class AbstractOpsTest {

	@Parameter
	protected Context context;

	@Parameter
	protected OpService ops;

	@Parameter
	protected OpMatchingService matcher;

	/** Subclasses can override to create a context with different services. */
	protected Context createContext() {
		return new Context(OpService.class, OpMatchingService.class);
	}

	/** Sets up a SciJava context with {@link OpService}. */
	@Before
	public void setUp() {
		createContext().inject(this);
	}

	/**
	 * Disposes of the {@link OpService} that was initialized in {@link #setUp()}.
	 */
	@After
	public synchronized void cleanUp() {
		if (context != null) {
			context.dispose();
			context = null;
			ops = null;
			matcher = null;
		}
	}

	private int seed;

	private int pseudoRandom() {
		return seed = 3170425 * seed + 132102;
	}

	public Img<ByteType> generateByteTestImg(final boolean fill,
		final long... dims)
	{
		final byte[] array =
			new byte[(int) Intervals.numElements(new FinalInterval(dims))];

		if (fill) {
			seed = 17;
			for (int i = 0; i < array.length; i++) {
				array[i] = (byte) pseudoRandom();
			}
		}

		return ArrayImgs.bytes(array, dims);
	}

	public Img<UnsignedByteType> generateUnsignedByteTestImg(final boolean fill,
		final long... dims)
	{
		final byte[] array =
			new byte[(int) Intervals.numElements(new FinalInterval(dims))];

		if (fill) {
			seed = 17;
			for (int i = 0; i < array.length; i++) {
				array[i] = (byte) pseudoRandom();
			}
		}

		return ArrayImgs.unsignedBytes(array, dims);
	}
	
	public Img<DoubleType> generateDoubleTestImg(final boolean fill,
			final long... dims)
		{
			final double[] array =
				new double[(int) Intervals.numElements(new FinalInterval(dims))];

			if (fill) {
				seed = 17;
				for (int i = 0; i < array.length; i++) {
					array[i] = (double) pseudoRandom();
				}
			}

			return ArrayImgs.doubles(array, dims);
		}
	
	public Img<FloatType> generateFloatTestImg(final boolean fill,
			final long... dims)
		{
			final float[] array =
				new float[(int) Intervals.numElements(new FinalInterval(dims))];

			if (fill) {
				seed = 17;
				for (int i = 0; i < array.length; i++) {
					array[i] = (float) pseudoRandom();
				}
			}

			return ArrayImgs.floats(array, dims);
		}
}
