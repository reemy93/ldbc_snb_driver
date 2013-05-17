package com.ldbc.generator;

import org.apache.commons.math3.random.RandomDataGenerator;

import com.ldbc.data.ByteIterator;
import com.ldbc.data.RandomByteIterator;

// TODO somehow generalize to all ByteIterators?
public class RandomByteIteratorGenerator extends Generator<ByteIterator>
{
    private final Generator<Integer> lengthGenerator;

    protected RandomByteIteratorGenerator( RandomDataGenerator random, Generator<Integer> lengthGenerator )
    {
        super( random );
        this.lengthGenerator = lengthGenerator;
    }

    @Override
    protected ByteIterator doNext() throws GeneratorException
    {
        return new RandomByteIterator( lengthGenerator.next(), getRandom() );
    }

}
