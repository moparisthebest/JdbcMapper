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
package bugs.b40862;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.test.databinding.beans.b40682.Company;
import org.apache.beehive.netui.test.databinding.beans.b40682.Report;

// external imports

/**
 *
 */
@Jpf.Controller
public class Controller
    extends PageFlowController
{
    private Report[] reports = null;

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "index",
                path = "index.jsp") 
        })
    public Forward begin()
    {
        initReports();

        return new Forward("index", "reports", reports);
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "index",
                path = "index.jsp") 
        })
    public Forward editCompanies()
    {
        return new Forward("index");
    }

    private void initReports()
    {
        reports = new Report[4];

        reports[0] = new Report();
        reports[0].setNumber(0);
        reports[0].setCompanies(new Company[] {new Company("RHAT", "Up"), new Company("SUNW", "Down")});
        reports[0].setUrl("http://nyc-db-07.jpmorganchase.com/IBGCMWEB.NSF/GCMRCDSSpreadWidenersNACurrent?ReadForm");
        reports[0].setText("CDS Spread Wideners Report - NA");

        reports[1] = new Report();
        reports[1].setNumber(1);
        reports[1].setCompanies(null);
        reports[1].setUrl("http://nyc-db-07.jpmorganchase.com/IBGCMWEB.NSF/GCMRCDSSpreadWidenersEMEACurrent?ReadForm");
        reports[1].setText("CDS Spread Wideners Report - EMEA");

        reports[2] = new Report();
        reports[2].setNumber(2);
        reports[2].setCompanies(null);
        reports[2].setUrl("http://nyc-db-07.jpmorganchase.com/IBGCMWEB.NSF/GCMRCDSSpreadWidenersEMEACurrent?ReadForm");
        reports[2].setText("CDS Spread Wideners Report - BARFOO");

        reports[3] = new Report();
        reports[3].setNumber(3);
        reports[3].setCompanies(null);
        reports[3].setUrl("http://nyc-db-07.jpmorganchase.com/IBGCMWEB.NSF/GCMRCDSSpreadWidenersEMEACurrent?ReadForm");
        reports[3].setText("CDS Spread Wideners Report - FOOBAR");
    }
}
