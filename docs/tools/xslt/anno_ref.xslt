<?xml version="1.0" encoding="UTF-8"?>
 <!DOCTYPE xsl:stylesheet [ 
    <!ENTITY nbsp "&#160;">   <!-- white space in XSL -->
    ]> 
<xsl:stylesheet version="1.0" 
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
  xmlns:ref="http://www.w3.org/2001/XMLSchema"
  exclude-result-prefixes="ref">
	<!--<xsl:preserve-space elements="*"/>-->
	<xsl:output doctype-public="-//APACHE//DTD Documentation V2.0//EN" doctype-system="http://forrest.apache.org/dtd/document-v20.dtd"/>

    <xsl:template match="/">  

		<document>
		    <header>
		        <title>Reference Documentation: <xsl:value-of select="chapter@xreflabel"/></title>
		    </header>
		    <body>
		        <!-- ############### Description Section ################-->
		        <xsl:for-each select="//sect1//indexterm/secondary">
		            <section>
		             <title></title>
		            	<p><xsl:value-of select="."/></p>
		            </section>
	            </xsl:for-each>
		        <!-- ############### Elements Section ################-->
		        <section>
		        	<title>Elements</title>
			        <xsl:for-each select="//ref:indexterm/ref:primary">
			        	<section>
			        	    <title>&lt;<xsl:value-of select="."/>></title>
			        	    <p><xsl:value-of select="."/> &nbsp; </p>
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
