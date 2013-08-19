package org.libimobiledevice.ios.driver.test;

import org.libimobiledevice.ios.driver.binding.IMobileDeviceFactory;
import org.libimobiledevice.ios.driver.binding.exceptions.LibImobileException;
import org.libimobiledevice.ios.driver.binding.sdk.IDeviceSDK;
import org.libimobiledevice.ios.driver.binding.sdk.InformationService;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.libimobiledevice.ios.driver.test.ConnectedDevices.device2;
import static org.libimobiledevice.ios.driver.test.ConnectedDevices.main;

public class IDeviceNewThreadTest {

  private IMobileDeviceFactory factory = IMobileDeviceFactory.INSTANCE;

  @DataProvider(name = "devices", parallel = true)
  public Object[][] createData1() {
    return new Object[][]{
        {main},
        {device2},
    };
  }


  @Test(dataProvider = "devices")
  public void canCreateDevicesInParallel(String uuid) throws LibImobileException {
    IDeviceSDK d=  new IDeviceSDK(uuid);
  }



  @Test(dataProvider = "devices")
  public void deviceCanSetLocale(String uuid) throws LibImobileException {
    IDeviceSDK d;
    synchronized (this) {
      d = new IDeviceSDK(uuid);
    }
    InformationService service = new InformationService(d);
    service.setLanguage("en");
    service.setLanguage("en_US");
    service.setLanguage("fr_CA");
    service.release();
    d.release();
  }


}
