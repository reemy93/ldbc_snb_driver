/**                                                                                                                                                                                
 * Copyright (c) 2010 Yahoo! Inc. All rights reserved.                                                                                                                             
 *                                                                                                                                                                                 
 * Licensed under the Apache License, Version 2.0 (the "License"); you                                                                                                             
 * may not use this file except in compliance with the License. You                                                                                                                
 * may obtain a copy of the License at                                                                                                                                             
 *                                                                                                                                                                                 
 * http://www.apache.org/licenses/LICENSE-2.0                                                                                                                                      
 *                                                                                                                                                                                 
 * Unless required by applicable law or agreed to in writing, software                                                                                                             
 * distributed under the License is distributed on an "AS IS" BASIS,                                                                                                               
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or                                                                                                                 
 * implied. See the License for the specific language governing                                                                                                                    
 * permissions and limitations under the License. See accompanying                                                                                                                 
 * LICENSE file.                                                                                                                                                                   
 */

package com.ldbc.generator;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.commons.math3.random.RandomDataGenerator;

// TODO move "last()" to MinMaxGeneratorWrapper
public abstract class Generator<T> implements Iterator<T>
{
    private T last = null;
    private T next = null;
    private final RandomDataGenerator random;

    protected Generator( RandomDataGenerator random )
    {
        this.random = random;
    }

    // Return null if nothing more to generate
    protected abstract T doNext() throws GeneratorException;

    // TODO synchronized for now as Generators are shared among threads
    // TODO consider re-architecting project to not share Generators
    public final synchronized T next()
    {
        next = ( next == null ) ? doNext() : next;
        if ( null == next ) throw new NoSuchElementException( "Generator has nothing more to generate" );
        last = next;
        next = null;
        return last;
    }

    @Override
    public final boolean hasNext()
    {
        next = ( next == null ) ? doNext() : next;
        return ( next != null );
    }

    @Override
    public final void remove()
    {
        throw new UnsupportedOperationException( "Iterator.remove() not supported by Generator" );
    }

    // TODO move to different Wrapper class
    /**
     * Previous item generated by distribution. Should not advance distribution,
     * nor have side effects. If next() has not yet been called, last() should
     * return something reasonable
     */
    public final T last() throws GeneratorException
    {
        if ( null == last )
        {
            last = next();
            // TODO should throw Exception
            // TODO but that breaks current/old code
            // throw new GeneratorException(
            // "last() called before first next() call" );
        }
        return last;
    }

    protected final RandomDataGenerator getRandom()
    {
        return random;
    }

    @Override
    public String toString()
    {
        return "Generator [last=" + last + ", next=" + next + "]";
    }
}