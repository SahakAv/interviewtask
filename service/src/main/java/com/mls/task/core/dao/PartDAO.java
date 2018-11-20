package com.mls.task.core.dao;

import com.mls.task.core.model.entity.Part;
import com.mls.task.core.model.search.PartSearchModel;

import java.util.List;

public interface PartDAO {

    List<Part> getAllParts();

    List<Part> getPartsBySearchModel(final PartSearchModel partSearchModel);

}
