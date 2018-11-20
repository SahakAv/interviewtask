package com.mls.task.service;

import com.mls.task.core.PartService;
import com.mls.task.core.dao.PartDAO;
import com.mls.task.core.model.entity.Part;
import com.mls.task.core.model.response.PartResponseModel;
import com.mls.task.core.model.search.PartSearchModel;
import com.mls.task.implementation.PartServicesImpl;
import com.mls.task.implementation.converter.PartResponseConverter;
import com.mls.task.implementation.dao.PartDAOImpl;
import org.junit.Before;
import org.junit.Test;


import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class PartServiceImplTests {

    private PartDAO partDAO;


    private PartService partService;

    @Before
    public void setUp() {
        partDAO = mock(PartDAOImpl.class);
        partService = new PartServicesImpl();
        ((PartServicesImpl) partService).setPartDAO(partDAO);
    }

    @Test
    public void testGetAllParts() {
        //Test data region
        final String name = "name";
        final String number = "number";
        final Part part = createPart(name, number);
        final List<Part> parts = Collections.singletonList(part);
        //Mock region
        when(partDAO.getAllParts()).thenReturn(parts);
        //Service call
        final List<PartResponseModel> response = partService.getAllParts();
        final PartResponseModel partResponseModel = response.get(0);
        //Assert region
        verify(partDAO).getAllParts();
        assertNotNull(response.get(0));
        assertEquals(partResponseModel, PartResponseConverter.convertToResponseModel(part));
        verifyNoMoreInteractions(partDAO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPartsWithSearchModelWithInvalidArguments() {
        //Should thrown exception
        partService.getPartsBySearchModel(null);
    }

    @Test
    public void testGetPartWithSearchModel() {
        //Test data region
        final String name = "name";
        final String number = "number";
        final PartSearchModel partSearchModel = createPartSearchModel(name, number);
        final Part part = createPart(name,number);
        final List<Part> parts = Collections.singletonList(part);
        //Mock region
        when(partDAO.getPartsBySearchModel(eq(partSearchModel))).thenReturn(parts);
        //Service call
        final List<PartResponseModel> response = partService.getPartsBySearchModel(partSearchModel);
        final PartResponseModel partResponseModel = response.get(0);
        //Assert region
        verify(partDAO).getPartsBySearchModel(partSearchModel);
        assertNotNull(response.get(0));
        assertEquals(partResponseModel, PartResponseConverter.convertToResponseModel(part));
        assertEquals(name, PartResponseConverter.convertToResponseModel(part).getName());
        assertEquals(number, PartResponseConverter.convertToResponseModel(part).getNumber());
        verifyNoMoreInteractions(partDAO);
    }


    private Part createPart(final String name, final String number) {
        final Part part = new Part();
        part.setNumber(number);
        part.setName(name);
        part.setVendor("Vendor");
        part.setQty(Integer.MIN_VALUE);
        part.setShipped(new Date());
        part.setReceived(new Date());
        return part;
    }


    private PartSearchModel createPartSearchModel(final String name, final String number) {
        return new PartSearchModel(name, number, null, null, null, null, null, null);
    }
}
