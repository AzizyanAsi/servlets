package com.example.webik.servlets;

import com.example.webik.models.Item;
import com.example.webik.models.Stock;
import com.example.webik.util.ErrorEntity;
import com.example.webik.util.HttpConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "myServlet", value= "/myServlet")
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
        Stock item = objectMapper.readValue(body, Stock.class);
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
