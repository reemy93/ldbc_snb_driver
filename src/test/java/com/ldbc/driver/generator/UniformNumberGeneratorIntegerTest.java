package com.ldbc.driver.generator;

import com.google.common.collect.Range;
import com.ldbc.driver.generator.Generator;
import com.ldbc.driver.util.Histogram;
import com.ldbc.driver.util.Bucket.NumberRangeBucket;

public class UniformNumberGeneratorIntegerTest extends NumberGeneratorTest<Integer, Long>
{
    private final int min = 0;
    private final int max = 5;

    @Override
    public double getMeanTolerance()
    {
        return 0.1;
    }

    @Override
    public double getDistributionTolerance()
    {
        return 0.01;
    }

    @Override
    public Generator<Integer> getGeneratorImpl()
    {
        return getGeneratorFactory().uniformNumberGenerator( min, max );
    }

    @Override
    public Histogram<Integer, Long> getExpectedDistribution()
    {
        Histogram<Integer, Long> expectedDistribution = new Histogram<Integer, Long>( 0l );
        expectedDistribution.addBucket( new NumberRangeBucket<Integer>( Range.closedOpen( 0d, 1d ) ), 1l );
        expectedDistribution.addBucket( new NumberRangeBucket<Integer>( Range.closedOpen( 1d, 2d ) ), 1l );
        expectedDistribution.addBucket( new NumberRangeBucket<Integer>( Range.closedOpen( 2d, 3d ) ), 1l );
        expectedDistribution.addBucket( new NumberRangeBucket<Integer>( Range.closedOpen( 3d, 4d ) ), 1l );
        expectedDistribution.addBucket( new NumberRangeBucket<Integer>( Range.closedOpen( 4d, 5d ) ), 1l );
        expectedDistribution.addBucket( new NumberRangeBucket<Integer>( Range.closed( 5d, 6d ) ), 1l );
        return expectedDistribution;
    }

    @Override
    public double getExpectedMean()
    {
        return ( (double) max - (double) min ) / 2;
    }
}