package com.training.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.training.business.bean.PurchaseBean;
import com.training.business.bean.VendorWisePurchaseReportBean;
import com.training.business.service.ReportsService;
import com.training.exception.GlobalExceptionHandler;

public class ReportsControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ReportsService reportsService;

    @InjectMocks
    private ReportsController reportsController;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders
                .standaloneSetup(reportsController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    private VendorWisePurchaseReportBean createRequest() {

        VendorWisePurchaseReportBean bean = new VendorWisePurchaseReportBean();

        bean.setVendorName("ABC Vendor");
        bean.setFromDate(new Date());
        bean.setToDate(new Date());

        return bean;
    }

    private PurchaseBean createPurchaseBean() {

        PurchaseBean bean = new PurchaseBean();

        bean.setPurchaseId(1);
        bean.setVendorName("ABC Vendor");
        bean.setBrandName("ACC Cement");
        bean.setMaterialCategoryId("CAT01");
        bean.setMaterialTypeId("TYPE01");
        bean.setQuantity(25);
        bean.setPurchaseAmount(2500.0);
        bean.setPurchaseDate(new Date());
        bean.setStatus("SUCCESS");

        return bean;
    }

    @Test
    void testGetPurchaseDetailsSuccess() throws Exception {

        List<PurchaseBean> list = new ArrayList<>();
        list.add(createPurchaseBean());

        when(reportsService.getVendorWisePurchaseDetails(
                any(),
                any(),
                any()))
                .thenReturn(list);

        mockMvc.perform(post("/report/controller/getPurchaseDetails")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest())))
                .andExpect(status().isOk());

        verify(reportsService, times(1))
                .getVendorWisePurchaseDetails(
                        any(),
                        any(),
                        any());
    }

    @Test
    void testGetPurchaseDetailsEmptyList() throws Exception {

        when(reportsService.getVendorWisePurchaseDetails(
                any(),
                any(),
                any()))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(post("/report/controller/getPurchaseDetails")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest())))
                .andExpect(status().isOk());
    }

    @Test
    void testGetPurchaseDetailsException() throws Exception {

        when(reportsService.getVendorWisePurchaseDetails(
                any(),
                any(),
                any()))
                .thenThrow(new RuntimeException("Database Error"));

        mockMvc.perform(post("/report/controller/getPurchaseDetails")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest())))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testInvalidJson() throws Exception {

        mockMvc.perform(post("/report/controller/getPurchaseDetails")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{invalid json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testEmptyRequestBody() throws Exception {

        mockMvc.perform(post("/report/controller/getPurchaseDetails")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testVerifyServiceCalled() throws Exception {

        when(reportsService.getVendorWisePurchaseDetails(
                any(),
                any(),
                any()))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(post("/report/controller/getPurchaseDetails")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest())))
                .andExpect(status().isOk());

        verify(reportsService, times(1))
                .getVendorWisePurchaseDetails(
                        any(),
                        any(),
                        any());
    }
}