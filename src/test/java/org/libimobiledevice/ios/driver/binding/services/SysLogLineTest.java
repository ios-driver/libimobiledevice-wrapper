/*
 * Copyright 2012-2013 eBay Software Foundation and ios-driver committers
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.libimobiledevice.ios.driver.binding.services;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Calendar;

public class SysLogLineTest {


  private String[] lines = new String[]{
  /*0*/    "Oct  5 00:31:43  wifid[14] <Error>: WiFi:[402618703.480754]: WiFi unquiescing requested by \"locationd\"",
  /*1*/    "Oct  5 00:32:12  lockdownd[44] <Notice>: 00281000 set_language: Prepending the language list with en",
  /*2*/    "Oct  5 00:32:12  SpringBoard[3634] <Warning>: Preferred language has changed from 'de' to 'en'. Relaunching.",
  /*3*/    "Oct  5 00:32:12  com.apple.launchd[1] <Error>: (com.apple.mediaserverd) Exited with code: 254",
  /*4*/    "Oct  5 00:32:12  com.apple.launchd[1] <Error>: (com.apple.itunesstored) Exited with code: 253",
  /*5*/    "Oct  5 00:32:12  CommCenterMobileHelper[3654] <Error>: VM_ALLOCATE 920l (mhs1)",
  /*6*/    "Oct  5 00:32:12  tccd[3640] <Notice>: Exiting due to language change",
  /*7*/    "Oct  5 00:32:12  mediaserverd[3652] <Error>: libMobileGestalt copySystemVersionDictionaryValue: Could not lookup ReleaseType from system version dictionary",
  /*8*/    "Oct  5 00:32:12  locationd[45] <Warn",
  /*9*/    "Warning>: Launch Services: Unable to find app identifier com.apple.AssistantServices" };

  @Test
  public void canExtractDate() throws SysLogLine.LogParsingException {
    //Oct  5 00:31:43
    SysLogLine line = new SysLogLine(lines[0]);

    Calendar c = Calendar.getInstance();
    c.setTime(line.getDate());

    Assert.assertEquals(c.get(Calendar.MONTH), Calendar.OCTOBER);
    Assert.assertEquals(c.get(Calendar.DAY_OF_MONTH), 5);
    Assert.assertEquals(c.get(Calendar.HOUR_OF_DAY), 0);
    Assert.assertEquals(c.get(Calendar.MINUTE), 31);
    Assert.assertEquals(c.get(Calendar.SECOND), 43);

  }

  @Test
  public void assumesCurrentYear() throws SysLogLine.LogParsingException {
    SysLogLine line = new SysLogLine(lines[0]);

    Calendar c = Calendar.getInstance();
    c.setTime(line.getDate());

    Calendar now = Calendar.getInstance();
    Assert.assertEquals(c.get(Calendar.YEAR), now.get(Calendar.YEAR));

  }

  @Test
  public void canExtractProcessAndPid() throws SysLogLine.LogParsingException {
    SysLogLine wifi = new SysLogLine(lines[0]);
    SysLogLine lockdownd = new SysLogLine(lines[1]);
    SysLogLine springBoard = new SysLogLine(lines[2]);
    SysLogLine launchd = new SysLogLine(lines[3]);

    Assert.assertEquals(wifi.getProcess(), "wifid");
    Assert.assertEquals(lockdownd.getProcess(), "lockdownd");
    Assert.assertEquals(springBoard.getProcess(), "SpringBoard");
    Assert.assertEquals(launchd.getProcess(), "com.apple.launchd");

    Assert.assertEquals(wifi.getPid(), 14);
    Assert.assertEquals(lockdownd.getPid(), 44);
    Assert.assertEquals(springBoard.getPid(), 3634);
    Assert.assertEquals(launchd.getPid(), 1);

  }

  @Test
  public void canExtractLevel() throws SysLogLine.LogParsingException {
    SysLogLine wifi = new SysLogLine(lines[0]);
    SysLogLine lockdownd = new SysLogLine(lines[1]);
    SysLogLine springBoard = new SysLogLine(lines[2]);
    SysLogLine launchd = new SysLogLine(lines[3]);

    Assert.assertEquals(wifi.getLevel(), "Error");
    Assert.assertEquals(lockdownd.getLevel(), "Notice");
    Assert.assertEquals(springBoard.getLevel(), "Warning");
    Assert.assertEquals(launchd.getLevel(), "Error");

  }

  @Test
  public void canExtractMessage() throws SysLogLine.LogParsingException {
    SysLogLine wifi = new SysLogLine(lines[0]);
    SysLogLine lockdownd = new SysLogLine(lines[1]);
    SysLogLine springBoard = new SysLogLine(lines[2]);
    SysLogLine launchd = new SysLogLine(lines[3]);

    Assert.assertEquals(wifi.getMessage(), "WiFi:[402618703.480754]: WiFi unquiescing requested by \"locationd\"");
    Assert.assertEquals(lockdownd.getMessage(), "00281000 set_language: Prepending the language list with en");
    Assert.assertEquals(springBoard.getMessage(), "Preferred language has changed from 'de' to 'en'. Relaunching.");
    Assert.assertEquals(launchd.getMessage(), "(com.apple.mediaserverd) Exited with code: 254");

  }

  @Test
  public void survivesPartialLines() throws SysLogLine.LogParsingException {
    SysLogLine startMissing = new SysLogLine(lines[8]);
    SysLogLine endMissing = new SysLogLine(lines[9]);


  }
}
