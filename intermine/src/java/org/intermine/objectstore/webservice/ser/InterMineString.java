package org.intermine.objectstore.webservice.ser;

/*
 * Copyright (C) 2002-2004 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

/**
 * This class is a wrapper for the XML string used to represent
 * InterMineObjects when they are serialized
 * @author Mark Woodbridge
 */
public class InterMineString
{
    protected String string;

    /**
     * No-arg constructor
     */
    public InterMineString() {
    }

    /**
     * Construct a InterMineString using a string
     * @param string a LiteRendered XML string
     */
    public InterMineString(String string) {
        setString(string);
    }

    /**
     * Set the internal string
     * @param string a LiteRendered XML string
     */
    public void setString(String string) {
        this.string = string;
    }

    /**
     * Get the internal string
     * @return the LiteRendered XML string
     */
    public String getString() {
        return string;
    }

    /**
       * @see Object#toString
       */
    public String toString() {
        return string;
    }
}
