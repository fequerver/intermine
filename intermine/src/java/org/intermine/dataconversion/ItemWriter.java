package org.intermine.dataconversion;

/*
 * Copyright (C) 2002-2004 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import java.util.Collection;

import org.intermine.model.fulldata.Item;
import org.intermine.objectstore.ObjectStoreException;

/**
 * Stores Items in something - an objectstore or otherwise.
 *
 * @author Matthew Wakeling
 * @author Mark Woodbridge
 */
public interface ItemWriter
{
    /**
     * Stores the given Item and all its associated attributes, references, and referencelists.
     *
     * @param item the Item to store
     * @throws ObjectStoreException if something goes wrong. Note that for performance reasons, not
     * all implementations of ItemWriter necessarily actually perform the store before this method
     * terminates, therefore a problem could result in an ObjectStoreException being thrown at a
     * later time
     */
    public void store(Item item) throws ObjectStoreException;

    /**
     * Stores the given Collection of Items and all their associated attributes, references, and
     * referencelists.
     *
     * @param items the Collection of Items to store
     * @throws ObjectStoreException if something goes wrong. Note that for performance reasons, not
     * all implementations of ItemWriter necessarily actually perform the store before this method
     * terminates, therefore a problem could result in an ObjectStoreException being thrown at a
     * later time
     */
    public void storeAll(Collection items) throws ObjectStoreException;

    /**
     * Flushes any store queue, closes transactions, and generally makes sure that every Item passed
     * to the store() method is committed to the destination.
     *
     * @throws ObjectStoreException if something goes wrong. If a problem has occurred with ANY of
     * the previously store()ed Items that has not already caused an ObjectStoreException to be
     * thrown, then this method MUST throw an ObjectStoreException
     */
    public void close() throws ObjectStoreException;
}

