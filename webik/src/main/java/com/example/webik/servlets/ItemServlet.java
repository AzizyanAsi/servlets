package com.example.webik.servlets;

import com.example.webik.models.Generative;
import com.example.webik.models.Group;
import com.example.webik.models.Item;
import com.example.webik.models.Stock;
import com.example.webik.service.ItemNotFoundException;
import com.example.webik.service.Storage;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.jfr.internal.consumer.StringParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

import static com.example.webik.service.Storage.groups;

@WebServlet(name = "itemServlet", value = "/item")
public class ItemServlet extends HttpServlet {
    public static final String PARAM_TYPE = "type";
    private Item Item;
    private final ObjectMapper objectMapper = new ObjectMapper();
    public static String URL_ID = "id";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        try {
            resp.getWriter().write(objectMapper.writeValueAsString(Storage.findItemByTitle(req.getParameter("name"))));
        } catch (ItemNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String typeParam = req.getParameter(PARAM_TYPE);

        if (typeParam == null || typeParam.isEmpty()) {
            resp.setStatus(400);
            resp.getWriter().write("no parameter" + PARAM_TYPE);
            return;

        }
        String body = req.getReader()
                .lines()
                .collect(Collectors.joining());


        ItemType type = ItemType.valueOf(typeParam);
        Item item;

        switch (type) {
            case STOCK:
//                item = Storage.addItem(objectMapper.readValue(body, Stock.class), Item.getId(), groups);
                break;
            case GENERATIVE:
//                item = Storage.addItem(objectMapper.readValue(body, Generative.class),Item.getId(), groups);
                break;
            default:
                throw new RuntimeException("Invalid type");
        }

        resp.getWriter().write(objectMapper.writeValueAsString(item));
    }

    public enum ItemType {

        STOCK, GENERATIVE

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String urlId = req.getParameter(URL_ID);
        String path = req.getContextPath();

        if (urlId == null || urlId.isEmpty()) {
            resp.setStatus(400);
            resp.getWriter().write("no id" + PARAM_TYPE);

        } else if (path.endsWith(urlId)) {
            try {
                Storage.updateItem(urlId, req.getParameter("name"));
            } catch (ItemNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String urlId = req.getParameter(URL_ID);
        String path = req.getContextPath();

        if (urlId == null || urlId.isEmpty()) {
            resp.setStatus(400);
            resp.getWriter().write("no id" + PARAM_TYPE);

        } else if (path.endsWith(urlId)) {
            try {
                Storage.deleteItem(urlId);
            } catch (ItemNotFoundException e) {
                e.printStackTrace();
            }
        }

    }


}

