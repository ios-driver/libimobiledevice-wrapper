package org.libimobiledevice.ios.driver.test;

import org.libimobiledevice.ios.driver.binding.LibImobileDeviceWrapperFactory;
import org.libimobiledevice.ios.driver.binding.exceptions.LibImobileException;
import org.libimobiledevice.ios.driver.binding.exceptions.SDKException;
import org.libimobiledevice.ios.driver.binding.services.DeviceService;
import org.libimobiledevice.ios.driver.binding.services.IOSDevice;
import org.libimobiledevice.ios.driver.binding.services.InformationService;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.libimobiledevice.ios.driver.test.ConnectedDevices.device2;
import static org.libimobiledevice.ios.driver.test.ConnectedDevices.main;

public class IDeviceNewThreadTest {

  private LibImobileDeviceWrapperFactory factory = LibImobileDeviceWrapperFactory.INSTANCE;

  @DataProvider(name = "devices", parallel = true)
  public Object[][] createData1() {
    return new Object[][]{
        {main},
        {device2},
    };
  }

  @Test(dataProvider = "devices")
  public void canCreateDevicesInParallel(String uuid) throws LibImobileException, SDKException {
    IOSDevice device = DeviceService.get(uuid);
  }

  @Test(dataProvider = "devices")
  public void deviceCanSetLocale(String uuid) throws LibImobileException, SDKException {
    InformationService service = new InformationService(DeviceService.get(uuid));
    service.setLanguage("en");
    service.setLanguage("en_US");
    service.setLanguage("fr_CA");
    service.free();

    DeviceService.free();
  }


}
