package com.mls.task.implementation;

import com.mls.task.core.PartService;
import com.mls.task.core.dao.PartDAO;
import com.mls.task.core.model.entity.Part;
import com.mls.task.core.model.response.PartResponseModel;
import com.mls.task.core.model.search.PartSearchModel;
import com.mls.task.implementation.converter.PartResponseConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class PartServicesImpl implements PartService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PartService.class);

    private PartDAO partDAO;


    public PartServicesImpl() {
    }


    public List<PartResponseModel> getAllParts() {
        LOGGER.info("Requested to get all parts");
        final List<Part> parts = partDAO.getAllParts();
        LOGGER.info("Got parts ", parts);
        return parts.stream()
                .map(PartResponseConverter::convertToResponseModel)
                .collect(Collectors.toList());
    }

    public List<PartResponseModel> getPartsBySearchModel(final PartSearchModel partSearchModel) {
        if (partSearchModel == null){
            throw new IllegalArgumentException("Part search model should be null");
        }
        LOGGER.info("Requested to get  parts by a search model '{}'", partSearchModel);
        final List<Part> partsBySearchModel = partDAO.getPartsBySearchModel(partSearchModel);
        LOGGER.info("Got parts ", partsBySearchModel);
        return partsBySearchModel.stream()
                .map(PartResponseConverter::convertToResponseModel)
                .collect(Collectors.toList());
    }

    public void setPartDAO(PartDAO partDAO) {
        this.partDAO = partDAO;
    }
}
