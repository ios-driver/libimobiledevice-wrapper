package org.libimobiledevice.ios.driver.binding.sdk;

import com.sun.jna.ptr.PointerByReference;

import org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary;

import java.nio.IntBuffer;

public class DebugService {

  private final ImobiledeviceSdkLibrary.sdk_idevice_debug_service_t service;

  public DebugService(IDeviceSDK d) {
    PointerByReference ptr = new PointerByReference();
    ImobiledeviceSdkLibrary.debug_service_new(d.getHandle(), ptr);
    service = new ImobiledeviceSdkLibrary.sdk_idevice_debug_service_t(ptr.getValue());
  }

  public void free() {
    ImobiledeviceSdkLibrary.debug_service_free(service);
  }

  public void launch(String bundleId) {
    PointerByReference envt = new PointerByReference();
    PointerByReference args = new PointerByReference();
    IntBuffer pidptr = IntBuffer.allocate(1);
    ImobiledeviceSdkLibrary
        .debug_service_launch_application_by_bundle_identifier(service, bundleId, envt, args,
                                                               pidptr);

  }
}


