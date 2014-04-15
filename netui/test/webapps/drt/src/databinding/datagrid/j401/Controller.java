/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Header:$
 */
package databinding.datagrid.j401;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Some useful Controller for BEEHIVE-401.
 */
@Jpf.Controller (
   simpleActions={
      @Jpf.SimpleAction( name="begin", path="index.jsp" )
   }
)
public class Controller extends PageFlowController {

   private static final Logger logger =
      Logger.getLogger( "BEEHIVE-401 Controller" );

   public Collection getMyData() {
      Collection myData = new ArrayList( 11 );
      myData.add( new SomeStuff( "01", "akj skdjkjfkjdss df") );
      myData.add( new SomeStuff( "02", "akj skdjkjfkjdss df") );
      myData.add( new SomeStuff( "03", "akj skdjkjfkjdss df") );
      myData.add( new SomeStuff( "04", "akj skdjkjfkjdss df") );
      myData.add( new SomeStuff( "05", "akj skdjkjfkjdss df") );
      myData.add( new SomeStuff( "06", "akj skdjkjfkjdss df") );
      myData.add( new SomeStuff( "07", "akj skdjkjfkjdss df") );
      myData.add( new SomeStuff( "08", "akj skdjkjfkjdss df") );
      myData.add( new SomeStuff( "09", "akj skdjkjfkjdss df") );
      myData.add( new SomeStuff( "10", "akj skdjkjfkjdss df") );
      myData.add( new SomeStuff( "11", "akj skdjkjfkjdss df") );
      myData.add( new SomeStuff( "12", "akj skdjkjfkjdss df") );

      return myData;
   }

   public static class SomeStuff {
      private String row;
      private String stuff;

      public String getRow() {
         return row;
      }
      public String getStuff() {
         return stuff;
      }
      public void setRow( String row ) {
         this.row = row;
      }
      public void setStuff( String stuff ) {
         this.stuff = stuff;
      }

      public SomeStuff( String row, String stuff ) {
         this.row = row;
         this.stuff = stuff;
      }
   }
}
