package org.intermine.ontology;

/*
 * Copyright (C) 2002-2004 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import java.util.HashSet;
import java.util.Set;


/**
 * Class to hold information about DAG (directed acyclic graph) term.  A term is only
 * away of its direct child/component terms.
 *
 * @author Richard Smith
 * @author Mark Woodbridge
 */
public class DagTerm
{
    private final String id;
    private String name;
    private Set children = new HashSet();
    private Set synonyms = new HashSet();
    private Set components = new HashSet();

    /**
     * Construct with an id and name.
     * @param id the id of this DAG term, may not be changed after construction
     * @param name a name for this DAG term
     */
    public DagTerm(String id, String name) {
        if (id == null || name == null) {
            throw new IllegalArgumentException("id and name arguments may not be null");
        }
        this.id = id;
        this.name = name;
    }

    /**
     * Get the id of this term.
     * @return the id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Get the name of this term.
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Add a child DagTerm to this term (isa relationship).
     * @param child the child term
     */
    public void addChild(DagTerm child) {
        this.children.add(child);
    }

    /**
     * Get a set of direct child DagTerms of this term.
     * @return set of direct child DagTerms
     */
    public Set getChildren() {
        return this.children;
    }

    /**
     * Add a component DagTerm to this term (partof relationship).
     * @param component the component term
     */
    public void addComponent(DagTerm component) {
        this.components.add(component);
    }

    /**
     * Get a set of direct component DagTerms of this term.
     * @return set of direct component DagTerms
     */
    public Set getComponents() {
        return this.components;
    }

    /**
     * Add a synonym for this term, stored as Strings.
     * @param synonym the synonym for this term
     */
    public void addSynonym(String synonym) {
        this.synonyms.add(synonym);
    }

    /**
     * Get a set of synonyms (Strings) for this term.
     * @return a set of synonyms
     */
    public Set getSynonyms() {
        return this.synonyms;
    }

    /**
     * Test for equality with given object, terms are equal if id, name, synonyms
     * and direct children and components are the same.
     * @param o an object to test for equality
     * @return true if equal
     */
    public boolean equals(Object o) {
        if (o instanceof DagTerm) {
            DagTerm d = (DagTerm) o;
            return name.equals(d.name)
                && id.equals(d.id)
                && children.equals(d.children)
                && components.equals(d.components)
                && synonyms.equals(d.synonyms);
        }
        return false;
    }

    /**
     * Generate a hashCode for the DagTerm.
     * @return a hashCode
     */
    public int hashCode() {
        return 3 * id.hashCode() + 5 * name.hashCode()
            + 7 * synonyms.hashCode() + 11 * children.hashCode()
            + 13 * components.hashCode();
    }

    /**
     * Create a string representation of the term.
     * @return a string represenation of the term
     */
    public String toString() {
        return id + ", " + name;
    }

}
