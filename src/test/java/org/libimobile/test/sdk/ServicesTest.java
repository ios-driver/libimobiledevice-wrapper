package org.libimobile.test.sdk;

import org.libimobiledevice.binding.raw.exceptions.LibImobileException;
import org.libimobiledevice.ios.driver.sdk.IDeviceSDK;
import org.libimobiledevice.ios.driver.sdk.InformationService;
import org.testng.annotations.Test;

import static org.libimobile.test.ConnectedDevices.main;

public class ServicesTest {


  @Test
  public void infoServiceFree() throws LibImobileException {
    IDeviceSDK d = new IDeviceSDK(main);
    InformationService service = new InformationService(d);
    service.release();

    d = new IDeviceSDK(main);
    service = new InformationService(d);
    service.release();
  }
}
