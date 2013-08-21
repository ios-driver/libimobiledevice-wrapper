package org.libimobile.test;

import org.libimobiledevice.binding.raw.IMobileDeviceFactory;
import org.libimobiledevice.ios.driver.sdk.IDeviceSDK;
import org.libimobiledevice.ios.driver.sdk.WebInspectorService;
import org.testng.annotations.Test;

import static org.libimobile.test.ConnectedDevices.main;

public class WebInspectorTest {

  private IMobileDeviceFactory factory = IMobileDeviceFactory.INSTANCE;

  @Test(groups = "smoke")
  public void webInspectorClient() throws InterruptedException {

    final IDeviceSDK device = new IDeviceSDK(main);

    final WebInspectorService service = new WebInspectorService(device);
    service.startWebInspector();

    new Thread(new Runnable() {
      @Override
      public void run() {
        while (true) {
          try {
            String r = service.receiveMessage();
            if (r != null) {
              System.out.println("\n\nRESPONSE :\n --" + r+"--");
            }

          } catch (Exception e) {
            System.err.println(e.getMessage());
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

    Thread.sleep(100000);
    service.stopWebInspector();
    System.out.println("STOPPED");
  }


}
