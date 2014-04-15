Custom Doclet: JSP Tag Library Reference
----------------------------------------

This document describes how the JSP Tag Library Reference is generated using a custom doclet.

Introduction
------------

The custom doclet was created especially for JSP tag library documentation.
From a JSP tag-user perspective, standard Javadoc can be difficult to read: standard javadoc
output contains too many implementation details and the information that the tag-user is 
looking for often gets lost in the thicket of information.

To solve this problem, the custom doclet generates HTML topics oriented specifically for the tag-user:
They include (1) a syntax section that describes the JSP tag's syntax, (2) an attributes section which describes
how each JSP tag attribute works, and (3) an example section that shows the JSP tag in action on a JSP page.

Custom Doclet Files
-------------------

The custom doclet files are contained in the JAR file BEEHIVE_HOME/netui/docs/tools/lib/jsptagrefdoclet.jar.
This JAR file contains the following resources:

(1) Doclet classes that extend the Javadoc API. (The source code for these classes will be posted in the Apache Commons project at a later date.)

(2) Static HTML and CSS files.

(3) XML stylesheets (XSTL files) that convert XML files into HTML files.

How the Custom Doclet Works
---------------------------

The doclet generates HTML documentation in two stages.

    	-----              -----            -----
        |   |              |   |            |   |
        |   |  ---(1)--->  |   | ---(2)---> |   |
        |   |              |   |            |   |
        -----              -----            ----- 

      JAVA tag           XML files       HTML topics
    handler classes

   (1) First XML documents are constructed from the javadoc content in the Java source files (= the tag handler classes). 
   (2) HTML topics are constructed from the XML documents.

In the XML-generating step, the doclet makes one XML document for each Java tag handler class.
The doclet reads content from the @jsptagref.xxx javadoc annotations in the Java source files,
and creates an XML document from the content.

For example, here is a snippet from the tag handler class Anchor.java, which handles the <netui:anchor> JSP tag.

 * @jsptagref.tagdescription
 * <p>
 * Generates an anchor that can link...
 
The doclet reads this content, and constructs the following XML snippet.

 <tagdescription prefix="jsptagref">
    <lead>
    &lt;p>Generates an anchor that can link...

In the HTML-generating step, XML stylesheets (XSLT files) are used to construct HTML topics based on 
the XML files.  The stylesheets map the content in the XML files into HTML files.
The following diagram shows how the stylesheets divide up the real estate in the HTML pages.
     
                                --------------------------
                                |      |                 |
                                |      |                 |
 taglib-overview-frame.xslt --> |      |                 |
                                |      |                 |
                                --------                 |
                                |      |                 |
                                |      |                 |
                                |      |                 |  <--- taglib-overview-summary.xslt
                                |      |                 |  <--- taglib-summary.xslt
    all-tablibs-frame.xslt ---> |      |                 |  <--- tag_topics.xslt
         taglib-frame.xslt ---> |      |                 |
                                |      |                 |
                                |      |                 |
                                |      |                 |
                                |      |                 |
                                |      |                 |
                                --------------------------
                              
                          
