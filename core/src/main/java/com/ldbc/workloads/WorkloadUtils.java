package com.ldbc.workloads;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.random.RandomDataGenerator;

import com.ldbc.DBRecordKey;
import com.ldbc.WorkloadException;
import com.ldbc.generator.Generator;
import com.ldbc.generator.GeneratorBuilder;
import com.ldbc.generator.MinMaxGeneratorWrapper;
import com.ldbc.generator.ycsb.YcsbExponentialGenerator;
import com.ldbc.util.ByteIterator;
import com.ldbc.util.Pair;
import com.ldbc.util.RandomByteIterator;

public class WorkloadUtils
{
    // TODO temp, this should be given in the constructor, remove later
    static RandomDataGenerator random = new RandomDataGenerator();

    public static Map<String, ByteIterator> buildValuedFields( Generator<Set<String>> fieldSelectionGenerator,
            Generator<Integer> valueLengthGenerator ) throws WorkloadException
    {
        Map<String, ByteIterator> values = new HashMap<String, ByteIterator>();
        for ( String fieldName : fieldSelectionGenerator.next() )
        {
            ByteIterator data = new RandomByteIterator( valueLengthGenerator.next(), random );
            values.put( fieldName, data );
        }
        return values;
    }

    // TODO test / or remove entirely
    // Generate the next random keyNumber
    // NOTES:
    // Not sure how exactly this works
    // Seems to generate random numbers UNTIL it finds one matching a given
    // criteria (e.g. in range)
    // Seems inefficient and generally a bad approach
    public static DBRecordKey nextKey( Generator<Long> requestKeyGenerator,
            MinMaxGeneratorWrapper<Long> transactionInsertKeyGenerator ) throws WorkloadException
    {
        long keyNumber;
        if ( requestKeyGenerator instanceof YcsbExponentialGenerator )
        {
            do
            {
                keyNumber = transactionInsertKeyGenerator.getMax() - requestKeyGenerator.next();
            }
            while ( keyNumber < 0 );
        }
        else
        {
            do
            {
                keyNumber = requestKeyGenerator.next();
            }
            while ( keyNumber > transactionInsertKeyGenerator.getMax() );
        }
        return new DBRecordKey( keyNumber );
    }

    public static <T extends Number> Generator<T> buildFieldLengthGenerator( GeneratorBuilder generatorBuilder,
            Distribution distribution, T lowerBound, T upperBound ) throws WorkloadException
    {
        switch ( distribution )
        {
        case CONSTANT:
            if ( false == lowerBound.equals( upperBound ) )
            {
                throw new WorkloadException( "For ConstantGenerator lowerbound must equal upperbound" );
            }
            return generatorBuilder.constantGenerator( lowerBound ).build();
        case UNIFORM:
            return generatorBuilder.uniformNumberGenerator( lowerBound, upperBound ).build();
        case ZIPFIAN:
            return generatorBuilder.zipfianNumberGenerator( lowerBound, upperBound ).build();
        default:
            String errMsg = String.format( "Invalid Distribution [%s], use one of the following: %s, %s, %s, %s",
                    distribution, Distribution.CONSTANT, Distribution.UNIFORM, Distribution.ZIPFIAN );
            throw new WorkloadException( errMsg );
        }
    }

    public static Generator<Set<String>> buildFieldSelectionGenerator( GeneratorBuilder generatorBuilder,
            String fieldNamePrefix, int fieldCount, boolean isReturnAllFields )
    {
        if ( false == isReturnAllFields )
        {
            Set<Pair<Double, String>> fields = new HashSet<Pair<Double, String>>();
            for ( int i = 0; i < fieldCount; i++ )
            {
                Pair<Double, String> field = new Pair<Double, String>( 1d, fieldNamePrefix + i );
                fields.add( field );
            }
            int fieldsToRetrieve = 1;
            Generator<Integer> fieldsToRetrieveGenerator = generatorBuilder.constantGenerator( fieldsToRetrieve ).build();
            return generatorBuilder.discreteMultiGenerator( fields, fieldsToRetrieveGenerator ).build();
        }
        else
        {
            Set<String> fields = new HashSet<String>();
            for ( int i = 0; i < fieldCount; i++ )
            {
                String field = fieldNamePrefix + i;
                fields.add( field );
            }
            return generatorBuilder.constantGenerator( fields ).build();
        }
    }
}