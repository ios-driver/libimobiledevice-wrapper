package org.libimobiledevice.ios.driver.test;

import org.libimobiledevice.ios.driver.binding.exceptions.SDKException;
import org.libimobiledevice.ios.driver.binding.raw.JNAInit;
import org.libimobiledevice.ios.driver.binding.services.DebugService;
import org.libimobiledevice.ios.driver.binding.services.DeviceService;
import org.libimobiledevice.ios.driver.binding.services.IOSDevice;
import org.libimobiledevice.ios.driver.binding.services.InstallCallback;
import org.libimobiledevice.ios.driver.binding.services.InstallerService;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

import static org.libimobiledevice.ios.driver.test.ConnectedDevices.main;

public class DebugTest {


  @Test(groups = "smoke")
  public void deviceCanDebugLaunch() throws InterruptedException,
                                            SDKException {

    DebugService service = new DebugService(DeviceService.get(main));
    int pid = service.launch("com.yourcompany.UICatalog");
    System.out.println(pid);

    service.free();
  }

  @Test(groups = "smoke")
  public void openURL() throws InterruptedException,
                               SDKException {

    IOSDevice d = DeviceService.get(main);
    InstallerService installer = new InstallerService(d);
    String appId = "ios-driver.ios-driver";
    try {
      installer.getApplication("ios-driver.ios-driver");
    } catch (SDKException e) {
      final InstallCallback cb = new InstallCallback() {
        @Override
        protected void onUpdate(String operation, int percent, String message) {
          sout(operation, percent, message);
        }
      };
      installer.install(appId, new File("/Users/freynaud/ios-driver.ipa"), cb);
    }

    DebugService service = new DebugService(DeviceService.get(main));
    int pid = service.launch("ios-driver.ios-driver");
    System.out.println("pid = " + pid);
    service.free();
  }

  @Test(groups = "smoke")
  public void startSafari() throws SDKException, IOException, InterruptedException {
    JNAInit.init();
    DebugService service = new DebugService(DeviceService.get(main));
    service.startSafari();

    System.out.println("done");
    service.stopSafari();

  }

}
