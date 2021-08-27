package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Item;
import com.fasterxml.jackson.core.JsonProcessingException;
import util.ErrorEntity;
import util.HttpConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
//@WebServlet(name = "myServlet", urlPatterns = "/myServlet")
public class MyServlet extends HttpServlet {
    private final List<Item> items = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        resp.setContentType(HttpConstants.ContentType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        String response = objectMapper.writeValueAsString(items);
        PrintWriter writer = resp.getWriter();
        writer.write(response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        if (!req.getContentType().equals(HttpConstants.ContentType.APPLICATION_JSON)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "not_supported_format");
        }

    BufferedReader bufferedReader = req.getReader();
    String body = bufferedReader.lines().collect(Collectors.joining());

        try {
        Item item = objectMapper.readValue(body, Item.class);
        items.add(item);
        resp.getWriter().write(objectMapper.writeValueAsString(item));
    } catch (RuntimeException e) {
        ErrorEntity errorEntity = new ErrorEntity("json_parse_failed:" + e.getMessage());
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().write(objectMapper.writeValueAsString(errorEntity));
    }
}

    protected String getItemAsJson(Item item) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(item);
    }



}
