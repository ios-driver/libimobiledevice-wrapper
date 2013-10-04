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

import java.nio.IntBuffer;

import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceInstrumentsLibrary.processcontrol_client_free;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceInstrumentsLibrary.processcontrol_client_kill_pid;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceInstrumentsLibrary.processcontrol_client_launch_suspended_process_with_bundle_identifier;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceInstrumentsLibrary.processcontrol_client_new;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceInstrumentsLibrary.processcontrol_client_process_identifier_for_bundle_identifier;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceInstrumentsLibrary.processcontrol_client_resume_pid;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceInstrumentsLibrary.processcontrol_client_start_observing_pid;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceInstrumentsLibrary.processcontrol_client_stop_observing_pid;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceInstrumentsLibrary.processcontrol_client_t;
import static org.libimobiledevice.ios.driver.binding.exceptions.InstrumentsErrorCode.throwIfNeeded;

class ProcessControlClient {

  private final processcontrol_client_t process_control_client;
  private final InstrumentsClient instrumentsClient;
  private boolean allocated = false;

  ProcessControlClient(InstrumentsClient instrumentsClient) throws InstrumentsSDKException {
    if (instrumentsClient == null) {
      throw new InstrumentsSDKException(
          "Need a valid instruments client to create an automation client.");
    }
    this.instrumentsClient = instrumentsClient;
    process_control_client = startService();
    allocated = true;
  }

  private processcontrol_client_t startService() throws InstrumentsSDKException {
    if (process_control_client != null) {
      throw new InstrumentsSDKException("The service has already been started.");
    }
    PointerByReference ptr = new PointerByReference();
    throwIfNeeded(processcontrol_client_new(instrumentsClient.getCHandle(), ptr));
    return new processcontrol_client_t(ptr.getValue());
  }

  public int getPid(String bundleId) throws InstrumentsSDKException {
    IntBuffer pid_ptr = IntBuffer.allocate(1);
    throwIfNeeded(
        processcontrol_client_process_identifier_for_bundle_identifier(process_control_client,
                                                                       bundleId,
                                                                       pid_ptr));
    return pid_ptr.get(0);
  }

  public void kill(int pid) throws InstrumentsSDKException {
    checkPid(pid);
    throwIfNeeded(processcontrol_client_kill_pid(process_control_client, pid));

  }

  public int launchSuspendedProcess(String bundleId, PointerByReference environment,
                                    PointerByReference arguments) throws InstrumentsSDKException {
    if (environment == null) {
      environment = new PointerByReference();
    }
    if (arguments == null) {
      arguments = new PointerByReference();
    }
    IntBuffer ptr = IntBuffer.allocate(1);
    throwIfNeeded(processcontrol_client_launch_suspended_process_with_bundle_identifier(
        process_control_client, bundleId, environment, arguments, ptr));
    return ptr.get(0);
  }

  public void startObserving(int pid) throws InstrumentsSDKException {
    checkPid(pid);
    throwIfNeeded(processcontrol_client_start_observing_pid(process_control_client, pid));
  }

  public void stopObserving(int pid) throws InstrumentsSDKException {
    checkPid(pid);
    throwIfNeeded(processcontrol_client_stop_observing_pid(process_control_client, pid));
  }

  private void checkPid(int pid) throws InstrumentsSDKException {
    if (pid <= 0) {
      throw new InstrumentsSDKException("pid must be > 0. " + pid + " isn't.");
    }
  }

  public void resume(int pid) throws InstrumentsSDKException {
    checkPid(pid);
    throwIfNeeded(processcontrol_client_resume_pid(process_control_client, pid));
  }

  public void free() {
    if (allocated) {
      processcontrol_client_free(process_control_client);
      allocated = false;
    }
  }
}
