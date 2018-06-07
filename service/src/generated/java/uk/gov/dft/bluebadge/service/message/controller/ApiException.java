package uk.gov.dft.bluebadge.service.message.controller;

public class ApiException extends Exception {
  private int code;

  public ApiException(int code, String msg) {
    super(msg);
    this.code = code;
  }
}