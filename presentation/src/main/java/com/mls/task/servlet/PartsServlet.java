package com.mls.task.servlet;

import com.mls.task.constants.ServletConstants;
import com.mls.task.core.PartService;
import com.mls.task.core.model.PartProperties;
import com.mls.task.core.model.response.PartResponseModel;
import com.mls.task.core.model.search.PartSearchModel;
import com.mls.task.implementation.PartServicesImpl;
import com.mls.task.implementation.dao.PartDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PartsServlet extends HttpServlet implements ServletConstants {

    private final Logger LOGGER = LoggerFactory.getLogger(PartsServlet.class);

    private PartService partService;

    public PartsServlet() {
        partService = new PartServicesImpl();
        ((PartServicesImpl) partService).setPartDAO(new PartDAOImpl());
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info("Requested to get parts");
        final String name = req.getParameter(PartProperties.NAME.getColumnName());
        final String vendor = req.getParameter(PartProperties.VENDOR.getColumnName());
        final String number = req.getParameter(PartProperties.NUMBER.getColumnName());
        final String qty = req.getParameter(PartProperties.QTY.getColumnName());
        final String receivedAfter = req.getParameter(PartProperties.RECEIVED_AFTER.getColumnName());
        final String receivedBefore = req.getParameter(PartProperties.RECEIVED_BEFORE.getColumnName());
        final String shippedAfter = req.getParameter(PartProperties.SHIPPED_AFTER.getColumnName());
        final String shippedBefore = req.getParameter(PartProperties.SHIPPED_BEFORE.getColumnName());
        final PartSearchModel partSearchModel = new PartSearchModel(name, number, vendor, qty, shippedAfter, shippedBefore, receivedAfter, receivedBefore);
        final List<PartResponseModel> parts = new ArrayList<>();
        if (partSearchModel.isEmpty()) {
            LOGGER.info("Search parameters are empty getting all parts");
            parts.addAll(partService.getAllParts());
        } else {
            LOGGER.info("Getting parts by a search parameters  '{}'", partSearchModel);
            parts.addAll(partService.getPartsBySearchModel(partSearchModel));
        }
        req.setAttribute("parts" , parts);
        req.getRequestDispatcher(PARTS_JSP).forward(req,resp);
    }
}
