package org.libimobiledevice.ios.driver.sdk.test;

import org.libimobiledevice.ios.driver.binding.exceptions.LibImobileException;
import org.libimobiledevice.ios.driver.binding.wrapper.IDeviceSDK;
import org.libimobiledevice.ios.driver.binding.services.InformationService;
import org.testng.annotations.Test;

import static org.libimobiledevice.ios.driver.test.ConnectedDevices.main;

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
