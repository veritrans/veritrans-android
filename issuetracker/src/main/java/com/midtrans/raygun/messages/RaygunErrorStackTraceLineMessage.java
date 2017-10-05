package com.midtrans.raygun.messages;

public class RaygunErrorStackTraceLineMessage {
  private int lineNumber;
  private String className;
  private String fileName;
  private String methodName;

  public RaygunErrorStackTraceLineMessage(StackTraceElement element) {
    lineNumber = element.getLineNumber();
    className = element.getClassName();
    fileName = element.getFileName();
    methodName = element.getMethodName();
  }

  public int getLineNumber() {
    return lineNumber;
  }

  public void setLineNumber(int lineNumber) {
    this.lineNumber = lineNumber;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getMethodName() {
    return methodName;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }
}
