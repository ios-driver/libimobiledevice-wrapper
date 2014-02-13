package org.libimobiledevice.ios.driver.test;

import org.libimobiledevice.ios.driver.binding.exceptions.LibImobileException;
import org.libimobiledevice.ios.driver.binding.exceptions.SDKException;
import org.libimobiledevice.ios.driver.binding.services.DeviceService;
import org.libimobiledevice.ios.driver.binding.services.IOSDevice;
import org.libimobiledevice.ios.driver.binding.services.WebInspectorService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.libimobiledevice.ios.driver.test.ConnectedDevices.main;

public class WebInspectorTest {


  IOSDevice device;
  WebInspectorService service;


  @BeforeMethod
  public void setup() throws LibImobileException, SDKException {
    device = DeviceService.get(main);
    service = new WebInspectorService(device);
  }

  @Test(groups = "smoke", invocationCount = 5)
  public void webInspectorClient() throws InterruptedException, LibImobileException, SDKException {

    service.startWebInspector();

    new Thread(new Runnable() {
      @Override
      public void run() {
        while (true) {
          try {
            String r = service.receiveMessage();
            if (r != null) {
              System.out.println("\n\nRESPONSE :\n --" + r + "--");
            }

          } catch (Exception e) {
            System.err.println(e.getMessage());
            return;
          }

        }
      }
    }).start();

    service.sendMessage("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n" +
                        "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">"
                        + "\n" +
                        "<plist version=\"1.0\">" + "\n" +
                        " <dict>" + "\n" +
                        " <key>__argument</key>" + "\n" +
                        " <dict>" + "\n" +
                        " <key>WIRConnectionIdentifierKey</key>" + "\n" +
                        " <string>9128c1d9-069d-4751-b45b-bbcb6e7e8591</string>" + "\n" +
                        " </dict>" + "\n" +
                        " <key>__selector</key>" + "\n" +
                        " <string>_rpc_reportIdentifier:</string>" + "\n" +
                        " </dict>" + "\n" +
                        "</plist>" + "\n");

    Thread.sleep(3000);
    service.stopWebInspector();

  }


}
