package org.libimobiledevice.ios.driver.binding.sdk;

import com.sun.jna.ptr.PointerByReference;

import org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary;

/**
 * Created with IntelliJ IDEA. User: fviscomi Date: 9/30/13 Time: 3:56 PM To change this template
 * use File | Settings | File Templates.
 */
public class AppContainerService {

  private final ImobiledeviceSdkLibrary.sdk_idevice_app_container_service_t service;


  public AppContainerService(IDeviceSDK d) {
    PointerByReference ptr = new PointerByReference();
    ImobiledeviceSdkLibrary.app_container_service_new(d.getHandle(), ptr);
    service = new ImobiledeviceSdkLibrary.sdk_idevice_app_container_service_t(ptr.getValue());
  }

  public void clean(String bundleId) {
    ImobiledeviceSdkLibrary.app_container_service_empty_application_cache(service, bundleId);

  }

  public void free() {
    ImobiledeviceSdkLibrary.app_container_service_free(service);
  }
}
