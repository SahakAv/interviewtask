package com.mls.task.implementation.dao;


import com.mls.task.configuration.DBConnectionProvider;
import com.mls.task.core.dao.PartDAO;
import com.mls.task.core.model.PartProperties;
import com.mls.task.core.model.QueryConstants;
import com.mls.task.core.model.entity.Part;
import com.mls.task.core.model.search.PartSearchModel;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class PartDAOImpl implements PartDAO, QueryConstants {

    private final Connection connection;

    private final Logger LOGGER = LoggerFactory.getLogger(PartDAO.class);

    public PartDAOImpl() {
        connection = DBConnectionProvider.getConnection();
    }

    @Override
    public List<Part> getAllParts() {
        final List<Part> parts = new ArrayList<>();
        try {
            final PreparedStatement statement = connection.prepareStatement(GET_ALL_PARTS);
            final ResultSet resultSet = statement.executeQuery();
            mapQueryResult(parts, resultSet);
        } catch (SQLException e) {
            LOGGER.error("Exception occurred while trying to get all parts from database", e);
        }
        return parts;
    }

    @Override
    public List<Part> getPartsBySearchModel(final PartSearchModel partSearchModel) {
        final List<Part> parts = new ArrayList<>();
        try {
            final PreparedStatement preparedStatement = connection.prepareStatement(buildQuery(partSearchModel));
            final Map<PartProperties, Integer> partPropertiesIndexes = generatePropertyIndexes(partSearchModel);
            addQueryParameters(partPropertiesIndexes, preparedStatement, partSearchModel);
            final ResultSet resultSet = preparedStatement.executeQuery();
            mapQueryResult(parts, resultSet);
        } catch (SQLException e) {
            LOGGER.error(String.format("Exception occurred while trying to get parts by a search model %s from database", partSearchModel), e);
        }
        return parts;
    }

    private void addQueryParameters(final Map<PartProperties, Integer> partPropertiesIndexes, final PreparedStatement preparedStatement, final PartSearchModel partSearchModel) throws SQLException {
        if (partPropertiesIndexes.containsKey(PartProperties.NAME)) {
            preparedStatement.setString(partPropertiesIndexes.get(PartProperties.NAME), partSearchModel.getName().get());
        }
        if (partPropertiesIndexes.containsKey(PartProperties.NUMBER)) {
            preparedStatement.setString(partPropertiesIndexes.get(PartProperties.NUMBER), partSearchModel.getNumber().get());
        }
        if (partPropertiesIndexes.containsKey(PartProperties.VENDOR)) {
            preparedStatement.setString(partPropertiesIndexes.get(PartProperties.VENDOR), partSearchModel.getVendor().get());
        }
        if (partPropertiesIndexes.containsKey(PartProperties.QTY)) {
            preparedStatement.setInt(partPropertiesIndexes.get(PartProperties.QTY), partSearchModel.getQty().get());
        }
        if (partPropertiesIndexes.containsKey(PartProperties.SHIPPED_AFTER)) {
            preparedStatement.setDate(partPropertiesIndexes.get(PartProperties.SHIPPED_AFTER), new java.sql.Date(partSearchModel.getShippedAfter().get().getTime()));
        }
        if (partPropertiesIndexes.containsKey(PartProperties.SHIPPED_BEFORE)) {
            preparedStatement.setDate(partPropertiesIndexes.get(PartProperties.SHIPPED_BEFORE), new java.sql.Date(partSearchModel.getShippedBefore().get().getTime()));
        }
        if (partPropertiesIndexes.containsKey(PartProperties.RECEIVED_AFTER)) {
            preparedStatement.setDate(partPropertiesIndexes.get(PartProperties.RECEIVED_AFTER), new java.sql.Date(partSearchModel.getQty().get()));
        }
        if (partPropertiesIndexes.containsKey(PartProperties.RECEIVED_BEFORE)) {
            preparedStatement.setDate(partPropertiesIndexes.get(PartProperties.RECEIVED_BEFORE), new java.sql.Date(partSearchModel.getReceivedBefore().get().getTime()));
        }
    }

    private String buildQuery(final PartSearchModel partSearchModel) {
        final StringBuilder queryBuilder = new StringBuilder(GET_ALL_PARTS);
        final List<Tuple2<String, String>> queryingPairs = new ArrayList<>();
        partSearchModel.getName().ifPresent(s -> queryingPairs.add(Tuple.of(PartProperties.NAME.getColumnName() + "LIKE ", QUESTION_MARK )));
        partSearchModel.getNumber().ifPresent(s -> queryingPairs.add(Tuple.of(PartProperties.NUMBER.getColumnName() + "LIKE ", QUESTION_MARK)));
        partSearchModel.getVendor().ifPresent(s -> queryingPairs.add(Tuple.of(PartProperties.VENDOR.getColumnName() + "LIKE ", QUESTION_MARK)));
        partSearchModel.getQty().ifPresent(s -> queryingPairs.add(Tuple.of(PartProperties.QTY.getColumnName() + "<= ", "")));
        if (!queryingPairs.isEmpty()) {
            queryBuilder.append(" WHERE ");
            queryingPairs.forEach(
                    tuple2 -> queryBuilder.append(tuple2._1).append(tuple2._2));
        }
        queryBuilder.append(generateQueryOperatorForDate(partSearchModel.getShippedAfter(), partSearchModel.getShippedBefore(), PartProperties.SHIPPED.getColumnName()));
        queryBuilder.append(generateQueryOperatorForDate(partSearchModel.getReceivedAfter(), partSearchModel.getReceivedBefore(), PartProperties.RECEIVED.getColumnName()));
        return queryBuilder.toString();
    }

    private Map<PartProperties, Integer> generatePropertyIndexes(final PartSearchModel partSearchModel) {
        int index = 0;
        Map<PartProperties, Integer> propertiesIndex = new HashMap<>();
        partSearchModel.getName().ifPresent(s -> propertiesIndex.put(PartProperties.NAME, index + 1));
        partSearchModel.getNumber().ifPresent(s -> propertiesIndex.put(PartProperties.NUMBER, index + 1));
        partSearchModel.getVendor().ifPresent(s -> propertiesIndex.put(PartProperties.VENDOR, index + 1));
        partSearchModel.getQty().ifPresent(s -> propertiesIndex.put(PartProperties.QTY, index + 1));
        partSearchModel.getShippedAfter().ifPresent(date -> propertiesIndex.put(PartProperties.SHIPPED_AFTER, index + 1));
        partSearchModel.getShippedBefore().ifPresent(date -> propertiesIndex.put(PartProperties.SHIPPED_BEFORE, index + 1));
        partSearchModel.getReceivedAfter().ifPresent(date -> propertiesIndex.put(PartProperties.RECEIVED_AFTER, index + 1));
        partSearchModel.getReceivedBefore().ifPresent(date -> propertiesIndex.put(PartProperties.RECEIVED_BEFORE, index + 1));
        return propertiesIndex;
    }


    private String generateQueryOperatorForDate(final Optional<Date> startDate, final Optional<Date> endDate, final String columnName) {
        final StringBuilder queryBuilder = new StringBuilder();
        if (startDate.isPresent() && endDate.isPresent()) {
            return queryBuilder.append(" AND ").append(columnName).append(" BETWEEN ").append(QUESTION_MARK).append(" AND ").append(QUESTION_MARK).toString();
        }
        if (!endDate.isPresent() && startDate.isPresent()) {
            return queryBuilder.append(" AND ").append(columnName).append(" >=").append(QUESTION_MARK).toString();
        }
        if (endDate.isPresent() && !startDate.isPresent()) {
            return queryBuilder.append(" AND ").append(columnName).append(" <= ").append(QUESTION_MARK).toString();
        } else {
            return "";
        }
    }


    private void mapQueryResult(final List<Part> parts, final ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            final Part part = new Part();
            part.setName(resultSet.getNString(PartProperties.NAME.getColumnName()));
            part.setNumber(resultSet.getNString(PartProperties.NUMBER.getColumnName()));
            part.setVendor(resultSet.getNString(PartProperties.VENDOR.getColumnName()));
            part.setQty(resultSet.getInt(PartProperties.QTY.getColumnName()));
            part.setShipped(resultSet.getDate(PartProperties.SHIPPED.getColumnName()));
            part.setReceived(resultSet.getDate(PartProperties.RECEIVED.getColumnName()));
            parts.add(part);
        }
    }
}
