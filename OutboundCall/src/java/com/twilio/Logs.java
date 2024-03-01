package com.twilio;

import com.twilio.base.ResourceSet;
import com.twilio.rest.api.v2010.account.Call;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Logs extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String callLogsHtml;
        try {
            callLogsHtml = retrieveCallLogFromTwilio();
        } catch (IOException ex) {
            Logger.getLogger(Logs.class.getName()).log(Level.SEVERE, null, ex);

            callLogsHtml = "Error retrieving call logs: " + ex.getMessage();
        }

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Call Logs</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Call Logs</h1>");
            out.println(callLogsHtml);
            out.println("</body>");
            out.println("</html>");
        }
    }

    private String retrieveCallLogFromTwilio() throws IOException {
        Twilio.init("AC00a5a55bf7bce79117818a1dc21f24b9", "56ba9a7141b8c47ff476a831b0294fd6");
        StringBuilder htmlBuilder = new StringBuilder();
        ResourceSet<Call> callList = Call.reader().limit(100).read();

        for (Call call : callList) {
            htmlBuilder.append("Call SID: ").append(call.getSid()).append("<br>");
            htmlBuilder.append("To: ").append(call.getTo()).append("<br>");
            htmlBuilder.append("From: ").append(call.getFrom()).append("<br>");
            htmlBuilder.append("Status: ").append(call.getStatus()).append("<br>");
            htmlBuilder.append("Date and Time: ").append(call.getDateCreated()).append("<br>");
            htmlBuilder.append("==========================").append("<br>");
        }

        return htmlBuilder.toString();
    }
}
