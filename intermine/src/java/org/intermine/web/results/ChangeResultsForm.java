package org.intermine.web.results;

/*
 * Copyright (C) 2002-2004 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form bean to represent the inputs to a text-based query
 *
 * @author Andrew Varley
 */
public class ChangeResultsForm extends ActionForm
{

    protected String pageSize = "10";
    protected String[] selectedObjects = {};
    protected String bagName;
    protected String newBagName;


    /**
     * Set the page size
     *
     * @param pageSize the page size to display
     */
    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * Get the page size
     *
     * @return the page size
     */
    public String getPageSize() {
        return pageSize;
    }


    /**
     * Sets the selected objects
     *
     * @param selectedObjects the selected objects
     */
    public void setSelectedObjects(String[] selectedObjects) {
        this.selectedObjects = selectedObjects;
    }

    /**
     * Gets the selected objects
     *
     * @return the selected objects
     */
    public String[] getSelectedObjects() {
        return selectedObjects;
    }

    /**
     * Set the bag name (existing bags)
     *
     * @param bagName the bag name to save to
     */
    public void setBagName(String bagName) {
        this.bagName = bagName;
    }

    /**
     * Get the bag name (existing bags)
     *
     * @return the bag name
     */
    public String getBagName() {
        return bagName;
    }

    /**
     * Set the bag name (new bag)
     *
     * @param bagName the bag name to save to
     */
    public void setNewBagName(String bagName) {
        this.newBagName = bagName;
    }

    /**
     * Get the bag name (new bag)
     *
     * @return the bag name
     */
    public String getNewBagName() {
        return newBagName;
    }

    /**
     * Reset the form to the initial state
     *
     * @param mapping the mapping
     * @param request the request
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        selectedObjects = new String[] {};
    }

}
