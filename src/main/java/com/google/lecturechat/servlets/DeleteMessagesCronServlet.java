// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.lecturechat.servlets;

import com.google.lecturechat.data.DatastoreAccess;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for deleting all chat messages before a certain time. Only to be called by a cron job.
 */
@WebServlet("/delete-messages")
public class DeleteMessagesCronServlet extends HttpServlet {

  private static final int TIMEFRAME_MESSAGES_TO_KEEP = 24;
  private static DatastoreAccess datastore;

  @Override
  public void init() {
    datastore = DatastoreAccess.getDatastoreAccess();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String cronHeader = request.getHeader("X-Appengine-Cron");
    if (cronHeader == null || !cronHeader.equals("true")) {
      return;
    }
    datastore.deleteMessagesOlderThan(TIMEFRAME_MESSAGES_TO_KEEP);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    doGet(request, response);
  }
}
