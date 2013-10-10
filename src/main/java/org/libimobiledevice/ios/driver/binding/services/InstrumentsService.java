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

import org.libimobiledevice.ios.driver.binding.exceptions.InstrumentsSDKException;

public class InstrumentsService {


  private final InstrumentsClient instrumentsClient;
  private int pid;
  private ProcessControlService processControlService;
  private UIAutomationService uiAutomationService;

  public InstrumentsService(IOSDevice device) throws InstrumentsSDKException {
    instrumentsClient = new InstrumentsClient(device);
  }

  public static void sleepWell(long ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
  }

  public final synchronized ProcessControlService getProcessControlService()
      throws InstrumentsSDKException {
    if (processControlService == null) {
      processControlService = new ProcessControlService(instrumentsClient);
    }
    return processControlService;
  }

  public final synchronized UIAutomationService getUIAutomationService()
      throws InstrumentsSDKException {
    if (uiAutomationService == null) {
      uiAutomationService = new UIAutomationService(instrumentsClient, getProcessControlService());
    }
    return uiAutomationService;
  }



  public void free() throws InstrumentsSDKException {
    if (instrumentsClient != null) {
      instrumentsClient.free();
    }
    if (processControlService != null) {
      processControlService.free();
    }
  }
}
