/*
 * Copyright 2020  Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.grpcweb;

import io.grpc.Metadata;

import java.util.*;
import javax.servlet.http.HttpServletRequest;

public class MetadataUtil {
  private static final String BINARY_HEADER_SUFFIX = "-bin";
  private static final String GRPC_HEADER_PREFIX = "x-grpc-";
  private static final List<String> EXCLUDED = Arrays.asList("x-grpc-web", "content-type",
      "grpc-accept-encoding", "grpc-encoding");
  private static final List<String> INCLUDED = new ArrayList<>();

  public static void addIncludedHeader(String headerName) {
    INCLUDED.add(headerName);
  }

  public static void addExcludedHeader(String headerName) {
    EXCLUDED.add(headerName);
  }

  static Metadata getHtpHeaders(HttpServletRequest req) {
    Metadata httpHeaders = new Metadata();
    Enumeration<String> headerNames = req.getHeaderNames();
    if (headerNames == null) {
      return httpHeaders;
    }
    // copy all headers "x-grpc-*" into Metadata
    // TODO: do we need to copy all "x-*" headers instead?
    while (headerNames.hasMoreElements()) {
      String headerName = headerNames.nextElement();
      if (EXCLUDED.contains(headerName.toLowerCase())) {
        continue;
      }
      if (INCLUDED.contains(headerName.toLowerCase()) || headerName.toLowerCase().startsWith(GRPC_HEADER_PREFIX)) {
        // Get all the values of this header.
        Enumeration<String> values = req.getHeaders(headerName);
        if (values != null) {
          // Java enumerations have klunky API. lets convert to a list.
          // this will be a short list usually.
          List<String> list = Collections.list(values);
          for (String s : list) {
            if (headerName.toLowerCase().endsWith(BINARY_HEADER_SUFFIX)) {
              // Binary header
              httpHeaders.put(
                  Metadata.Key.of(headerName, Metadata.BINARY_BYTE_MARSHALLER), s.getBytes());
            } else {
              // String header
              httpHeaders.put(
                  Metadata.Key.of(headerName, Metadata.ASCII_STRING_MARSHALLER), s);
            }
          }
        }
      }
    }
    return httpHeaders;
  }

  static Map<String, String> getHttpHeadersFromMetadata(Metadata trailer) {
    Map<String, String> map = new HashMap<>();
    for (String key : trailer.keys()) {
      if (EXCLUDED.contains(key.toLowerCase())) {
        continue;
      }
      if (key.endsWith(Metadata.BINARY_HEADER_SUFFIX)) {
        // TODO allow any object type here
        byte[] value =  trailer.get(Metadata.Key.of(key, Metadata.BINARY_BYTE_MARSHALLER));
        map.put(key, new String(value));
      } else {
        String value = trailer.get(Metadata.Key.of(key, Metadata.ASCII_STRING_MARSHALLER));
        map.put(key, value);
      }
    }
    return map;
  }
}
