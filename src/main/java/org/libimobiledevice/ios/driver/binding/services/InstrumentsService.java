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

import com.sun.jna.ptr.PointerByReference;

import org.libimobiledevice.ios.driver.binding.exceptions.InstrumentsSDKException;

public class InstrumentsService {


  private final InstrumentsClient instrumentsClient;
  private final AutomationClient automationClient;
  private final ProcessControlClient processControlClient;
  private final UIAScriptMessageHandler handler;
  private final UIAExceptionHandler exHandler;
  private final String bundleId;
  private int pid;

  public InstrumentsService(IOSDevice device, String bundleId) throws InstrumentsSDKException {
    this.bundleId = bundleId;

    handler = new UIAScriptMessageHandler() {
      @Override
      public void onMessage(String message) {
        System.out.println(message);
      }
    };

    exHandler = new UIAExceptionHandler() {
      @Override
      public void onException(String message) {
        System.err.println(message);
      }
    };

    instrumentsClient = new InstrumentsClient(device);
    automationClient = instrumentsClient.getAutomationClient();
    automationClient.setMessageHandler(handler);
    automationClient.setExceptionHandler(exHandler);

    processControlClient = instrumentsClient.getProcessControlClient();

    pid = processControlClient.getPid(bundleId);
    if (pid > 0) {
      processControlClient.kill(pid);
    }

    automationClient.configureLaunchEnvironment(new PointerByReference());

    pid = processControlClient.launchSuspendedProcess(bundleId, null, null);
    processControlClient.startObserving(pid);
    processControlClient.resume(pid);

    automationClient.startAgent(pid, bundleId);

    while (!automationClient.isReady()) {
      sleepWell(500);
    }
  }

  private void sleepWell(long ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
  }

  /**
   * executes a script on the device. Doesn't wait for the script to return, and will crash if you
   * send a script before the previous one has returned.
   */
  public void executeScriptNonManaged(String script) throws InstrumentsSDKException {
    String safe = "try{ " + script + ""
                  + "} catch (err){"
                  + "UIALogger.logMessage('there was an error.'+err.message);"
                  //+ "throw err;"
                  + "}";
    automationClient.executeScript(safe, bundleId);
  }

  public void free() throws InstrumentsSDKException {
    processControlClient.stopObserving(pid);
    processControlClient.kill(pid);
    if (instrumentsClient != null) {
      instrumentsClient.free();
    }
  }
}
