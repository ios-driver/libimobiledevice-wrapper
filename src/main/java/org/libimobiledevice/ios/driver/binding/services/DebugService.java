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

import org.libimobiledevice.ios.driver.binding.exceptions.SDKException;
import org.libimobiledevice.ios.driver.binding.raw.JNAInit;

import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.libimobiledevice.ios.driver.binding.exceptions.SDKErrorCode.throwIfNeeded;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.debug_service_free;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.debug_service_launch_application_by_bundle_identifier;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.debug_service_new;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.sdk_idevice_debug_service_t;

public class DebugService {

  private final Lock lock = new ReentrantLock();
  private final Condition c = lock.newCondition();
  private final sdk_idevice_debug_service_t service;
  private final String udid;
  private Process safari;
  private final IOSDevice device;

  public DebugService(IOSDevice d) throws SDKException {
    udid = d.getUUID();
    device = d;
    PointerByReference ptr = new PointerByReference();
    throwIfNeeded(debug_service_new(d.getSDKHandle(), ptr));
    service = new sdk_idevice_debug_service_t(ptr.getValue());
  }

  public void free() throws SDKException {
    throwIfNeeded(debug_service_free(service));
  }

  public int launch(String bundleId) throws SDKException {
    PointerByReference envt = new PointerByReference();
    PointerByReference args = new PointerByReference();
    IntBuffer pidptr = IntBuffer.allocate(1);
    int res =
        debug_service_launch_application_by_bundle_identifier(service, bundleId, envt, args,
                                                              pidptr);
    int pid = pidptr.get(0);
    return pid;
  }


  public void startSafari() throws IOException, SDKException {
    File exe = new File(JNAInit.getTemporaryJNAFolder(), "idevicedebug");

    String
        cmd =
        exe.getAbsolutePath() + " --udid " + udid + " run com.apple.mobilesafari  -u about:blank";
    String[] args = cmd.split(" ");
    ProcessBuilder builder = new ProcessBuilder(args);
    Map<String, String> env = builder.environment();
    env.put("LD_LIBRARY_PATH", JNAInit.getTemporaryJNAFolder().getAbsolutePath());
    builder.directory(JNAInit.getTemporaryJNAFolder());
    safari = builder.start();

    device.getSysLogService().addListener(new SysLogListener() {
      @Override
      public void onLog(SysLogLine line) {
        if (line != null && line.getMessage().contains("Start debugging com.apple.mobilesafari ")) {
          try {
            lock.lock();
            c.signal();
          } finally {
            lock.unlock();
          }
        }
      }
    });

    try {
      lock.lock();
      c.await(5, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      throw new SDKException("Cannot start safari");
    } finally {
      lock.unlock();
    }
  }

  public void stopSafari() {
    if (safari == null) {
      return;
    }
    safari.destroy();
  }


}


