package org.libimobile.test;

import org.libimobiledevice.binding.raw.IMobileDeviceFactory;
import org.libimobiledevice.binding.raw.exceptions.LibImobileException;
import org.libimobiledevice.ios.driver.sdk.IDeviceSDK;
import org.libimobiledevice.ios.driver.sdk.InformationService;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.libimobile.test.ConnectedDevices.device2;
import static org.libimobile.test.ConnectedDevices.main;

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
