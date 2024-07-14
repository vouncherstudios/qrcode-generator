package com.vouncherstudios.qrcodecreator.url;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Nonnull;

public final class UrlQueryBuilder {
  private final String url;
  private final Map<String, String> parameters = new LinkedHashMap<>();

  public UrlQueryBuilder(@Nonnull String url) {
    this.url = url;
  }

  public UrlQueryBuilder add(@Nonnull String key, @Nonnull String value) {
    this.parameters.put(key, value);
    return this;
  }

  public String toURL() {
    if (this.parameters.isEmpty()) {
      return this.url;
    }

    StringBuilder builder = new StringBuilder(this.url);
    int parameterIndex = 0;

    for (String key : this.parameters.keySet()) {
      if (parameterIndex == 0) {
        builder.append("?");
      } else {
        builder.append("&");
      }

      String encodedValue = URLEncoder.encode(this.parameters.get(key), StandardCharsets.UTF_8);

      builder.append(key).append("=").append(encodedValue);
      parameterIndex++;
    }

    return builder.toString();
  }
}
