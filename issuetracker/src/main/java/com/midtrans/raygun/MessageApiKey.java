package com.midtrans.raygun;

import java.io.Serializable;

public class MessageApiKey implements Serializable {
  public String apiKey;
  public String message;

  public MessageApiKey() { }

  public MessageApiKey(String apiKey, String message)
  {
    this.apiKey = apiKey;
    this.message = message;
  }
}
