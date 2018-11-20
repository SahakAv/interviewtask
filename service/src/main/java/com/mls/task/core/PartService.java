package com.mls.task.core;


import com.mls.task.core.model.response.PartResponseModel;
import com.mls.task.core.model.search.PartSearchModel;

import java.util.List;

public interface PartService {

   List<PartResponseModel> getAllParts();

   List<PartResponseModel> getPartsBySearchModel(final PartSearchModel partSearchModel);
}
