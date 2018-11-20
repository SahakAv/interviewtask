package com.mls.task.implementation.converter;

import com.mls.task.core.model.entity.Part;
import com.mls.task.core.model.response.PartResponseModel;

public class PartResponseConverter {


    public static PartResponseModel convertToResponseModel(final Part part){
        final PartResponseModel responseModel = new PartResponseModel();
        responseModel.setName(part.getName());
        responseModel.setVendor(part.getVendor());
        responseModel.setNumber(part.getNumber());
        responseModel.setQty(part.getQty());
        responseModel.setReceived(part.getReceived());
        responseModel.setShipped(part.getShipped());
        return responseModel;
    }
}
