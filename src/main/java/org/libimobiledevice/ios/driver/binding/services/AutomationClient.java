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

import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceInstrumentsLibrary.uiautomation_client_configure_launch_environment;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceInstrumentsLibrary.uiautomation_client_free;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceInstrumentsLibrary.uiautomation_client_get_agent_ready;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceInstrumentsLibrary.uiautomation_client_new;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceInstrumentsLibrary.uiautomation_client_set_exception_handler;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceInstrumentsLibrary.uiautomation_client_set_message_handler;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceInstrumentsLibrary.uiautomation_client_start_agent_for_app_with_pid;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceInstrumentsLibrary.uiautomation_client_start_script_with_info;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceInstrumentsLibrary.uiautomation_client_t;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceInstrumentsLibrary.uiautomation_exception_handler_t;
import static org.libimobiledevice.ios.driver.binding.exceptions.InstrumentsErrorCode.throwIfNeeded;

class AutomationClient {

  private final uiautomation_client_t automation_client;
  private final InstrumentsClient instrumentsClient;
  private boolean allocated = false;

  AutomationClient(InstrumentsClient instrumentsClient) throws InstrumentsSDKException {
    if (instrumentsClient == null) {
      throw new InstrumentsSDKException(
          "Need a valid instruments client to create an automation client.");
    }
    this.instrumentsClient = instrumentsClient;
    automation_client = startService();
    allocated = true;
  }

  private uiautomation_client_t startService() throws InstrumentsSDKException {
    if (automation_client != null) {
      throw new InstrumentsSDKException("The service has already been started.");
    }
    PointerByReference ptr = new PointerByReference();
    throwIfNeeded(uiautomation_client_new(instrumentsClient.getCHandle(), ptr));
    return new uiautomation_client_t(ptr.getValue());
  }

  public void setMessageHandler(UIAScriptMessageHandler handler) throws InstrumentsSDKException {
    throwIfNeeded(uiautomation_client_set_message_handler(automation_client, handler));
  }

  public void setExceptionHandler(uiautomation_exception_handler_t exceptionHandler)
      throws InstrumentsSDKException {
    throwIfNeeded(uiautomation_client_set_exception_handler(automation_client, exceptionHandler));
  }

  public void configureLaunchEnvironment(PointerByReference ptr) throws InstrumentsSDKException {
    throwIfNeeded(uiautomation_client_configure_launch_environment(automation_client,
                                                                   new PointerByReference()));
  }

  public void startAgent(int pid, String bundleId) throws InstrumentsSDKException {
    throwIfNeeded(
        uiautomation_client_start_agent_for_app_with_pid(automation_client, bundleId, pid));
  }

  public boolean isReady() throws InstrumentsSDKException {
    return 1 == uiautomation_client_get_agent_ready(automation_client);
  }

  public void executeScript(String script, String bundleId) throws InstrumentsSDKException {
    throwIfNeeded(uiautomation_client_start_script_with_info(automation_client, bundleId, script,"ios-driver"));
  }

  public void free() {
    if (allocated) {
      uiautomation_client_free(automation_client);
      allocated = false;
    }

  }
}
