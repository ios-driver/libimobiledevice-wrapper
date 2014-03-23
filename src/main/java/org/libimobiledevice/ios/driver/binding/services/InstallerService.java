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

import org.libimobiledevice.ios.driver.binding.model.ApplicationInfo;
import org.libimobiledevice.ios.driver.binding.exceptions.LibImobileException;
import org.libimobiledevice.ios.driver.binding.exceptions.SDKException;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.libimobiledevice.ios.driver.binding.exceptions.SDKErrorCode.throwIfNeeded;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.installation_service_free;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.installation_service_get_application_list;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.installation_service_get_application_list_as_xml;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.installation_service_install_application_from_archive_with_callback;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.installation_service_new;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.installation_service_uninstall_application;
import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.sdk_idevice_installation_service_t;

public class InstallerService {

  private static final Object lock = new Object();
  private final sdk_idevice_installation_service_t service;
  private final IOSDevice device;


  public static enum ApplicationType {
    SYSTEM(2), USER(1), ALL(0);
    private final int code;

    ApplicationType(int code) {
      this.code = code;
    }

    public int code() {
      return code;
    }
  }

  public InstallerService(IOSDevice d) throws SDKException {
    synchronized (lock) {
      PointerByReference ptr = new PointerByReference();
      this.device = d;
      throwIfNeeded(installation_service_new(d.getSDKHandle(), ptr));
      service = new sdk_idevice_installation_service_t(ptr.getValue());
    }
  }

  public List<ApplicationInfo> listApplications(ApplicationType type) throws SDKException {
    PointerByReference ptr = new PointerByReference();
    throwIfNeeded(installation_service_get_application_list_as_xml(service, type.code(), ptr));
    String all = ptr.getValue().getString(0);
    return parse(all);
  }


  public ApplicationInfo getApplication(String bundleId) throws SDKException {
    List<ApplicationInfo> all = listApplications(ApplicationType.ALL);
    for (ApplicationInfo app : all){
      if (bundleId.equals(app.getApplicationId())){
        return app;
      }
    }
    throw new SDKException("Cannot find "+bundleId);
  }

  private List<ApplicationInfo> parse(String raw) {
    List<ApplicationInfo> infos = ApplicationInfo.extractApplications(raw);
    return infos;
  }

  public void install(final File ipa, final InstallCallback cb) throws SDKException {
    if (!ipa.exists()) {
      throw new SDKException("the ipa file " + ipa + " doesn't exist.");
    }
    InformationService info = new InformationService(device);
    final Date now = info.getDate();

    ExecutorService es = Executors.newFixedThreadPool(1);
    final Future<Integer> future = es.submit(new Callable<Integer>() {
      @Override
      public Integer call() throws Exception {
        short res = installation_service_install_application_from_archive_with_callback(service,
                                                                                        ipa.getAbsolutePath(),
                                                                                        cb,
                                                                                        null);
        return new Integer(res);
      }
    });

    SysLogListener checkSyslogForCompletion = new SysLogListener() {
      @Override
      public void onLog(SysLogLine line) {
        if (line.getMessage()
            .equals("LaunchServices: Adding com.ebay.iphone to registration list")) {
          if (line.getDate().after(now)) {
            cb.onUpdate("Done from syslog", 100, "Done");
            future.cancel(true);
          }
        }
      }
    };

    device.getSysLogService().addListener(checkSyslogForCompletion);

    try {
      int res = future.get();
      throwIfNeeded(res);
    } catch (InterruptedException e) {
      // ignore
    } catch (ExecutionException e) {
      // ignore
    } finally {
      device.getSysLogService().remove(checkSyslogForCompletion);
    }

  }

  public void uninstall(String bundleId) throws SDKException {
    throwIfNeeded(installation_service_uninstall_application(service, bundleId));
  }

  public void free() throws SDKException {
    throwIfNeeded(installation_service_free(service));
  }

}
