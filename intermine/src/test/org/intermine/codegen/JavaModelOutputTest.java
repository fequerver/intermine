package org.intermine.codegen;

/*
 * Copyright (C) 2002-2004 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import junit.framework.TestCase;

import java.util.*;
import java.io.*;

import org.intermine.util.*;
import org.intermine.metadata.*;


public class JavaModelOutputTest extends TestCase
{

    private String INDENT = ModelOutput.INDENT;
    private String ENDL = ModelOutput.ENDL;
    private Model model;
    private File file;
    private JavaModelOutput mo;
    private String uri = "http://www.intermine.org/model/testmodel";

    public JavaModelOutputTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        model = new Model("model", uri, new HashSet());
        file = new File("temp.xml");
        mo = new JavaModelOutput(model, file);
    }

    public void testProcess() throws Exception {
        ClassDescriptor cld1 = new ClassDescriptor("Class1", null, false, new HashSet(), new HashSet(), new HashSet());
        Model model = new Model("model", uri, new HashSet(Collections.singleton(cld1)));

        new JavaModelOutput(model, new File("./")).process();

        File file = new File("./Class1.java");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + ENDL);
            }

            String expected = "public class Class1 implements org.intermine.model.InterMineObject" + ENDL + "{" + ENDL
                + INDENT + "// Attr: org.intermine.model.InterMineObject.id" + ENDL
                + INDENT + "protected java.lang.Integer id;" + ENDL
                + INDENT + "public java.lang.Integer getId() { return id; }" + ENDL
                + INDENT + "public void setId(java.lang.Integer id) { this.id = id; }" + ENDL + ENDL
                + INDENT + "public boolean equals(Object o) { return (o instanceof Class1 && id != null) ? id.equals(((Class1)o).getId()) : false; }" + ENDL
                + INDENT + "public int hashCode() { return (id != null) ? id.hashCode() : super.hashCode(); }" + ENDL
                + INDENT + "public String toString() { return \"Class1 [\"+id+\"] \"; }" + ENDL
                + "}" + ENDL;
            assertEquals(expected, buffer.toString());
        } finally {
            file.delete();
        }
    }

    public void testGenerateModel() throws Exception {
        assertNull(mo.generate(model));
    }

    public void testGenerateClassDescriptorIsClass() throws Exception {
        ClassDescriptor cld1 = new ClassDescriptor("package.name.Class1", null, false, new HashSet(), new HashSet(), new HashSet());
        Model model = new Model("model", uri, new HashSet(Collections.singleton(cld1)));

        String expected = "package package.name;" + ENDL + ENDL
            + "public class Class1 implements org.intermine.model.InterMineObject" + ENDL + "{" + ENDL
            + INDENT + "// Attr: org.intermine.model.InterMineObject.id" + ENDL
            + INDENT + "protected java.lang.Integer id;" + ENDL
            + INDENT + "public java.lang.Integer getId() { return id; }" + ENDL
            + INDENT + "public void setId(java.lang.Integer id) { this.id = id; }" + ENDL + ENDL
            + INDENT + "public boolean equals(Object o) { return (o instanceof Class1 && id != null) ? id.equals(((Class1)o).getId()) : false; }" + ENDL
            + INDENT + "public int hashCode() { return (id != null) ? id.hashCode() : super.hashCode(); }" + ENDL
            + INDENT + "public String toString() { return \"Class1 [\"+id+\"] \"; }" + ENDL
            + "}" + ENDL;

        assertEquals(expected, mo.generate(cld1));
    }

    public void testGenerateClassDescriptorIsInterface() throws Exception {
        ClassDescriptor cld1 = new ClassDescriptor("package.name.Interface1", null, true, new HashSet(), new HashSet(), new HashSet());
        Model model = new Model("model", uri, new HashSet(Collections.singleton(cld1)));

        String expected = "package package.name;" + ENDL + ENDL
            + "public interface Interface1 extends org.intermine.model.InterMineObject" + ENDL + "{" + ENDL
            + "}" + ENDL;

        assertEquals(expected, mo.generate(cld1));
    }

    public void testGenerateClassDescriptorHasSuperclass() throws Exception {
        ClassDescriptor cld1 = new ClassDescriptor("package.name.Class1", null, false, new HashSet(), new HashSet(), new HashSet());
        ClassDescriptor cld2 = new ClassDescriptor("package.name.Class2", "package.name.Class1", false, new HashSet(), new HashSet(), new HashSet());
        Model model = new Model("model", uri, new HashSet(Arrays.asList(new Object[] {cld1, cld2})));

        String expected = "package package.name;" + ENDL + ENDL
            + "public class Class2 extends package.name.Class1" + ENDL + "{" + ENDL
            + INDENT + "public boolean equals(Object o) { return (o instanceof Class2 && id != null) ? id.equals(((Class2)o).getId()) : false; }" + ENDL
            + INDENT + "public int hashCode() { return (id != null) ? id.hashCode() : super.hashCode(); }" + ENDL
            + INDENT + "public String toString() { return \"Class2 [\"+id+\"] \"; }" + ENDL
            + "}" + ENDL;

        assertEquals(expected, mo.generate(cld2));
    }

    public void testGenerateClassDescriptorHasSubclasses() throws Exception {
        ClassDescriptor cld1 = new ClassDescriptor("package.name.Class1", null, false, new HashSet(), new HashSet(), new HashSet());
        ClassDescriptor cld2 = new ClassDescriptor("package.name.Class2", "package.name.Class1", false, new HashSet(), new HashSet(), new HashSet());
        Model model = new Model("model", uri, new HashSet(Arrays.asList(new Object[] {cld1, cld2})));

        String expected = "package package.name;" + ENDL + ENDL
            + "public class Class1 implements org.intermine.model.InterMineObject" + ENDL + "{" + ENDL
            + INDENT + "// Attr: org.intermine.model.InterMineObject.id" + ENDL
            + INDENT + "protected java.lang.Integer id;" + ENDL
            + INDENT + "public java.lang.Integer getId() { return id; }" + ENDL
            + INDENT + "public void setId(java.lang.Integer id) { this.id = id; }" + ENDL + ENDL
            + INDENT + "public boolean equals(Object o) { return (o instanceof Class1 && id != null) ? id.equals(((Class1)o).getId()) : false; }" + ENDL
            + INDENT + "public int hashCode() { return (id != null) ? id.hashCode() : super.hashCode(); }" + ENDL
            + INDENT + "public String toString() { return \"Class1 [\"+id+\"] \"; }" + ENDL
            + "}" + ENDL;

        assertEquals(expected, mo.generate(cld1));
    }

    public void testGenerateClassDescriptorImplementsInterfaces() throws Exception {
        ClassDescriptor cld1 = new ClassDescriptor("package.name.Interface1", null, true, new HashSet(), new HashSet(), new HashSet());
        ClassDescriptor cld2 = new ClassDescriptor("package.name.Interface2", null, true, new HashSet(), new HashSet(), new HashSet());
        ClassDescriptor cld3 = new ClassDescriptor("package.name.Class1", "package.name.Interface1 package.name.Interface2", false, new HashSet(), new HashSet(), new HashSet());
        Model model = new Model("model", uri, new HashSet(Arrays.asList(new Object[] {cld1, cld2, cld3})));

        String expected = "package package.name;" + ENDL + ENDL
            + "public class Class1 implements package.name.Interface1, package.name.Interface2" + ENDL + "{" + ENDL
            + INDENT + "// Attr: org.intermine.model.InterMineObject.id" + ENDL
            + INDENT + "protected java.lang.Integer id;" + ENDL
            + INDENT + "public java.lang.Integer getId() { return id; }" + ENDL
            + INDENT + "public void setId(java.lang.Integer id) { this.id = id; }" + ENDL + ENDL
            + INDENT + "public boolean equals(Object o) { return (o instanceof Class1 && id != null) ? id.equals(((Class1)o).getId()) : false; }" + ENDL
            + INDENT + "public int hashCode() { return (id != null) ? id.hashCode() : super.hashCode(); }" + ENDL
            + INDENT + "public String toString() { return \"Class1 [\"+id+\"] \"; }" + ENDL
            + "}" + ENDL;

        assertEquals(expected, mo.generate(cld3));
    }

    public void testGenerateClassDescriptorHasFields() throws Exception {
        AttributeDescriptor atd1 = new AttributeDescriptor("atd1", "java.lang.String");
        Set atts = new HashSet(Collections.singleton(atd1));
        ReferenceDescriptor rfd1 = new ReferenceDescriptor("rfd1", "package.name.Class2", null);
        Set refs = new HashSet(Collections.singleton(rfd1));
        CollectionDescriptor cod1 = new CollectionDescriptor("cod1", "package.name.Class2", null, true);
        Set cols = new HashSet(Collections.singleton(cod1));
        ClassDescriptor cld1 = new ClassDescriptor("package.name.Class1", null, false, atts, refs, cols);
        ClassDescriptor cld2 = new ClassDescriptor("package.name.Class2", null, false, new HashSet(), new HashSet(), new HashSet());
        Model model = new Model("model", uri, new HashSet(Arrays.asList(new Object[] {cld1, cld2})));

        String expected = "package package.name;" + ENDL + ENDL
            + "public class Class1 implements org.intermine.model.InterMineObject" + ENDL + "{" + ENDL
            + INDENT + "// Attr: package.name.Class1.atd1" + ENDL
            + INDENT + "protected java.lang.String atd1;" + ENDL
            + INDENT + "public java.lang.String getAtd1() { return atd1; }" + ENDL
            + INDENT + "public void setAtd1(java.lang.String atd1) { this.atd1 = atd1; }" + ENDL + ENDL
            + INDENT + "// Col: package.name.Class1.cod1" + ENDL
            + INDENT + "protected java.util.List cod1 = new java.util.ArrayList();" + ENDL
            + INDENT + "public java.util.List getCod1() { return cod1; }" + ENDL
            + INDENT + "public void setCod1(java.util.List cod1) { this.cod1 = cod1; }" + ENDL
            + INDENT + "public void addCod1(package.name.Class2 arg) { cod1.add(arg); }" + ENDL + ENDL
            + INDENT + "// Ref: package.name.Class1.rfd1" + ENDL
            + INDENT + "protected Object rfd1;" + ENDL
            + INDENT + "public package.name.Class2 getRfd1() { if (rfd1 instanceof org.intermine.objectstore.proxy.ProxyReference) { return ((package.name.Class2) ((org.intermine.objectstore.proxy.ProxyReference) rfd1).getObject()); }; return (package.name.Class2) rfd1; }" + ENDL
            + INDENT + "public void setRfd1(package.name.Class2 rfd1) { this.rfd1 = rfd1; }" + ENDL
            + INDENT + "public void proxyRfd1(org.intermine.objectstore.proxy.ProxyReference rfd1) { this.rfd1 = rfd1; }" + ENDL
            + INDENT + "public Object proxGetRfd1() { return rfd1; }" + ENDL + ENDL
            + INDENT + "// Attr: org.intermine.model.InterMineObject.id" + ENDL
            + INDENT + "protected java.lang.Integer id;" + ENDL
            + INDENT + "public java.lang.Integer getId() { return id; }" + ENDL
            + INDENT + "public void setId(java.lang.Integer id) { this.id = id; }" + ENDL + ENDL
            + INDENT + "public boolean equals(Object o) { return (o instanceof Class1 && id != null) ? id.equals(((Class1)o).getId()) : false; }" + ENDL
            + INDENT + "public int hashCode() { return (id != null) ? id.hashCode() : super.hashCode(); }" + ENDL
            + INDENT + "public String toString() { return \"Class1 [\"+id+\"] \" + getAtd1(); }" + ENDL
            + "}" + ENDL;

        assertEquals(expected, mo.generate(cld1));
    }

    public void testGenerateAttributeDescriptor() throws Exception {
        AttributeDescriptor atd1 = new AttributeDescriptor("atd1", "java.lang.String");
        Set atts = new HashSet(Collections.singleton(atd1));
        ClassDescriptor cld1 = new ClassDescriptor("Class1", null, false, atts, new HashSet(), new HashSet());
        Model model = new Model("model", uri, new HashSet(Collections.singleton(cld1)));

        String expected = INDENT + "// Attr: Class1.atd1" + ENDL
            + INDENT + "protected java.lang.String atd1;" + ENDL
            + INDENT + "public java.lang.String getAtd1() { return atd1; }" + ENDL
            + INDENT + "public void setAtd1(java.lang.String atd1) { this.atd1 = atd1; }" + ENDL + ENDL;

        assertEquals(expected, mo.generate(atd1, true));
    }

    public void testGenerateReferenceDescriptor() throws Exception {
        ReferenceDescriptor rfd1 = new ReferenceDescriptor("rfd1", "Class2", null);
        Set refs = new HashSet(Collections.singleton(rfd1));
        ClassDescriptor cld1 = new ClassDescriptor("Class1", null, false, new HashSet(), refs, new HashSet());
        ClassDescriptor cld2 = new ClassDescriptor("Class2", null, false, new HashSet(), new HashSet(), new HashSet());
        Model model = new Model("model", uri, new HashSet(Arrays.asList(new Object[] {cld1, cld2})));

        String expected = INDENT + "// Ref: Class1.rfd1" + ENDL
            + INDENT + "protected Object rfd1;" + ENDL
            + INDENT + "public Class2 getRfd1() { if (rfd1 instanceof org.intermine.objectstore.proxy.ProxyReference) { return ((Class2) ((org.intermine.objectstore.proxy.ProxyReference) rfd1).getObject()); }; return (Class2) rfd1; }" + ENDL
            + INDENT + "public void setRfd1(Class2 rfd1) { this.rfd1 = rfd1; }" + ENDL
            + INDENT + "public void proxyRfd1(org.intermine.objectstore.proxy.ProxyReference rfd1) { this.rfd1 = rfd1; }" + ENDL
            + INDENT + "public Object proxGetRfd1() { return rfd1; }" + ENDL + ENDL;

        assertEquals(mo.generate(rfd1, true) + "\n" + expected, expected, mo.generate(rfd1, true));
    }

    public void testGenerateCollectionDescriptorUnordered() throws Exception {
        CollectionDescriptor cod1 = new CollectionDescriptor("cod1", "Class2", null, false);
        Set cols = new HashSet(Collections.singleton(cod1));
        ClassDescriptor cld1 = new ClassDescriptor("Class1", null, false, new HashSet(), new HashSet(), cols);
        ClassDescriptor cld2 = new ClassDescriptor("Class2", null, false, new HashSet(), new HashSet(), new HashSet());
        Model model = new Model("model", uri, new HashSet(Arrays.asList(new Object[] {cld1, cld2})));

        String expected = INDENT + "// Col: Class1.cod1" + ENDL
            + INDENT + "protected java.util.Set cod1 = new java.util.HashSet();" + ENDL
            + INDENT + "public java.util.Set getCod1() { return cod1; }" + ENDL
            + INDENT + "public void setCod1(java.util.Set cod1) { this.cod1 = cod1; }" + ENDL
            + INDENT + "public void addCod1(Class2 arg) { cod1.add(arg); }" + ENDL + ENDL;

        assertEquals(expected, mo.generate(cod1, true));
    }

    public void testGenerateCollectionDescriptorOrdered() throws Exception {
        CollectionDescriptor cod1 = new CollectionDescriptor("cod1", "Class2", null, true);
        Set cols = new HashSet(Collections.singleton(cod1));
        ClassDescriptor cld1 = new ClassDescriptor("Class1", null, false, new HashSet(), new HashSet(), cols);
        ClassDescriptor cld2 = new ClassDescriptor("Class2", null, false, new HashSet(), new HashSet(), new HashSet());
        Model model = new Model("model", uri, new HashSet(Arrays.asList(new Object[] {cld1, cld2})));

        String expected = INDENT + "// Col: Class1.cod1" + ENDL
            + INDENT + "protected java.util.List cod1 = new java.util.ArrayList();" + ENDL
            + INDENT + "public java.util.List getCod1() { return cod1; }" + ENDL
            + INDENT + "public void setCod1(java.util.List cod1) { this.cod1 = cod1; }" + ENDL
            + INDENT + "public void addCod1(Class2 arg) { cod1.add(arg); }" + ENDL + ENDL;

        assertEquals(expected, mo.generate(cod1, true));
    }

    public void testGenerateGetSet() throws Exception {
        AttributeDescriptor atd1 = new AttributeDescriptor("atd1", "java.lang.String");
        Set atts = new HashSet(Collections.singleton(atd1));
        ClassDescriptor cld1 = new ClassDescriptor("Class1", null, false, atts, new HashSet(), new HashSet());
        Model model = new Model("model", uri, new HashSet(Collections.singleton(cld1)));

        String expected = INDENT + "public java.lang.String getAtd1() { return atd1; }" + ENDL
            + INDENT + "public void setAtd1(java.lang.String atd1) { this.atd1 = atd1; }" + ENDL;

        assertEquals(expected, mo.generateGetSet(atd1, true));
    }

    public void testGenerateEquals() throws Exception {
        AttributeDescriptor atd1 = new AttributeDescriptor("atd1", "java.lang.String");
        Set atts = new HashSet(Collections.singleton(atd1));
        ClassDescriptor cld1 = new ClassDescriptor("package.name.Class1", null, false, atts, new HashSet(), new HashSet());
        Model model = new Model("model", uri, new HashSet(Collections.singleton(cld1)));

        String expected =  INDENT + "public boolean equals(Object o) { return (o instanceof Class1 && id != null) ? id.equals(((Class1)o).getId()) : false; }" + ENDL;

        assertEquals(expected, mo.generateEquals(cld1));
    }

    public void testGenerateHashCode() throws Exception {
        ClassDescriptor cld1 = new ClassDescriptor("package.name.Class1", null, false, new HashSet(), new HashSet(), new HashSet());
        Model model = new Model("model", uri, new HashSet(Collections.singleton(cld1)));

        String expected = INDENT + "public int hashCode() { return (id != null) ? id.hashCode() : super.hashCode(); }" + ENDL;

        assertEquals(expected, mo.generateHashCode(cld1));
    }

    public void testGenerateToString() throws Exception {
        AttributeDescriptor atd1 = new AttributeDescriptor("atd1", "int");
        AttributeDescriptor atd2 = new AttributeDescriptor("atd2", "int");
        Set atts = new LinkedHashSet(Arrays.asList(new Object[] {atd1, atd2}));
        ClassDescriptor cld1 = new ClassDescriptor("package.name.Class1", null, false, atts, new HashSet(), new HashSet());
        Model model = new Model("model", uri, new LinkedHashSet(Collections.singleton(cld1)));

        String expected = INDENT + "public String toString() { return \"Class1 [\"+id+\"] \" + getAtd1() + \", \" + getAtd2(); }" + ENDL;

        assertEquals(expected, mo.generateToString(cld1));
    }

    public void testGetType() throws Exception {
        AttributeDescriptor atd1 = new AttributeDescriptor("atd1", "java.lang.String");
        assertEquals("java.lang.String", mo.getType(atd1));

        ReferenceDescriptor rfd1 = new ReferenceDescriptor("rfd1", "Class2", null);
        CollectionDescriptor cod1 = new CollectionDescriptor("cod1", "Class2", null, true);
        CollectionDescriptor cod2 = new CollectionDescriptor("cod2", "Class2", null, false);
        Set refs = new HashSet(Collections.singleton(rfd1));
        Set cols = new HashSet(Arrays.asList(new Object[] {cod1, cod2}));
        ClassDescriptor cld1 = new ClassDescriptor("Class1", null, false, new HashSet(), refs, cols);
        ClassDescriptor cld2 = new ClassDescriptor("Class2", null, false, new HashSet(), new HashSet(), new HashSet());
        Model model = new Model("model", uri, new HashSet(Arrays.asList(new Object[] {cld1, cld2})));

        assertEquals("Class2", mo.getType(rfd1));
        assertEquals("java.util.List", mo.getType(cod1));
        assertEquals("java.util.Set", mo.getType(cod2));
    }

    public void testGenerateMultiInheritanceLegal() throws Exception {
        AttributeDescriptor atd1 = new AttributeDescriptor("atd1", "int");
        Set atds1 = new HashSet(Collections.singleton(atd1));
        AttributeDescriptor atd2 = new AttributeDescriptor("atd1", "int");
        Set atds2 = new HashSet(Collections.singleton(atd2));
        ClassDescriptor cld1 = new ClassDescriptor("package.name.Class1", null, true, atds1, new HashSet(), new HashSet());
        ClassDescriptor cld2 = new ClassDescriptor("package.name.Class2", null, true, atds2, new HashSet(), new HashSet());
        ClassDescriptor cld3 = new ClassDescriptor("package.name.Class3", "package.name.Class1 package.name.Class2", false, new HashSet(), new HashSet(), new HashSet());
        Model model = new Model("model", uri, new HashSet(Arrays.asList(new Object[] {cld1, cld2, cld3})));

        String expected = "package package.name;" + ENDL + ENDL
            + "public class Class3 implements package.name.Class1, package.name.Class2" + ENDL + "{" + ENDL
            + INDENT + "// Attr: package.name.Class1.atd1" + ENDL
            + INDENT + "protected int atd1;" + ENDL
            + INDENT + "public int getAtd1() { return atd1; }" + ENDL
            + INDENT + "public void setAtd1(int atd1) { this.atd1 = atd1; }" + ENDL + ENDL
            + INDENT + "// Attr: org.intermine.model.InterMineObject.id" + ENDL
            + INDENT + "protected java.lang.Integer id;" + ENDL
            + INDENT + "public java.lang.Integer getId() { return id; }" + ENDL
            + INDENT + "public void setId(java.lang.Integer id) { this.id = id; }" + ENDL + ENDL
            + INDENT + "public boolean equals(Object o) { return (o instanceof Class3 && id != null) ? id.equals(((Class3)o).getId()) : false; }" + ENDL
            + INDENT + "public int hashCode() { return (id != null) ? id.hashCode() : super.hashCode(); }" + ENDL
            + INDENT + "public String toString() { return \"Class3 [\"+id+\"] \" + getAtd1(); }" + ENDL
            + "}" + ENDL;

        assertEquals(expected, mo.generate(cld3));
    }

}

