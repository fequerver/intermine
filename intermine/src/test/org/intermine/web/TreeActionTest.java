package org.intermine.web;

/*
 * Copyright (C) 2002-2004 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import java.util.Set;
import java.util.HashSet;

import servletunit.struts.MockStrutsTestCase;

public class TreeActionTest extends MockStrutsTestCase
{
    public TreeActionTest(String arg1) {
        super(arg1);
    }

    public void testExpand() throws Exception {
        Set openClasses = new HashSet();
        openClasses.add("org.intermine.model.testmodel.Thing");
        getSession().setAttribute("openClasses", openClasses);

        addRequestParameter("node", "org.intermine.model.testmodel.Department");

        setRequestPathInfo("/changeTree");
        addRequestParameter("method", "expand");

        actionPerform();
        verifyNoActionErrors();
        verifyForward("renderTree");
        
        Set expected = new HashSet();
        expected.add("org.intermine.model.testmodel.Thing");
        expected.add("org.intermine.model.testmodel.Department");
        assertEquals(expected, getSession().getAttribute("openClasses"));
    }

    public void testCollapse() throws Exception {
        Set openClasses = new HashSet();
        openClasses.add("org.intermine.model.testmodel.Thing");
        openClasses.add("org.intermine.model.testmodel.Department");
        getSession().setAttribute("openClasses", openClasses);

        addRequestParameter("node", "org.intermine.model.testmodel.Department");

        setRequestPathInfo("/changeTree");
        addRequestParameter("method", "collapse");

        actionPerform();
        verifyNoActionErrors();
        verifyForward("renderTree");
        
        Set expected = new HashSet();
        expected.add("org.intermine.model.testmodel.Thing");
        assertEquals(expected, getSession().getAttribute("openClasses"));
    }

//     public void testSelect() throws Exception {
//         addRequestParameter("node", "org.intermine.model.testmodel.Department");

//         setRequestPathInfo("/changeTree");
//         addRequestParameter("method", "select");

//         actionPerform();
//         verifyNoActionErrors();
//         verifyForward("buildquery");

//         assertEquals(Department.class, ((QueryClass) getSession().getAttribute("queryClass")).getType());
//     }
}
