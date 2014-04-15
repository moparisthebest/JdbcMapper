<?xml version="1.0" encoding="UTF-8"?>
 <!DOCTYPE xsl:stylesheet [ 
    <!ENTITY nbsp "&#160;">   <!-- white space in XSL -->
    ]> 
<xsl:stylesheet version="1.0" 
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
  xmlns:ref="http://www.w3.org/2001/XMLSchema"
  exclude-result-prefixes="ref">
	<!--<xsl:preserve-space elements="*"/>-->
	<xsl:output doctype-public="-//APACHE//DTD Documentation V1.2//EN" doctype-system="http://forrest.apache.org/dtd/document-v13.dtd"/>

    <xsl:template match="/">  

		<document>
		    <header>
		        <title>Reference Documentation: <xsl:value-of select="ref:schema/ref:element/@name"/>.xml File</title>
		    </header>
		    <body>
		        <!-- ############### Description Section ################-->
		        <xsl:for-each select="//ref:annotation">
		            <xsl:if test="@id = 'description'">
		            <section>
		                <title>Description</title>
		            	<p><xsl:value-of select="ref:documentation"/></p>
		            </section>
		            </xsl:if>
	            </xsl:for-each>
		        <!-- ############### Elements Section ################-->
		        <section>
		        	<title>Elements</title>
			        <xsl:for-each select="//ref:element">
			        	<xsl:sort select="@name"/>
			        	<section>
			        	    <title>&lt;<xsl:value-of select="@name"/>></title>
			        	    <p><xsl:value-of select="ref:annotation/ref:documentation"/> &nbsp; </p>
			        	</section>
			        </xsl:for-each>
				</section>
		        <!-- ############### Example Section ################-->
		        <xsl:for-each select="//ref:annotation">
		            <xsl:if test="@id = 'example'">
		            <section>
		                <title>Example</title>
		            	<p><xsl:value-of select="ref:documentation"/></p>
		            </section>
		            </xsl:if>
	            </xsl:for-each>
		    </body>
		</document>


	</xsl:template>


</xsl:stylesheet>
