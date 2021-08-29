package com.example.webik.servlets;

import com.example.webik.service.ItemNotFoundException;
import com.example.webik.service.Storage;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "search", value = "/search")
public class Search extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
    public int priceFrom;
    public int priceTo;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        priceFrom = Integer.parseInt(String.valueOf(Storage.findHighestPricedItem()));
        priceTo = Integer.parseInt(String.valueOf(Storage.findMinPricedItem()));
        resp.getWriter().write(objectMapper.writeValueAsString(priceFrom));
        resp.getWriter().write(objectMapper.writeValueAsString(priceTo));


    }
}
