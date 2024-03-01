package com.twilio;

import com.twilio.rest.api.v2010.account.Call;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Say;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.sql.SQLException;

@WebServlet("/CallServlet")
public class CallServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String toPhoneNumber = request.getParameter("toPhoneNumber");
        String message = request.getParameter("message");

        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            out.println("<body style='background: linear-gradient(to right, #673AB7, #2196F3); color: #fff; font-family: Arial, sans-serif; text-align: center; margin: 0; padding: 0; height: 100vh; display: flex; flex-direction: column; justify-content: center; align-items: center;'>");

            out.println("<div class='message-container'>");

            try {
                makeCall(toPhoneNumber, message);
                out.println("<h1 style='background: linear-gradient(to right, #673AB7, #2196F3); color: #fff; padding: 10px; border-radius: 5px; font-weight: bold;'>Call made successfully!</h1>");

            } catch (Exception e) {
                out.println("<h1 style='background: linear-gradient(to right, #FFA500, #FF0000); color: #fff; padding: 10px; border-radius: 5px; font-weight: bold;'>Error making call: " + e.getMessage() + "</h1>");
            }

            out.println("</div>");
            out.println("</body>");
        }
    }

    private void makeCall(String toPhoneNumber, String message) throws URISyntaxException, SQLException {
        Twilio.init("AC00a5a55bf7bce79117818a1dc21f24b9", "56ba9a7141b8c47ff476a831b0294fd6");
        VoiceResponse voiceResponse = new VoiceResponse.Builder()
                .say(new Say.Builder(message).build())
                .build();

        Call call = Call.creator(
                new com.twilio.type.PhoneNumber(toPhoneNumber),
                new com.twilio.type.PhoneNumber("+18155915841"),
                new com.twilio.type.Twiml(voiceResponse.toXml())
        ).create();

        System.out.println("Call SID: " + call.getSid());
    }
}
