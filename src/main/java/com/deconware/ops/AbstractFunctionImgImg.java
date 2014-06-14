package com.deconware.ops;

import net.imagej.ops.AbstractFunction;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.RealType;

public abstract class AbstractFunctionImgImg <T extends RealType<T>, S extends RealType<S>> extends
AbstractFunction<Img<T>, Img<S>>
{
	
	void InitializeOuput(Img<T> input,
			Img<S> output)
	{
		if (output==null)
		{
			
		}
	}

}
