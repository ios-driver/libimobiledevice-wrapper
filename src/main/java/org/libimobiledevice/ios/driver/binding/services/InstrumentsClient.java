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

import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceInstrumentsLibrary.instruments_client_free;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceInstrumentsLibrary.instruments_client_start_service;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceInstrumentsLibrary.instruments_client_t;
import static org.libimobiledevice.ios.driver.binding.exceptions.InstrumentsErrorCode.throwIfNeeded;

class InstrumentsClient {

  private final IOSDevice device;
  private final instruments_client_t instruments_client;
  private final int RETRY = 2;
  private AutomationClient automationClient;
  private ProcessControlClient processControlClient;
  private boolean allocated = false;

  public InstrumentsClient(IOSDevice device) throws InstrumentsSDKException {
    this.device = device;
    instruments_client = startServiceWithRetry(RETRY);
    allocated = true;
  }

  private instruments_client_t startServiceWithRetry(int retry)
      throws InstrumentsSDKException {
    int retryCount = 0;
    instruments_client_t client;
    while (retryCount < RETRY) {
      try {
        client = startService();
        return client;
      } catch (InstrumentsSDKException e) {
        retryCount++;
      }
    }

    throw new InstrumentsSDKException(
        "Failed to start the instrumens service after " + retry + " retries.");
  }

  instruments_client_t getCHandle() {
    return instruments_client;
  }

  private instruments_client_t startService()
      throws InstrumentsSDKException {
    if (instruments_client != null) {
      throw new InstrumentsSDKException("The service has already been started.");
    }
    PointerByReference ptr = new PointerByReference();
    throwIfNeeded(instruments_client_start_service(device.getHandle(), ptr, "libimobile-java"));
    return new instruments_client_t(ptr.getValue());
  }

  public synchronized AutomationClient getAutomationClient() throws InstrumentsSDKException {
    if (allocated = false) {
      throw new InstrumentsSDKException("Instruments has been freed");
    }
    if (automationClient == null) {
      automationClient = new AutomationClient(this);
    }
    return automationClient;
  }

  public synchronized ProcessControlClient getProcessControlClient()
      throws InstrumentsSDKException {
    if (!allocated) {
      throw new InstrumentsSDKException("Instruments has been freed");
    }
    if (processControlClient == null) {
      processControlClient = new ProcessControlClient(this);
    }
    return processControlClient;
  }

  public void free() {
    if (allocated) {
      instruments_client_free(instruments_client);
      if (processControlClient != null) {
        processControlClient.free();
        processControlClient = null;
      }
      if (automationClient != null) {
        automationClient.free();
        automationClient = null;
      }
      allocated = false;
    }
  }
}
