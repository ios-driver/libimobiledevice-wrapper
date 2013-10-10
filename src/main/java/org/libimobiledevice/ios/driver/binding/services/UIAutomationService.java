package org.libimobiledevice.ios.driver.binding.services;

import com.sun.jna.ptr.PointerByReference;

import org.libimobiledevice.ios.driver.binding.exceptions.InstrumentsSDKException;

public class UIAutomationService {

  private static final long POLL_TIME = 500;
  private final InstrumentsClient instrumentsClient;
  private final AutomationClient automationClient;
  private final ProcessControlService processControlService;
  private final UIAScriptMessageHandler handler;
  private final UIAExceptionHandler exHandler;
  private String bundleId;
  private int pid = -1;

  UIAutomationService(InstrumentsClient instrumentsClient, ProcessControlService pcs)
      throws InstrumentsSDKException {
    this.instrumentsClient = instrumentsClient;
    this.automationClient = instrumentsClient.getAutomationClient();
    this.processControlService = pcs;

    handler = new UIAScriptMessageHandler() {
      @Override
      public void onMessage(String message) {
        System.out.println(message);
      }
    };

    exHandler = new

        UIAExceptionHandler() {
          @Override
          public void onException(String message) {
            System.err.println(message);
          }
        };
    automationClient.setMessageHandler(handler);
    automationClient.setExceptionHandler(exHandler);

  }

  public final void startApp(String bundleId) throws InstrumentsSDKException {
    startApp(bundleId, true);
  }

  public final void startApp(String bundleId, boolean waitForAgent) throws InstrumentsSDKException {
    int pid = processControlService.getPid(bundleId);
    processControlService.kill(pid);

    automationClient.configureLaunchEnvironment(new PointerByReference());

    pid = processControlService.launch(bundleId);

    automationClient.startAgent(pid, bundleId);

    while (waitForAgent && !automationClient.isReady()) {
      InstrumentsService.sleepWell(POLL_TIME);
    }
    this.bundleId = bundleId;
    this.pid = pid;
  }

  public final void stopApp() throws InstrumentsSDKException {
    if (pid > 0) {
      processControlService.releaseProcess(pid);
    }
  }

  /**
   * executes a script on the device. Doesn't wait for the script to return, and will crash if you
   * send a script before the previous one has returned.
   */
  public final void executeScriptNonManaged(String script) throws InstrumentsSDKException {
    String safe = "try{ " + script + ""
                  + "} catch (err){"
                  + "UIALogger.logMessage('there was an error.'+err.message);"
                  //+ "throw err;"
                  + "}";
    automationClient.executeScript(safe, bundleId);
  }

}
