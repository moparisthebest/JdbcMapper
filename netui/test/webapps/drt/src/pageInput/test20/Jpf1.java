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
package pageInput.test20;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

/**
 * @jpf:controller
 * @jpf:forward name="gotoDone" path="/resources/jsp/done.jsp"
 * @jpf:forward name="gotoError" path="/resources/jsp/error.jsp"
 */
@Jpf.Controller(
    forwards = {
        @Jpf.Forward(
            name = "gotoDone",
            path = "/resources/jsp/done.jsp"),
        @Jpf.Forward(
            name = "gotoError",
            path = "/resources/jsp/error.jsp") 
    })
public class Jpf1 extends PageFlowController
   {
   private final static int            _SIZE       = 50;
   private final static String         _STRING     = "Str: ";
   private              String         _str[];
   private              String         _strMulti[][];
   private              int            _int[];
   private              boolean        _bool[];
   private              ArrayList      _aLst;
   private              float          _flt[];
   private              Hashtable      _hash;
   private              Vector         _vec;
   private              Integer        _integer[];
   private              Float          _float[];
   private              Boolean        _boolean[];
   private              StringBuffer   _strBuf[];

   /**
    * @jpf:action
    * @jpf:forward name="gotoPg1" path="Jsp1.jsp"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg1",
                path = "Jsp1.jsp") 
        })
   protected Forward begin()
      {
          //System.out.println(">>> Jpf1.begin");
      _str = new String[_SIZE];
      for (int i = 0; i < _SIZE; i++)
         {
         _str[i] = _STRING + Integer.toString(i);
         }
      Forward fwd = new Forward("gotoPg1");
      fwd.addPageInput("Repeater", _str);
      return fwd;
      }

   /**
    * @jpf:action
    * @jpf:forward name="gotoPg2" path="Jsp2.jsp"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg2",
                path = "Jsp2.jsp") 
        })
   protected Forward action1()
      {
          //System.out.println(">>> Jpf1.action1");
      _int = new int[_SIZE];
      for (int i = 0; i < _SIZE; i++)
         {
         _int[i] = i;
         }
      Forward fwd = new Forward("gotoPg2");
      fwd.addPageInput("Repeater", _int);
      return fwd;
      }

   /**
    * @jpf:action
    * @jpf:forward name="gotoPg3" path="Jsp3.jsp"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg3",
                path = "Jsp3.jsp") 
        })
   protected Forward action2()
      {
          //System.out.println(">>> Jpf1.action2");
      boolean x = true;
      _bool = new boolean[_SIZE];
      for (int i = 0; i < _SIZE; i++)
         {
         _bool[i] = x;
         if (x == true) { x = false; } else { x = true; }
         }
      Forward fwd = new Forward("gotoPg3");
      fwd.addPageInput("Repeater", _bool);
      return fwd;
      }

   /**
    * @jpf:action
    * @jpf:forward name="gotoPg4" path="Jsp4.jsp"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg4",
                path = "Jsp4.jsp") 
        })
   protected Forward action3()
      {
          //System.out.println(">>> Jpf1.action3");
      _aLst = new ArrayList(_SIZE);
      for (int i = 0; i < _SIZE; i++)
         {
         _aLst.add(_STRING + Integer.toString(i));
         }
      Forward fwd = new Forward("gotoPg4");
      fwd.addPageInput("Repeater", _aLst);
      return fwd;
      }

   /**
    * @jpf:action
    * @jpf:forward name="gotoPg5" path="Jsp5.jsp"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg5",
                path = "Jsp5.jsp") 
        })
   protected Forward action4()
      {
          //System.out.println(">>> Jpf1.action4");
      _flt = new float[_SIZE];
      for (int i = 0; i < _SIZE; i++)
         {
         _flt[i] = new Integer(i).floatValue();
         }
      Forward fwd = new Forward("gotoPg5");
      fwd.addPageInput("Repeater", _flt);
      return fwd;
      }

   /**
    * @jpf:action
    * @jpf:forward name="gotoPg6" path="Jsp6.jsp"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg6",
                path = "Jsp6.jsp") 
        })
   protected Forward action5()
      {
          //System.out.println(">>> Jpf1.action5");
      _hash = new Hashtable(_SIZE);
      for (int i = 0; i < _SIZE; i++)
         {
         _hash.put(new Integer(i), new String(_STRING + Integer.toString(i)));
         }
      Forward fwd = new Forward("gotoPg6");
      fwd.addPageInput("Repeater", _hash);
      return fwd;
      }

   /**
    * @jpf:action
    * @jpf:forward name="gotoPg7" path="Jsp7.jsp"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg7",
                path = "Jsp7.jsp") 
        })
   protected Forward action6()
      {
          //System.out.println(">>> Jpf1.action6");
      _vec = new Vector(_SIZE);
      for (int i = 0; i < _SIZE; i++)
         {
         _vec.add(new String(_STRING + Integer.toString(i)));
         }
      Forward fwd = new Forward("gotoPg7");
      fwd.addPageInput("Repeater", _hash);
      return fwd;
      }

   /**
    * @jpf:action
    * @jpf:forward name="gotoPg8" path="Jsp8.jsp"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg8",
                path = "Jsp8.jsp") 
        })
   protected Forward action7()
      {
          //System.out.println(">>> Jpf1.action7");
      _float = new Float[_SIZE];
      for (int i = 0; i < _SIZE; i++)
         {
         _float[i] = new Float(Integer.toString(i));
         }
      Forward fwd = new Forward("gotoPg8");
      fwd.addPageInput("Repeater", _float);
      return fwd;
      }

   /**
    * @jpf:action
    * @jpf:forward name="gotoPg9" path="Jsp9.jsp"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg9",
                path = "Jsp9.jsp") 
        })
   protected Forward action8()
      {
          //System.out.println(">>> Jpf1.action8");
      _integer = new Integer[_SIZE];
      for (int i = 0; i < _SIZE; i++)
         {
         _integer[i] = new Integer(i);
         }
      Forward fwd = new Forward("gotoPg9");
      fwd.addPageInput("Repeater", _integer);
      return fwd;
      }

   /**
    * @jpf:action
    * @jpf:forward name="gotoPg10" path="Jsp10.jsp"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg10",
                path = "Jsp10.jsp") 
        })
   protected Forward action9()
      {
          //System.out.println(">>> Jpf1.action9");
      _boolean = new Boolean[_SIZE];
      boolean  x = true;
      for (int i = 0; i < _SIZE; i++)
         {
         _boolean[i] = new Boolean(x);
         if (x == true) { x = false; } else { x = true; }
         }
      Forward fwd = new Forward("gotoPg10");
      fwd.addPageInput("Repeater", _boolean);
      return fwd;
      }

   /**
    * @jpf:action
    * @jpf:forward name="gotoPg11" path="Jsp11.jsp"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg11",
                path = "Jsp11.jsp") 
        })
   protected Forward action10()
      {
          //System.out.println(">>> Jpf1.action10");
      _strBuf = new StringBuffer[_SIZE];
      boolean  x = true;
      for (int i = 0; i < _SIZE; i++)
         {
         _strBuf[i] = new StringBuffer(_STRING + Integer.toString(i));
         }
      Forward fwd = new Forward("gotoPg11");
      fwd.addPageInput("Repeater", _strBuf);
      return fwd;
      }

   /**
    * @jpf:action
    * @jpf:forward name="gotoPg12" path="Jsp12.jsp"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg12",
                path = "Jsp12.jsp") 
        })
   protected Forward action11()
      {
          //System.out.println(">>> Jpf1.action11");
      _strMulti = new String[_SIZE][_SIZE];
      for (int i = 0; i < _SIZE; i++)
         {
         for (int j = 0; j < _SIZE; j++)
            {
            _strMulti[i][j] = _STRING + Integer.toString(i);
            }
         }
      Forward fwd = new Forward("gotoPg12");
      fwd.addPageInput("Repeater", _strMulti);
      return fwd;
      }

   /**
    * @jpf:action
    */
    @Jpf.Action(
        )
   protected Forward finish()
      {
          //System.out.println(">>> Jpf1.finish");
      return new Forward("gotoDone");
      }
   }
